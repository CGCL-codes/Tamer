package net.sf.leechget.cloud.transfer.client.handler.message;

import net.sf.leechget.cloud.transfer.message.annotation.Opcode;
import org.apache.mina.core.buffer.IoBuffer;

@Opcode(ClientMessage.CM_TRANSFER_STOP)
public class CM_TRANSFER_STOP extends ClientMessage {

    private long id;

    public CM_TRANSFER_STOP(long id) {
        this.id = id;
    }

    @Override
    public void write(IoBuffer buffer) {
        buffer.putLong(id);
    }
}
