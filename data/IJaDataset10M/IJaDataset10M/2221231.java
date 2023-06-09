package COM.winserver.wildcat;

import java.io.IOException;

public class TWildcatLogOptions extends WcRecord {

    public boolean EnableSessionTrace;

    public TWildcatLogPeriod LogPeriod;

    public int dwMaxSize;

    public int dwVerbosity;

    public static final int SIZE = 0 + 4 + TWildcatLogPeriod.SIZE + 4 + 4 + 16 * 1;

    public TWildcatLogOptions() {
    }

    public TWildcatLogOptions(byte[] x) {
        fromByteArray(x);
    }

    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeBoolean(EnableSessionTrace);
        LogPeriod.writeTo(out);
        out.writeInt(dwMaxSize);
        out.writeInt(dwVerbosity);
        out.write(new byte[16 * 1]);
    }

    protected void readFrom(WcInputStream in) throws IOException {
        super.readFrom(in);
        EnableSessionTrace = in.readBoolean();
        LogPeriod = new TWildcatLogPeriod();
        LogPeriod.readFrom(in);
        dwMaxSize = in.readInt();
        dwVerbosity = in.readInt();
        in.skip(16 * 1);
    }
}
