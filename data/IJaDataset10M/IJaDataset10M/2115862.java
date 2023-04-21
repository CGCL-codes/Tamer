package net.sourceforge.jepesi.jsch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author mawi
 *
 */
public interface SshChannel {

    /**
	 * @return the OutputStream
	 * @throws IOException
	 */
    public OutputStream getOutputStream() throws IOException;

    /**
	 * @return the InputStream
	 * @throws IOException
	 */
    public InputStream getInputStream() throws IOException;

    /**
	 * connects the channel
	 * @throws Exception
	 */
    public void connect() throws Exception;

    /**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
    public void setPtySize(int arg0, int arg1, int arg2, int arg3);

    /**
	 * disconnects the channel
	 */
    public void disconnect();
}
