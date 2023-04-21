package net.jfellow.tail.util;

import java.io.*;

/**
 * @author @author@
 *
 * Copies binary and text files.
 * <br/>Created 12.09.2003
 */
public class BinaryFileCopy {

    private int bufferSize = 1024;

    /**
     * Set the size of the byte[] buffer used to
     * <br/>- read the data in from file and
     * <br/>- write the data out to file
     * <br/>Deault is byte[1024].
     * @param byteBufferSize
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Copies binary and text files using DataInputStream
     * and DataOutputStream.
     *
     * @param file1 File to copy
     * @param file2 File to write to
     * @return boolean True if no errors
     * @throws Exception Now only the exception message is used.
     *                                           If compiled an run under jdk1.4 please uncomment
     *                                           the line "throw new Exception(e.getMessage(), e);"
     */
    public boolean copy(String file1, String file2) throws IOException {
        File f1 = new File(file1);
        File f2 = new File(file2);
        return this.copy(f1, f2);
    }

    /**
     * Copies binary and text files using DataInputStream
     * and DataOutputStream.
     *
     * @param file1 File to copy
     * @param file2 File to write to
     * @return boolean True if no errors
     * @throws Exception Now only the exception message is used.
     *                                           If compiled an run under jdk1.4 please uncomment
     *                                           the line "throw new Exception(e.getMessage(), e);"
     */
    public boolean copy(File file1, File file2) throws IOException {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(file1)));
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file2)));
            byte[] b = new byte[this.bufferSize];
            while (true) {
                int i = in.read(b);
                if (i == -1) {
                    break;
                }
                out.write(b, 0, i);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e1) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e1) {
                }
            }
        }
        return true;
    }
}
