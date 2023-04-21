package net.sourceforge.antme.tasks.jad;

import net.sourceforge.antme.jad.JadFile;

/**
 * This class implements the "icon" operation portion of the
 * JadEditTask.
 * 
 * @author khunter
 *
 */
public class Icon extends AttributeOperation {

    /**
	 * Standard constructor.  All the work is done in the base class.
	 *
	 */
    public Icon() {
        super("icon", JadFile.ATTR_MIDLET_ICON);
    }
}
