package org.apache.myfaces.trinidadinternal.image.laf.browser;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidadinternal.share.io.InputStreamProvider;
import org.apache.myfaces.trinidadinternal.image.ImageConstants;
import org.apache.myfaces.trinidadinternal.image.ImageContext;
import org.apache.myfaces.trinidadinternal.image.painter.ImageUtils;

/**
 * Private utility methods for loading source icons.
 *
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/image/laf/browser/SourceUtils.java#0 $) $Date: 10-nov-2005.19:05:11 $
 */
class SourceUtils {

    public static Image getSourceIcon(ImageContext context, Map<Object, Object> properties) {
        return getSourceIcon(context, properties, ImageConstants.SOURCE_INPUT_STREAM_PROVIDER_KEY);
    }

    /**
   * Returns the source Image for the specified requested properties
   */
    public static Image getSourceIcon(ImageContext context, Map<Object, Object> properties, Object key) {
        InputStreamProvider provider = (InputStreamProvider) properties.get(key);
        if (provider == null) {
            _log(properties, _PROVIDER_ERROR + " (" + _getKeyName(key) + ")", null);
            return null;
        }
        InputStream in = null;
        try {
            in = provider.openInputStream();
        } catch (IOException e) {
            _log(properties, _INPUT_STREAM_ERROR, e);
            return null;
        }
        if (in == null) {
            _log(properties, _INPUT_STREAM_ERROR, null);
            return null;
        }
        Image source = ImageUtils.getImageFromStream(in);
        if (source == null) {
            _log(properties, _IMAGE_ERROR, null);
        }
        return source;
    }

    private static void _log(Map<Object, Object> properties, String message, Throwable t) {
        if (_LOG.isWarning()) {
            String source = (String) properties.get(ImageConstants.SOURCE_KEY);
            if (source != null) message += ("for source icon " + source);
            _LOG.warning(message, t);
        }
    }

    private static String _getKeyName(Object key) {
        return key.toString();
    }

    private static final String _PROVIDER_ERROR = "Could not get InputStreamProvider";

    private static final String _INPUT_STREAM_ERROR = "Could not get InputStream";

    private static final String _IMAGE_ERROR = "Could not create image";

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(SourceUtils.class);
}
