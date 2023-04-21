package org.apache.axis2.builder;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.servlet.http.HttpServletRequest;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.util.MultipleEntryHashMap;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

public class MultipartFormDataBuilder implements Builder {

    /**
     * @return Returns the document element.
     */
    public OMElement processDocument(InputStream inputStream, String contentType, MessageContext messageContext) throws AxisFault {
        MultipleEntryHashMap parameterMap;
        HttpServletRequest request = (HttpServletRequest) messageContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
        if (request == null) {
            throw new AxisFault("Cannot create DocumentElement without HttpServletRequest");
        }
        String charSetEncoding = (String) messageContext.getProperty(Constants.Configuration.CHARACTER_SET_ENCODING);
        if (charSetEncoding == null) {
            charSetEncoding = request.getCharacterEncoding();
        }
        try {
            parameterMap = getParameterMap(request, charSetEncoding);
            return BuilderUtil.buildsoapMessage(messageContext, parameterMap, OMAbstractFactory.getSOAP12Factory());
        } catch (FileUploadException e) {
            throw AxisFault.makeFault(e);
        }
    }

    private MultipleEntryHashMap getParameterMap(HttpServletRequest request, String charSetEncoding) throws FileUploadException {
        MultipleEntryHashMap parameterMap = new MultipleEntryHashMap();
        List items = parseRequest(new ServletRequestContext(request));
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            DiskFileItem diskFileItem = (DiskFileItem) iter.next();
            boolean isFormField = diskFileItem.isFormField();
            Object value;
            try {
                if (isFormField) {
                    value = getTextParameter(diskFileItem, charSetEncoding);
                } else {
                    value = getFileParameter(diskFileItem);
                }
            } catch (Exception ex) {
                throw new FileUploadException(ex.getMessage());
            }
            parameterMap.put(diskFileItem.getFieldName(), value);
        }
        return parameterMap;
    }

    private static List parseRequest(ServletRequestContext requestContext) throws FileUploadException {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        return upload.parseRequest(requestContext);
    }

    private String getTextParameter(DiskFileItem diskFileItem, String characterEncoding) throws Exception {
        String encoding = diskFileItem.getCharSet();
        if (encoding == null) {
            encoding = characterEncoding;
        }
        String textValue;
        if (encoding == null) {
            textValue = new String(diskFileItem.get());
        } else {
            textValue = new String(diskFileItem.get(), encoding);
        }
        return textValue;
    }

    private DataHandler getFileParameter(DiskFileItem diskFileItem) throws Exception {
        DataSource dataSource = new DiskFileDataSource(diskFileItem);
        DataHandler dataHandler = new DataHandler(dataSource);
        return dataHandler;
    }
}
