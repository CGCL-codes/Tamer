package com.mac.invalidname.spmovie;

import javax.media.*;
import javax.media.protocol.*;
import java.io.*;
import java.net.*;

/** A class to provide a DataSource to an entry within a 
    jar file, perhaps found with ClassLoader.getResource().
    Basically, this class exists to provide PullSourceStreams
    that do little more than wrap the input stream derived
    from the jar entry.
 */
public class JarEntryDataSource extends PullDataSource {

    protected static Object[] EMPTY_OBJECT_ARRAY = {};

    protected JarEntryPullStream jarIn;

    protected PullSourceStream[] sourceStreams;

    public JarEntryDataSource(MediaLocator ml) throws IllegalArgumentException, IOException {
        super();
        setLocator(ml);
    }

    protected void createJarIn() throws IOException {
        jarIn = new JarEntryPullStream();
        sourceStreams = new PullSourceStream[1];
        sourceStreams[0] = jarIn;
    }

    public void setLocator(MediaLocator ml) throws IllegalArgumentException {
        if (!ml.getProtocol().equals("jar")) throw new IllegalArgumentException("Not a jar:-style URL: " + ml.toString());
        super.setLocator(ml);
        try {
            createJarIn();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public PullSourceStream[] getStreams() {
        return sourceStreams;
    }

    public void connect() throws IOException {
    }

    public void disconnect() {
        if (jarIn != null) {
            jarIn.close();
            jarIn = null;
        }
    }

    /** A pretty bare-bones implementation, only supports
        <code>video.quicktime</code>, <code>video.mpeg</code>
        and <code>video.x_msvideo</code>, based solely on
        filename extension.
     */
    public String getContentType() {
        try {
            URL url = getLocator().getURL();
            String urlFile = url.getFile();
            if (urlFile.endsWith(".mov")) return "video.quicktime"; else if (urlFile.endsWith(".mpg")) return "video.mpeg"; else if (urlFile.endsWith(".avi")) return "video.x_msvideo"; else return "unknown";
        } catch (MalformedURLException murle) {
            return "unknown";
        }
    }

    public void start() {
    }

    public void stop() {
    }

    public Time getDuration() {
        return DataSource.DURATION_UNKNOWN;
    }

    public Object getControl(String controlName) {
        return null;
    }

    public Object[] getControls() {
        return EMPTY_OBJECT_ARRAY;
    }

    class JarEntryPullStream extends Object implements PullSourceStream, Seekable {

        protected InputStream in;

        protected ContentDescriptor unknownCD = new ContentDescriptor("unknown");

        protected long tellPoint;

        public JarEntryPullStream() throws IOException {
            open();
        }

        public void open() throws IOException {
            URL url = getLocator().getURL();
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            in = conn.getInputStream();
            tellPoint = 0;
        }

        public void close() {
            try {
                in.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        public void thoroughSkip(long skipCount) throws IOException {
            long totalSkipped = 0;
            while (totalSkipped < skipCount) {
                long skipped = in.skip(skipCount - totalSkipped);
                totalSkipped += skipped;
                tellPoint += skipped;
            }
        }

        public int read(byte[] buf, int off, int length) throws IOException {
            int bytesRead = in.read(buf, off, length);
            tellPoint += bytesRead;
            return bytesRead;
        }

        public boolean willReadBlock() {
            try {
                return (in.available() > 0);
            } catch (IOException ioe) {
                return true;
            }
        }

        public long getContentLength() {
            return SourceStream.LENGTH_UNKNOWN;
        }

        public boolean endOfStream() {
            try {
                return (in.available() == -1);
            } catch (IOException ioe) {
                return true;
            }
        }

        public ContentDescriptor getContentDescriptor() {
            return unknownCD;
        }

        public Object getControl(String controlType) {
            return null;
        }

        public Object[] getControls() {
            return EMPTY_OBJECT_ARRAY;
        }

        public boolean isRandomAccess() {
            return true;
        }

        public long seek(long position) {
            try {
                if (position > tellPoint) {
                    thoroughSkip(position - tellPoint);
                } else {
                    close();
                    open();
                    thoroughSkip(position);
                }
                return tellPoint;
            } catch (IOException ioe) {
                return 0;
            }
        }

        public long tell() {
            return tellPoint;
        }
    }
}
