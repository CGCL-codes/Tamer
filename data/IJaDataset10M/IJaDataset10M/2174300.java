package COM.winserver.wildcat;

import java.io.IOException;

public class TGetObjectInfo_Response extends TWildcatRequest {

    public int ObjectId;

    public int ObjectFlags;

    public int ObjectNumber;

    public String ObjectName;

    public static final int SIZE = TWildcatRequest.SIZE + 4 + 4 + 4 + 260;

    public TGetObjectInfo_Response() {
    }

    public TGetObjectInfo_Response(byte[] x) {
        fromByteArray(x);
    }

    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeInt(ObjectId);
        out.writeInt(ObjectFlags);
        out.writeInt(ObjectNumber);
        out.writeString(ObjectName, 260);
    }

    protected void readFrom(WcInputStream in) throws IOException {
        super.readFrom(in);
        ObjectId = in.readInt();
        ObjectFlags = in.readInt();
        ObjectNumber = in.readInt();
        ObjectName = in.readString(260);
    }
}
