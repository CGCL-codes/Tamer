package cc.mallet.pipe;

import java.io.*;
import cc.mallet.types.*;

/**
 * Unimplemented.
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */
public class InstanceListTrimFeaturesByCount extends Pipe implements Serializable {

    int minCount;

    public InstanceListTrimFeaturesByCount(int minCount) {
        super(new Alphabet(), null);
        this.minCount = minCount;
    }

    public Instance pipe(Instance carrier) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static final long serialVersionUID = 1;

    private static final int CURRENT_SERIAL_VERSION = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_SERIAL_VERSION);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
    }
}
