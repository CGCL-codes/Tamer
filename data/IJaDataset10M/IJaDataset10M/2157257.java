package org.parallelj.common.jdt.mergers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Tools {

    public static final String TEST_PATH = "src/test/java/org/parallelj/common/jdt/mergers/";

    public static final String INPUT_TEST_PATH = TEST_PATH + "input/";

    public static final String OUTPUT_TEST_PATH = TEST_PATH + "output/";

    /**
	 * Read content of a file as String.
	 * 
	 * @param path
	 *            Path to the file
	 * @return Content as text of this file
	 * @throws FileNotFoundException
	 *             If the file cannot be found
	 * @throws IOException
	 *             If an error occurs during file processing
	 */
    public static final String getFileContent(String path) throws FileNotFoundException, IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            byte[] contentAsCharArray = new byte[(int) raf.length()];
            raf.readFully(contentAsCharArray);
            return new String(contentAsCharArray);
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ioe) {
                }
            }
        }
    }
}
