package net.sf.leechget.cloud.transfer.server.message;

import net.sf.leechget.cloud.transfer.message.annotation.Opcode;
import org.apache.mina.core.buffer.IoBuffer;

@Opcode(ServerMessage.SM_NOOP)
public class SM_NOOP extends ServerMessage {

    @Override
    public void write(IoBuffer buffer) {
    }
}
