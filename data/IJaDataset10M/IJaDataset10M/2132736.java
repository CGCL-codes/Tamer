package org.wikiup.core.imp.releasable;

import java.io.Closeable;
import java.io.IOException;
import org.wikiup.core.inf.Releasable;
import org.wikiup.util.StreamUtil;

public class CloseableObjectRelease implements Releasable, Closeable {

    private Closeable closeable;

    public CloseableObjectRelease(Closeable object) {
        closeable = object;
    }

    public void release() {
        StreamUtil.close(closeable);
    }

    public void close() throws IOException {
        closeable.close();
    }
}
