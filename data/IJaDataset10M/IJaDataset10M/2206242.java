package com.rubecula.beanpot.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author Robin Hillyard
 * 
 */
public class SerializeInputStream extends ObjectInputStream {

    /**
	 * @param in
	 * @throws IOException
	 */
    public SerializeInputStream(final InputStream in) throws IOException {
        super(in);
        this._in = in;
    }

    /**
	 * This is necessary because super.available() always yields 0. Since we are
	 * contracted to provide {@link #available()}, it seems we should return
	 * something reasonable. However, this may not be much more reliable.
	 * 
	 * @return the result of calling {@link InputStream#available()} on
	 *         {@link #_in}.
	 * @see java.io.ObjectInputStream#available()
	 */
    @Override
    public int available() throws IOException {
        return this._in.available();
    }

    /**
	 * Invokes {@link #close()} on the super object, and also on {@link #_in}.
	 * 
	 * @see java.io.ObjectInputStream#close()
	 */
    @Override
    public void close() throws IOException {
        super.close();
        this._in.close();
    }

    private final InputStream _in;
}
