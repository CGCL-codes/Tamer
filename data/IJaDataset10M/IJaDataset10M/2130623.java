package ru.javawebcrowler.spider.statistics;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author enaku_adm
 * @since 14.10.2009 17:09:11
 */
public final class StatisticsInputStream extends InputStream {

    private final InputStream stream;

    public StatisticsInputStream(InputStream stream) {
        this.stream = stream;
    }

    private long bytesRead = 0;

    public long getBytesRead() {
        return bytesRead;
    }

    public int read() throws IOException {
        int read = stream.read();
        if (read != -1) bytesRead++;
        return read;
    }
}
