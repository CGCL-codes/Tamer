package moxie.log;

import ostore.util.ByteArrayOutputBuffer;
import ostore.util.ByteUtils;
import ostore.util.CountBuffer;
import ostore.util.InputBuffer;
import ostore.util.OutputBuffer;
import ostore.util.QuickSerializable;
import java.util.Arrays;
import java.io.IOException;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.XdrEncodingStream;
import org.acplt.oncrpc.XdrDecodingStream;

public class DataBlockImpl extends log_data_block implements DataBlock {

    private static final int BYTES_TO_PRINT = 4;

    public DataBlockImpl(byte[] data) {
        this.size = data.length;
        this.data = new log_data();
        this.data.empty = false;
        this.data.data = data;
        return;
    }

    protected DataBlockImpl(LogMetadata md) {
        CountBuffer cb = new CountBuffer();
        md.serialize(cb);
        byte[] bytes = new byte[cb.size()];
        ByteArrayOutputBuffer bb = new ByteArrayOutputBuffer(bytes);
        md.serialize(bb);
        this.size = bytes.length;
        this.data = new log_data();
        this.data.empty = false;
        this.data.data = bytes;
        return;
    }

    protected DataBlockImpl(int size) {
        this.size = size;
        this.data = new log_data();
        this.data.empty = true;
        this.data.data = null;
        return;
    }

    public DataBlockImpl(log_data_block data) {
        this.size = data.size;
        this.data = data.data;
        return;
    }

    public DataBlockImpl(InputBuffer buffer) throws ostore.util.QSException {
        this.size = buffer.nextInt();
        this.data = new log_data();
        this.data.empty = (buffer.nextInt() == 1);
        if (!this.data.empty) {
            int size = buffer.nextInt();
            assert size == this.size : "size=" + size + " != this.size=" + this.size;
            this.data.data = new byte[this.size];
            buffer.nextBytes(this.data.data, 0, this.size);
        }
        return;
    }

    public DataBlockImpl(XdrDecodingStream buffer) throws OncRpcException, IOException {
        xdrDecode(buffer);
    }

    /** Specified by ostore.util.QuickSerializable */
    public void serialize(OutputBuffer buffer) {
        buffer.add(this.size);
        buffer.add((int) (this.data.empty ? 1 : 0));
        if (!this.data.empty) {
            assert this.data.data != null && this.data.data.length == this.size : "this.size=" + this.size + " != this.data.data.length=" + (this.data.data != null ? this.data.data.length : -1);
            buffer.add(this.data.data.length);
            buffer.add(this.data.data, 0, this.data.data.length);
        }
        return;
    }

    /** Specified by moxie.log.DataBlock */
    public boolean isEmpty() {
        return this.data.empty;
    }

    /** Specified by moxie.log.DataBlock */
    public int getSize() {
        return this.size;
    }

    /** Specified by moxie.log.DataBlock */
    public byte[] getData() {
        return this.data.data;
    }

    /** Specified by java.lang.Object */
    public boolean equals(Object other) {
        boolean equal = false;
        if (other == null) {
            equal = false;
        } else if (!(other instanceof DataBlockImpl)) {
            equal = false;
        } else {
            if (other == this) {
                equal = true;
            } else {
                DataBlockImpl other_block = (DataBlockImpl) other;
                equal = (this.size == other_block.size) && (this.data.empty == other_block.data.empty) && (Arrays.equals(this.data.data, other_block.data.data));
            }
        }
        return equal;
    }

    /** Specified by java.lang.Object */
    public String toString() {
        int bytes_to_print = Math.min(this.size, BYTES_TO_PRINT);
        String s = "(DataBlock:" + " empty=" + this.data.empty + " size=" + this.size + " data=" + (this.data.empty ? "null" : ByteUtils.print_bytes(this.data.data, 0, bytes_to_print)) + ")";
        return s;
    }
}
