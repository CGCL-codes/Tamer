package com.tecacet.jflat;

import java.util.HashMap;
import java.util.Map;
import com.tecacet.util.introspection.CommonsBeanUtilsPropertyAccessor;
import com.tecacet.util.introspection.PropertyAccessor;

/**
 * Basic implementation of WriterRowMapper that uses a columnMapping to
 * determine properties to map and a BeanManipulator to set/get the bean
 * properties.
 * 
 * @author Dimitri Papaioannou
 * 
 * @param <T>
 */
public class BeanWriterRowMapper<T> implements WriterRowMapper<T> {

    private ColumnMapping columnMapping;

    private PropertyAccessor<T> propertyAccessor;

    private Map<String, ValueExtractor<T>> extractors = new HashMap<String, ValueExtractor<T>>();

    public BeanWriterRowMapper(ColumnMapping columnMapping, PropertyAccessor<T> propertyAccessor) {
        this.columnMapping = columnMapping;
        this.propertyAccessor = propertyAccessor;
    }

    public BeanWriterRowMapper(Class<T> type, ColumnMapping mappingStrategy) {
        this(mappingStrategy, new CommonsBeanUtilsPropertyAccessor<T>());
    }

    public BeanWriterRowMapper(Class<T> type, String[] properties) {
        this(new ColumnPositionMapping(properties), new CommonsBeanUtilsPropertyAccessor<T>());
    }

    public String[] getRow(T bean) {
        String[] row = new String[columnMapping.getNumberOfColumns()];
        for (int i = 0; i < row.length; i++) {
            String property = columnMapping.getProperty(i);
            if (property == null) {
                continue;
            }
            ValueExtractor<T> extractor = extractors.get(property);
            if (extractor == null) {
                row[i] = (String) propertyAccessor.getProperty(bean, property);
            } else {
                row[i] = extractor.getValue(bean);
            }
        }
        return row;
    }

    public ColumnMapping getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(ColumnMapping columnMapping) {
        this.columnMapping = columnMapping;
    }

    public void registerValueExtractor(String property, ValueExtractor<T> extractor) {
        extractors.put(property, extractor);
    }

    public void deregisterValueExtractor(String property) {
        extractors.remove(property);
    }
}
