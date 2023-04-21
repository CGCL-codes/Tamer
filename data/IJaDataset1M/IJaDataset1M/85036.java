package cn._2dland.uploader;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends FilterOutputStream {

    private long total;

    private long size;

    private IProgressHandler handler;

    private boolean finished = false;

    public CountingOutputStream(OutputStream os, long totalSize, IProgressHandler handler) {
        super(os);
        this.total = totalSize;
        this.handler = handler;
        size = 0;
        finished = false;
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        size += b.length;
        handle();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        size += len;
        handle();
    }

    /**
	 * 处理进度更新
	 */
    private void handle() {
        if (finished) return;
        if (handler != null) {
            handler.update(size, total);
            if (size >= total) {
                handler.finish();
                finished = true;
            }
        }
    }
}
