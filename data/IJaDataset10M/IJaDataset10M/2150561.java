package org.apache.myfaces.trinidadinternal.taglib.listener;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.http.HttpServletResponse;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.FacesBeanImpl;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.util.ComponentUtils;
import org.apache.myfaces.trinidad.util.MessageFactory;
import org.apache.myfaces.trinidadinternal.util.MimeUtility;

/**
 * @todo Look at moving to org.apache.myfaces.trinidad.event
 * @todo Extending FacesBean is very lame if we make this
 *   class part of our public API, but the FacesBean API
 *   would otherwise require a private subclass of FacesBeanImpl.
 *   We need a better way out.
 */
public class FileDownloadActionListener extends FacesBeanImpl implements ActionListener, StateHolder {

    public static final FacesBean.Type TYPE = new FacesBean.Type();

    public static final PropertyKey FILENAME_KEY = TYPE.registerKey("filename");

    public static final PropertyKey CONTENT_TYPE_KEY = TYPE.registerKey("contentType");

    public static final PropertyKey METHOD_KEY = TYPE.registerKey("method", MethodExpression.class, PropertyKey.CAP_NOT_BOUND);

    /**
    * <p>The message identifier of the {@link FacesMessage} to be created when
    * there is a download error.</p>
    */
    public static final String DOWNLOAD_MESSAGE_ID = "org.apache.myfaces.trinidad.event.FileDownloadActionListener.DOWNLOAD_ERROR";

    static {
        TYPE.lock();
    }

    public FileDownloadActionListener() {
    }

    public void processAction(ActionEvent event) {
        String filename = getFilename();
        String contentType = getContentType();
        FacesContext context = FacesContext.getCurrentInstance();
        Object response = context.getExternalContext().getResponse();
        if (!(response instanceof HttpServletResponse)) {
            _LOG.warning("FILE_DOWNLOAD_LISTENER_REQUIRES_SERVLET");
        } else {
            HttpServletResponse hsr = (HttpServletResponse) response;
            try {
                if (contentType != null) hsr.setContentType(contentType);
                if (filename != null) {
                    boolean isGecko = true;
                    Map<String, String> headers = context.getExternalContext().getRequestHeaderMap();
                    String agentName = headers.get("User-Agent").toLowerCase();
                    if (agentName.contains("msie") || agentName.contains("applewebkit") || agentName.contains("safari")) isGecko = false;
                    hsr.setHeader("Content-Disposition", "attachment; filename=" + MimeUtility.encodeHTTPHeader(filename, !isGecko));
                }
                MethodExpression method = getMethod();
                OutputStream out = new BufferedOutputStream(hsr.getOutputStream());
                method.invoke(context.getELContext(), new Object[] { context, out });
                out.close();
            } catch (Exception e) {
                hsr.reset();
                _LOG.warning(e);
                FacesMessage message = MessageFactory.getMessage(context, DOWNLOAD_MESSAGE_ID);
                context.addMessage(null, message);
                context.renderResponse();
                return;
            }
        }
        context.responseComplete();
    }

    public MethodExpression getMethod() {
        return (MethodExpression) getProperty(METHOD_KEY);
    }

    public void setMethod(MethodExpression method) {
        setProperty(METHOD_KEY, method);
    }

    public String getFilename() {
        return ComponentUtils.resolveString(getProperty(FILENAME_KEY));
    }

    public void setFilename(String filename) {
        setProperty(FILENAME_KEY, filename);
    }

    public String getContentType() {
        return ComponentUtils.resolveString(getProperty(CONTENT_TYPE_KEY));
    }

    public void setContentType(String contentType) {
        setProperty(CONTENT_TYPE_KEY, contentType);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(FileDownloadActionListener.class);
}
