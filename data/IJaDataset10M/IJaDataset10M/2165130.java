package ru.adv.util.image;

import ru.adv.logger.TLogger;
import ru.adv.util.ErrorCodeException;
import java.util.Iterator;
import java.util.HashMap;

/**
 *
 * @version $Revision: 1.5 $
 */
public class AutoImageInfoReader extends ImageInfoReader {

    private static final HashMap READERS = new HashMap();

    private TLogger logger = new TLogger(AutoImageInfoReader.class);

    static {
        READERS.put("jpg", new JPEGInfoReader());
        READERS.put("gif", new GIFInfoReader());
        READERS.put("png", new PNGInfoReader());
    }

    protected ImageInfo doReadInfo(String filename) throws ImageInfoReadException {
        ImageInfoReadException ioException = null;
        ImageInfo ii = null;
        ImageInfoReader defaultReader = (ImageInfoReader) READERS.get(getExtension(filename));
        if (defaultReader != null) {
            try {
                defaultReader.setURLResolver(getURLResolver());
                ii = defaultReader.readInfo(filename);
            } catch (ImageInfoReadException e) {
                logger.debug(e);
                if (isIOError(e)) {
                    ioException = e;
                }
            }
        }
        if (ioException == null) {
            for (Iterator i = READERS.values().iterator(); i.hasNext() && ii == null; ) {
                ImageInfoReader reader = (ImageInfoReader) i.next();
                if (reader == defaultReader) {
                    continue;
                }
                try {
                    reader.setURLResolver(getURLResolver());
                    ii = reader.readInfo(filename);
                } catch (ImageInfoReadException e) {
                    logger.debug(e);
                    if (isIOError(e)) {
                        ioException = e;
                        break;
                    }
                }
            }
        }
        if (ii == null) {
            if (ioException == null) {
                throw new ImageInfoReadException("Unknown file type: " + filename, filename);
            } else {
                throw ioException;
            }
        }
        return ii;
    }

    private boolean isIOError(ImageInfoReadException e) {
        return e.getCode() == ErrorCodeException.IO_ERROR;
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1 || index == filename.length() - 1) {
            return "";
        }
        return filename.substring(index + 1).toLowerCase();
    }
}
