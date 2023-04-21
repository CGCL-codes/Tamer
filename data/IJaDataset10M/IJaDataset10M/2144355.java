package soapdust;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import soapdust.wsdl.Definition;
import soapdust.wsdl.Message;
import soapdust.wsdl.Operation;
import soapdust.wsdl.Part;
import soapdust.wsdl.Type;
import soapdust.wsdl.WebServiceDescription;
import soapdust.wsdl.WsdlParser;

/**
 * Instances of this class allow to query a remote soap server.
 */
public class Client {

    private URL endPointUrl;

    private String password;

    private String userName;

    private boolean wsdlSet;

    private static Map<URL, WebServiceDescription> WSDL_CACHE = new WeakHashMap<URL, WebServiceDescription>();

    private WebServiceDescription serviceDescription;

    static {
        String handlers = System.getProperty("java.protocol.handler.pkgs");
        if (handlers == null) handlers = "";
        handlers = handlers + "|soapdust.urlhandler";
        System.setProperty("java.protocol.handler.pkgs", handlers);
    }

    /**
	 * Sets a wsdl url for this client. It is the url this client 
	 * will use to get the wsdl describing the remote soap service.
	 * 
	 * You must set this url before being able to call remote service
	 * or get explanation about remote service.
	 * 
	 * Getting wdsl and analysing them is an expensive operation.
	 * For this reason, a cache of service descriptions is shared
	 * among all Client instances. The key in this cache is the
	 * wsdl url.
	 * 
	 * This method will try to get the service description from the
	 * cache first, using the wsdl url as key. Then, only if no 
	 * service description can be found, will it get the wsdl from 
	 * the url and analyse it to store service description in the 
	 * cache. 
	 * 
	 * This cache mechanism is thread safe.
	 * 
	 * The cache uses a WeakHashMap so that its entries will be 
	 * automatically dropped from memory if not used or memory needed.
	 * 
	 * @see explain
	 * @see call
	 * 
	 * @see WeakHashMap
	 * 
	 * @param wsdlUrl the url to get wsdl from.
	 * @throws IOException if the wsdl is not reachable for any reason.
	 * @throws MalformedWsdlException if it can not analyse the wsdl.
	 */
    public void setWsdlUrl(String wsdlUrl) throws IOException, MalformedWsdlException {
        synchronized (WSDL_CACHE) {
            serviceDescription = WSDL_CACHE.get(new URL(wsdlUrl));
            if (serviceDescription == null) parseWsdl(new URL(wsdlUrl));
        }
        this.wsdlSet = true;
    }

    /**
	 * Set the url of the remote service to query.
	 * 
	 * You must set this url before trying to call any method.
	 * 
	 * @param url of the end point of the remote service.
	 * @throws MalformedURLException
	 */
    public void setEndPoint(String url) throws MalformedURLException {
        this.endPointUrl = new URL(url);
    }

    /**
	 * Displays a description of the remote soap service from its wsdl.
	 * You must set a wsdl url before being able to call this method.
	 * The description is written in out.
	 * You are responsible to close out.
	 * 
	 * Ex:
	 * <code> client.explain(System.out); </code>
	 * 
	 * @param out the Writer to print description to.
	 * @throws IOException
	 */
    public void explain(Writer out) throws IOException {
        if (!this.wsdlSet) throw new IllegalStateException("you must set a wsdl url to get an explanation...");
        BufferedWriter bout = new BufferedWriter(out);
        if (bout != null) {
            for (Definition definition : serviceDescription.getDefinitions()) {
                for (Operation operation : definition.operations.values()) {
                    bout.write(operation.name);
                    printInput(bout, "\t", operation);
                    bout.newLine();
                }
                bout.flush();
            }
        }
    }

    /**
	 * @see explain(Writer)
	 * @param out
	 * @throws IOException
	 */
    public void explain(OutputStream out) throws IOException {
        explain(new OutputStreamWriter(out));
    }

