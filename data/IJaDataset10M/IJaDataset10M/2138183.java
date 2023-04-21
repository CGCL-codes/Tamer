package org.jsynthlib.synthdrivers.RolandD10.message;

import org.jsynthlib.synthdrivers.RolandD10.D10Constants;
import org.jsynthlib.synthdrivers.RolandD10.Entity;

/**
 * @author Roger
 */
public class D10RequestMessage extends D10TransferMessage {

    public D10RequestMessage(int address, int size) {
        super(D10Constants.CMD_RQ1, D10Constants.SIZE_ADDRESS + D10Constants.SIZE_SIZE, address);
        setSize(size);
    }

    public D10RequestMessage(Entity address, Entity size) {
        super(D10Constants.CMD_RQ1, D10Constants.SIZE_ADDRESS + D10Constants.SIZE_SIZE, address.getDataValue());
        setSize(size.getDataValue());
    }

    /**
     * Sets the size.
     *
     * @param size The size to set
     */
    public void setSize(int size) {
        set3Bytes(D10Constants.OFS_SIZE, size);
    }
}
