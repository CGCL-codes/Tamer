package org.nightlabs.editor2d.image;

import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import org.nightlabs.editor2d.render.RenderConstants;
import org.nightlabs.editor2d.util.ImageUtil;

/**
 * @author Daniel.Mazurek <at> NightLabs <dot> de
 *
 */
public class GrayscaleColorConvertDelegate extends AbstractColorConvertDelegate {

    @Override
    public String getRenderMode() {
        return RenderConstants.GRAY_MODE;
    }

    @Override
    protected BufferedImageOp getBufferedImageOp(RenderModeMetaData metaData) {
        return new ColorConvertOp(ImageUtil.COLOR_MODEL_GRAY.getColorSpace(), getRenderingHints(metaData));
    }
}
