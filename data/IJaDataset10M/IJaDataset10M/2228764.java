package jxl.write.biff;

import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import jxl.common.Logger;

/**
 * Used to generate the excel biff data using a temporary file.  This
 * class wraps a RandomAccessFile
 */
class FileDataOutput implements ExcelDataOutput {

    private static Logger logger = Logger.getLogger(FileDataOutput.class);

    /** 
   * The temporary file
   */
    private File temporaryFile;

    /**
   * The excel data
   */
    private RandomAccessFile data;

    /**
   * Constructor
   *
   * @param tmpdir the temporary directory used to write files.  If this is
   *               NULL then the sytem temporary directory will be used
   */
    public FileDataOutput(File tmpdir) throws IOException {
        temporaryFile = File.createTempFile("jxl", ".tmp", tmpdir);
        temporaryFile.deleteOnExit();
        data = new RandomAccessFile(temporaryFile, "rw");
    }

    /**
   * Writes the bytes to the end of the array, growing the array
   * as needs dictate
   *
   * @param d the data to write to the end of the array
   */
    public void write(byte[] bytes) throws IOException {
        data.write(bytes);
    }

    /**
   * Gets the current position within the file
   *
   * @return the position within the file
   */
    public int getPosition() throws IOException {
        return (int) data.getFilePointer();
    }

    /**
   * Sets the data at the specified position to the contents of the array
   * 
   * @param pos the position to alter
   * @param newdata the data to modify
   */
    public void setData(byte[] newdata, int pos) throws IOException {
        long curpos = data.getFilePointer();
        data.seek(pos);
        data.write(newdata);
        data.seek(curpos);
    }

    /** 
   * Writes the data to the output stream
   */
    public void writeData(OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int length = 0;
        data.seek(0);
        while ((length = data.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
    }

    /**
   * Called when the final compound file has been written
   */
    public void close() throws IOException {
        data.close();
        temporaryFile.delete();
    }
}
