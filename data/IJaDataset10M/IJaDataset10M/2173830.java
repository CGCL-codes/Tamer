package net.sf.amemailchecker.mail.parser.mime;

import net.sf.amemailchecker.mail.impl.letter.RawMessagePart;
import net.sf.amemailchecker.mail.parser.reader.InputReader;
import java.io.IOException;

public class SimpleBodyContentHandler {

    public void handle(InputReader reader, RawMessagePart bodyPart) throws IOException {
        readPayload(reader, bodyPart);
    }

    void readPayload(InputReader reader, RawMessagePart messageBodyPart) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) readPayloadLine(line, messageBodyPart);
    }

    void readPayloadLine(String line, RawMessagePart messageBodyPart) {
        messageBodyPart.insertPayloadLine(line);
    }
}
