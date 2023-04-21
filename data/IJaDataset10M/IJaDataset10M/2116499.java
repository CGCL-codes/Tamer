package org.mobicents.eclipslee.servicecreation.util;

import java.util.HashMap;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorConfigPropertyXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ConfigPropertiesUtil {

    public static HashMap[] getConfigProperties(ResourceAdaptorXML sbb) {
        ResourceAdaptorConfigPropertyXML[] entries = sbb.getConfigProperties();
        HashMap map[] = new HashMap[entries.length];
        for (int i = 0; i < entries.length; i++) {
            map[i] = new HashMap();
            map[i].put("Name", entries[i].getName() == null ? "" : entries[i].getName());
            map[i].put("Default Value", entries[i].getValue() == null ? "" : entries[i].getValue());
            map[i].put("Type", entries[i].getType() == null ? "" : entries[i].getType());
            map[i].put("Description", entries[i].getDescription() == null ? "" : entries[i].getDescription());
        }
        return map;
    }
}
