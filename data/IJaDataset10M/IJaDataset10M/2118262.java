package org.kablink.teaming.docconverter.impl;

import java.io.File;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uri.ExternalUriReferenceTranslator;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import org.kablink.teaming.UncheckedIOException;
import org.kablink.teaming.docconverter.HtmlConverter;
import org.kablink.teaming.docconverter.util.OpenOfficeHelper;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.DefinableEntity;
import org.kablink.teaming.domain.FileAttachment;
import org.kablink.teaming.repository.RepositoryServiceException;
import org.kablink.teaming.web.util.MiscUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Performs file conversions to HTML using OpenOffice.
 * 
 * The <code>Converter</code> class uses the {@link Export Export} 
 * technology according to the properties provided in a given 
 * configuration file.  The configuration file is assumed to be 
 * correctly formatted.
 *
 *	IMPORTANT: OpenOffice Server must be running; to start:
 *					% C:\Program Files\OpenOffice.org 2.0\program\soffice.exe "-accept=socket,port=8100;urp;"
 *
 * @author rsordillo
 * @version 1.00
 * @see Export Export
 */
public class HtmlOpenOfficeConverter extends HtmlConverter implements HtmlOpenOfficeConverterMBean, InitializingBean, DisposableBean {

    private String _host = null;

    private int _port = 0;

    public HtmlOpenOfficeConverter() {
        super();
    }

    public void afterPropertiesSet() throws Exception {
    }

    public void destroy() throws Exception {
    }

    public String getHost() {
        return _host;
    }

    public void setHost(String host_in) {
        _host = host_in;
    }

    public int getPort() {
        return _port;
    }

    public void setPort(int port_in) {
        _port = port_in;
    }

    public String convertToUrl(File f, XComponentContext xComponentContext) throws java.net.MalformedURLException {
        String returnUrl = null;
        java.net.URL u = f.toURL();
        returnUrl = ExternalUriReferenceTranslator.create(xComponentContext).translateToInternal(u.toExternalForm());
        return returnUrl;
    }

    /**
	 *  Run the conversion using the given input path, output path.
	 *
	 *  @param ifp     Input path.
	 *  @param ofp     Output path.
	 *  @param timeout Export process timeout in milliseconds.
	 *  @return <code>true</code> if successful, <code>false</code> otherwise
	 */
    public void convert(String origFileName, String ifp, String ofp, long timeout, String parameters) throws Exception {
        XStorable xstorable = null;
        XComponent xcomponent = null;
        XUnoUrlResolver xurlresolver = null;
        XComponentContext xcomponentcontext = null;
        XComponentLoader xcomponentloader = null;
        XPropertySet xpropertysetMultiComponentFactory = null;
        XMultiComponentFactory xmulticomponentfactory = null;
        File ofile = null, ifile = null;
        Object objectUrlResolver = null, objectInitial = null, objectDocumentToStore = null, objectDefaultContext = null;
        String url = "", convertType = "";
        try {
            ifile = new File(ifp);
            ofile = new File(ofp);
            if (!ifile.exists()) {
                return;
            }
            xcomponentcontext = Bootstrap.createInitialComponentContext(null);
            xmulticomponentfactory = xcomponentcontext.getServiceManager();
            objectUrlResolver = xmulticomponentfactory.createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", xcomponentcontext);
            xurlresolver = (XUnoUrlResolver) UnoRuntime.queryInterface(XUnoUrlResolver.class, objectUrlResolver);
            objectInitial = xurlresolver.resolve("uno:socket,host=" + _host + ",port=" + _port + ";urp;StarOffice.ServiceManager");
            xmulticomponentfactory = (XMultiComponentFactory) UnoRuntime.queryInterface(XMultiComponentFactory.class, objectInitial);
            xpropertysetMultiComponentFactory = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xmulticomponentfactory);
            objectDefaultContext = xpropertysetMultiComponentFactory.getPropertyValue("DefaultContext");
            xcomponentcontext = (XComponentContext) UnoRuntime.queryInterface(XComponentContext.class, objectDefaultContext);
            xcomponentloader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, xmulticomponentfactory.createInstanceWithContext("com.sun.star.frame.Desktop", xcomponentcontext));
            PropertyValue propertyValues[] = new PropertyValue[1];
            propertyValues[0] = new PropertyValue();
            propertyValues[0].Name = "Hidden";
            propertyValues[0].Value = new Boolean(true);
            url = convertToUrl(ifile, xcomponentcontext);
            objectDocumentToStore = xcomponentloader.loadComponentFromURL(url, "_blank", 0, propertyValues);
            if (objectDocumentToStore == null) {
                logger.error("HtmlOpenOfficeConverter.convert( \"Could not load file '" + url + "'\" )");
                return;
            }
            xstorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, objectDocumentToStore);
            convertType = OpenOfficeHelper.getConvertType(OpenOfficeHelper.ConvertType.VIEW, OpenOfficeHelper.getExtension(ifp));
            if (!(MiscUtil.hasString(convertType))) {
                logger.error("HtmlOpenOfficeConverter.convert( \"Could not determine the convert type for '" + ifp + "' type files.\" )");
                return;
            }
            logger.debug("HtmlOpenOfficeConverter.convert( \"Using convert type '" + convertType + "' for '" + url + "' \" )");
            propertyValues = new PropertyValue[2];
            propertyValues[0] = new PropertyValue();
            propertyValues[0].Name = "Overwrite";
            propertyValues[0].Value = new Boolean(true);
            propertyValues[1] = new PropertyValue();
            propertyValues[1].Name = "FilterName";
            propertyValues[1].Value = convertType;
            url = convertToUrl(ofile, xcomponentcontext);
            xstorable.storeToURL(url, propertyValues);
        } finally {
            if (xstorable != null) {
                xcomponent = (XComponent) UnoRuntime.queryInterface(XComponent.class, xstorable);
                if (xcomponent != null) xcomponent.dispose();
            }
        }
        return;
    }

    @Override
    public void deleteConvertedFile(Binder binder, DefinableEntity entry, FileAttachment fa) throws UncheckedIOException, RepositoryServiceException {
        super.deleteConvertedFile(binder, entry, fa, this.HTML_SUBDIR, this.HTML_FILE_SUFFIX);
    }
}
