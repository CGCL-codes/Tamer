package org.pixory.pxtapestry;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.AbstractService;
import org.apache.tapestry.engine.IEngineServiceView;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.IMonitor;
import org.apache.tapestry.request.ResponseOutputStream;
import org.pixory.pxfoundation.PXStringUtility;
import org.pixory.pximage.PXImage;
import org.pixory.pximage.PXImageException;

public class PXImageService extends AbstractService {

    private static final Log LOG = LogFactory.getLog(PXImageService.class);

    public static final String SERVICE_NAME = "pximage";

    private static final int BUFFER_SIZE = 1024;

    /**
	 * a response header; see rfc2616
	 */
    private static final String LAST_MODIFIED_HEADER_KEY = "Last-Modified";

    /**
	 * a request header; see rfc2616
	 */
    private static final String IF_MODIFIED_HEADER_KEY = "If-Modified-Since";

    private static final String EXPIRES_HEADER_KEY = "Expires";

    private static final long EXPIRED_DATE_VALUE = 0;

    private static final String CACHE_CONTROL_HEADER_KEY = "Cache-Control";

    /**
	 * a response header value; see rfc2616
	 */
    private static final String MUST_REVALIDATE_HEADER_VALUE = "must-revalidate";

    private static boolean _decodeParameters = false;

    public PXImageService() {
    }

    public static void setDecodeParameters(boolean decodeParameters_) {
        _decodeParameters = decodeParameters_;
    }

    public String getName() {
        return SERVICE_NAME;
    }

    /**
	 * called by PXImageComponent.render(); Note that whoever created the PXImage
	 * is responsible for closing it-- you can't do that here if baseUri is
	 * absent (normal case), generated URLs will be relative; otherwise, baseUri
	 * will be used as the base for an absolute URL.
	 */
    public String buildURL(IRequestCycle cycle_, PXImage image_, URI baseUri_) throws PXImageException {
        String buildURL = null;
        if ((cycle_ != null) && (image_ != null)) {
            String[] urlParameters = urlParametersForImage(image_);
            ILink link = this.constructLink(cycle_, this.getName(), null, urlParameters, false);
            if (link != null) {
                if (baseUri_ == null) {
                    buildURL = link.getURL();
                } else {
                    buildURL = link.getAbsoluteURL(baseUri_.getScheme(), baseUri_.getHost(), baseUri_.getPort(), null, true);
                }
            }
        } else {
            String aMethodName = "buildURL";
            throw new IllegalArgumentException(this.getClass().getName() + "." + aMethodName + "does not allow null arguments");
        }
        return buildURL;
    }

    public ILink getLink(IRequestCycle cycle_, IComponent component_, Object[] parameters_) {
        return this.constructLink(cycle_, this.getName(), null, parameters_, false);
    }

