package gnu.kinsight.photo.ekspos;

import java.io.IOException;
import java.util.*;
import javax.imageio.*;
import javax.imageio.spi.*;

/** ImageReaderWriterPreferences
 *
 * @author  Kiyut
 */
public class ImageReaderWriterPreferences {

    protected Map suffixMap;

    protected Map readerSpiMap;

    /** Creates a new instance of ImageReaderWriterPreferences */
    public ImageReaderWriterPreferences() {
        init();
    }

    /** initialize */
    private void init() {
        suffixMap = new TreeMap();
        readerSpiMap = new TreeMap();
        String[] formatNames = ImageIO.getReaderFormatNames();
        for (int i = 0; i < formatNames.length; i++) {
            Iterator it = ImageIO.getImageReadersByFormatName(formatNames[i]);
            while (it.hasNext()) {
                ImageReader reader = (ImageReader) it.next();
                javax.imageio.spi.ImageReaderSpi spi = reader.getOriginatingProvider();
                if (spi == null) {
                    continue;
                }
                String[] suffixes = spi.getFileSuffixes();
                for (int j = 0; j < suffixes.length; j++) {
                    suffixMap.put(suffixes[j], formatNames[i]);
                }
            }
        }
        Iterator it = suffixMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Iterator readerIt = ImageIO.getImageReadersByFormatName((String) entry.getValue());
            if (readerIt.hasNext()) {
                ImageReader reader = (ImageReader) readerIt.next();
                readerSpiMap.put(entry.getValue(), reader.getOriginatingProvider());
            }
        }
    }

    /** Return ImageReaderSpiMap.
     * The key=formatName value=ImageReaderSpi
     * @return ImageReaderSpiMap
     */
    public Map getPreferredImageReaderSpi() {
        return readerSpiMap;
    }

    /** Return preferred ImageReader
     * @param formatName the Format Name
     * @return preferred ImageReader
     * @see javax.imageio.ImageIO
     */
    public synchronized ImageReader getPreferredImageReaderByFormatName(String formatName) {
        ImageReader reader = null;
        Object obj = readerSpiMap.get(formatName);
        if (obj != null) {
            ImageReaderSpi spi = (ImageReaderSpi) obj;
            try {
                reader = spi.createReaderInstance();
            } catch (IOException ex) {
            }
        }
        return reader;
    }

    /** Return preferred ImageReader
     * @param suffix the file suffix
     * @return preferred ImageReader
     * @see javax.imageio.ImageIO
     */
    public synchronized ImageReader getPreferredImageReaderBySuffix(String suffix) {
        ImageReader reader = null;
        String format = (String) suffixMap.get(suffix);
        if (format != null) {
            reader = getPreferredImageReaderByFormatName(format);
        }
        return reader;
    }

    /** TODO: not implemented yet, return null*/
    public synchronized ImageWriter getPreferredImageWriterByFormatName(String formatName) {
        return null;
    }

    /** TODO not implemented yet, return null */
    public synchronized ImageWriter getPreferredImageWriterBySuffix(String suffix) {
        return null;
    }
}
