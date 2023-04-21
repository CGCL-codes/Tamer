package net.sourceforge.hobbes.common;

import java.io.IOException;
import net.sourceforge.hobbes.common.protocol.HPDocument;
import net.sourceforge.hobbes.common.protocol.HobbesProtocolException;
import net.sourceforge.hobbes.common.protocol.IllegalReplyException;
import net.sourceforge.hobbes.common.protocol.IllegalRequestException;

/**
 * Allows transmittance of <code>HPDocument</code> s through direct method
 * calls rather than a network connection. <code>writeOut</code> simulates
 * sending a document over the network and <code>receive</code> simulates
 * receiving a document from the socket.
 * 
 * @author Daniel M. Hackney
 * @created Aug 29, 2005
 *  
 */
public class DirectDocumentSource implements DocumentSource {

    private ClientCreator parentCreator;

    private DocumentSender parentDS;

    private HPDocument out;

    public DirectDocumentSource() {
        parentCreator = null;
    }

    /**
     * Creates an <code>HPDocument</code> for registration.
     * 
     * @return An <code>HPDocument</code> requesting registration.
     */
    public HPDocument registrationDocument() {
        return HPDocument.registrationDocument(getIP(), getRecipientIP(), this.getClass().getName());
    }

    public void setClientCreator(ClientCreator inParent) {
        parentCreator = inParent;
    }

    public void close() {
    }

    /**
     * Returns the the result of a <code>writeOut</code> method call.
     * 
     * @return The document sent by <code>writeOut</code>.
     */
    public HPDocument getOutput() {
        return out;
    }

    public String getIP() {
        return "127.0.0.1";
    }

    public String getRecipientIP() {
        return "127.0.0.1";
    }

    public void receive(HPDocument toProcess) {
        try {
            switch(toProcess.getTargetIndex()) {
                case 9:
                    if (toProcess.getActionType().equals("reply")) {
                        if (toProcess.getData().length < 1) throw new IllegalReplyException("Registration reply must contain name"); else parentDS = (DocumentSender) parentCreator.addSource(this, toProcess.getData()[0]);
                    } else throw new IllegalRequestException("ClientConnection may not receive a registration request");
                    break;
                case 10:
                    System.err.println("Error received by " + parentDS.getName() + ": " + toProcess.getData()[0]);
                    break;
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 12:
                case 13:
                case 11:
                    parentDS.processSpecial(toProcess);
                    break;
                default:
                    throw new IllegalArgumentException("Action Target " + toProcess.getActionTarget() + " not supported.");
            }
        } catch (HobbesProtocolException e) {
            writeOut(HPDocument.errorDocument(toProcess.getRecipientIP(), toProcess.getSenderIP(), this.getClass().getName(), e));
        } catch (IOException e) {
            close();
        }
    }

    public void writeOut(HPDocument toWrite) {
        out = toWrite;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DirectDocumentSource)) return false;
        DirectDocumentSource other = (DirectDocumentSource) obj;
        if (this.out != null && other.out != null) if (!this.out.equals(other.out)) return false;
        if (!this.parentDS.equals(other.parentDS)) return false;
        return true;
    }
}
