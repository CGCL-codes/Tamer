package FFIT.binFileReader;

import FFIT.IdentificationFile;
import java.io.IOException;

/**
 * The <code>InputStreamByteReader</code> class is a <code>ByteReader</code> that
 * reads its data from the <code>System.in</code> input stream.
 *
 * @author linb
 */
public class InputStreamByteReader extends StreamByteReader {

    /** Creates a new instance of UrlByteReader */
    private InputStreamByteReader(IdentificationFile theIDFile, boolean readFile) {
        super(theIDFile);
        if (readFile) {
            this.readInputStream();
        }
    }

    /**
     * Static constructor for class.  Trys to read stream into a buffer. If it doesn't fit, 
     * save it to a file, and return a FileByteReader with that file.
     */
    static ByteReader newInputStreamByteReader(IdentificationFile theIDFile, boolean readFile) {
        InputStreamByteReader byteReader = new InputStreamByteReader(theIDFile, readFile);
        if (byteReader.tempFile == null) {
            return byteReader;
        } else {
            return new FileByteReader(theIDFile, readFile, byteReader.tempFile.getPath());
        }
    }

    /** Read data into buffer or temporary file from the <code>System.in</code> input stream.
     */
    private void readInputStream() {
        try {
            readStream(System.in);
        } catch (IOException ex) {
            this.setErrorIdent();
            this.setIdentificationWarning("Input stream could not be read");
        }
    }

    /**
     * Checks if the path represents the input stream
     * @param path the path to check
     * @return <code>true</code> if <code>path</code> is equal to "-", <code>false</code> otherwise
     */
    public static boolean isInputStream(String path) {
        if ("-".equals(path)) {
            return true;
        } else {
            return false;
        }
    }
}
