package bm.vm;

import bm.core.J2MEException;

/**
 * Base exception for virtual machine.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision$
 */
public class VirtualMachineException extends J2MEException {

    public VirtualMachineException(final int errorNumber) {
        super(errorNumber);
    }

    public VirtualMachineException(final int errorNumber, final String string) {
        super(errorNumber, string);
    }

    public VirtualMachineException(final int errorNumber, final Throwable source) {
        super(errorNumber, source);
    }

    public VirtualMachineException(final int errorNumber, final Throwable source, final String message) {
        super(errorNumber, source, message);
    }
}
