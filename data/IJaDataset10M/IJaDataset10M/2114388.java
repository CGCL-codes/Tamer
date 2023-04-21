package net.turambar.just.util;

import net.turambar.just.SourceWriter;
import java.io.*;

/** A <code>SourceWriter</code> which writes everything to a file.
 *  If the specified target file doesn't exist, it will be created together with all directories in the path.
 */
public class FileSourceWriter extends SourceWriter {

    private File file;

    /** Creates an instance not associated with any file. */
    public FileSourceWriter() {
    }

    /** Creates an instance wich will write to the given file.
     * @param fileName a system path specifying the target file.
     * @throws IOException if the file cannot be created.
     */
    public FileSourceWriter(String fileName) throws IOException {
        setFile(fileName);
    }

    /** Creates an instance wich will write to the given file.
     * @param file file to which this instance will write.
     * @throws IOException if the file cannot be created.
     */
    public FileSourceWriter(File file) throws IOException {
        setFile(file);
    }

    /** Creates an instance wich will write to the given file using the specified encoding.
     * @param file file to which this instance will write.
     * @param charset encoding to use.
     * @throws IOException if the file cannot be created.
     */
    public FileSourceWriter(File file, String charset) throws IOException {
        setFile(file, charset);
    }

    /** Returns the file associated with this instance. */
    public File getFile() {
        return file;
    }

    /** Sets the file to which output will be printed.
     * @param file file to which this instance will write.
     * @throws IOException if the file cannot be created.
     */
    public void setFile(File file) throws IOException {
        this.file = file;
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();
        super.setWriter(new FileWriter(file));
    }

    /** Sets the file to which output will be printed. Will use specified encoding.
     * @param file file to which this instance will write.
     * @param charset encoding to use
     * @throws IOException if the file cannot be created.
     */
    public void setFile(File file, String charset) throws IOException {
        this.file = file;
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();
        if (charset == null) {
            super.setWriter(new FileWriter(file));
        } else {
            super.setWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
        }
    }

    /** Sets the file to which output will be printed.
     * @param fileName a system path specifying the target file.
     * @throws IOException if the file cannot be created.
     */
    public void setFile(String fileName) throws IOException {
        setFile(new File(fileName));
    }

    /** Returns the name of the file to which the stream is printed. */
    public String getFileName() {
        return file.getName();
    }
}
