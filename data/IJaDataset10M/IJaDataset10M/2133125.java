package org.indi.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.xml.parsers.SAXParserFactory;
import org.indi.clientmessages.GetProperties;
import org.indi.objects.BlobVector;
import org.indi.objects.NumberVector;
import org.indi.objects.SwitchVector;
import org.indi.objects.TextVector;
import org.indi.objects.Vector;
import org.indi.reactor.QueueHandler;

/**
 * A class to allow indidrivers written in C to interact with this indiserver
 * writing in Java. The class acts as a proxy class communicating with the
 * indidriver that runs in a diffrent process via the stdin stdout streams.
 * 
 * @author Dirk Hünniger
 * 
 */
public class ExternalDevice extends BasicDevice implements QueueHandler<java.lang.Object>, Runnable {

    /**
     * process the indiserver writen in C is runnning in
     */
    private Process process;

    /**
     * the input strem connected to the porcess of the driver
     */
    private final InputStream in;

    /**
     * the output stream connected to the driver
     */
    private final OutputStream out;

    /**
     * the input stream used to read by the sax parser to read from the driver.
     * It is connected to the stdout stream of the driver but contains an
     * additional prelude used to intialize the sax parser
     */
    private final InputStream fromDriver;

    /**
     * a queue into which the parsing thread writes the indiobjects reacieved
     * from the driver
     */
    private final Queue<java.lang.Object> threadToDeviceQueue;

    /**
     * class constuctor
     * 
     * @param server
     *                the server to host the driver
     * @param command
     *                the command to start the external driver
     */
    public ExternalDevice(String command) {
        super();
        try {
            this.process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.in = this.process.getInputStream();
        this.out = this.process.getOutputStream();
        try {
            this.out.write("<enableBLOB>Also</enableBLOB>".getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        InputStream prelude = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <doc>".getBytes());
        this.fromDriver = new SequenceInputStream(prelude, this.in);
        this.threadToDeviceQueue = new LinkedBlockingQueue<java.lang.Object>();
        (new Thread(this)).start();
    }

    /**
     * run asynchronously. Entrypoint of the parsing thread.
     */
    public void run() {
        javax.xml.parsers.SAXParser parser;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        org.indi.client.SaxHandler h = new org.indi.client.SaxHandler(this.threadToDeviceQueue);
        try {
            parser.parse(this.fromDriver, h);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNew(SwitchVector vector) {
        sendVector(vector);
    }

    @Override
    public void onNew(NumberVector vector) {
        sendVector(vector);
    }

    @Override
    public void onNew(BlobVector vector) {
        sendVector(vector);
    }

    @Override
    public void onNew(TextVector vector) {
        sendVector(vector);
    }

    /**
     * Send a vector to external driver
     * 
     * @param vector
     *                the vector to be sent
     */
    public void sendVector(Vector vector) {
        try {
            this.out.write(vector.getXML(vector.getTransferType()).getBytes());
            this.out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGetProperties(GetProperties o) {
        try {
            this.out.write((o.getXML()).getBytes("UTF-8"));
            this.out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * @return the queue connecting the parsing thread to the device
     */
    public Queue<java.lang.Object> getQueue() {
        return this.threadToDeviceQueue;
    }

    /**
     * called by the reactor when a new item is available in the queue. (it has
     * been put into the queue by the parsing thread)
     * 
     * @param input
     *                the item just received from the queue
     */
    public void onRead(java.lang.Object input) {
        org.indi.objects.Object i = (org.indi.objects.Object) input;
        sendToClients(i, i.getTransferType(), i.getMessage());
    }

    /**
     * Runs an indiserver hosting an external device.
     * 
     * @param args
     *                Command line arguments (ignored)
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        IndiServer s = new IndiServer();
        Device d = new ExternalDevice("/home/dirk/indi/src/examples/tutorial_three");
        s.addDevice(d);
        while (true) {
            s.reactor.handleEvents(10);
        }
    }

    @Override
    public void setConnection(DeviceConnection connection) {
        super.setConnection(connection);
        connection.getReactor().register(this);
    }
}
