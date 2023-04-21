package net.cimd.packets;

import net.cimd.packets.parameters.*;

/**
 * @version $Revision: 1.1 $ $Date: 2007/03/14 14:15:08 $
 */
public class DeliveryRequest extends CimdPacket {

    public DeliveryRequest() {
        super(Opcodes.PCK_DELIVERY_REQUEST);
    }

    public byte getMode() {
        return (byte) getIntegerParamValue(Opcodes.OP_DELIVERY_REQUEST_MODE);
    }

    public void setMode(byte mode) throws InvalidContentException {
        addParam(new IntegerParameter(Opcodes.OP_DELIVERY_REQUEST_MODE, mode, new BoundedIntParamRestriction(1, 9)));
    }
}
