package gnagck.block.attributes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import javax.swing.JComponent;

/**
 * Modifies the behavior of a Block.
 * @author royer
 *
 */
public abstract class BlockAttribute {

    /**
	 * Launches an editor for this attribute.
	 * @param parent The parent component
	 */
    public abstract void doEdit(JComponent parent);

    /**
	 * Returns a distinct copy of this object.
	 * @return A distinct copy of this object
	 */
    public abstract BlockAttribute clone();

    /**
	 * Persistence.  Reads the attribute from persistent storage.
	 * @param din The binary input stream
	 * @throws IOException
	 */
    public abstract void read(DataInput din) throws IOException;

    /**
	 * Persistence.  Writes the attribute to persistent storage.
	 * @param dout The binary output stream
	 * @throws IOException
	 */
    public abstract void write(DataOutput dout) throws IOException;
}
