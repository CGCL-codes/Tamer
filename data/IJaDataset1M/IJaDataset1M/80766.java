package javax.media.protocol;

import java.io.*;

public interface PullSourceStream extends SourceStream {

    public boolean willReadBlock();

    public int read(byte[] buffer, int offset, int length) throws IOException;
}
