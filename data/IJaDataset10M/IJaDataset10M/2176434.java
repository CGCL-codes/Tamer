package ejp.presenter.parser;

import java.io.IOException;

public abstract class ByteBufferHelper {

    private ByteBufferHelper() {
    }

    public static String readString(IDataInput buffer_) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (; ; ) {
            byte b = buffer_.get();
            if (b == 0) {
                break;
            }
            sb.append((char) b);
        }
        return sb.toString();
    }

    public static long readEncoded(IDataInput buffer_) throws IOException {
        long result = 0;
        int shift = 0;
        while (true) {
            long curr = readU1(buffer_);
            if (curr < 0x80) return result + (curr << shift);
            result += (curr & 0x7F) << shift;
            shift += 7;
        }
    }

    public static short readU1(IDataInput buffer_) throws IOException {
        byte b = buffer_.get();
        short result = b;
        if (result < 0) {
            result += 0x100;
        }
        return result;
    }

    public static long readU4(IDataInput buffer_) throws IOException {
        int i = buffer_.getInt();
        long result = i;
        if (result < 0) {
            result += 0x100000000L;
        }
        return result;
    }
}
