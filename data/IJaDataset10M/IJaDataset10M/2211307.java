package orbe.model;

import java.util.BitSet;

/**
 * Object that contains flags.
 * 
 * @author Damien Coraboeuf
 * @version $Id: Flagged.java,v 1.1 2006/11/17 16:56:04 guinnessman Exp $
 */
public class Flagged {

    /**
	 * Flags
	 */
    private BitSet flags = new BitSet(2);

    public boolean getFlag(int flag) {
        return flags.get(flag);
    }

    public void setFlag(int flag, boolean b) {
        flags.set(flag, b);
    }
}
