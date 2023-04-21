package net.sourceforge.freejava.sio;

import java.io.IOException;

public interface ISimpleByteOut {

    /**
     * @param b
     *            The byte to write, only lower 8 bits are written, the higher 24 bits are ignored.
     */
    void write(int b) throws IOException;
}