    public void service(IEngineServiceView engine_, IRequestCycle cycle_, ResponseOutputStream output_) throws ServletException, IOException {
        try {
            Object[] parameters = this.getParameters(cycle_);
            PXImage image = imageForURLParameters(parameters);
            IMonitor monitor = cycle_.getMonitor();
            if (monitor != null) {
                monitor.serviceBegin(SERVICE_NAME, parameters.toString());
            }
            if (image != null) {
                int imageLength = (int) image.getLength();
                String imageMIMEType = image.getMIMEType();
                InputStream inputStream = image.getInputStream();
                if ((imageLength != PXImage.NOT_AVAILABLE) && (imageMIMEType != null) && (inputStream != null)) {
                    boolean originModified = true;
                    Date originLastModified = image.getLastModified();
                    HttpServletResponse response = cycle_.getRequestContext().getResponse();
                    if (originLastModified != null) {
                        long ifModifiedSince = PXRequestCycleUtility.getRequestDateHeader(cycle_, IF_MODIFIED_HEADER_KEY);
                        if (ifModifiedSince != -1) {
                            if (originLastModified.getTime() <= ifModifiedSince) {
                                originModified = false;
                            }
                        }
                        response.setDateHeader(LAST_MODIFIED_HEADER_KEY, originLastModified.getTime());
                        response.setDateHeader(EXPIRES_HEADER_KEY, EXPIRED_DATE_VALUE);
                    }
                    if (originModified) {
                        response.setContentLength(imageLength);
                        output_.setContentType(imageMIMEType);
                        output_.forceFlush();
                        byte[] buffer = new byte[BUFFER_SIZE];
                        while (true) {
                            int bytesReadCount = inputStream.read(buffer);
                            if (bytesReadCount < 0) {
                                break;
                            }
                            output_.write(buffer, 0, bytesReadCount);
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    }
                } else {
                    cycle_.getRequestContext().getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                image.close();
            }
            if (monitor != null) {
                monitor.serviceEnd(SERVICE_NAME);
            }
        } catch (Exception e) {
            throw new ApplicationRuntimeException(e);
        }
    }

    private static String[] urlParametersForImage(PXImage image_) throws PXImageException {
        String[] urlParametersForImage = null;
        if (image_ != null) {
            String path = image_.getPath();
            if (path != null) {
                String theImageType = image_.getClass().getName();
                urlParametersForImage = new String[] { theImageType, path };
            } else {
                throw new PXImageException("No path specified for PXImage " + image_);
            }
        } else {
            String aMethodName = "urlParametersForImage";
            throw new IllegalArgumentException("PXImageService." + aMethodName + "does not allow null arguments");
        }
        return urlParametersForImage;
    }

    private static PXImage imageForURLParameters(Object[] parameters_) throws PXImageException {
        PXImage imageForURLParameters = null;
        if ((parameters_ != null) && (parameters_.length == 2)) {
            try {
                String imageClassName = (String) parameters_[0];
                Class imageClass = Class.forName(imageClassName);
                try {
                    Constructor constructor = imageClass.getConstructor(PXImage.CONSTRUCTOR_SIGNATURE);
                    String[] someConstructorArgs = new String[] { (String) parameters_[1] };
                    imageForURLParameters = (PXImage) constructor.newInstance(someConstructorArgs);
                } catch (NoSuchMethodException anException) {
                    throw new NoSuchMethodException(imageClass.getName() + " must have a constructor that takes a single String argument");
                }
            } catch (Exception anException) {
                throw new PXImageException(anException);
            }
        } else {
            String aMethodName = "imageForURLParameters";
            throw new IllegalArgumentException("PXImageService." + aMethodName + " only accepts Object[] of length=2");
        }
        return imageForURLParameters;
    }

    protected Object[] getParameters(IRequestCycle cycle_) {
        Object[] getParameters = null;
        if (_decodeParameters) {
            getParameters = this.decodeParameters(cycle_);
        } else {
            getParameters = super.getParameters(cycle_);
        }
        return getParameters;
    }

    /**
	 * Tapestry uses the outputEncoding to encode URL parameters. However, the reverse
	 * leg of the trip is problematic. There is nothing in the HTTP or HTML specs or W3 notes
	 * which allows a URL to indicate what encoding was used for parameters. So the app
	 * servers have to make some assumptions. Some passages in the Servlet spec indicate
	 * that the Servlet container should use the outputEncoding, but some container
	 * Vendors are not doing that. This method assumes that the container decoded the
	 * parameters as ASCII (single byte/character), which means that only the low bytes
	 * in the resulting Java String are carrying the original data.
	 * It recovers the original (undecoded bytes)
	 * from the java String by pulling out the low bytes, and then redecodes the bytes
	 * using the outputEncoding.
	 */
    protected Object[] decodeParameters(IRequestCycle cycle_) {
        Object[] decodeParameters = null;
        if (cycle_ != null) {
            decodeParameters = super.getParameters(cycle_);
            if ((decodeParameters != null) && (decodeParameters.length > 0)) {
                String encoding = cycle_.getEngine().getOutputEncoding();
                for (int i = 0; i < decodeParameters.length; i++) {
                    Object parameter = decodeParameters[i];
                    if (parameter instanceof String) {
                        try {
                            String stringParameter = (String) parameter;
                            byte[] parameterBytes = PXStringUtility.getLowBytes(stringParameter);
                            String decodedParameter = new String(parameterBytes, encoding);
                            decodeParameters[i] = decodedParameter;
                        } catch (Exception e) {
                            LOG.warn(null, e);
                        }
                    }
                }
            }
        }
        return decodeParameters;
    }
}
