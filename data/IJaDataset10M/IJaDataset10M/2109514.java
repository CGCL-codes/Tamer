package backend;

import java.io.File;
import java.io.FileFilter;
import javax.activation.FileDataSource;

/**
 * This Filter checks the passed file according to the MIME type and the
 * file ending. Java detects MIME types under the following pattern:
 * .jpg/.jpeg files - image/jpeg
 * .bmp files - application/octet-stream
 * .png files - application/octet-stream
 * .gif files - image/gif
 * Therefore only mime type image/* is at the moment and a better MIME type
 * detection needs to be found. 
 * 
 * @author Rajmund Witt
 * @version 0.5
 */
public class ImageFileFilter implements FileFilter {

    public boolean accept(File pathname) {
        FileDataSource currentFile = new FileDataSource(pathname);
        boolean isImage = false;
        if (currentFile.getContentType().equalsIgnoreCase("image/jpeg")) {
            isImage = true;
        } else if (currentFile.getContentType().equalsIgnoreCase("image/gif")) {
            isImage = true;
        } else if (currentFile.getContentType().startsWith("image")) {
            isImage = true;
        } else if (pathname.getName().endsWith(".jpg") || pathname.getName().endsWith(".JPG") || pathname.getName().endsWith(".jpeg") || pathname.getName().endsWith(".JPEG") || pathname.getName().endsWith(".png") || pathname.getName().endsWith(".PNG") || pathname.getName().endsWith(".gif") || pathname.getName().endsWith(".GIF") || pathname.getName().endsWith(".bmp") || pathname.getName().endsWith(".BMP")) {
            isImage = true;
        }
        return isImage;
    }
}
