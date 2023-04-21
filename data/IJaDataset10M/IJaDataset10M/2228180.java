package org.t2framework.t2.format.amf.io.reader;

import java.io.DataInputStream;
import java.io.IOException;

public interface AmfDataReader {

    Object read(DataInputStream inputStream) throws IOException;
}
