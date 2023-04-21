package eulergui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class ReaderUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private ReaderUtils() {
    }

    /** @return the number of characters copied */
    public static int copyReader(Reader input, Writer output) throws IOException {
        return copyReader(null, input, output);
    }

    /**
	 * Copy chars from a Reader to a Writer; also closes input and output;
	 * copied from
	 * http://www.devdaily.com/java/jwarehouse/commons-io-1.0/src/java
	 * /org/apache/commons/io/CopyUtils.java.shtml
	 * 
	 * @param input
	 *            the Reader to read from; may be null
	 * @param output
	 *            the Writer to write to
	 * @return the number of characters copied
	 * @throws IOException
	 *             In case of an I/O problem
	 */
    public static int copyReader(String intro, Reader input, Writer output) throws IOException {
        if (intro != null) {
            output.write(intro);
            output.write('\n');
        }
        final char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        if (input != null) {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            input.close();
        }
        output.close();
        return count;
    }

    /** */
    public static void copyStreams(InputStream is, OutputStream result) {
        try {
            int i;
            while ((i = is.read()) != -1) {
                result.write(i);
            }
            result.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param localN3File
	 * @return
	 */
    public static boolean hasContent(File localFile) {
        StringWriter output = new StringWriter();
        try {
            copyReader(new FileReader(localFile), output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString().length() > 0;
    }
}
