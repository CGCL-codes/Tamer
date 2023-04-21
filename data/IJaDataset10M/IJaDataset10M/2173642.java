package com.jaxws.json.codec.doc.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import com.jaxws.json.codec.JSONCodec;
import com.jaxws.json.codec.doc.AbstractHttpMetadataProvider;
import com.jaxws.json.codec.doc.HttpMetadataProvider;
import com.jaxws.json.codec.doc.JSONHttpMetadataPublisher;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.transport.http.WSHTTPConnection;

/**
 * @author Sundaramurthi Saminathan
 * @since JSONWebservice codec version 0.4
 * @version 1.0
 * 
 */
public class MethodFormProvider extends AbstractHttpMetadataProvider implements HttpMetadataProvider {

    private static final String[] queries = new String[] { "form" };

    /**
	 * Cached form content
	 * <Endpoint,<operation name,content>>
	 */
    private static final Map<QName, Map<String, String>> operationDocuments = Collections.synchronizedMap(new HashMap<QName, Map<String, String>>());

    /**
	 * Request recived codec holder.
	 */
    private JSONCodec codec = null;

    /**
	 * "config" query handled.
	 */
    public String[] getHandlingQueries() {
        return queries;
    }

    /**
	 * Document request with config query handled by ServiceConfigurationServer
	 * 
	 */
    public boolean canHandle(String queryString) {
        return queryString != null && queryString.startsWith(queries[0]);
    }

    public void setJSONCodec(JSONCodec codec) {
        this.codec = codec;
    }

    public String getContentType() {
        return "text/html; charset=\"utf-8\"";
    }

    /**
	 * Init configuration holder
	 */
    public void process() {
        WSEndpoint<?> endPoint = this.codec.getEndpoint();
        JAXBContextImpl context = (JAXBContextImpl) endPoint.getSEIModel().getJAXBContext();
        WSDLPort port = endPoint.getPort();
        if (!operationDocuments.containsKey(port.getBinding().getName())) {
            BufferedReader ins = new BufferedReader(new InputStreamReader(MethodFormProvider.class.getResourceAsStream("methodForm.htm")));
            StringBuffer content = new StringBuffer();
            try {
                String line = ins.readLine();
                while (line != null) {
                    content.append(line + "\n");
                    line = ins.readLine();
                }
            } catch (Throwable th) {
            }
            Map<String, String> contents = new HashMap<String, String>();
            SEIModel seiModel = endPoint.getSEIModel();
            for (WSDLBoundOperation operation : seiModel.getPort().getBinding().getBindingOperations()) {
                String requestJSON = JSONHttpMetadataPublisher.getJSONAsString(operation.getInParts(), context, this.codec);
                contents.put(operation.getOperation().getName().getLocalPart(), content.toString().replaceAll("#INPUT_JSON#", String.format("{\"%s\":%s}", operation.getName().getLocalPart(), requestJSON)).replaceAll("#METHOD_NAME#", operation.getName().getLocalPart()));
            }
            operationDocuments.put(port.getBinding().getName(), contents);
        }
    }

    public void doResponse(WSHTTPConnection ouStream) throws IOException {
        process();
        String oper = ouStream.getQueryString().substring(4);
        if (!oper.isEmpty()) doResponse(ouStream, operationDocuments.get(this.codec.getEndpoint().getPort().getBinding().getName()).get(oper)); else ouStream.getOutput().write("add operation name in query string after 'form'. formxxxx E.g ?formgetChart".getBytes());
        ouStream.getOutput().flush();
    }

    public int compareTo(HttpMetadataProvider o) {
        if (o.equals(this)) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }
}
