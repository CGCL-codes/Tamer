package org.jrcaf.internal.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Wrapps a Bundle to add images passed by config parameters. 
 */
public class ImageResourceBundle extends ResourceBundle {

    private final Bundle parent;

    private final Map<String, Image> map;

    /**
    * Creates a new ImageResourceBundle.
    * @param aBundle The bundle to wrap.
    * @param aResourceMap The map of image resources.
    */
    public ImageResourceBundle(Bundle aBundle, Map<String, Image> aResourceMap) {
        super();
        parent = aBundle;
        map = aResourceMap;
    }

    @Override
    protected Object handleGetObject(String aKey) {
        Object object = map.get(aKey);
        if (object == null) {
            String keyString = "%" + aKey;
            String result = Platform.getResourceString(parent, keyString);
            if (result.equals(keyString)) object = Platform.getResourceString(parent, aKey); else object = result;
        }
        return object;
    }

    /**
    * @see java.util.ResourceBundle#getKeys()
    */
    @Override
    public Enumeration<String> getKeys() {
        List<String> keys = new ArrayList<String>();
        for (String key : map.keySet()) keys.add(key);
        return Collections.enumeration(keys);
    }
}
