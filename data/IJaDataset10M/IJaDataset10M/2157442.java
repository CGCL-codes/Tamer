package org.p2pws.jxta.pipe.unicast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLElement;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.OutputPipe;
import net.jxta.protocol.PipeAdvertisement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.p2pws.ServiceDescriptor;
import org.p2pws.loaddistribution.message.P2PMessage;
import org.p2pws.loaddistribution.request.AbstractP2PServerRequest;

/**
 * @author panisson
 *
 */
public class PipeRequest extends AbstractP2PServerRequest {

    private static final Log log = LogFactory.getLog(PipeRequest.class);

    private InputStream inputStream;

    private PipeAdvertisement rpa;

    private OutputPipe output = null;

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private boolean sent = false;

    public PipeRequest(PeerGroup peerGroup, P2PMessage message, ServiceDescriptor descriptor) throws IOException {
        super(descriptor);
        byte[] msg = message.getByteArrayElement("message");
        if (msg == null) {
            throw new IOException("Server: error could not find the 'message' tag!");
        }
        byte[] remoteInputPipeAdvertisement = message.getByteArrayElement("remote-input-pipe");
        if (log.isInfoEnabled()) log.info("-> SOAPService:acceptOnPublicPipe(...) - get 'remote-input-pipe' element: \n" + new String(remoteInputPipeAdvertisement));
        if (remoteInputPipeAdvertisement == null) {
            throw new IOException("Server: error could not find the 'remote-input-pipe' tag!");
        }
        StructuredDocument rpaDoc = StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"), new ByteArrayInputStream(remoteInputPipeAdvertisement));
        rpa = (PipeAdvertisement) AdvertisementFactory.newAdvertisement((XMLElement) rpaDoc);
        if (log.isInfoEnabled()) log.info("Client PeerID: " + rpa.getDescription());
        int attempt = 1;
        long timeout = 5000;
        boolean redo = true;
        do {
            try {
                log.info("-> SOAPService:acceptOnPublicPipe(...) - binding op with remote ip... (" + attempt + ")\t");
                output = peerGroup.getPipeService().createOutputPipe(rpa, timeout);
                log.info("OK");
                redo = false;
            } catch (Exception e) {
                log.warn(" Exception in remote binding phase! TIMEOUT expired!", e);
                attempt++;
                timeout *= 2;
            }
        } while (redo && attempt < 5);
        if (attempt >= 5) {
            throw new IOException("Server: error in remote binding phase.");
        }
        inputStream = new ByteArrayInputStream(msg);
    }

    public void close() {
        try {
            if (output != null) {
                if (!sent) {
                    shutdownOutput();
                }
                output.close();
            }
        } catch (Exception e) {
        }
        output = null;
    }

    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    public void shutdownOutput() throws IOException {
        Message returnMessage = new Message();
        ByteArrayMessageElement returnMessageElement = new ByteArrayMessageElement("message", null, outputStream.toByteArray(), null);
        returnMessage.addMessageElement(returnMessageElement);
        log.info("-> SOAPService:invokeService(...) - send back response message");
        output.send(returnMessage);
        sent = true;
    }

    public boolean isMainExecutor() {
        return true;
    }

    public void processP2PMessage(P2PMessage message) {
    }
}
