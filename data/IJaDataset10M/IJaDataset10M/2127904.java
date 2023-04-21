package org.apache.shindig.gadgets.rewrite.image;

import org.apache.commons.io.IOUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.shindig.gadgets.http.HttpResponse;
import javax.imageio.ImageIO;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Optimize JPEG images by either converting them to PNGs or re-encoding them with a more
 * appropriate compression level.
 */
public class JPEGOptimizer extends BaseOptimizer {

    public static BufferedImage readJpeg(InputStream is) throws ImageReadException, IOException {
        byte[] bytes = IOUtils.toByteArray(is);
        Sanselan.getMetadata(bytes, null);
        byte[] iccBytes = Sanselan.getICCProfileBytes(bytes);
        if (iccBytes != null && iccBytes.length > 0) {
            ICC_Profile iccProfile = Sanselan.getICCProfile(bytes, null);
            if (iccProfile == null) {
                throw new ImageReadException("Image has ICC but it is corrupt and cannot be read");
            }
        }
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }

    private boolean usePng;

    public JPEGOptimizer(OptimizerConfig config, HttpResponse original) {
        super(config, original);
    }

    @Override
    protected void rewriteImpl(BufferedImage image) throws IOException {
        OptimizerConfig pngConfig = new OptimizerConfig(config.getMaxInMemoryBytes(), config.getMaxPaletteSize(), false, config.getJpegCompression(), config.getMinThresholdBytes());
        PNGOptimizer pngOptimizer = new PNGOptimizer(pngConfig, originalResponse);
        pngOptimizer.rewriteImpl(image);
        int pngLength = Integer.MAX_VALUE;
        if (pngOptimizer.getRewrittenImage() != null) {
            minBytes = pngOptimizer.getRewrittenImage();
            minLength = minBytes.length;
            pngLength = minLength;
        }
        write(image);
        if (pngLength == minLength) {
            usePng = true;
        }
    }

    @Override
    protected String getOutputContentType() {
        if (usePng) {
            return "image/png";
        }
        return "image/jpeg";
    }

    @Override
    protected String getOriginalContentType() {
        return "image/jpeg";
    }

    @Override
    protected String getOriginalFormatName() {
        return "jpeg";
    }
}
