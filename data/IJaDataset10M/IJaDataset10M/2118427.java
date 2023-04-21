package com.aelitis.azureus.ui.mdi;

/**
 * @author TuxPaper
 * @created Feb 13, 2009
 */
public interface MdiEntryDropListener {

    /**
	 * @param entry
	 * @param droppedObject
	 * @return true if you handled it, false if you didn't
	 */
    public boolean mdiEntryDrop(MdiEntry entry, Object droppedObject);
}
