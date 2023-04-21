package com.google.code.http4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 *
 */
public interface Connection extends Closeable {

    void connect() throws IOException;

    void setTimeout(int timeout);

    boolean isClosed();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    Host getHost();

    void setReusable(boolean reusable);

    /**
	 * @return <code>true</code> if the connection is based on HTTP/1.1 above and is not closed.
	 * 			<code>false</code> if the connection is based on HTTP/1.0 or is closed.
	 */
    boolean isReusable();
}
