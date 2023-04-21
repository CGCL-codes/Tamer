package org.pengyou.client.lib.properties;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.pengyou.client.lib.Property;
import org.pengyou.client.lib.BaseProperty;
import org.pengyou.client.lib.ResponseEntity;
import org.pengyou.client.lib.WebdavException;

/**
 * Factory for instanciating {@link org.apache.webdav.lib.Property}s.
 */
public class PropertyFactory {

    private static final Class[] CTOR_PARAMS = { ResponseEntity.class, Element.class };

    /**
     * Maps namespace URIs to maps, that map names to constructors. 
     */
    private static Map propertyClasses = new HashMap();

    static {
        try {
            PropertyFactory.register("DAV:", AclProperty.TAG_NAME, AclProperty.class);
            PropertyFactory.register("DAV:", CheckedinProperty.TAG_NAME, CheckedinProperty.class);
            PropertyFactory.register("DAV:", CheckedoutProperty.TAG_NAME, CheckedoutProperty.class);
            PropertyFactory.register("DAV:", CreationDateProperty.TAG_NAME, CreationDateProperty.class);
            PropertyFactory.register("DAV:", CurrentUserPrivilegeSetProperty.TAG_NAME, CurrentUserPrivilegeSetProperty.class);
            PropertyFactory.register("DAV:", GetContentLengthProperty.TAG_NAME, GetContentLengthProperty.class);
            PropertyFactory.register("DAV:", GetLastModifiedProperty.TAG_NAME, GetLastModifiedProperty.class);
            PropertyFactory.register("DAV:", LockDiscoveryProperty.TAG_NAME, LockDiscoveryProperty.class);
            PropertyFactory.register("DAV:", ModificationDateProperty.TAG_NAME, ModificationDateProperty.class);
            PropertyFactory.register("DAV:", OwnerProperty.TAG_NAME, OwnerProperty.class);
            PropertyFactory.register("DAV:", PrincipalCollectionSetProperty.TAG_NAME, PrincipalCollectionSetProperty.class);
            PropertyFactory.register("DAV:", ResourceTypeProperty.TAG_NAME, ResourceTypeProperty.class);
            PropertyFactory.register("DAV:", SupportedLockProperty.TAG_NAME, SupportedLockProperty.class);
            PropertyFactory.register("DAV:", ExportProperty.TAG_NAME, ExportProperty.class);
        } catch (Exception e) {
            throw new WebdavException(e);
        }
    }

    /**
     * Creates a new property from an xml element provided in an WebDAV response.
     * 
     * <p>If no property class was registered a {@link BaseProperty} will returned.
     * 
     * @see Property
     * @see BaseProperty
     */
    public static Property create(ResponseEntity response, Element element) {
        Map nsMap = (Map) propertyClasses.get(element.getNamespaceURI());
        if (nsMap != null) {
            Constructor ctor = (Constructor) nsMap.get(element.getLocalName());
            if (ctor != null) {
                try {
                    Object[] params = { response, element };
                    return (Property) ctor.newInstance(params);
                } catch (Exception e) {
                    throw new WebdavException(e);
                }
            }
        }
        return new BaseProperty(response, element);
    }

    ;

    /**
     * Registers a new property.
     * 
     * @param namespaceUri namespace of the property
     * @param elmentName name of the property
     * @param cls class that implements the property. Must have a constructor that
     * takes two parameters of type ResponseEntity and Element.
     * 
     * @throws NoSuchMethodException if cls does not implement the required ctor.
     */
    public static void register(String namespaceUri, String elementName, Class cls) throws NoSuchMethodException, SecurityException {
        Constructor ctor = cls.getConstructor(CTOR_PARAMS);
        Map nsMap = (Map) propertyClasses.get(namespaceUri);
        if (nsMap == null) {
            nsMap = new HashMap();
            propertyClasses.put(namespaceUri, nsMap);
        }
        nsMap.put(elementName, ctor);
    }
}
