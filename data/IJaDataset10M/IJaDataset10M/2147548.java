package com.volantis.mcs.build.themes.definitions.values;

import java.io.PrintStream;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ValueSource {

    void writeSetter(String indent, PrintStream out, String variable);
}
