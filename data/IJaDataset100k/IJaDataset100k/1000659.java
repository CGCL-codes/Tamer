package org.indi.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.indi.clientmessages.BlobEnable;
import org.indi.clientmessages.EnableBlob;
import org.indi.clientmessages.GetProperties;
import org.indi.objects.BlobVector;
import org.indi.objects.Message;
import org.indi.objects.TransferType;
import org.indi.objects.Vector;
import org.indi.reactor.OutputQueue;
import org.indi.reactor.Reactor;
import org.xml.sax.SAXException;

/**
 * State class to keep track wheather blobs a enabled for a particular device
 * and/or its subsystems
 * 
 * @author Dirk Hünniger
 * 
 */
class BlobEnabler {

    /**
     * the default BlobEnable state for the whole device
     */
    public BlobEnable defaults;

    /**
     * the BlobEnable state of each property of the device
     */
    public Map<String, BlobEnable> specific;

    /**
     * class constructor
     * 
     */
    public BlobEnabler() {
        this.specific = new HashMap<String, BlobEnable>();
        this.defaults = BlobEnable.Never;
    }
}

/**
 * A class to handle a connection to a single client
 * 
 * @author Dirk Hünniger
 * 
 */
public class ClientHandler extends OutputQueue {

    private final Log log = LogFactory.getLog(ClientHandler.class);

    /**
     * Queue from the parsing thread to the device drivers
     */
    private Queue<java.lang.Object> threadToDriverQueue;

    /**
     * The parsed used to parse the xml received from the client
     */
    private final SAXParser parser;

    /**
     * the blob enable state of each device.
     */
    private Map<String, BlobEnabler> blobEnableMap = null;

    /**
     * the name of the device this client is interested in. any if null
     */
    private String device = null;

    /**
     * true if the client is interested in all devices
     */
    private boolean allDevices = false;

    /**
     * the default blobenable state for all devices
     */
    BlobEnable defaults = BlobEnable.Never;

    NonBlockungXMLByteBuffer nonBlockungXMLByteBuffer;

    SaxHandler saxHandler;

    IndiServer server;

    /**
     * class constructor
     * 
     * @param r
     *                the reactor to register with
     * @param ch
     *                the channel to communicate with the client
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public ClientHandler(Reactor r, SelectableChannel ch) throws IOException, ParserConfigurationException, SAXException {
        super(r, ch);
        nonBlockungXMLByteBuffer = new NonBlockungXMLByteBuffer();
        this.parser = SAXParserFactory.newInstance().newSAXParser();
        this.blobEnableMap = new HashMap<String, BlobEnabler>();
    }

    public void shutDown() {
        this.reactor.unregister(this);
    }

    /**
     * set the queue to send parsed messages from the parsing thread to the
     * device drivers
     * 
     * @param queue
     */
    public void setServer(IndiServer server) {
        this.server = server;
        this.saxHandler = new SaxHandler(server.getDispatcher(), this);
    }

    @Override
    public void onRead() throws IOException {
        try {
            long oldpos = nonBlockungXMLByteBuffer.getBuffer().position();
            ((ReadableByteChannel) this.channel).read(nonBlockungXMLByteBuffer.getBuffer());
            if (nonBlockungXMLByteBuffer.getBuffer().position() - oldpos == 0) {
                throw new RuntimeException("Client Disconnected");
            }
            for (byte[] doc = nonBlockungXMLByteBuffer.next(); doc != null; doc = nonBlockungXMLByteBuffer.next()) {
                this.parser.parse(new ByteArrayInputStream(doc), this.saxHandler);
            }
        } catch (Exception e) {
            server.bogusClient(this, e);
        }
    }

    /**
     * called when the client disconnects
     * 
     */
    private void onClientDisconnected() {
        this.reactor.unregister(this);
    }

    /**
     * called when a get properties message is received from the client
     * 
     * @param o
     */
    public void onGetProperties(GetProperties o) {
        if (o.device == null) {
            this.allDevices = true;
        } else {
            this.device = o.device;
        }
    }

    /**
     * called when an enable blob message ist received from the client
     * 
     * @param eb
     *                the blobenable object describing the request of the client
     */
    public synchronized void onEnableBlob(EnableBlob eb) {
        if (this.device == null) {
            this.defaults = eb.blobenable;
        }
        BlobEnabler ber = this.blobEnableMap.get(eb.device);
        if (ber == null) {
            BlobEnabler be = new BlobEnabler();
            if (eb.name == null) {
                be.defaults = eb.blobenable;
                this.blobEnableMap.put(eb.device, be);
            } else {
                be.specific.put(eb.name, eb.blobenable);
                this.blobEnableMap.put(eb.device, be);
            }
        } else {
            if (eb.name == null) {
                ber.defaults = eb.blobenable;
            } else {
                ber.specific.put(eb.name, eb.blobenable);
            }
        }
    }

    /**
     * Checkes whenther property of a given kind should sent to client using a
     * specific blobenable state.
     * 
     * @param blob
     *                ture if the property in question is a BLOB
     * @param be
     *                the blobenable state to used for the check
     * @return true is the property should be sent to the client
     */
    private static boolean getEnabled(boolean blob, BlobEnable be) {
        if (blob) {
            switch(be) {
                case Only:
                    return true;
                case Also:
                    return true;
                case Never:
                    return false;
            }
        } else {
            switch(be) {
                case Only:
                    return false;
                case Also:
                    return true;
                case Never:
                    return true;
            }
        }
        throw new RuntimeException();
    }

    /**
     * Checks whether a given indivector should be send to the client with
     * respect to the blobenable configuration for the client.
     * 
     * @param vec
     *                the indivector to be checked
     * @return true if the given indivector should be send to the client
     */
    public synchronized boolean getVectorEnabled(Vector vec) {
        BlobEnabler ber = this.blobEnableMap.get(vec.getDevice());
        if (ber == null) {
            return getEnabled(vec instanceof BlobVector, this.defaults);
        } else {
            BlobEnable be = ber.specific.get(vec.getName());
            if (be == null) {
                return getEnabled(vec instanceof BlobVector, ber.defaults);
            } else {
                return getEnabled(vec instanceof BlobVector, be);
            }
        }
    }

    /**
     * send a particular indiobject to the client
     * 
     * @param object
     *                the object to be send
     * @param type
     *                the way the obejct should be send
     * @param message
     *                the message to be sent along wiht the object
     */
    public synchronized void send(org.indi.objects.Object object, TransferType type, String message) {
        String dev = null;
        if (object instanceof Vector) {
            Vector vec = (Vector) object;
            dev = vec.getDevice();
            if (!getVectorEnabled(vec)) {
                return;
            }
        }
        if (object instanceof Message) {
            dev = ((Message) object).getDevice();
            BlobEnabler ber = this.blobEnableMap.get(dev);
            if (ber == null) {
                if (!getEnabled(false, this.defaults)) {
                    return;
                }
            } else {
                if (!getEnabled(false, ber.defaults)) {
                    return;
                }
            }
        }
        if ((dev == this.device) | (this.allDevices)) {
            String data = object.getXML(type, message);
            ByteBuffer output = ByteBuffer.allocate(data.length());
            output.put(data.getBytes());
            output.flip();
            write(output);
        }
    }
}
