package org.simpleframework.http;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import org.simpleframework.http.session.Session;

/**
 * This interface is used to represent a HTTP request. This defines 
 * the attributes that a HTTP request has such as a request line and 
 * the headers that come with the message header. 
 * <p>
 * The <code>Request</code> is used to provide an interface to the 
 * HTTP <code>InputStream</code> and message header. The stream can
 * have certain characteristics, these characteristics are available 
 * by this object. The <code>Request</code> provides methods that 
 * allow the <code>InputStream</code>'s semantics to be known, for 
 * example if the stream is keep-alive or if the stream has a length.
 * <p>
 * The <code>Request</code> origin is also retrievable from the
 * <code>Request</code> as is the attributes <code>Map</code> object
 * which defines specific connection attributes. And acts as a 
 * simple model for the request transaction.
 * <p>
 * It is important to note that the <code>Request</code> controls
 * the processing of the HTTP pipeline. The next HTTP request is 
 * not processed until the request has read all of the content body
 * within the <code>InputStream</code>. The stream must be fully
 * read or closed for the next request to be processed. 
 *
 * @author Niall Gallagher
 */
public interface Request extends RequestHeader {

    /**
    * This is a convenience method that is used to determine whether 
    * or not this message has the <code>Connection: close</code> 
    * header. If the close token is present then this stream is not
    * a keep-alive connection. If this has no <code>Connection</code> 
    * header then the keep-alive status is determined by the HTTP
    * version, that is, HTTP/1.1 is keep-alive by default, HTTP/1.0
    * is not keep-alive by default.
    *
    * @return returns true if this has a keep-alive stream
    */
    public boolean isKeepAlive();

    /**
    * This is used to acquire all the form parameters from the
    * HTTP request. This includes the query and POST data values
    * as well as the parts of a multipart request. The form is 
    * a convenience object enabling easy access to state.
    * 
    * @return this returns the form containing the state
    * 
    * @throws Exception thrown if it could not be acquired
    */
    public Form getForm() throws Exception;

    /**
    * This is used to provide quick access to the parameters. This
    * avoids having to acquire the request <code>Form</code> object.
    * This basically acquires the parameters object and invokes 
    * the <code>getParameters</code> method with the given name.
    * 
    * @param name this is the name of the parameter value
    *
    * @exception Exception thrown if there is an I/O problem       
    */
    public String getParameter(String name) throws Exception;

    /**
    * This method is used to acquire a <code>Part</code> from the
    * form using a known name for the part. This is typically used 
    * when there is a file upload with a multipart POST request.
    * All parts that are not files are added to the query values
    * as strings so that they can be used in a convenient way.
    * 
    * @param name this is the name of the part to acquire
    * 
    * @return the named part or null if the part does not exist
    */
    public Part getPart(String name) throws Exception;

    /**
    * This can be used to retrieve the response attributes. These can
    * be used to keep state with the response when it is passed to
    * other systems for processing. Attributes act as a convenient
    * model for storing objects associated with the response. This 
    * also inherits attributes associated with the client connection.
    *
    * @return the attributes of this <code>Response</code> object
    */
    public Map getAttributes();

    /**
    * This is used as a shortcut for acquiring attributes for the
    * response. This avoids acquiring the attribute <code>Map</code>
    * in order to retrieve the attribute directly from that object.
    * The attributes contain data specific to the response.
    * 
    * @param key this is the key of the attribute to acquire
    * 
    * @return this returns the attribute for the specified name
    */
    public Object getAttribute(Object key);

    /**
    * This is used to acquire the remote client address. This can 
    * be used to acquire both the port and the I.P address for the 
    * client. It allows the connected clients to be logged and if
    * require it can be used to perform course grained security.
    * 
    * @return this returns the client address for this request
    */
    public InetSocketAddress getClientAddress();

    /**
    * This is used to get the content body. This will essentially get
    * the content from the body and present it as a single string.
    * The encoding of the string is determined from the content type
    * charset value. If the charset is not supported this will throw
    * an exception. Typically only text values should be extracted
    * using this method if there is a need to parse that content.
    *     
    * @return this returns the message bytes as an encoded string
    */
    public String getContent() throws Exception;

    /**
    * This is used to read the content body. The specifics of the data
    * that is read from this <code>InputStream</code> can be determined
    * by the <code>getContentLength</code> method. If the data sent by
    * the client is chunked then it is decoded, see RFC 2616 section
    * 3.6. Also multipart data is available as <code>Part</code> objects
    * however the raw content of the multipart body is still available.
    *
    * @return this returns an input stream containing the message body
    */
    public InputStream getInputStream() throws Exception;

    /**
    * This is used to read the content body. The specifics of the data
    * that is read from this <code>ReadableByteChannel</code> can be 
    * determined by the <code>getContentLength</code> method. If the 
    * data sent by the client is chunked then it is decoded, see RFC 
    * 2616 section 3.6. This stream will never provide empty reads as
    * the content is internally buffered, so this can do a full read.
    * 
    * @return this returns the byte channel used to read the content
    */
    public ReadableByteChannel getByteChannel() throws Exception;

    /**
    * This method is used to acquire a <code>Session</code> for the
    * request. The object retrieved provides a container for data
    * associated to the connected client. This allows the request
    * to perform more complex operations based on knowledge that is
    * built up through a series of requests. The session is known
    * to the system using a <code>Cookie</code>, which contains
    * the session reference. This cookie value should not be 
    * modified as it used to reference the active session object.
    *
    * @return returns an active <code>Session</code> object
    */
    public Session getSession() throws Exception;

    /**
    * This method is used to acquire a <code>Session</code> for the
    * request. The object retrieved provides a container for data
    * associated to the connected client. This allows the request
    * to perform more complex operations based on knowledge that is
    * built up through a series of requests. The session is known
    * to the system using a <code>Cookie</code>, which contains
    * the session reference. This cookie value should not be 
    * modified as it used to reference the active session object.
    *
    * @param create creates the session if it does not exist
    *
    * @return returns an active <code>Session</code> object
    */
    public Session getSession(boolean create) throws Exception;
}
