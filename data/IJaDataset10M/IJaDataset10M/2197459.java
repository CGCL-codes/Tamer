package de.fzi.mapevo.aws;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

/**
 * This Object is used for responding to heartbeat signals from the server. It must be started in a separate thread.
 * The thread will run until a WorkerMessage with END state has been send and received.
 * @see de.fzi.mapevo.aws.WorkerObjectCommunicator
 * @author Carsten Daenschel, Michael Mutter
 */
public class WorkerObjectCommunicator implements Runnable {

    Logger log = Logger.getLogger(this.getClass().getName());

    private boolean isAlive;

    private ServerSocket ss;

    private ObjectOutputStream out;

    private ObjectInputStream in;

    private List<WorkerMessage> received;

    public WorkerObjectCommunicator(int port) throws IOException {
        ss = new ServerSocket(port);
        Socket s = ss.accept();
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        received = new CopyOnWriteArrayList<WorkerMessage>();
        isAlive = true;
        log.debug("Construction done");
    }

    @Override
    public void run() {
        try {
            while (isAlive) {
                read();
            }
        } catch (Exception e) {
            if (isAlive) log.warn(e, e);
        }
    }

    private void read() throws IOException, ClassNotFoundException {
        try {
            WorkerMessage msg = (WorkerMessage) in.readObject();
            log.debug("Read state " + msg.state);
            if (msg.isHEARTBEAT()) {
                send(WorkerMessage.getHeartbeatMessage());
            } else {
                received.add(msg);
            }
        } catch (EOFException e) {
            log.warn("EOF read. Closing ServerSocket.");
            ss.close();
        }
    }

    public synchronized void send(WorkerMessage o) throws IOException, ClassNotFoundException {
        log.debug("Thread " + Thread.currentThread().getName() + " sending state " + o.state);
        out.writeObject(o);
        out.flush();
        out.reset();
        if (o.isEND()) {
            isAlive = false;
        }
    }

    public WorkerMessage getMessage() throws IOException, ClassNotFoundException {
        log.debug("Receivedqueue has " + received.size() + " elements.");
        while (received.isEmpty()) {
        }
        WorkerMessage result = received.get(0);
        received.remove(result);
        log.debug("Received a new message. Done reading.");
        return result;
    }

    public void terminate() throws IOException, ClassNotFoundException {
        this.send(WorkerMessage.getEndMessage());
        log.info("Closing ServerSocket");
        ss.close();
    }
}
