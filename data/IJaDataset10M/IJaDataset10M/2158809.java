package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.codec.CodecUtil;

/**
 * Abstract implementation of {@link StorageProvider} that implements
 * {@link StorageProvider#store(InputStream) store(InputStream)} by copying the
 * input stream to a {@link StorageOutputStream} obtained from
 * {@link StorageProvider#createStorageOutputStream() createStorageOutputStream()}.
 */
public abstract class AbstractStorageProvider implements StorageProvider {

    /**
     * Sole constructor.
     */
    protected AbstractStorageProvider() {
    }

    /**
     * This implementation creates a {@link StorageOutputStream} by calling
     * {@link StorageProvider#createStorageOutputStream() createStorageOutputStream()}
     * and copies the content of the given input stream to that output stream.
     * It then calls {@link StorageOutputStream#toStorage()} on the output
     * stream and returns this object.
     *
     * @param in
     *            stream containing the data to store.
     * @return a {@link Storage} instance that can be used to retrieve the
     *         stored content.
     * @throws IOException
     *             if an I/O error occurs.
     */
    public final Storage store(InputStream in) throws IOException {
        StorageOutputStream out = createStorageOutputStream();
        CodecUtil.copy(in, out);
        return out.toStorage();
    }
}
