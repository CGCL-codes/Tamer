package org.agile.dfs.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import org.agile.dfs.client.cache.BlockCache;
import org.agile.dfs.client.service.DfsServiceLocator;
import org.agile.dfs.name.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DfsOutputStream extends OutputStream {

    private static final Logger logger = LoggerFactory.getLogger(DfsOutputStream.class);

    private static final FileService fileService = DfsServiceLocator.lookup(FileService.class);

    private DfsFile dfsFile;

    private BlockCache cache;

    public DfsOutputStream(String schema, String fileFullPath) throws IOException {
        this(new DfsFile(schema, fileFullPath));
    }

    public DfsOutputStream(DfsFile dfile) throws IOException {
        this.dfsFile = dfile;
        if (!dfile.exists()) {
            throw new FileNotFoundException(dfile.toString());
        }
        cache = new BlockCache(dfile);
    }

    public void write(int b) throws IOException {
        this.write(new byte[] { (byte) b }, 0, 1);
    }

    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        cache.write(b, off, len);
    }

    public void close() throws IOException {
        if (cache != null) {
            cache.flush();
            cache.close();
            fileService.commit(dfsFile.getSchema(), dfsFile.getFullPath());
            cache = null;
            logger.info("Close dfs output stream." + dfsFile);
        }
    }

    public void flush() throws IOException {
        cache.flush();
    }

    protected void finalize() throws Throwable {
        this.close();
    }
}
