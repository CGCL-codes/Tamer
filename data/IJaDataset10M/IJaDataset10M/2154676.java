package org.jbeanmapper;

/**
 * Data type converter to manage conversion of objects from one type to another.
 *
 * @author Brian Pugh
 */
public interface BeanConverter {

    /**
   * Convert the specified input object into an output object of the specified type.
   *
   * @param type    Data type to which this value should be converted
   * @param value   The input value to be converted
   * @param context the context for this mapping.
   * @return an instance of <code>type</code> converted from <code>value</code>.
   * @throws PropertyMappingException if property mapping fails.
   * @throws BeanMappingException     if a bean mapping fails.
   */
    public Object convert(Class type, Object value, MappingContext context) throws PropertyMappingException, BeanMappingException;
}
