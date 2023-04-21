package org.simpleframework.http.connect;

import org.simpleframework.http.Pipeline;
import org.simpleframework.http.PipelineFactory;
import org.simpleframework.http.PipelineHandler;
import org.simpleframework.util.lease.LeaseManager;
import org.simpleframework.util.lease.Cleaner;
import org.simpleframework.util.lease.Lease;
import java.net.Socket;

/**
 * The <code>SocketHandler</code> object is used in conjunction 
 * with the <code>Connection</code> to dispatch <code>Pipeline</code>
 * objects to a <code>PipelineHandler</code> for incoming TCP 
 * connections. The <code>PipelineFactory</code> object can be used
 * to dispatch specialized <code>Pipeline</code> objects to the 
 * pipeline handler. This is useful when buffering is required for 
 * performance or when security measures are required.
 *
 * @author Niall Gallagher
 */
public class SocketHandler {

    /**
	* This is used to manage leases for the sockets used.
	*/
    protected LeaseManager<Socket> manager;

    /**
    * This will produce objects with desired functionality.
    */
    protected PipelineFactory factory;

    /**
    * The handler for the TCP connections established.
    */
    protected PipelineHandler handler;

    /**
    * This is the cleaner that is used to clean sockets.
    */
    protected Terminator cleaner;

    /**
    * This constructor creates a <code>SocketHandler</code> using 
    * a <code>PipelineHandler</code> object. The handler will 
    * dispatch <code>Pipeline</code> objects to the given
    * <code>PipelineHandler</code> once a socket is issued.
    *
    * @param handler the <code>PipelineHandler</code> used
    * to process the connections
    */
    public SocketHandler(PipelineHandler handler) {
        this(handler, null);
    }

    /**
    * This constructor creates a <code>SocketHandler</code> using 
    * a <code>PipelineHandler</code> object. The processor will 
    * dispatch <code>Pipeline</code> objects to the given
    * <code>PipelineHandler</code> once a socket is issued.
    * <p>
    * The <code>Pipeline</code>'s are created once a socket is
    * given to the <code>PipelineFactory</code>. This enables
    * the handler to attach special functionality to the 
    * pipelines dispatched to the <code>PipelineHandler</code>.
    *
    * @param handler the <code>PipelineHandler</code> used    
    * to process the connections
    * @param factory this is the factory for implementations 
    * of the <code>Pipeline</code> object
    */
    public SocketHandler(PipelineHandler handler, PipelineFactory factory) {
        this.cleaner = new Terminator(this);
        this.manager = new LeaseManager<Socket>(cleaner);
        this.handler = handler;
        this.factory = factory;
    }

    /**
    * Once the <code>Socket</code> has been configured it can be
    * used to create a <code>Pipeline</code> object. This object
    * is then given to the <code>PipelineHandler</code> so that
    * the connection can be processed and given to the 
    * <code>ProtocolHandler</code> so the HTTP request can be
    * processed.
    *
    * @param sock this is the connected socket that will be used
    * to create the <code>Pipeline</code>
    *
    * @exception Exception if the socket could not be processed
    */
    public void process(Socket sock) throws Exception {
        Pipeline pipe = create(sock);
        handler.process(pipe);
    }

    /**
    * This will perform the cleaning of the specified socket by
    * simply closing the socket down. If the socket close causes
    * an exception to be thrown the exception is smothered. This
    * is invoked when the lease for the socket has expired.
    * 
    * @param sock the socket that has had its lease expired
    */
    protected void terminate(Socket sock) throws Exception {
        try {
            sock.close();
        } catch (Throwable e) {
            return;
        }
    }

    /**
    * This method is used to create a socket an lease that socket
    * for a fixed peroid of time. By default the created lease is
    * for 10 seconds, which is sufficient time for the server to
    * wait for the first request to arrive from the client.
    * 
    * @param sock this is the socket to wrap within a pipeline
    * 
    * @return returns a pipeline with the lease for the socket
    * 
    * @throws Exception thrown if there is a creation problem
    */
    protected Pipeline create(Socket sock) throws Exception {
        Lease lease = manager.lease(sock, 10000);
        if (factory == null) {
            return new Pipeline(sock, lease);
        }
        return factory.getInstance(sock, lease);
    }

    /**
    * This is the delegate that is used to recieve all notification 
    * from the lease manager so that sockets can be cleaned. This 
    * will delegate to the <code>terminate</code> method so that 
    * the implementation can be overridden by a subclass object.
    * 
    * @see org.simpleframework.util.lease.Cleaner
    */
    private class Terminator implements Cleaner<Socket> {

        /**
	    * This represents the socket handler to terminate with.
	    */
        private SocketHandler handler;

        /**
	    * Constructor for the <code>Terminator</code> object. This
	    * is used to delegate to a socket handler for socket close
	    * events. This basically hides the lease framework from 
	    * within the socket handler to simplify the process.
	    * 
	    * @param handler this is what will terminate sockets
	    */
        public Terminator(SocketHandler handler) {
            this.handler = handler;
        }

        /**
	    * This is invoked by the lease manager when the socket has
	    * expired. This occurs if either the lease is canceled or 
	    * if the lease has not been renewed from the last duration.
	    * 
	    * @param sock this is the socket that is to be terminated
	    */
        public void clean(Socket sock) {
            try {
                handler.terminate(sock);
            } catch (Throwable e) {
                return;
            }
        }
    }
}
