package jawara;

import java.util.Enumeration;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.FileInputStream;
import java.net.URL;
import net.jxta.peergroup.*;
import net.jxta.exception.*;
import net.jxta.pipe.*;
import net.jxta.endpoint.*;
import net.jxta.protocol.*;

public class JawaraOutputPipe {

    private OutputPipe pipe;

    private PipeAdvertisement pipeadv;

    private PipeService pipes;

    public JawaraOutputPipe(PipeAdvertisement pipeadv) {
        try {
            pipes = Jawara.group.getPipeService();
            pipe = pipes.createOutputPipe(pipeadv, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String data) {
        Message msg;
        try {
            msg = pipes.createMessage();
            msg.setString("DataTag", data);
            pipe.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
