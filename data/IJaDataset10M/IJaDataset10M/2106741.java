package com.volantis.mock.samples;

import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * A simple interface.
 *
 * @mock.generate
 */
public interface Interface1 {

    void foo1(int i);

    void withThrows() throws IOException, SAXException, IllegalArgumentException;
}
