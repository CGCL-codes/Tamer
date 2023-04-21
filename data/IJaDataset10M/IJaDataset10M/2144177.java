package net.sf.doolin.cs.support;

import java.io.IOException;
import java.net.Socket;
import net.sf.doolin.cs.SocketHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * Implementation of a {@link SocketHandler} that reads the content of a
 * {@link Socket} as a serialized {@link RemoteInvocation}, calls an associated
 * {@link OODelegate} and serializes the returned {@link RemoteInvocationResult}
 * back in the {@link Socket} response.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class OOSocketHandler implements SocketHandler {

    private static final Log log = LogFactory.getLog(OOSocketHandler.class);

    private final String serverName;

    private final Socket socket;

    private final int socketCount;

    private final SocketSupport socketSupport;

    private final OODelegate delegate;

    /**
	 * Constructor
	 * 
	 * @param serverName
	 *            Server name (for debug purpose)
	 * @param socket
	 *            Socket to handle
	 * @param socketCount
	 *            Sequential number for this socket (for debug purpose)
	 * @param delegate
	 *            Associated delegate for the actual invocation
	 */
    public OOSocketHandler(String serverName, Socket socket, int socketCount, OODelegate delegate) {
        this.serverName = serverName;
        this.socket = socket;
        this.socketCount = socketCount;
        this.socketSupport = new SocketSupport(socket);
        this.delegate = delegate;
    }

    @Override
    public void run() {
        log.debug(String.format("[%s] Handling socket #%d", this.serverName, this.socketCount));
        try {
            RemoteInvocation invocation;
            try {
                invocation = this.socketSupport.read();
            } catch (IOException e) {
                log.error(String.format("[%s][#%d] Error reading input message", this.serverName, this.socketCount), e);
                error(e);
                return;
            }
            log.debug(String.format("[%s][#%d] Receiving:%n%s", this.serverName, this.socketCount, invocation.getMethodName()));
            RemoteInvocationResult result;
            try {
                result = handle(invocation);
                log.debug(String.format("[%s][#%d] Responding:%n%s", this.serverName, this.socketCount, result.getValue()));
            } catch (Throwable ex) {
                result = new RemoteInvocationResult(ex);
                log.debug(String.format("[%s][#%d] Responding:%n%s", this.serverName, this.socketCount, ex));
            }
            try {
                this.socketSupport.write(result);
            } catch (IOException e) {
                log.error(String.format("[%s][#%d] Error writing output message%n%s", this.serverName, this.socketCount, result), e);
            }
        } finally {
            try {
                this.socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
	 * This method is called when an error is raised while reading the socket
	 * content. First, the error is written back as a
	 * {@link RemoteInvocationResult} in the {@link Socket} and if this fails, a
	 * log entry is created.
	 * 
	 * @param e
	 *            Error
	 */
    protected void error(Throwable e) {
        RemoteInvocationResult result = new RemoteInvocationResult(e);
        try {
            this.socketSupport.write(result);
        } catch (IOException ex) {
            log.error(String.format("[%s][#%d] Error writing error message%n%s", this.serverName, this.socketCount, e), ex);
        }
    }

    /**
	 * Calls the {@link OODelegate}.
	 * 
	 * @param invocation
	 *            Invocation definition
	 * @return Result of the invocation
	 * @see OODelegate#invoke(RemoteInvocation)
	 */
    protected RemoteInvocationResult handle(RemoteInvocation invocation) {
        return this.delegate.invoke(invocation);
    }
}
