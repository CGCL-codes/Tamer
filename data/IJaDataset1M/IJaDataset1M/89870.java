package com.netx.cubigraf.servlets;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import com.netx.generics.basic.Checker;
import com.netx.generics.io.Directory;
import com.netx.generics.io.File;
import com.netx.generics.io.FilenameFilter;
import com.netx.generics.io.SearchOptions;
import com.netx.generics.io.Streams;
import com.netx.generics.io.SearchOptions.ORDER;
import com.netx.generics.tasks.ForegroundTask;
import com.netx.ebs.Session;

public class ForegroundCopy extends ForegroundTask {

    public static final String FILE_COPY_SIZE = "file-copy.size";

    public static final String FILE_COPY_BYTES_WRITTEN = "file-copy.bytes-written";

    public static final String FILE_COPY_STARTED = "file-copy.started";

    private final Session _session;

    private final Directory _to;

    private final Directory _from;

    private long _totalBytes;

    private long _bytesWritten;

    private SearchOptions _options;

    public ForegroundCopy(Session session, Directory to, Directory from) {
        Checker.checkNull(session, "session");
        Checker.checkNull(to, "to");
        Checker.checkNull(from, "from");
        _session = session;
        _to = to;
        _from = from;
        _bytesWritten = 0;
        _totalBytes = 0;
        _options = new SearchOptions((FilenameFilter) null, true, ORDER.NAME_ASCENDING);
    }

    protected void performWork() throws Throwable {
        _totalBytes = _from.getSize(true);
        synchronized (_session) {
            _session.setAttribute(ForegroundCopy.FILE_COPY_STARTED, _from.getName());
            _session.setAttribute(FILE_COPY_SIZE, new Long(_totalBytes));
            _session.setAttribute(FILE_COPY_BYTES_WRITTEN, new Long(_bytesWritten));
        }
        _copyDirectory(_to, _from);
        synchronized (_session) {
            _session.setAttribute(FILE_COPY_STARTED, null);
        }
    }

    private void _copyDirectory(Directory to, Directory from) throws IOException, InterruptedException {
        getLogger().info("copying directory: " + from.getPath());
        File[] filesFrom = from.getFiles(_options);
        for (int i = 0; i < filesFrom.length; i++) {
            getLogger().info("copying file: " + filesFrom[i].getPath());
            _copyFile(to, filesFrom[i]);
        }
        Directory[] dirsFrom = from.getDirectories(_options);
        for (int i = 0; i < dirsFrom.length; i++) {
            Directory dirTo = to.mkdirs(dirsFrom[i].getName());
            _copyDirectory(dirTo, dirsFrom[i]);
        }
    }

    private void _copyFile(Directory to, File file) throws IOException, InterruptedException {
        File dest = to.createFile(file.getName());
        OutputStream out = dest.getOutputStream();
        InputStream in = file.getInputStream();
        byte[] buffer = new byte[Streams.getDefaultBufferSize()];
        int bytesRead = in.read(buffer, 0, buffer.length);
        _bytesWritten += bytesRead;
        while (bytesRead > 0) {
            Thread.sleep(1);
            out.write(buffer, 0, bytesRead);
            bytesRead = in.read(buffer, 0, buffer.length);
            if (bytesRead != -1) {
                _bytesWritten += bytesRead;
            }
            synchronized (_session) {
                _session.setAttribute(FILE_COPY_BYTES_WRITTEN, new Long(_bytesWritten));
            }
        }
        in.close();
        out.close();
        dest.setLastModified(file.getLastModified());
        if (file.isReadOnly()) {
            dest.makeReadOnly();
        }
    }
}
