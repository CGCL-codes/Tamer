package nl.headspring.photoz.common.image;

import nl.headspring.photoz.common.ImageUtils;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Class SerializableJpegImage.
 *
 * @author Eelco Sommer
 * @since Oct 18, 2010
 */
public class Thumbnail implements SerializableImage {

    private final String uniqueImageId;

    private final byte[] bytes;

    private final ImageObserver imageObserver = new Container();

    private Image image;

    public Thumbnail(String uniqueImageId, byte[] bytes) {
        this.uniqueImageId = uniqueImageId;
        this.bytes = bytes;
    }

    public String getUniqueImageId() {
        return uniqueImageId;
    }

    public Image getImage() {
        if (this.image == null) {
            this.image = ImageUtils.loadImage(bytes);
        }
        return image;
    }

    public int getWidth() {
        return getImage().getWidth(imageObserver);
    }

    public int getHeight() {
        return getImage().getHeight(imageObserver);
    }

    public long getSize() {
        return bytes.length;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }
}
