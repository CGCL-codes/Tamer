package vavi.net.inet;

import java.net.Socket;

/**
 * SocketHandlerFactory. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050821 nsano initial version <br>
 */
public interface SocketHandlerFactory {

    /** 
     * @return Runnable to {@link java.util.concurrent.ExecutorService#submit(java.lang.Runnable) submit}
     */
    public Runnable getSocketHandler(Socket socket);
}
