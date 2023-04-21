package net.sourceforge.x360mediaserve.formats.impl.streamers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.x360mediaserve.api.formats.Streamer;

/**
 * Simple streamer that opens a file and reads it to the outputstream
 * 
 * @author tom
 * 
 */
public class NativeFile implements Streamer {

    Logger logger = LoggerFactory.getLogger(NativeFile.class);

    /**
	 * Copies a given file to the OutputStream
	 * 
	 * @param file
	 * @param os
	 */
    File file;

    long startPoint = 0;

    String mimeType;

    public NativeFile() {
    }

    public void setMimeType(String type) {
        this.mimeType = type;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean setStartPoint(long index) {
        this.startPoint = index;
        return true;
    }

    public boolean writeToStream(OutputStream os) {
        BufferedInputStream is = null;
        boolean success = false;
        try {
            logger.debug("Playing:" + file + " from " + this.startPoint);
            is = new BufferedInputStream(new FileInputStream(file));
            if (this.startPoint != 0) is.skip(this.startPoint);
            byte input[] = new byte[4096];
            int bytesread;
            long totalbytes = 0;
            while ((bytesread = is.read(input)) != -1) {
                os.write(input, 0, bytesread);
                totalbytes += bytesread;
            }
            os.flush();
            logger.debug("End of File Reached, wrote:" + totalbytes);
            logger.debug("End of File Reached, wrote:" + is.read());
            success = true;
        } catch (Exception e) {
            logger.debug(e.toString());
        } finally {
            if (is != null) try {
                is.close();
            } catch (Exception e) {
            }
        }
        return success;
    }

    public void cleanUp() {
    }

    public void setSizeOfContent(long size) {
    }

    public Long getSizeOfContent() {
        return file.length();
    }

    public boolean supportsRanges() {
        return true;
    }

    public String getContentType() {
        return this.mimeType;
    }
}
