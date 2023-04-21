package org.opencraft.server.net.codec;

import java.util.Arrays;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.opencraft.server.net.packet.Packet;
import org.opencraft.server.net.packet.PacketDefinition;
import org.opencraft.server.net.packet.PacketField;

/**
 * An implementation of a <code>ProtocolEncoder</code> which encodes Minecraft
 * packet objects into buffers and then dispatches them.
 * @author Graham Edgecombe
 */
public final class MinecraftProtocolEncoder extends ProtocolEncoderAdapter {

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        Packet packet = (Packet) message;
        PacketDefinition def = packet.getDefinition();
        IoBuffer buf = IoBuffer.allocate(def.getLength() + 1);
        buf.put((byte) def.getOpcode());
        for (PacketField field : def.getFields()) {
            switch(field.getType()) {
                case BYTE:
                    buf.put(packet.getNumericField(field.getName()).byteValue());
                    break;
                case SHORT:
                    buf.putShort(packet.getNumericField(field.getName()).shortValue());
                    break;
                case INT:
                    buf.putInt(packet.getNumericField(field.getName()).intValue());
                    break;
                case LONG:
                    buf.putLong(packet.getNumericField(field.getName()).longValue());
                    break;
                case BYTE_ARRAY:
                    byte[] data = packet.getByteArrayField(field.getName());
                    byte[] resized = Arrays.copyOf(data, 1024);
                    buf.put(resized);
                    break;
                case STRING:
                    String str = packet.getStringField(field.getName());
                    data = str.getBytes();
                    resized = Arrays.copyOf(data, 64);
                    for (int i = 0; i < resized.length; i++) {
                        if (resized[i] == 0) {
                            resized[i] = ' ';
                        }
                    }
                    buf.put(resized);
                    break;
            }
        }
        buf.flip();
        out.write(buf);
    }
}
