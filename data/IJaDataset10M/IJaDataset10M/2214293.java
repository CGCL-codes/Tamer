package utils.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import com.imagero.uio.impl.AbstractRandomAccessInput;

public abstract class UIONumberInputStream extends FilterInputStream {

    private final AbstractRandomAccessInput abstractRandomAccessInput;

    public UIONumberInputStream(AbstractRandomAccessInput abstractRandomAccessInput) {
        super(abstractRandomAccessInput);
        this.abstractRandomAccessInput = abstractRandomAccessInput;
    }

    public abstract Number readBigEndianSigned() throws IOException;

    public abstract Number readLittleEndianSigned() throws IOException;

    public abstract Number readBigEndianUnsigned() throws IOException;

    public abstract Number readLittleEndianUnsigned() throws IOException;

    public void seek(long position) throws IOException {
        this.abstractRandomAccessInput.seek(position);
    }
}
