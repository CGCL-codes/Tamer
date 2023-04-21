package org.apache.myfaces.trinidadinternal.ui.laf.base;

import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.data.BoundValue;

/**
 * BoundValue used to retrieve a translated String from the Skin,
 * with a specified key.
 * <p>
 * If either the the key can not be found, an error message
 * will be written to the error log.
 * <p>
 * @see org.apache.myfaces.trinidadinternal.share.nls.LocaleContext
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public class SkinTranslatedBoundValue implements BoundValue {

    /**
   * Create a SkinTranslatedBoundValue that will retrieve the translated String
   * specified by <b>key</b> from the Skin.
   * @param key Key to use to look up value in ResourceBundle
   */
    public SkinTranslatedBoundValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        _key = key;
    }

    /**
   * Retrieves the translated value from the ResourceBundle.
   * @param context the rendering context
   */
    public Object getValue(UIXRenderingContext context) {
        return context.getTranslatedValue(_key);
    }

    private String _key;
}