    /**
	 * Calls a remote operation without parameters.
	 * 
	 * The operation is identified by the given operation parameter.
	 * 
	 * @see explain to have information about the remote service available operations.
	 * 
	 * @param operation the remote operation to call
	 * @return the response of the remote server in a ComposedValue
	 * @throws FaultResponseException if the remote server returns a SOAP fault
	 * @throws IOException in case of problem during communication with remote server
	 * @throws MalformedResponseException if the remote server returns a malformed, 
	 * non-soap response.
	 */
    public ComposedValue call(String operation) throws FaultResponseException, IOException, MalformedResponseException {
        return call(operation, new ComposedValue());
    }

    /**
	 * Calls a remote operation.
	 * 
	 * The operation is identified by the given operation parameter.
	 *
	 * Parameters to use for the operation are specified in the given parameters.
	 * 
	 * @see explain to have information about the remote service available operations
	 * and expected parameters.
	 * 
	 * @param operation the remote operation to call
	 * @param parameters the parameters to transmit
	 * @return the response of the remote server in a ComposedValue
	 * @throws FaultResponseException if the remote server returns a SOAP fault
	 * @throws IOException in case of problem during communication with remote server
	 * @throws MalformedResponseException if the remote server returns a malformed, 
	 * non-soap response.
	 */
    public ComposedValue call(String operation, ComposedValue parameters) throws FaultResponseException, IOException, MalformedResponseException {
        if (!this.wsdlSet) throw new IllegalStateException("you must set a wsdl url before trying to call a remote method...");
        if (this.endPointUrl == null) throw new IllegalStateException("you must set an end point url before trying to call a remote method...");
        HttpURLConnection connection = initHttpConnection(this.endPointUrl);
        addSoapAction(connection, operation);
        try {
            Document request = new RequestBuilder(serviceDescription).build(operation, parameters);
            sendRequest(request, connection);
            return readResponse(connection);
        } finally {
            connection.disconnect();
        }
    }

    /**
	 * Set a user name for BASIC-AUTH authentication.
	 * @param userName
	 */
    public void setUsername(String userName) {
        this.userName = userName;
    }

    /**
	 * Set a password for BASIC-AUTH authentication.
	 * @param password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * Avoid using this method. Prefer setWsdlUrl() instead.
	 * 
	 * This method is only usefull if you want to override a service description
	 * previously stored in cache for the given wsdlUrl.
	 * 
	 * @see setWsdlUrl
	 * 
	 * @param wsdlUrl
	 * @throws IOException
	 * @throws MalformedWsdlException
	 */
    public void setWsdlUrlOverrideCache(String wsdlUrl) throws IOException, MalformedWsdlException {
        parseWsdl(new URL(wsdlUrl));
        this.wsdlSet = true;
    }

    /**
	 * Override this method if you want to customize the http connection.
	 * For instance you may set a connect or read timeout.
	 * @param connection the HttpURLConnection to customize
	 */
    protected void customizeHttpConnectionBeforeCall(HttpURLConnection connection) {
    }

    private void addSoapAction(HttpURLConnection connection, String operationName) {
        Operation operation = serviceDescription.findOperation(operationName);
        if (operation.soapAction != null) {
            connection.addRequestProperty("SOAPAction", operation.soapAction);
        }
    }

    private void printInput(BufferedWriter bout, String indentation, Operation operation) throws IOException {
        Message input = operation.input;
        if (input == null) return;
        switch(operation.style) {
            case Operation.STYLE_DOCUMENT:
                for (Part part : input.getParts()) {
                    printType(bout, indentation, part.type);
                }
                break;
            case Operation.STYLE_RPC:
            default:
                for (Part part : input.getParts()) {
                    bout.newLine();
                    bout.write(indentation);
                    bout.write(part.name);
                    printType(bout, indentation + "\t", part.type);
                }
                break;
        }
    }

