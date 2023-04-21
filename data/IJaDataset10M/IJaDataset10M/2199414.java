package net.sf.dozer.util.mapping.propertydescriptor;

import net.sf.dozer.util.mapping.MappingException;
import net.sf.dozer.util.mapping.fieldmap.FieldMap;

/**
 * Internal class used for copy by reference mappings. Only intended for internal use.
 * 
 * @author garsombke.franz
 */
public class SelfPropertyDescriptor implements DozerPropertyDescriptorIF {

    private final Class self;

    public SelfPropertyDescriptor(Class self) {
        this.self = self;
    }

    public Class getPropertyType() throws MappingException {
        return self;
    }

    public void setPropertyValue(Object bean, Object value, FieldMap fieldMap) throws MappingException {
    }

    public Object getPropertyValue(Object bean) throws MappingException {
        return bean;
    }
}
