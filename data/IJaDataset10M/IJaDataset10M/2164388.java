package org.uinator.io.adapter;

import java.io.IOException;

/**
 *
 * @author nikelin
 */
public interface Adapter {

    public void write(Object message) throws IOException;

    public String read() throws IOException;
}
