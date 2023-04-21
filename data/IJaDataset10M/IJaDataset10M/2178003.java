package nl.huub.van.amelsvoort.bsp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;

/**
 * An arhive of files in Quake 1/2 PAK File format
 *
 * @author Kevin Glass
 */
public class PAKArchive {

    /** The file listing of this pak */
    private PAKListing listing;

    /** The physical PAK file */
    private File file;

    /** 
     * Creates new PAKArhive 
     *
     * @param file The file to read as a PAK
     */
    public PAKArchive(File file) throws IOException {
        this.file = file;
        System.out.println("Loading PAK: " + file.getName());
        PAKHeader header = new PAKHeader(file);
        listing = new PAKListing(header, file);
    }

    /**
     * Retrieve access to a specified file in this pak
     *
     * @return The name of the file to retrieve access to
     */
    public InputStream getFile(String filename) throws IOException {
        int offset = listing.getFileOffset(filename);
        int length = listing.getFileLength(filename);
        if (offset == -1) throw new FileNotFoundException("File " + filename + " not found in PAK");
        FileInputStream in = new FileInputStream(file);
        in.skip(offset);
        return new RestrictedLengthInputStream(in, length);
    }

    /**
     * An input stream that allows access only to a defined number of bytes
     */
    private class RestrictedLengthInputStream extends InputStream {

        /** The number of bytes to allows access to */
        private int length;

        /** The input stream that this stream reads from */
        private InputStream in;

        /**
         * Construct a new input stream
         *
         * @param in The input stream to read from
         * @param length The number of bytes to allow access to
         */
        public RestrictedLengthInputStream(InputStream in, int length) throws IOException {
            this.in = in;
            if (length > in.available()) this.length = in.available(); else this.length = length;
        }

        /**
         * Read a single byte from the stream
         *
         * @return The byte read
         */
        public int read() throws IOException {
            length--;
            return in.read();
        }

        /**
         * Check how many bytes are available on this stream
         *
         * @return The number of bytes available on this stream
         */
        public int available() throws IOException {
            return length;
        }
    }

    public static void main(String argv[]) {
        try {
            new PAKArchive(new File("c:\\codin\\java3dcvs\\data\\pak0.pak"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
