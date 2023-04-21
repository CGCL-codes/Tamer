package MessageFramework;

public class LIMessage {

    private static byte STX = 2;

    private static byte ETX = 3;

    protected MessageType m_msgType;

    protected String m_payload;

    public LIMessage(MessageType msgType, String payload) {
        m_msgType = msgType;
        m_payload = payload;
    }

    public LIMessage() {
    }

    public byte[] getEncodedMsg() {
        byte[] result = new byte[4 + m_payload.length()];
        result[0] = STX;
        result[1] = (byte) m_msgType.ordinal();
        result[2] = ':';
        for (int i = 0; i < m_payload.length(); i++) {
            result[i + 3] = (byte) m_payload.charAt(i);
        }
        result[3 + m_payload.length()] = ETX;
        return result;
    }

    public static LIMessage setEncodedMsg(byte[] msg) {
        MessageType msgType = (msg[1] == 0 ? MessageType.Command : MessageType.Debug);
        byte[] payloadBytesOnly = new byte[msg.length - 4];
        for (int i = 0; i < payloadBytesOnly.length; i++) {
            payloadBytesOnly[i] = msg[i + 3];
        }
        String payload = new String(payloadBytesOnly);
        return new LIMessage(msgType, payload);
    }
}

enum MessageType {

    Command, Debug
}
