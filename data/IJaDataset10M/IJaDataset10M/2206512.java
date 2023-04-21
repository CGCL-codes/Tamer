package org.jscsi.scsi.protocol.sense.exceptions;

import org.jscsi.scsi.protocol.sense.KCQ;
import org.jscsi.scsi.protocol.sense.additional.FieldPointer;

public class InvalidCommandOperationCodeException extends IllegalRequestException {

    private static final long serialVersionUID = -8517939119703217907L;

    public InvalidCommandOperationCodeException() {
        super(KCQ.INVALID_COMMAND_OPERATION_CODE, true);
    }

    @Override
    protected FieldPointer getFieldPointer() {
        return new FieldPointer(true, (byte) -1, 0);
    }
}
