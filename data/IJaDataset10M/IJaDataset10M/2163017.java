package tripleo.framework.svr.storage;

import tripleo.framework.fs.FileOpFailure;
import tripleo.framework.io.ReadableByteStream;

public interface FileReader {

    public abstract long available();

    public abstract ReadableByteStream read(int i) throws FileOpFailure;
}
