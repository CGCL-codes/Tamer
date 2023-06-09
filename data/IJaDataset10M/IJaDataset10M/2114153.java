package COM.winserver.wildcat;

import java.io.IOException;

public class TLoginUser_Request extends TWildcatRequest {

    public static final int LOGIN_NETSCAPE_16 = 0x00000001;

    public static final int LOGIN_NETSCAPE_32 = 0x00000002;

    public static final int LOGIN_IEXPLORER_16 = 0x00000004;

    public static final int LOGIN_IEXPLORER_32 = 0x00000008;

    public static final int LOGIN_NO_QUESNEW = 0x00000100;

    public static final int LOGIN_IGNORE_IDLE_TIME = 0x00000200;

    public static final int LOGIN_IGNORE_TIME_ONLINE = 0x00000400;

    public int ClientRequestTypes;

    public String Name;

    public String Password;

    public int AddNewUser;

    public static final int SIZE = TWildcatRequest.SIZE + 2 + 28 + 40 + 1;

    public TLoginUser_Request() {
        type = WildcatRequest.wrLoginUser;
    }

    public TLoginUser_Request(byte[] x) {
        fromByteArray(x);
    }

    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeShort(ClientRequestTypes);
        if (Password == null) {
            out.writeString(Name, 28 + 40);
        } else {
            out.writeString(Name, 28);
            out.writeString(Password, 40);
        }
        out.writeByte(AddNewUser);
    }

    protected void readFrom(WcInputStream in) throws IOException {
        super.readFrom(in);
        ClientRequestTypes = in.readUnsignedShort();
        Name = in.readString(28);
        Password = in.readString(40);
        AddNewUser = in.readUnsignedByte();
    }
}
