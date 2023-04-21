package at.co.ait.utils;

import java.io.File;
import java.io.IOException;
import at.co.ait.domain.services.TikaService;

/**
 * @author dodo
 *
 */
public class TikaUtils {

    /**
	 * Detect MIME type of file.
	 */
    public static String detectedMimeType(File fileObj) {
        String mimeType = null;
        try {
            mimeType = TikaService.tika.detect(fileObj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimeType;
    }
}
