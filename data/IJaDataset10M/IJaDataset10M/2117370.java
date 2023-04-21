package co.edu.unal.ungrid.image.dicom.network;

import co.edu.unal.ungrid.image.dicom.core.AttributeList;
import co.edu.unal.ungrid.image.dicom.core.DicomException;

/**
 * <p>
 * This class provides a mechanism to process each identifier response of a
 * C-FIND as it is received.
 * </p>
 * 
 * <p>
 * Typically a private sub-class would be declared and instantiated with
 * overriding methods to do something useful with the identifier, rather than
 * the default behavior which is just to dump it to stderr.
 * </p>
 * 
 * @see co.edu.unal.ungrid.image.dicom.network.FindSOPClassSCU
 * 
 * 
 */
public class IdentifierHandler {

    /**
	 * <p>
	 * Called when a response identifier has been received.
	 * </p>
	 * 
	 * @param identifier
	 *            the list of attributes received
	 */
    public void doSomethingWithIdentifier(AttributeList identifier) throws DicomException {
        System.err.println("IdentifierHandler.doSomethingWithIdentifier():");
        System.err.print(identifier);
    }
}
