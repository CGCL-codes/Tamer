package org.openscience.cdk.io;

import java.io.Reader;

/**
 * @cdkPackage io
 */
public class CACheReader extends DummyReader {

    public CACheReader(Reader input) {
    }

    public String getFormatName() {
        return "CAChe (not implemented, post a feature request if you need it)";
    }
}
