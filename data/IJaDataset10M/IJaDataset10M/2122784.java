package de.fraunhofer.ipsi.ipsixq.ui.graphical.event;

/**
 * Informs about name changes.
 *
 * @author Patrick.Lehti@ipsi.fhg.de
 * @version 1.0
 */
public interface NameChangedListener {

    /**
	 * Informs about the name change.
	 *
	 * @args newName the new name
	 */
    void nameChanged(String newName);
}
