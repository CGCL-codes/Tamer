package gtagame;

import java.io.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PhoeniX
 */
public class TCPMessage implements Serializable {

    private Vector<Object> Params;

    public TCPMessage() {
        Params = new Vector<Object>(10);
    }

    public void add(Object o) {
        if (Params == null) {
            System.err.println("Params null");
        } else {
            if (o == null) {
                System.err.println("o null");
            } else {
                Params.addElement(o);
            }
        }
    }

    public void writeObjectPublic(ObjectOutputStream out) {
        try {
            writeObject(out);
        } catch (IOException ex) {
            Logger.getLogger(TCPMessage.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("writeObject fail");
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        try {
            int i = 0;
            while (i < Params.size()) {
                out.writeObject(Params.elementAt(i));
                i++;
            }
        } catch (IOException e) {
            Logger.getLogger(TCPMessage.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("writeObject fail");
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.getLogger(TCPMessage.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("writeObject fail:outbounds");
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    }
}
