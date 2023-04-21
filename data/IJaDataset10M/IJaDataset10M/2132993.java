package de.fu_berlin.inf.gmanda.util.progress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import de.fu_berlin.inf.gmanda.exceptions.UserCancelationException;

/**
 * InputStream that counts the number of bytes succesfully read or skipped and
 * notifies a listener of changes.
 * 
 * Adapted from from com.limegroup.gnutella.util;
 */
public class ProgressInputStream extends FilterInputStream {

    protected int count = 0;

    protected StatusListener listener;

    /**
	 * Use this is you want to use the progress input stream and use your own
	 * listener;
	 */
    public interface StatusListener {

        void setStatus(int status);

        void close();
    }

    public static class IProgressListener implements StatusListener {

        int bytesPerProgress = 1;

        int status = -1;

        public IProgress progress;

        public void close() {
            progress.done();
        }

        public IProgressListener(IProgress progress, File f) {
            this(progress, (int) (f.length() / 100));
        }

        public IProgressListener(IProgress progress, int bytesPerProgress) {
            this.progress = progress;
            progress.setStyle(IProgress.ProgressStyle.ROTATING);
            progress.setScale(100);
            this.bytesPerProgress = Math.max(1, bytesPerProgress);
        }

        public void setStatus(int status) {
            if (progress.isCanceled()) throw new UserCancelationException();
            if (status / bytesPerProgress > this.status) {
                if (this.status == -1) progress.start();
                this.status = status / bytesPerProgress;
                progress.setProgress(this.status);
            }
        }
    }

    public ProgressInputStream(InputStream in, StatusListener listener) {
        super(in);
        this.listener = listener;
    }

    /**
	 * Using a rotating progress using the given bytesPerProgress tick and a
	 * scale of 100
	 * 
	 * @param in
	 * @param progress
	 * @param bytesPerProgress
	 */
    public ProgressInputStream(InputStream in, IProgress progress, int bytesPerProgress) {
        this(in, new IProgressListener(progress, bytesPerProgress));
    }

    /**
	 * Open a input Stream based on the given file
	 * @param f
	 * @param progress
	 * @throws FileNotFoundException
	 */
    public ProgressInputStream(File f, IProgress progress) throws FileNotFoundException {
        this(new FileInputStream(f), new IProgressListener(progress, f));
    }

    public int read() throws IOException {
        int read = super.read();
        count++;
        listener.setStatus(count);
        return read;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int read = super.read(b, off, Math.min(128, len));
        count += read;
        listener.setStatus(count);
        return read;
    }

    public long skip(long n) throws IOException {
        long skipped = super.skip(n);
        count += (int) skipped;
        listener.setStatus(count);
        return skipped;
    }
}
