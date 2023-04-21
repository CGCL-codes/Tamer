package net.sf.picomapping.meta;

import java.lang.reflect.Method;
import net.sf.picomapping.property.Setter;

/**
 * 
 */
public class ReferenceMetaProperty implements MetaProperty {

    private String propertyName;

    private PrimaryKeyMetaProperty parentKey;

    private Method setter;

    private MetaClass metaClass;

    /**
     * @param propertyName
     * @param columnName
     */
    public ReferenceMetaProperty(String propertyName, PrimaryKeyMetaProperty parentKey, MetaClass metaClass) {
        this.propertyName = propertyName;
        this.metaClass = metaClass;
        this.parentKey = parentKey;
    }

    public Setter createSetter() {
        return null;
    }
}
