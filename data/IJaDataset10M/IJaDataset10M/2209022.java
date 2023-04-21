package ps.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AddUserContent implements PacketContent {

    String userName = "";

    byte[] authId = new byte[16];

    AddUserContent() {
    }

    public AddUserContent(String userName, byte[] authId) {
        this.userName = userName;
        this.authId = authId;
    }

    @Override
    public void writeContent(OutputStream out) throws IOException {
        Packet.writeString(out, userName);
        out.write(authId);
    }

    @Override
    public void readContent(InputStream in) throws IOException {
        userName = Packet.readString(in);
        in.read(authId);
    }

    @Override
    public String toString() {
        String ret = "[ AddUser |";
        ret += " userName=" + userName;
        ret += " authId=" + "################";
        ret += " ]";
        return ret;
    }

    public String getUserName() {
        return userName;
    }

    public byte[] getAuthId() {
        return authId;
    }
}
