package spcp7.imagegallery.abstractionlayer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import spcp7.imagegallery.abstractionlayer.face.ImageFilterFace;

/**
 * @author Phillip Merensky
 * 
 */
public class ImageFilterImpl implements ImageFilterFace {

    private Log log = LogFactory.getLog(ImageFilterImpl.class);

    /**
     * The file endings which should be accepted
     */
    private final String[] contentTypes = { "jpg", "jpeg", "png", "gif" };

    /**
     * The mimetypes which should be accepted
     */
    private final Pattern imageMimeTypes = Pattern.compile(".+((image/jpeg)|(image/png)|(image/gif)|(image/jpg)).+");

    /**
     * Default empty constructor
     */
    public ImageFilterImpl() {
    }

    /**
     * @see spcp7.imagegallery.abstractionlayer.face.ImageFilterFace#accept(java.lang.String)
     */
    public boolean accept(String imagePath) {
        log.debug("Does " + imagePath + " contain an image?");
        for (int i = 0; i < contentTypes.length; i++) {
            if (!imagePath.equals("") && imagePath.toLowerCase().endsWith(contentTypes[i])) {
                log.debug("yes!");
                return true;
            }
        }
        log.debug(imagePath + " contains no image!");
        return false;
    }

    /**
     * @see spcp7.imagegallery.abstractionlayer.face.ImageFilterFace#acceptImageMimetype(java.lang.String)
     */
    public boolean acceptImageMimetype(String mimeTypeString) {
        log.debug("Does " + mimeTypeString + " contain an image mime type?");
        Matcher m = imageMimeTypes.matcher(mimeTypeString);
        if (m.matches()) {
            log.debug("yes!");
            return true;
        }
        log.debug(mimeTypeString + " contains no image mime type!");
        return false;
    }
}
