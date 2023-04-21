package net.sf.asyncobjects.io.util;

import net.sf.asyncobjects.io.AByteOutput;
import net.sf.asyncobjects.io.BinaryData;

/**
 * Byte version of {@link MulticastOutput}
 * 
 * @author const
 * 
 */
public class MulticastByteOutput extends MulticastOutput<BinaryData, AByteOutput> implements AByteOutput {

    /**
	 * A constructor
	 * 
	 * @param outputs
	 *            an outputs
	 */
    public MulticastByteOutput(AByteOutput... outputs) {
        super(outputs);
    }
}
