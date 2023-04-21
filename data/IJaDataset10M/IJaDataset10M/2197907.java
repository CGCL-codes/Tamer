package utils.stream;

import java.io.IOException;
import com.imagero.uio.impl.AbstractRandomAccessInput;

public class UIOByteInputStream extends UIONumberInputStream {

    public UIOByteInputStream(AbstractRandomAccessInput abstractRandomAccessInput) {
        super(abstractRandomAccessInput);
    }

    @Override
    public Byte readBigEndianSigned() throws IOException {
        return (byte) super.read();
    }

    @Override
    public Short readBigEndianUnsigned() throws IOException {
        return (short) super.read();
    }

    @Override
    public Byte readLittleEndianSigned() throws IOException {
        return (byte) super.read();
    }

    @Override
    public Short readLittleEndianUnsigned() throws IOException {
        return (short) super.read();
    }
}
