package pl.szpadel.android.gadu.packets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import pl.szpadel.android.gadu.AGLog;

/** Package header */
public class Header {

    private static final String TAG = "Header";

    private static ByteBuffer mReadBuffer = ByteBuffer.allocateDirect(8);

    private static ByteBuffer mWriteBuffer = ByteBuffer.allocateDirect(8);

    private int mType = 0;

    private int mLength = 0;

    public int getType() {
        return mType;
    }

    public int getLength() {
        return mLength;
    }

    public Header(int type, int len) {
        mType = type;
        mLength = len;
    }

    public static Header createFromChannel(ReadableByteChannel channel) throws IOException {
        mReadBuffer.clear();
        while (mReadBuffer.position() < 8) {
            int r = channel.read(mReadBuffer);
            if (r < 0) {
                throw new IOException("Channel closed while reading header");
            } else if (r == 0) {
                throw new IOException("Header: channel has no data!!!! read so far:" + mReadBuffer.position());
            }
        }
        AGLog.d(TAG, "reading finished");
        mReadBuffer.flip();
        mReadBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return new Header(mReadBuffer.getInt(), mReadBuffer.getInt());
    }

    public void toChannel(WritableByteChannel channel) throws IOException {
        mWriteBuffer.clear();
        mWriteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mWriteBuffer.putInt(mType);
        mWriteBuffer.putInt(mLength);
        mWriteBuffer.flip();
        channel.write(mWriteBuffer);
    }

    public String toString() {
        return "Header [type=0x" + Integer.toHexString(mType) + ", len=" + mLength + " ]";
    }
}
