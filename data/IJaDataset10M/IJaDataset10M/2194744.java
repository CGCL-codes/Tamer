package org.apache.shindig.protocol.conversion.xstream;

/**
 *
 */
public class ClassFieldMapping {

    /**
   * The name of the element to map the class to.
   */
    private String elementName;

    /**
   * The class being mapped.
   */
    private Class<?> mappedClazz;

    /**
   * An optional parent element name.
   */
    private String fieldParentName;

    /**
   * Create a simple element class mapping, applicable to all parent elements.
   *
   * @param elementName
   *          the name of the element
   * @param mappedClazz
   *          the class to map to the name of the element
   */
    public ClassFieldMapping(String elementName, Class<?> mappedClazz) {
        this.elementName = elementName;
        this.mappedClazz = mappedClazz;
        this.fieldParentName = null;
    }

    /**
   * Create a element class mapping, that only applies to one parent element
   * name.
   *
   * @param parentName
   *          the name of the parent element that this mapping applies to
   * @param elementName
   *          the name of the element
   * @param mappedClazz
   *          the class to map to the name of the element
   */
    public ClassFieldMapping(String parentName, String elementName, Class<?> mappedClazz) {
        this.elementName = elementName;
        this.mappedClazz = mappedClazz;
        this.fieldParentName = parentName;
    }

    /**
   * @return get the element name.
   */
    public String getElementName() {
        return elementName;
    }

    /**
   * @return get the mapped class.
   */
    public Class<?> getMappedClass() {
        return mappedClazz;
    }

    /**
   * Does this ClassFieldMapping match the supplied parent and type.
   *
   * @param parent
   *          the parent element, which may be null
   * @param type
   *          the type of the field being stored
   * @return true if this mapping is a match for the combination
   */
    public boolean matches(String parent, Class<?> type) {
        if (fieldParentName == null) {
            return mappedClazz.isAssignableFrom(type);
        }
        return fieldParentName.equals(parent) && mappedClazz.isAssignableFrom(type);
    }
}
