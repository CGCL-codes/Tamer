package org.xymos.ifilter4j.contrib;

import java.util.ArrayList;
import java.util.HashMap;
import org.xymos.ifilter4j.contrib.registry.RegistryHelper;
import org.xymos.ifilter4j.contrib.registry.RegistryKeyValue;

/**
 *
 * @author mschlegel
 */
public class IFilterHelper {

    private static final String CLASSES_KEY = "HKEY_LOCAL_MACHINE\\Software\\Classes";

    private static final String PERSISTENT_HANDLER_KEY = "HKEY_LOCAL_MACHINE\\Software\\Classes\\%1$s\\PersistentHandler";

    private static final String EXTENSION_CLASS_KEY = "HKEY_LOCAL_MACHINE\\Software\\Classes\\%1$s";

    private static final String EXTENSION_CLASS_ID_KEY = "HKEY_LOCAL_MACHINE\\Software\\Classes\\%1$s\\CLSID";

    private static final String PERSISTENTHANDLER_OF_EXTENSION_CLASS_KEY = "HKEY_LOCAL_MACHINE\\Software\\Classes\\CLSID\\%1$s\\PersistentHandler";

    private static final String GUID_KEY = "HKEY_LOCAL_MACHINE\\Software\\Classes\\CLSID\\%1$s\\PersistentAddinsRegistered\\{89BCB740-6119-101A-BCB7-00DD010655AF}";

    private static String[] m_Extensions = null;

    private static HashMap<String, String> m_MimeType2ExtensionMap = new HashMap<String, String>();

    private IFilterHelper() {
    }

    /**
     * Gets the supported extensions.
     *
     * @return The supported extensions.
     * @throws Exception If getting the extensions failed.
     */
    public static String[] getSupportedExtensions() throws Exception {
        if (m_Extensions == null) {
            String l_RegistryKey = CLASSES_KEY;
            String[] l_ClassChildren = RegistryHelper.getRegistryKeyChildren(l_RegistryKey);
            if (l_ClassChildren == null) {
                throw new Exception("Reading windows registry failed");
            }
            m_MimeType2ExtensionMap.clear();
            ArrayList<String> l_ExtensionList = new ArrayList<String>();
            for (int i = 0; i < l_ClassChildren.length; i++) {
                if (l_ClassChildren[i].startsWith(".")) {
                    l_RegistryKey = String.format(PERSISTENT_HANDLER_KEY, l_ClassChildren[i]);
                    RegistryKeyValue l_PersistentHandlerGuid = RegistryHelper.getRegistryKeyValue(l_RegistryKey, RegistryHelper.DEFAULT_KEYVALUENAME);
                    l_RegistryKey = String.format(EXTENSION_CLASS_KEY, l_ClassChildren[i]);
                    RegistryKeyValue l_MimeType = RegistryHelper.getRegistryKeyValue(l_RegistryKey, "Content Type");
                    if (l_PersistentHandlerGuid != null && l_PersistentHandlerGuid.isNull() == false) {
                        l_ExtensionList.add(l_ClassChildren[i].substring(1));
                        if (l_MimeType != null && l_MimeType.isNull() == false) {
                            m_MimeType2ExtensionMap.put(l_ClassChildren[i].substring(1), l_MimeType.getStringValue());
                        }
                    }
                }
            }
            m_Extensions = new String[l_ExtensionList.size()];
            l_ExtensionList.toArray(m_Extensions);
        }
        return m_Extensions;
    }

    /**
     * Gets the supported mimetypes.
     *
     * @return The supported mimetypes.
     */
    public static String[] getSupportedMimeTypes() {
        String[] l_SupportedMimeTypes = new String[m_MimeType2ExtensionMap.size()];
        l_SupportedMimeTypes = m_MimeType2ExtensionMap.keySet().toArray(l_SupportedMimeTypes);
        return l_SupportedMimeTypes;
    }

    /**
     * Gets the apropriate IFilter GUID for a file extension. <br />
     * The description comes from <a href="http://www.codeproject.com/csharp/FullTextSearchingIFinters.asp">http://www.codeproject.com/csharp/FullTextSearchingIFinters.asp</a>
     *
     * @param p_Extension The file extension to get the GUID for,
     *        e.g.
     * @return The IFilter GUID for the extension.
     * @throws Exception If getting the GUID failed.
     */
    public static String getIFilterGUIDForExtension(final String p_Extension) throws Exception {
        String l_IFilterGuid = null;
        if (p_Extension != null) {
            String l_Extension = (p_Extension.startsWith(".")) ? p_Extension : "." + p_Extension;
            String l_RegistryKey = String.format(PERSISTENT_HANDLER_KEY, l_Extension);
            RegistryKeyValue l_KeyValue = RegistryHelper.getRegistryKeyValue(l_RegistryKey, null);
            String l_PersistentHandlerGuid = l_KeyValue.getStringValue();
            if (l_PersistentHandlerGuid == null) {
                l_RegistryKey = String.format(EXTENSION_CLASS_KEY, l_Extension);
                l_KeyValue = RegistryHelper.getRegistryKeyValue(l_RegistryKey, null);
                String l_ExtensionClass = l_KeyValue.getStringValue();
                if (l_ExtensionClass == null) {
                    throw new Exception("Unknown file extension: " + p_Extension);
                }
                l_RegistryKey = String.format(EXTENSION_CLASS_ID_KEY, l_ExtensionClass);
                l_KeyValue = RegistryHelper.getRegistryKeyValue(l_RegistryKey, null);
                String l_ExtensionClsid = l_KeyValue.getStringValue();
                if (l_ExtensionClsid == null) {
                    throw new Exception("CLSID of extension class " + l_ExtensionClass + " not found");
                }
                l_RegistryKey = String.format(PERSISTENTHANDLER_OF_EXTENSION_CLASS_KEY, l_ExtensionClsid);
                l_KeyValue = RegistryHelper.getRegistryKeyValue(l_RegistryKey, null);
                l_PersistentHandlerGuid = l_KeyValue.getStringValue();
                if (l_PersistentHandlerGuid == null) {
                    throw new Exception("PersistentHandler of extension class " + l_ExtensionClass + " not found");
                }
            }
            l_RegistryKey = String.format(GUID_KEY, l_PersistentHandlerGuid);
            l_KeyValue = RegistryHelper.getRegistryKeyValue(l_RegistryKey, null);
            l_IFilterGuid = l_KeyValue.getStringValue();
            if (l_IFilterGuid == null) {
                throw new Exception("GUID of PersistentHandler not found for extension " + p_Extension);
            }
            l_IFilterGuid = "clsid:" + l_IFilterGuid.substring(1, l_IFilterGuid.length() - 1);
        }
        return l_IFilterGuid;
    }
}
