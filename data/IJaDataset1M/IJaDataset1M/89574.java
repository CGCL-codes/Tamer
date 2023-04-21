package org.xaware.server.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import org.springframework.core.io.Resource;
import org.xaware.server.resources.ResourceHelper;
import org.xaware.shared.util.ThreadLocalClassLoader;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class is a class utility for performing XSL transformations. All XSL transformations within the application
 * should be performed using this utility.
 * 
 * @author Tim Uttormark
 */
public class XSLTransformUtil {

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(XSLTransformUtil.class.getName());

    private static final String classLocation = "XSLTransformUtil";

    /**
     * Constructor. Made private to enforce class utility pattern.
     */
    private XSLTransformUtil() {
    }

    /**
     * Performs a specified XSL transformation against a specified JDOM Document and returns the result of the
     * transformation.
     * 
     * @param sourceDocToTransform
     *            the JDOM Document to be transformed.
     * @param styleSheetFileName
     *            the name of the XSL stylesheet file.
     * @return the JDOM Document which results from applying the XSL transform to the source Document.
     * @throws XAwareException
     *             if the stylesheet file cannot be found or read, or if any failure occurs in the transformation.
     */
    public static Document performXSLTransform(final Document sourceDocToTransform, final String styleSheetFileName) throws XAwareException {
        final Resource styleSheetResource = ResourceHelper.getResource(styleSheetFileName);
        if (!styleSheetResource.exists()) {
            final String errMsg = "XSL stylesheet \"" + styleSheetFileName + "\" not found in resource path.";
            lf.severe(errMsg, classLocation, "performXSLTransform");
            throw new XAwareException(errMsg);
        }
        InputStream styleSheetDocInStream = null;
        try {
            styleSheetDocInStream = styleSheetResource.getInputStream();
            return performXSLTransform(sourceDocToTransform, styleSheetDocInStream);
        } catch (final IOException e) {
            final String errMsg = "Failed to read XSL stylesheet; " + e.getMessage();
            lf.severe(errMsg, classLocation, "performXSLTransform");
            throw new XAwareException(errMsg, e);
        } finally {
            if (styleSheetDocInStream != null) {
                try {
                    styleSheetDocInStream.close();
                } catch (final IOException e) {
                }
            }
            if (styleSheetResource.isOpen()) {
                try {
                    styleSheetResource.getInputStream().close();
                } catch (final IOException e) {
                }
            }
        }
    }

    /**
     * Performs a specified XSL transformation against a specified JDOM Document and returns the result of the
     * transformation.
     * 
     * @param sourceDocToTransform
     *            the JDOM Document to be transformed.
     * @param styleSheetDoc
     *            a JDOM Document of the XSL stylesheet.
     * @return the JDOM Document which results from applying the XSL transform to the source Document.
     * @throws XAwareException
     *             if any failure occurs in the transformation.
     * @throws JDOMException
     *             if a failure occurs transforming the stylesheet from JDOM to DOM.
     */
    public static Document performXSLTransform(final Document sourceDocToTransform, final Document styleSheetDoc) throws XAwareException, JDOMException {
        final DOMOutputter domOutputter = new DOMOutputter();
        final org.w3c.dom.Document domDocument = domOutputter.output(styleSheetDoc);
        final DOMSource domSource = new DOMSource(domDocument);
        return executeXSLTransform(sourceDocToTransform, domSource);
    }

    /**
     * Performs a specified XSL transformation against a specified JDOM Document and returns the result of the
     * transformation.
     * 
     * @param sourceDocToTransform
     *            the JDOM Document to be transformed.
     * @param styleSheetDocInStream
     *            an InputStream to stream in the XSL stylesheet.
     * @return the JDOM Document which results from applying the XSL transform to the source Document.
     * @throws XAwareException
     *             if any failure occurs in the transformation.
     */
    public static Document performXSLTransform(final Document sourceDocToTransform, final InputStream styleSheetDocInStream) throws XAwareException {
        return executeXSLTransform(sourceDocToTransform, new StreamSource(styleSheetDocInStream));
    }

    /**
     * Performs a specified XSL transformation against a specified JDOM Document and returns the result of the
     * transformation.
     * 
     * @param styleSheetContents
     *            a String containing the contents of the XSL stylesheet.
     * @param sourceDocToTransform
     *            the JDOM Document to be transformed.
     * @return the JDOM Document which results from applying the XSL transform to the source Document.
     * @throws XAwareException
     *             if any failure occurs in the transformation.
     */
    public static Document performXSLTransform(final String styleSheetContents, final Document sourceDocToTransform) throws XAwareException {
        return executeXSLTransform(sourceDocToTransform, new StreamSource(new StringReader(styleSheetContents)));
    }

    /**
     * Contains the business logic to execute the XSL transformation.
     * 
     * @param sourceDocToTransform
     *            the JDOM Document to be transformed
     * @param styleSheetSource
     *            the XML source of the XSL file defining the transformation.
     * @return the JDOM Document which results from applying the XSL transform to the source Document.
     * @throws XAwareException
     *             if any failure occurs in the transformation.
     */
    private static Document executeXSLTransform(final Document sourceDocToTransform, final Source styleSheetSource) throws XAwareException {
        final ClassLoader previousCCL = Thread.currentThread().getContextClassLoader();
        try {
            final ClassLoader cl = ThreadLocalClassLoader.get();
            if (cl != null) {
                Thread.currentThread().setContextClassLoader(cl);
            }
            lf.finest("Calling transform", classLocation, "executeXSLTransform");
            final JDOMResult out = new JDOMResult();
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSheetSource);
            transformer.transform(new JDOMSource(sourceDocToTransform), out);
            return out.getDocument();
        } catch (final Exception e) {
            final String errMsg = "XSL transformation failed, " + e;
            lf.severe(errMsg, classLocation, "executeXSLTransform");
            throw new XAwareException(errMsg, e);
        } finally {
            lf.fine("Returned from transform", classLocation, "executeXSLTransform");
            Thread.currentThread().setContextClassLoader(previousCCL);
        }
    }
}
