package Messages;

import java.io.*;

/**
 *
 * @author Setsuna
 */
public class DataSetMessage extends Message {

    private User user;

    private int connId;

    private byte[] passwordHash;

    private int errorCode;

    public DataSetMessage(byte[] message) {
        super(message);
        passwordHash = new byte[16];
        try {
            DataInputStream input = new DataInputStream(new ByteArrayInputStream(message));
            input.skip(1);
            connId = input.readInt();
            if (input.available() > 1) {
                input.read(passwordHash, 0, 16);
                if (super.isZeroArray(passwordHash)) {
                    passwordHash = null;
                }
                user = new User("", input.readUTF());
            } else {
                errorCode = input.readByte();
            }
        } catch (IOException e) {
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getConnId() {
        return connId;
    }

    public String getPasswordHash() {
        return (passwordHash == null) ? "" : MD5.asHex(passwordHash);
    }

    public User getUser() {
        return user;
    }

    public static byte[] generateClientMessage(int connId, byte[] password, String furtherData) {
        try {
            byte type = (byte) Message.TYPE_DATASET;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(bos);
            os.write(type);
            os.writeInt(connId);
            os.write(password);
            os.writeUTF(furtherData);
            return Message.writeSizeToPackage(bos.toByteArray());
        } catch (IOException e) {
        }
        return null;
    }

    public static byte[] generateServerMessage(int connId, int errorCode) {
        try {
            byte type = (byte) Message.TYPE_DATASET;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(bos);
            os.write(type);
            os.writeInt(connId);
            os.write(errorCode);
            return Message.writeSizeToPackage(bos.toByteArray());
        } catch (IOException e) {
        }
        return null;
    }
}
