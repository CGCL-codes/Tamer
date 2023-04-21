package org.xmlcml.cif;

import java.util.logging.Logger;

/**
 * save frame.
 * 
 * differs from datablock in that it cannot have save block children
 */
public class CIFSaveFrame extends AbstractBlock {

    /**
	 * save frame tag.
	 * 
	 */
    public static final String TAG = "saveframe";

    static Logger logger = Logger.getLogger(CIFSaveFrame.class.getName());

    /**
	 * create empty svae frame.
	 */
    public CIFSaveFrame() {
        super(TAG);
    }

    /**
	 * create save frame with id.
	 * 
	 * @param id
	 * @throws CIFException
	 */
    public CIFSaveFrame(String id) throws CIFException {
        super(TAG, id);
    }
}
