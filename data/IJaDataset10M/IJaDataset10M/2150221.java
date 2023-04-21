package net.sourceforge.hlm.util.xml.read;

import java.io.*;
import org.xml.sax.*;

public class DummyResolver implements EntityResolver {

    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(new ByteArrayInputStream(new byte[0]));
    }
}
