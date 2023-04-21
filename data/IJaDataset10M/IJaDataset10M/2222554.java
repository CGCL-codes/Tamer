package org.xaware.server.engine.instruction.bizcomps.soap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.Resource;
import org.xaware.server.data.soap.MimeContent;
import org.xaware.server.data.soap.MimeMultiPart;
import org.xaware.server.data.soap.MimeXml;
import org.xaware.server.data.soap.SoapEnv;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.exceptions.XAwarePathException;
import org.xaware.server.resources.ResourceHelper;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * @author jweaver
 * 
 */
public class SoapHttpMessageHelper {

    /**
     * 
     */
    public static final String SOAP_ENVELOPE_PART_NAME = "soap_envelope_part";

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger("org.xaware.server.bizcomps.helper.SoapHttpMessageHelper");

    private static final String classLocation = "SoapHttpMessageHelper";

    /**
     * 
     * @param p_serviceAddress
     * @param p_multipartElement
     * @param p_soapData
     * @param p_context
     * @param p_charset
     * 
     * @return
     * @throws JDOMException
     *             If the data from an mimeXml location attribute cannot be parsed.
     * @throws IOException
     *             If there is a problem reading the file.
     * @throws XAwareException
     */
    public static SoapHttpPostMethod createMultipartMessage(final String p_serviceAddress, final MimeMultiPart p_mimeMultiPart, final String p_soapData, final IBizViewContext p_context, final String p_charset) throws JDOMException, IOException, XAwareException {
        final XASoapMultipartPostMethod mpMethod = new XASoapMultipartPostMethod(p_serviceAddress);
        if (p_charset != null) {
            mpMethod.getParams().setContentCharset(p_charset);
        }
        final List parts = p_mimeMultiPart.getAttachments();
        final Part[] entityParts = new Part[parts.size() + 1];
        entityParts[0] = getSoapBodyPart(p_mimeMultiPart.getSoapEnv(), p_soapData, p_charset);
        int i = 1;
        final Iterator partItr = parts.iterator();
        while (partItr.hasNext()) {
            final Object obj = partItr.next();
            if (obj instanceof MimeContent) {
                final MimeContent content = (MimeContent) obj;
                entityParts[i++] = addFilePart(content);
            } else if (obj instanceof MimeXml) {
                final MimeXml mimeXml = (MimeXml) obj;
                entityParts[i++] = addXmlPart(mimeXml, p_context);
            }
        }
        MultipartRequestEntity entity = new SoapMultipartRequestEntity(entityParts, mpMethod.getParams());
        mpMethod.setRequestEntity(entity);
        return mpMethod;
    }

    /**
     * @param p_mpMethod
     * @param p_mimeXml
     * @param p_bizComp
     * @throws JDOMException
     * @throws IOException
     *             If there is a problem reading the file.
     * @throws XAwarePathException
     *             If there is a problem with the path.
     */
    private static Part addXmlPart(final MimeXml p_mimeXml, final IBizViewContext p_context) throws JDOMException, IOException, XAwarePathException {
        final String dataPath = p_mimeXml.getDataPath();
        final String partName = p_mimeXml.getPartName();
        final List children = p_mimeXml.getConfigElement().getChildren();
        final String location = p_mimeXml.getFile();
        Element dataElement = null;
        if (dataPath != null && !"".equals(dataPath.trim())) {
            dataElement = p_context.getElementAtPath(dataPath, p_mimeXml.getConfigElement(), null);
        } else if (location != null && !"".equals(location.trim())) {
            final Resource xmlResource = ResourceHelper.getResource(location);
            final Document inputDoc = ResourceHelper.getJdomDocumentFromResource(xmlResource);
            dataElement = inputDoc.getRootElement();
        } else if (!children.isEmpty()) {
            final Iterator itr = children.iterator();
            while (itr.hasNext()) {
                final Object obj = itr.next();
                if (obj instanceof Element) {
                    dataElement = (Element) obj;
                    break;
                }
            }
        }
        String xml = "";
        if (dataElement != null) {
            xml = elemToString(dataElement);
        }
        final SoapStringPart bodyPart = new SoapStringPart(partName, xml);
        bodyPart.setContentType("text/xml");
        return bodyPart;
    }

    /**
     * Gets the String equivalent of the given Element
     */
    private static final String elemToString(final Element elem) {
        if (elem == null) {
            return "";
        }
        final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        return outputter.outputString(elem);
    }

    /**
     * @param p_soapEnv
     * @param p_soapData
     * @param p_charset
     */
    private static Part getSoapBodyPart(final SoapEnv p_soapEnv, final String p_soapData, final String p_charset) {
        final SoapStringPart bodyPart = new SoapStringPart(SoapHttpMessageHelper.SOAP_ENVELOPE_PART_NAME, p_soapData);
        bodyPart.setContentType("text/xml");
        if (p_charset == null) {
            bodyPart.setCharSet("UTF-8");
        } else {
            bodyPart.setCharSet(p_charset);
        }
        lf.finest("SOAP data to send:" + p_soapData, classLocation, "addStringParts");
        return bodyPart;
    }

    /**
     * @param p_content
     * @throws FileNotFoundException
     *             If the file could not be found at the specified location.
     */
    private static Part addFilePart(final MimeContent p_content) throws FileNotFoundException {
        final String partName = p_content.getPartName();
        final String location = p_content.getFile();
        final Resource soapFileResource = ResourceHelper.getResource(location);
        File data = null;
        try {
            data = soapFileResource.getFile();
        } catch (final IOException e) {
        }
        if ((data == null || (!data.canRead()))) {
            throw new FileNotFoundException("File could not be read from location: " + location);
        }
        final SoapFilePart filePart = new SoapFilePart(partName, data, p_content.getMimeType(), null);
        return filePart;
    }
}
