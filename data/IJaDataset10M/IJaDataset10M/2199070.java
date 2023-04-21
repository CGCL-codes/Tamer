package org.simpleframework.http.serve;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.simpleframework.http.Response;
import org.simpleframework.http.Request;

/**
 * This is an abstract <code>Resource</code> that handles the basic 
 * HTTP status reports. For example messages like '404 Not Found' 
 * are represented using this abstraction. This <code>Resource</code> 
 * can be subclassed to give a <code>Resource</code> the ability to 
 * handle all generic status reports for 3xx, 4xx and 5xx defined by
 * RFC 2616.
 * <p>
 * This uses a various <code>Report</code> objects to generate error
 * and status messages using the <code>Format</code> supplied. This
 * also enables exceptions that propagate from the service objects
 * to be classified and for descriptions of those exceptions to be
 * represented as <code>Report</code> objects that can be be used to
 * generate a formatted message that can be presented to the client.
 *
 * @author Niall Gallagher
 */
public abstract class Component implements Resource {

    /**
    * The <code>Context</code> that this resource is in.
    */
    protected Context context;

    /**
    * Constructor that creates a <code>Component</code> without
    * any <code>Context</code> object. This is used so that if the
    * resource is a <code>ProtocolHandler</code> or some other form
    * of resource that does not require a context then this will
    * allow that resource to inherit the functionality of this. The
    * typical implementation however will not use this constructor.
    * If this is used and a context is not set then there will be
    * an exception in <code>handle(Request,Response,int)</code>.
    */
    protected Component() {
        super();
    }

    /**
    * Constructor for the <code>Component</code> is given the
    * <code>Context</code> so that it can generate status reports.
    * Every implementation of the <code>Component</code> needs
    * to be constructed with a <code>Context</code> to ensure that
    * the generation of error and status messages is successful.
    *
    * @param context the context that this resource is in
    */
    protected Component(Context context) {
        this.context = context;
    }

    /**
    * This <code>handle</code> is provided so that if any errors
    * occur when processing a HTTP transaction a '500 Server Error'
    * message will be sent to the client. This is used to invoke
    * the <code>process</code> method which subclasses should 
    * implement to process the HTTP transaction. If the exception
    * is a <code>SecurityException</code> '403 Forbidden' is used
    * and a <code>FileNotFoundException</code> is '404 Not Found'.
    * <p>
    * Any <code>Exception</code> thrown from the <code>process</code>
    * method will be captured and the <code>Request</code> and 
    * <code>Response</code> are handled by the default error handler
    * method <code>handle(Request,Response,int)</code> with the code
    * 500 which indicates the HTTP/1.1 error message 'Server Error'
    * <p>
    * This does not throw ant <code>Exception</code> however any
    * user should handle <code>RuntimeException</code>'s that may
    * be thrown from <code>handle(Request,Response,int)</code>.
    *
    * @param req the <code>Request</code> object to be processed
    * @param resp the <code>Response</code> object to be processed
    */
    public void handle(Request req, Response resp) {
        try {
            process(req, resp);
        } catch (SecurityException cause) {
            handle(req, resp, new ErrorReport(cause, 403));
        } catch (FileNotFoundException cause) {
            handle(req, resp, new ErrorReport(cause, 404));
        } catch (Throwable cause) {
            handle(req, resp, new ErrorReport(cause, 500));
        }
    }

    /**
    * This is used to generate the status report from a status code.
    * The set of status reports that can have a valid response are the 
    * messages that are defined in RFC 2616. If the status code given 
    * does not have a valid entry then this will result in an status
    * message description of 'Unknown'.
    * <p>
    * This does not throw <code>Exception</code>'s but users should
    * be prepared to handle any <code>RuntimeException</code>'s that 
    * could propagate from this. If the <code>Response</code> has
    * been committed then this will return quietly. Typically there
    * will be an <code>IOException</code> writing the content body if
    * the stream has been closed. This will not report such exceptions.
    *
    * @param req the <code>Request</code> object to be processed
    * @param resp the <code>Response</code> object to be processed
    * @param code this is the HTTP status code of the response
    */
    public void handle(Request req, Response resp, int code) {
        handle(req, resp, new StatusReport(code));
    }

