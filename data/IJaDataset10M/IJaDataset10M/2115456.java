package hotheart.starcraft.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatFile {

    InputStream is;

    public DatFile(InputStream _is) {
        is = _is;
    }

    public final void skip(int offset) throws IOException {
        is.skip(offset);
    }

    public final byte[] read1ByteData(int size) throws IOException {
        byte[] res = new byte[size];
        for (int i = 0; i < size; i++) {
            res[i] = (byte) is.read();
        }
        return res;
    }

    public final int[] read1ByteDataInt(int size) throws IOException {
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            res[i] = is.read() & 0xFF;
        }
        return res;
    }

    public final int[] read4ByteData(int size) throws IOException {
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            res[i] = (((byte) is.read()) & 0xFF) + ((((byte) is.read()) & 0xFF) << 8) + ((((byte) is.read()) & 0xFF) << 16) + ((((byte) is.read()) & 0xFF) << 24);
        }
        return res;
    }

    public final int[] read4ByteData2InnerBytes(int size) throws IOException {
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            is.read();
            res[i] = (((byte) is.read()) & 0xFF) + ((((byte) is.read()) & 0xFF) << 8);
            is.read();
        }
        return res;
    }

    public final int[] read2ByteData(int size) throws IOException {
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            res[i] = (((byte) is.read()) & 0xFF) + ((((byte) is.read()) & 0xFF) << 8);
        }
        return res;
    }
}
