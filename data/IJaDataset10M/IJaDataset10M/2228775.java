package org.t2framework.t2.format.amf.message.reader;

import java.io.IOException;
import org.t2framework.t2.format.amf.message.Message;

public interface MessageReader {

    Message read() throws IOException;
}
