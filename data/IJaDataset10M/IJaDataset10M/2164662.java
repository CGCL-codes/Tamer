package com.google.common.io;

import java.io.IOException;

/**
 * An factory for writable streams of bytes or characters.
 *
 * @author Chris Nokleberg
 * @since 9.09.15 <b>tentative</b>
 */
public interface OutputSupplier<T> {

    T getOutput() throws IOException;
}
