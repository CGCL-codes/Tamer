package net.googlecode.kharchenko.helpers;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Class for work with files.
 *
 * @author Kharchenko Yaroslav
 */
public interface FileHelper {

    /**
     * Save file to selected directory
     *
     * @param file   file, that you want to copy
     * @param target path to directory,where you want to save file
     * @throws IOException OutputStream error. When file is blocked.
     */
    public void copyFile(MultipartFile file, String target) throws IOException;
}
