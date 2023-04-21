package org.simpleframework.http.serve;

import org.simpleframework.http.Response;
import org.simpleframework.http.Request;

/**
 * A <code>Resource</code> provides an abstraction of any given 
 * object that can be retrieved using a HTTP request. The reason
 * for having this abstraction is to simplify the interface with 
 * any given <code>Resource</code>. 
 * <p>
 * This is a <code>ProtocolHandler</code> and as such handles 
 * the entire transaction. This means that the HTTP response
 * message is entirely managed by the <code>Resource</code>.
 *
 * @author Niall Gallagher
 */
public interface Resource {

    /**
    * This acts as the main processing method for the resources.
    * Implementations are required to provide the functions that 
    * will process the <code>Request</code> and generate a suitable
    * response for that request. This method is also responsible
    * for closing and comitting the <code>Response</code> unless
    * handed (chained) to another <code>Resource</code>.
    *
    * @param req the <code>Request</code> to be processed
    * @param resp the <code>Response</code> to be processed    
    */
    public void handle(Request req, Response resp);

    /**
    * When an error occurs while processing the HTTP request then 
    * this method will handle the error according to the suggested 
    * code. The code represents the HTTP response code that caused
    * the error. Any implementation of the <code>Resource</code>
    * must be able to handle a valid HTTP response message.
    *
    * @param req the <code>Request</code> to be processed
    * @param resp the <code>Response</code> to be processed
    * @param code this is the HTTP status code of the response
    */
    public void handle(Request req, Response resp, int code);

    /**
    * This is used to generate a formatted message using a report to
    * describe the change in status. This uses the <code>Report</code> 
    * object to prepare a formatted message that can be presented to
    * the client. This message will describe the status using the 
    * issued <code>Report</code> object. This is provided so that 
    * implementations have an convinient means to report errors.
    *
    * @param req the <code>Request</code> object to be processed
    * @param resp the <code>Response</code> object to be processed
    * @param report this is used to describe the change in status
    */
    public void handle(Request req, Response resp, Report report);
}
