package org.activebpel.rt.bpel.def.io.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import org.activebpel.rt.config.AeConfiguration;
import org.activebpel.rt.xml.def.io.AeMapBasedExtensionRegistry;

/**
 * An implementation of the extension Registry for WS-BPEL 2.0. This registry creates a map
 * of extension element, attribute and activity Qnames to their class names that implement
 * IAeExtensionObject interface from the EngineConfig instance and passes it onto it's parent
 */
public class AeEngineConfigExtensionRegistry extends AeMapBasedExtensionRegistry {

    /**
    * Constructor
    * @param aEngineConfig
    * @throws ClassNotFoundException 
    */
    public AeEngineConfigExtensionRegistry(AeConfiguration aEngineConfig) throws ClassNotFoundException {
        super(getExtensionsMap(aEngineConfig));
    }

    /**
    * Creates QName to Class Name map from the instance of engine config
    * @param aEngineConfig
    * @return
    * @throws ClassNotFoundException 
    */
    private static Map getExtensionsMap(AeConfiguration aEngineConfig) throws ClassNotFoundException {
        Map extensionMap = new HashMap();
        Map extRegistry = aEngineConfig.getMapEntry("ExtensionRegistry");
        if (extRegistry == null) return extensionMap;
        for (Iterator iter = extRegistry.keySet().iterator(); iter.hasNext(); ) {
            Map extension = (Map) extRegistry.get((String) iter.next());
            QName qName = new QName((String) extension.get("Namespace"), (String) extension.get("LocalName"));
            String className = (String) extension.get("ClassName");
            extensionMap.put(qName, Class.forName(className));
        }
        return extensionMap;
    }
}