    /**
    * This is used to generate a formatted message using a report to
    * describe the change in status. This uses the <code>Format</code> 
    * object to prepare a formatted message that can be presented to
    * the client. This message will describe the status using the 
    * issued <code>Report</code> obejct. If the status code given 
    * does not have a valid entry then this will result in an status
    * message description of 'Unknown'.
    * <p>
    * This does not throw <code>Exception</code>'s but users should
    * be prepared to handle any <code>RuntimeException</code>'s that 
    * could propagate from this. If the <code>Response</code> has
    * been committed then this will return quietly. Typically there
    * will be an <code>IOException</code> writing the content body if
    * the stream has been closed. This will not report such exceptions.
    *
    * @param req the <code>Request</code> object to be processed
    * @param resp the <code>Response</code> object to be processed
    * @param report this is used to describe the change in status
    */
    public void handle(Request req, Response resp, Report report) {
        try {
            if (!resp.isCommitted()) {
                resp.reset();
                process(req, resp, report);
            }
            resp.getOutputStream().close();
        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * This method is used to handle the HTTP status reports so that 
    * if any <code>Exception</code> occurs the <code>handle</code>
    * method can capture and deal with the exception.
    * <p>
    * Subclasses should treat this as the <code>handle</code> method 
    * of the <code>ProtocolHandler</code>. Exceptions that cannot be
    * recovered from, particularly <code>RuntimeException</code>'s
    * should be left propagate so the <code>handle</code> method can
    * deal with the <code>Exception</code> appropriately.
    *
    * @param req the <code>Request</code> object to be processed
    * @param resp the <code>Response</code> object to be processed
    * @param code this is the HTTP status code of the response
    *
    * @exception Exception this can throw an error for anything
    */
    protected void process(Request req, Response resp, int code) throws Exception {
        process(req, resp, new StatusReport(code));
    }

    /**
    * This method is used to handle the HTTP status reports so that 
    * if any <code>Exception</code> occurs the <code>handle</code>
    * method can capture and deal with the exception.
    * <p>
    * Subclasses should treat this as the <code>handle</code> method 
    * of the <code>ProtocolHandler</code>. Exceptions that cannot be
    * recovered from, particularly <code>RuntimeException</code>'s
    * should be left propagate so the <code>handle</code> method can
    * deal with the <code>Exception</code> appropriately.
    *
    * @param req the <code>Request</code> object to be processed
    * @param resp the <code>Response</code> object to be processed
    * @param report this is used to describe the change in status
    *
    * @exception Exception this can throw an error for anything
    */
    protected void process(Request req, Response resp, Report report) throws Exception {
        Format format = context.getFormat();
        byte[] page = format.getMessage(context, req.getURI(), report);
        resp.setCode(report.getCode());
        resp.setText(report.getText());
        resp.setDate("Date", System.currentTimeMillis());
        resp.setContentLength(page.length);
        resp.set("Content-Type", format.getContentType());
        resp.getOutputStream().write(page);
        resp.getOutputStream().close();
    }

    /**
    * This method is used to handle the HTTP transaction by subclasses
    * of the <code>Component</code>. This is used so that if there
    * are any <code>Exception</code>'s thrown while processing the
    * HTTP transaction they can be captured and the HTTP status line
    * will convey the status to the client.
    * <p>
    * Subclasses should treat this as the <code>handle</code> method 
    * of the <code>ProtocolHandler</code>. Exceptions that cannot be
    * recovered from, particularly <code>RuntimeException</code>'s
    * should be left propagate so the <code>handle</code> method can
    * prepare an appropriate response.
    *
    * @param req the <code>Request</code> object to be processed
    * @param resp the <code>Response</code> object to be processed
    *
    * @exception Exception this can throw an error for anything
    */
    protected abstract void process(Request req, Response resp) throws Exception;
}
