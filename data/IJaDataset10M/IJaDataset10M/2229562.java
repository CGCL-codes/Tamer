package java.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;

/**
 * @deprecated
 */
public interface RemoteCall {

    /**
   * @deprecated
   */
    ObjectOutput getOutputStream() throws IOException;

    /**
   * @deprecated
   */
    void releaseOutputStream() throws IOException;

    /**
   * @deprecated
   */
    ObjectInput getInputStream() throws IOException;

    /**
   * @deprecated
   */
    void releaseInputStream() throws IOException;

    /**
   * @deprecated
   */
    ObjectOutput getResultStream(boolean success) throws IOException, StreamCorruptedException;

    /**
   * @deprecated
   */
    void executeCall() throws Exception;

    /**
   * @deprecated
   */
    void done() throws IOException;
}
