package tjacobs.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

/**
 * The purpose of ReaderSource is to provide an interface to a resource that can
 * generate a reader repeatedly, as once a reader is read you can't read it again
 * normally
 * @author tjacobs01
 *
 */
public interface ReaderSource {

    public Reader getReader();

    public static class ReaderSourceFailedException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ReaderSourceFailedException(Exception ex) {
            super(ex);
        }
    }

    public static class FileReaderSource implements ReaderSource {

        File mFile;

        public FileReaderSource(File f) {
            mFile = f;
        }

        public Reader getReader() {
            try {
                return new FileReader(mFile);
            } catch (FileNotFoundException ex) {
                throw new ReaderSourceFailedException(ex);
            }
        }
    }

    public static class URLReaderSource implements ReaderSource {

        URL mURL;

        public URLReaderSource(URL url) {
            mURL = url;
        }

        public Reader getReader() {
            try {
                return new InputStreamReader(mURL.openStream());
            } catch (IOException ex) {
                throw new ReaderSourceFailedException(ex);
            }
        }
    }

    public static class StringReaderSource implements ReaderSource {

        String mString;

        public StringReaderSource(String str) {
            mString = str;
        }

        public Reader getReader() {
            return new StringReader(mString);
        }
    }
}
