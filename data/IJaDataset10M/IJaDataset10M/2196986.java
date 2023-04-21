package com.netx.cubigraf.backups;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.netx.generics.basic.Checker;
import com.netx.generics.basic.Logger;
import com.netx.generics.io.Directory;
import com.netx.generics.io.File;
import com.netx.generics.io.Streams;
import com.netx.generics.tasks.BackgroundTask;
import com.netx.generics.tasks.TaskWorker;

public class DirectoryCopyTask extends BackgroundTask {

    private final TaskWorker _endWorker;

    private final TaskWorker _failedWorker;

    private final String _username;

    private final String _reference;

    private long _srcBytesTotal = 0;

    private long _srcBytesRead = 0;

    public DirectoryCopyTask(String username, Directory src, Directory dest, TaskWorker endWorker, TaskWorker failedWorker, Logger logger) {
        Checker.checkNull(src, "src");
        Checker.checkNull(dest, "dest");
        _endWorker = endWorker;
        _failedWorker = failedWorker;
        _username = username;
        _reference = src.getName();
        try {
            _srcBytesTotal = src.getSize();
            _addFileCopyWorkers(dest, src, logger);
            if (_endWorker != null) {
                addWorker(_endWorker);
            }
        } catch (Throwable t) {
            logger.error(t);
            removeWorkers();
            if (_failedWorker != null) {
                addWorker(_failedWorker);
            }
        }
    }

    public String getUsername() {
        return _username;
    }

    public String getReference() {
        return _reference;
    }

    public double getBytesTotal() {
        return _srcBytesTotal;
    }

    public double getBytesCopied() {
        return _srcBytesRead;
    }

    private void _addFileCopyWorkers(Directory dest, Directory src, Logger logger) throws IOException {
        File[] files = src.getFiles();
        for (int i = 0; i < files.length; i++) {
            logger.info("adding worker for file \"" + files[i].getPath());
            addWorker(new FileCopyWorker(dest, files[i]));
        }
        Directory[] srcDirs = src.getDirectories();
        for (int i = 0; i < srcDirs.length; i++) {
            logger.info("descending into directory \"" + srcDirs[i].getPath() + "\"");
            Directory temp = dest.mkdirs(srcDirs[i].getName());
            _addFileCopyWorkers(temp, srcDirs[i], logger);
        }
    }

    private class FileCopyWorker extends TaskWorker {

        private final File _fIn;

        private final File _fOut;

        private final byte[] _buffer;

        private InputStream _in;

        private OutputStream _out;

        private int _bytesRead;

        public FileCopyWorker(Directory dest, File file) throws IOException {
            _fIn = file;
            _fOut = dest.createFile(_fIn.getName());
            _bytesRead = 0;
            _buffer = new byte[Streams.getDefaultBufferSize()];
        }

        protected void performWork() throws IOException {
            try {
                if (_in == null) {
                    getLogger().info("started copy of file \"" + _fIn.getPath() + "\"");
                    _in = _fIn.getInputStream();
                    _out = _fOut.getOutputStream();
                }
                _bytesRead = _in.read(_buffer, 0, _buffer.length);
                _srcBytesRead += _bytesRead;
                if (_bytesRead > 0) {
                    _out.write(_buffer, 0, _bytesRead);
                    _out.flush();
                } else {
                    _in.close();
                    _out.close();
                    _fOut.setLastModified(_fIn.getLastModified());
                    if (_fIn.isReadOnly()) {
                        _fOut.makeReadOnly();
                    }
                    markFinished();
                }
            } catch (Throwable t) {
                getLogger().error(t);
                removeWorkers();
                if (_failedWorker != null) {
                    addWorker(_failedWorker);
                }
                markFinished();
            }
        }
    }
}
