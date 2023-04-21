package gnagck.util.aiming;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author royer
 *
 */
public abstract class Aim {

    public abstract void doEdit();

    public abstract Aim clone();

    public abstract void read(DataInput din) throws IOException;

    public abstract void write(DataOutput dout) throws IOException;
}
