package jacky.lanlan.song.extension.struts.resource;

import java.io.*;

/**
 * A concrete implementation of the <code>Downloadable</code> interface which
 * represent a file on the disk.
 * @author Jacky.Song
 */
public class FileResource implements Downloadable {

    /**
   * The content type for this stream.
   */
    private String contentType;

    /**
   * The file name.
   */
    private String name;

    /**
   * The file to be downloaded.
   */
    private File file;

    /**
   * Constructs an instance of this class, based on the supplied parameters.
   * 
   * @param contentType
   *          The content type of the file.
   * @param file
   *          The file to be downloaded.
   * @param name the file name
   */
    public FileResource(String contentType, File file, String name) {
        this.contentType = contentType;
        this.file = file;
        this.name = name;
    }

    /**
   * Returns the content type of the stream to be downloaded.
   * 
   * @return The content type of the stream.
   */
    public String getContentType() {
        return this.contentType;
    }

    /**
   * Returns an input stream on the file to be downloaded. This stream will be
   * closed by the <code>DownloadAction</code>.
   * 
   * @return The input stream for the file to be downloaded.
   */
    public InputStream getInputStream() throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        return bis;
    }

    public String getName() {
        return name;
    }
}
