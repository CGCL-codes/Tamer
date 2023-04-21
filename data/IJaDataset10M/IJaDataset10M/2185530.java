package jerry;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.jcraft.jzlib.ZInputStream;

/**
 * Z-LIB decompression utility methods
 * 
 * @author (h0t@_G0|i
 */
public class ZlibDecompressUtil {

    /**
	 * Main method to test
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        File file = new File("c:\\test.zip");
        uncompressLogFile(new FileInputStream(file), new File("C:\\test.log"));
    }

    /**
	 * Reads the stream and uncompresses it to file object
	 *   
	 * @param stream
	 * @param file
	 * @return File output stream object on the file
	 * @throws IOException
	 */
    public static FileOutputStream uncompressLogFile(InputStream stream, File file) throws IOException {
        ZInputStream inpStream = new ZInputStream(stream);
        byte[] buffer = new byte[3000];
        FileOutputStream oStream = new FileOutputStream(file);
        int available = inpStream.read(buffer);
        while (available != -1) {
            oStream.write(buffer, 0, available);
            oStream.flush();
            available = inpStream.read(buffer);
        }
        return oStream;
    }

    /**
	 * Uncompresses the buffer byte array and returns another byte array
	 * @param buffer
	 * @param length
	 * @return byte array
	 * @throws IOException
	 */
    public static byte[] decryptBuffer(byte[] buffer, int length) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(buffer, 0, length);
        ZInputStream stream = new ZInputStream(inStream);
        byte[] result = new byte[stream.available()];
        stream.read(result);
        return result;
    }
}
