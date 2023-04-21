package nzdis.jasimpl.transport;

import javax.microedition.io.Connector;
import org.rakiura.util.Logger;
import javax.microedition.io.*;
import javax.agent.Envelope;
import javax.agent.Locator;
import javax.agent.Payload;
import javax.agent.TransportMessage;
import javax.agent.service.transport.MessageSender;
import javax.agent.service.transport.MessageTransportService;
import javax.agent.service.transport.NotBoundException;
import javax.agent.service.transport.NotLocatableException;
import javax.agent.service.transport.TransportFailure;
import javax.bluetooth.*;
import java.io.*;

/**
 * Implements the http-based MessageSender.
 * This implementation follows the FIPA00084 spec.
 *
 *<br><br>
 * HttpMessageSender.java<br>
 * Created: Sun Feb 24 14:40:31 2002<br>
 *
 * @author  <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @author <a href="mailto:pmallet@infoscience.otago.ac.nz">Pierre-Etienne Mallet</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/30 23:50:23 $
 */
class MIDPBluetoothMessageSender extends AbstractMessageSender implements MessageSender {

    /** Remote locator. */
    private Locator remote;

    /** Remote end URL. */
    private String url;

    /** the bluetooth MTS which owns this sender */
    private MIDPBluetoothMessageTransportService btMTS;

    /** service record used for the connection */
    private ServiceRecord serviceRecord;

    /**
	 * Creates a new <code>HTTPMessageSender</code> instance.
	 * @param aService a <code>MessageTransportService</code> value
	 */
    public MIDPBluetoothMessageSender(final MessageTransportService aService) {
        super(aService);
        this.btMTS = (MIDPBluetoothMessageTransportService) aService;
    }

    /**
	 * Binds this sender to the remote Locator.
	 * @param aLocator a <code>Locator</code> value
	 * @exception NotLocatableException if an error occurs
	 */
    public void bindToRemoteLocator(final Locator aLocator) throws NotLocatableException {
        try {
            Logger.getInstance().debug("Binding " + aLocator.getAddress());
            this.serviceRecord = findServiceRecord(aLocator.getAddress().substring(8, 20));
            this.url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            bind();
        } catch (Exception re) {
            throw new NotLocatableException("Cannot locate the remote end-point.", re);
        }
    }

    /**
	 * This function retrieve the service record of the device at 'btAddress'
	 * @param btAddress
	 * @return
	 * @throws NotLocatableException
	 */
    public ServiceRecord findServiceRecord(String btAddress) throws NotLocatableException {
        ServiceRecord record = null;
        try {
            Logger.getInstance().debug("Finding " + btAddress);
            record = (ServiceRecord) this.btMTS.getRemoteDevices().get(btAddress);
        } catch (Exception re) {
            throw new NotLocatableException("Find Service record for remote end-point.", re);
        }
        return record;
    }

    /**
	 * Returns <code>RemoteLocator</code>.
	 * @return a <code>Locator</code> value
	 * @exception NotBoundException if an error occurs
	 */
    public Locator getRemoteLocator() throws NotBoundException {
        if (!isBound()) {
            throw new NotBoundException();
        }
        return this.remote;
    }

    /**
	 * This method will bind to the Receiver obtained from the message and
	 * it will send the message.
	 * @param aMessage a <code>TransportMessage</code> value
	 * @exception NotLocatableException if an error occurs
	 * @exception TransportFailure if an error occurs
	 */
    public void sendMessage(final TransportMessage aMessage) throws NotLocatableException, TransportFailure {
        try {
            (new Thread() {

                public void run() {
                    try {
                        Logger.getInstance().debug("Bind to remote locator");
                        if (!btMTS.getRemoteDevices().containsKey(aMessage.getReceiver().getAddress().substring(8, 20))) {
                            Logger.getInstance().info("The device " + aMessage.getReceiver().getAddress() + " is not registered yet. " + "New search launched (Message delivering delayed for 10 seconds).");
                            btMTS.deviceSearch();
                            synchronized (this) {
                                wait(10000);
                            }
                        }
                        bindToRemoteLocator(aMessage.getReceiver());
                        Logger.getInstance().debug("Delivering");
                        deliver(aMessage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception ex) {
            Logger.getInstance().error("Error Sending Message " + ex.toString());
        }
    }

    /**
	 * This function builds the FIPA message in xml, open a connection to the server
	 * and send the message.
	 * @param aMsg
	 * @throws TransportFailure
	 */
    protected void deliver(final TransportMessage aMsg) throws TransportFailure {
        try {
            Logger.getInstance().debug("Delivering to " + aMsg.getReceiver().getAddress());
            if (serviceRecord != null) {
                try {
                    String agentMessage = packEnvelope(aMsg.getEnvelope());
                    agentMessage += "\n";
                    agentMessage += packPayload(aMsg.getPayload());
                    StreamConnection conn = (StreamConnection) Connector.open(url);
                    Logger.getInstance().debug("Connection url: " + url);
                    OutputStream out = conn.openDataOutputStream();
                    InputStream in = conn.openInputStream();
                    ((DataOutputStream) out).writeUTF(agentMessage);
                    out.flush();
                    in.read();
                    out.close();
                    in.close();
                    conn.close();
                } catch (Exception ioe) {
                    Logger.getInstance().error("Delivery failure " + ioe.toString() + " " + serviceRecord.toString());
                }
            }
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }

    /**
	 * Serialize the enveloppe
	 * 
	 * @param env
	 * @return
	 */
    public String packEnvelope(Envelope env) {
        try {
            final MidpXmlSerializer ser = new MidpXmlSerializer();
            return ser.serialize(env);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
	 * Serialize the Payload
	 * 
	 * @param anPayload
	 * @return
	 */
    public String packPayload(Payload anPayload) {
        try {
            final MidpXmlSerializer ser = new MidpXmlSerializer();
            return ser.serialize(anPayload);
        } catch (Exception ex) {
        }
        return null;
    }
}
