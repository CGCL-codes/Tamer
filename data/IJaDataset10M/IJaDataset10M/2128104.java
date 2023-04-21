package naga.packetreader;

import naga.PacketReader;
import naga.exception.ProtocolViolationException;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import java.nio.ByteBuffer;

/**
 * Example filter reader that decrypts the stream before passing it to its underlying reader.
 *
 * @author Christoffer Lerno
 */
public class CipherPacketReader implements PacketReader {

    private final Cipher m_cipher;

    private ByteBuffer m_internalBuffer;

    private PacketReader m_reader;

    /**
     * Creates a new CipherPacketReader.
     *
     * @param cipher the cipher to use.
     * @param reader the underlying packet reader we wish to employ.
     */
    public CipherPacketReader(Cipher cipher, PacketReader reader) {
        m_cipher = cipher;
        m_reader = reader;
    }

    public PacketReader getReader() {
        return m_reader;
    }

    public void setReader(PacketReader reader) {
        m_reader = reader;
    }

    public byte[] nextPacket(ByteBuffer byteBuffer) throws ProtocolViolationException {
        if (m_internalBuffer == null) {
            m_internalBuffer = ByteBuffer.allocate(m_cipher.getOutputSize(byteBuffer.remaining()));
        } else {
            if (byteBuffer.remaining() > 0) {
                ByteBuffer newBuffer = ByteBuffer.allocate(m_cipher.getOutputSize(byteBuffer.remaining()) + m_internalBuffer.remaining());
                newBuffer.put(m_internalBuffer);
                m_internalBuffer = newBuffer;
            }
        }
        if (byteBuffer.remaining() > 0) {
            try {
                m_cipher.update(byteBuffer, m_internalBuffer);
            } catch (ShortBufferException e) {
                throw new ProtocolViolationException("Short buffer");
            }
            m_internalBuffer.flip();
        }
        byte[] packet = m_reader.nextPacket(m_internalBuffer);
        if (m_internalBuffer.remaining() == 0) m_internalBuffer = null;
        return packet;
    }
}
