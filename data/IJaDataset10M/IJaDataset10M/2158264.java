package org.t2framework.t2.format.amf3.io.writer;

import java.io.DataOutputStream;
import java.io.IOException;
import org.t2framework.t2.format.amf.io.writer.AmfDataWriter;

public interface Amf3DataWriter extends AmfDataWriter {

    void writeAmf3Data(Object value, DataOutputStream outputStream) throws IOException;
}
