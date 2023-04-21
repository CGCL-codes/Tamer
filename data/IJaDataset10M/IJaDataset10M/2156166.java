package de.sonivis.tool.core.exception;

import de.sonivis.tool.core.datamodel.InfoSpace;

/**
 * This {@link SonivisException} should be thrown whenever there is a problem with a selected (or
 * not selected) {@link InfoSpace}.
 * 
 * @author Benedikt Meuthrath
 * @version $Revision$, $Date$
 */
public class InfoSpaceSelectionException extends SonivisRuntimeException {

    private static final long serialVersionUID = -655503471911530573L;

    public InfoSpaceSelectionException() {
        super("No InfoSpace selected.");
    }

    public InfoSpaceSelectionException(String arg0) {
        super("No InfoSpace selected. " + arg0);
    }

    public InfoSpaceSelectionException(Throwable arg0) {
        super(arg0);
    }

    public InfoSpaceSelectionException(String arg0, Throwable arg1) {
        super("No InfoSpace selected. " + arg0, arg1);
    }
}