    private void printType(BufferedWriter bout, String indentation, Type type) throws IOException {
        if (type == null) return;
        bout.newLine();
        bout.write(indentation);
        bout.write(type.name);
        for (Type subType : type.getTypes()) {
            printType(bout, indentation + "\t", subType);
        }
    }

    private ComposedValue readResponse(HttpURLConnection connection) throws FaultResponseException, IOException, MalformedResponseException {
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            int responseCode = connection.getResponseCode();
            if (responseCode != 200 && responseCode != -1) {
                InTrace inTrace = new InTrace(connection.getErrorStream());
                throw new ResponseParser().parseFault(inTrace.in, responseCode, inTrace.trace);
            } else {
                throw e;
            }
        }
        handleResponseCode(connection);
        InTrace inTrace = new InTrace(inputStream);
        return new ResponseParser().parse(inTrace.in, inTrace.trace);
    }

    private void handleResponseCode(HttpURLConnection connection) throws IOException, MalformedResponseException {
        int responseCode = connection.getResponseCode();
        String errorMessage = "unsupported HTTP response code " + responseCode;
        switch(responseCode) {
            case 200:
                return;
            case 302:
                errorMessage += " Location: " + connection.getHeaderField("Location");
            default:
                throw new MalformedResponseException(errorMessage, responseCode, readFully(connection.getInputStream()));
        }
    }

    private void sendRequest(Document message, HttpURLConnection connection) throws IOException {
        OutputStream out = connection.getOutputStream();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(message), new StreamResult(out));
            out.flush();
        } catch (TransformerException e) {
            throw new RuntimeException("Unexpected exception while sending soap request to server: " + e, e);
        } finally {
            out.close();
        }
    }

    private HttpURLConnection initHttpConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        addAuthenticationIfNeeded(connection);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestProperty("Accept", "*/*");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        customizeHttpConnectionBeforeCall(connection);
        return connection;
    }

    private void addAuthenticationIfNeeded(HttpURLConnection connection) {
        if (userName == null && password == null) {
            return;
        }
        if (userName != null && password == null) {
            throw new NullPointerException("userName is not null: password can not be null");
        }
        if (password != null && userName == null) {
            throw new NullPointerException("password is not null: userName can not be null");
        }
        String authenticationString = BASE64Encoder.encode((userName + ":" + password));
        connection.setRequestProperty("Authorization", "Basic " + authenticationString);
    }

    private void parseWsdl(URL wsdlUrl) throws IOException, MalformedWsdlException {
        try {
            serviceDescription = new WsdlParser(wsdlUrl).parse();
            synchronized (WSDL_CACHE) {
                WSDL_CACHE.put(wsdlUrl, serviceDescription);
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unexpected exception while \"analysing\" wsdl: " + e, e);
        } catch (SAXException e) {
            throw new MalformedWsdlException("Unable to \"analyse\" the specified wsdl: " + e, e);
        }
    }

    private static volatile boolean traceMode = false;

    /**
	 * Avoid using this method if you are not interested in the
	 * result. This may have performance penalty.
	 * 
	 * Change the trace mode status for all Client instances. 
	 * The trace mode defaults to false.
	 * 
	 * When trace mode is activated, every MalformedResponseException
	 * thrown by a Client will contain the data (as a byte array)
	 * received from the remote server.
	 * 
	 * This may have a performance penalty since it implies that all
	 * the data sent by the server be saved before trying to parse it.
	 * 
	 * @param active
	 */
    public static synchronized void activeTraceMode(boolean active) {
        traceMode = active;
    }

    private class InTrace {

        byte[] trace;

        InputStream in;

        InTrace(InputStream in) throws IOException {
            if (traceMode) {
                this.trace = readFully(in);
                this.in = new ByteArrayInputStream(trace);
            } else {
                this.in = in;
            }
        }
    }

    private byte[] readFully(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        for (int read = in.read(buffer, 0, buffer.length); read != -1; read = in.read(buffer, 0, buffer.length)) {
            content.write(buffer, 0, read);
        }
        return content.toByteArray();
    }
}
