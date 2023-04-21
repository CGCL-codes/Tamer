package edu.hawaii.ics.ami.element.model.unit;

import edu.hawaii.ics.ami.event.model.DataEvent;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;

/**
 * Reads the data in object format.
 *
 * @author   king
 * @since    July 8, 2002
 */
public class ObjectSourceUnit extends SourceUnitAdapter implements Runnable {

    /**
   * The thread which is responsible for reading data from the object stream and sending it
   * out to all registered listeners.
   */
    private Thread thread;

    /** The data stream where to read data from. */
    private ObjectInputStream objectInputStream = null;

    /**
   * Returns the name of this object.
   *
   * @return   The name of this element.
   */
    public String getName() {
        return "Object Source Unit";
    }

    /**
   * Main processing method for the ObjectSourceUnit object. Reads data from the
   * object input stream and outputs it to all registered listeners.
   */
    public void run() {
        try {
            getInput().open();
            this.objectInputStream = new ObjectInputStream(getInput().getInputStream());
        } catch (IOException e) {
            System.err.println(toString() + ": Error opening stream: " + e);
        }
        Thread thisThread = Thread.currentThread();
        while (this.thread == thisThread) {
            try {
                DataEvent dataEvent = (DataEvent) objectInputStream.readObject();
                sendDataEventToListeners(dataEvent);
            } catch (EOFException e) {
                thread = null;
            } catch (IOException e) {
                System.err.println("Exception in loop: " + e);
                thread = null;
            } catch (ClassNotFoundException e) {
                System.err.println("Exception in loop: " + e);
                thread = null;
            }
        }
        try {
            if (this.objectInputStream != null) {
                this.objectInputStream.close();
            }
            getInput().close();
        } catch (IOException e) {
            System.err.println(toString() + ": Exception closing stream: " + e.getMessage());
        }
        this.objectInputStream = null;
    }

    /**
   * Opens the connection to the source. And forwards all the data that is comming in to the
   * registered listeners.
   */
    public synchronized void open() throws IOException {
        thread = new Thread(this);
        thread.start();
    }

    /**
   * Closes the connection to the source. No more data will be sent out.
   */
    public synchronized void close() {
        thread = null;
    }
}
