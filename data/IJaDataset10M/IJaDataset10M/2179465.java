package org.exist.eclipse.auto.internal.mod;

/**
 * Implement this interface and you'll be informed as soon as the automation
 * gets modified.
 * 
 * @author Markus Tanner
 */
public interface IAutoModificationListener {

    /**
	 * Notify as soon as a modification was done.
	 * 
	 * @param event
	 */
    public void automationModified(AutoModEvent event);

    /**
	 * Notify if the modification was cleared.
	 * 
	 * @param event
	 */
    public void modificationCleared(AutoModEvent event);
}
