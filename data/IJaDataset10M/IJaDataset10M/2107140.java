package org.pixory.pximage;

import java.awt.image.BufferedImage;
import javax.imageio.ImageTypeSpecifier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixory.pxfoundation.PXObjectUtility;

/**
 * these guys aren't really guaranteed to work. It's mostly a guess that they
 * might :)
 */
public class PXGenericImageFamily extends PXBufferedImageFamily {

    private static final Log LOG = LogFactory.getLog(PXGenericImageFamily.class);

    private int[] _familyImageTypes;

    private BufferedImage _image;

    public PXGenericImageFamily(int imageType, int imageSize) {
        super(imageSize);
        if (imageType < 0) {
            throw new IllegalArgumentException("");
        }
        _familyImageTypes = new int[] { imageType };
    }

    public boolean containsImageType(int imageType) {
        boolean containsImageType = false;
        if (_familyImageTypes[0] == imageType) {
            containsImageType = true;
        }
        return containsImageType;
    }

    public int[] getImageTypes() {
        return _familyImageTypes;
    }

    public BufferedImage imageForType(ImageTypeSpecifier imageTypeSpecifier) {
        BufferedImage imageForType = null;
        int aBufferedImageType = imageTypeSpecifier.getBufferedImageType();
        if (this.containsImageType(aBufferedImageType)) {
            if (_image == null) {
                int anImageSize = this.getImageSize();
                _image = new BufferedImage(anImageSize, anImageSize, aBufferedImageType);
            }
            imageForType = _image;
        } else {
            throw new IllegalArgumentException("");
        }
        return imageForType;
    }

    public synchronized String toString() {
        StringBuffer toStringBuffer = new StringBuffer();
        toStringBuffer.append(PXObjectUtility.getUnqualifiedClassName(this));
        toStringBuffer.append("[");
        toStringBuffer.append("imageTypes=");
        toStringBuffer.append(this.getImageTypes());
        toStringBuffer.append(", imageSize=");
        toStringBuffer.append(this.getImageSize());
        toStringBuffer.append(", image=");
        toStringBuffer.append(_image);
        toStringBuffer.append("]");
        return toStringBuffer.toString();
    }
}
