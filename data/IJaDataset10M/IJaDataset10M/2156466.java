package de.huxhorn.lilith.swing;

import de.huxhorn.sulky.io.IOUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Font;
import java.io.InputStream;

public class RendererConstants {

    public static final float SMOOTHING_THRESHOLD = 6.0f;

    public static final String MONOSPACED_FAMILY = "Monospaced";

    private static final String MENSCH_FONT_RESOURCE = "/mensch.ttf";

    public static final Font MENSCH_FONT;

    static {
        final Logger logger = LoggerFactory.getLogger(RendererConstants.class);
        InputStream fontStream = RendererConstants.class.getResourceAsStream(MENSCH_FONT_RESOURCE);
        Font font = null;
        if (fontStream != null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                if (logger.isInfoEnabled()) logger.info("Created {} font.", MENSCH_FONT_RESOURCE);
                IOUtilities.closeQuietly(fontStream);
            } catch (Exception ex) {
                if (logger.isWarnEnabled()) logger.warn("Exception while creating font!", ex);
            }
        } else {
            if (logger.isWarnEnabled()) logger.warn("Could not find resource {}!", MENSCH_FONT_RESOURCE);
        }
        MENSCH_FONT = font;
    }
}
