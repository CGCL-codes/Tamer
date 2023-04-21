package cc.mallet.pipe;

import java.io.*;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

/**
 * Print the data field of each instance.
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */
public class PrintInput extends Pipe implements Serializable {

    String prefix = null;

    PrintStream stream = System.out;

    public PrintInput(String prefix) {
        this.prefix = prefix;
    }

    public PrintInput() {
    }

    public PrintInput(PrintStream out) {
        stream = out;
    }

    public PrintInput(String prefix, PrintStream out) {
        this.prefix = prefix;
        stream = out;
    }

    public Instance pipe(Instance carrier) {
        if (prefix != null) stream.print(prefix);
        stream.println(carrier.getData().toString());
        return carrier;
    }

    private static final long serialVersionUID = 1;

    private static final int CURRENT_SERIAL_VERSION = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_SERIAL_VERSION);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        prefix = null;
        stream = System.out;
    }
}
