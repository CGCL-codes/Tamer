package com.google.code.nanorm.internal.mapping.result;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.code.nanorm.DataSink;
import com.google.code.nanorm.TypeHandler;
import com.google.code.nanorm.TypeHandlerFactory;
import com.google.code.nanorm.exceptions.GenericException;
import com.google.code.nanorm.internal.Key;
import com.google.code.nanorm.internal.Request;
import com.google.code.nanorm.internal.config.PropertyMappingConfig;
import com.google.code.nanorm.internal.config.ResultMapConfig;
import com.google.code.nanorm.internal.introspect.Getter;
import com.google.code.nanorm.internal.introspect.IntrospectUtils;
import com.google.code.nanorm.internal.introspect.IntrospectionFactory;
import com.google.code.nanorm.internal.introspect.Setter;

/**
 * Implementation of the {@link RowMapper} that uses collection of property
 * mapping configurations to map the result set row into the result object.
 * 
 * @author Ivan Dubrov
 * @version 1.0 28.05.2008
 */
public class DefaultRowMapper implements RowMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRowMapper.class);

    private final Class<?> elementClass;

    private final ResultMapConfig config;

    private final IntrospectionFactory introspectionFactory;

    private final TypeHandlerFactory typeHandlerFactory;

    private DynamicConfig dynamicConfig;

    private final DynamicConfig finDynamicConfig;

    /**
     * Constructor.
     * 
     * @param resultType result type (could be generic collection)
     * @param config configuration
     * @param introspectionFactory introspection factory
     * @param typeHandlerFactory type handler factory
     */
    public DefaultRowMapper(Type resultType, ResultMapConfig config, IntrospectionFactory introspectionFactory, TypeHandlerFactory typeHandlerFactory) {
        this.config = config;
        this.introspectionFactory = introspectionFactory;
        this.typeHandlerFactory = typeHandlerFactory;
        this.elementClass = ResultCollectorUtil.resultClass(resultType);
        if (!config.isAuto()) {
            List<PropertyMappingConfig> list = Arrays.asList(config.getMappings());
            finDynamicConfig = generatePropertyMappers(list);
        } else {
            finDynamicConfig = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void processResultSet(Request request, ResultSet rs, DataSink<Object> callback) throws SQLException {
        DynamicConfig dc;
        if (config.isAuto()) {
            synchronized (this) {
                if (dynamicConfig == null) {
                    List<PropertyMappingConfig> configs = generateAutoConfig(rs.getMetaData());
                    dynamicConfig = generatePropertyMappers(configs);
                }
                dc = dynamicConfig;
            }
        } else {
            dc = finDynamicConfig;
        }
        Object result;
        Key key = generateRowKey(dc, rs);
        if (key != null) {
            result = null;
            Map<Key, Object> map = request.getKey2Objects().get(this);
            if (map != null) {
                result = map.get(key);
            }
            if (result == null) {
                result = createResult(request, dc.mappers, rs);
                callback.pushData(result);
                if (map == null) {
                    map = new HashMap<Key, Object>();
                    request.getKey2Objects().put(this, map);
                }
                map.put(key, result);
            }
        } else {
            result = createResult(request, dc.mappers, rs);
            callback.pushData(result);
        }
        for (NestedMapPropertyMapper mapper : dc.nestedMappers) {
            mapper.mapResult(request, result, rs);
        }
    }

    private Object createResult(Request request, PropertyMapper[] mappers, ResultSet rs) throws SQLException {
        Object result;
        try {
            result = elementClass.newInstance();
        } catch (Exception e) {
            throw new GenericException("Failed to create result instance of class " + elementClass, e);
        }
        for (PropertyMapper mapper : mappers) {
            mapper.mapResult(request, result, rs);
        }
        return result;
    }

    /**
     * Generate a key that identifies current result row.
     * 
     * @param dc dynamic configuration
     * @param rs result set
     * @return key that identifies current result row.
     * @throws SQLException propagated from result set operations
     */
    private Key generateRowKey(DynamicConfig dc, ResultSet rs) throws SQLException {
        String[] groupBy = config.getGroupBy();
        if (groupBy != null && groupBy.length > 0) {
            Object[] key = new Object[dc.valueGetters.length];
            for (int i = 0; i < dc.valueGetters.length; ++i) {
                key[i] = dc.valueGetters[i].getValue(rs);
            }
            return new Key(key);
        }
        return null;
    }

    /**
     * Generate configuration for automapping.
     * 
     * @param meta result set metainformation
     * @return collection of result mapping configurations
     * @throws SQLException
     */
    private List<PropertyMappingConfig> generateAutoConfig(ResultSetMetaData meta) throws SQLException {
        List<PropertyMappingConfig> configs = new ArrayList<PropertyMappingConfig>();
        configs.addAll(Arrays.asList(config.getMappings()));
        Set<String> usedColumns = config.isAuto() ? new HashSet<String>() : null;
        for (PropertyMappingConfig mappingConfig : config.getMappings()) {
            if (mappingConfig.getColumnIndex() != 0) {
                usedColumns.add(meta.getColumnLabel(mappingConfig.getColumnIndex()).toLowerCase());
            } else {
                usedColumns.add(mappingConfig.getColumn().toLowerCase());
            }
        }
        for (int i = 0; i < meta.getColumnCount(); ++i) {
            String column = meta.getColumnLabel(i + 1).toLowerCase();
            if (!usedColumns.contains(column)) {
                PropertyMappingConfig mappingConfig = new PropertyMappingConfig();
                mappingConfig.setColumn(column);
                Method getter = IntrospectUtils.findGetterCaseInsensitive(elementClass, column);
                if (getter.getName().startsWith("get")) {
                    String prop = Character.toLowerCase(getter.getName().charAt(3)) + getter.getName().substring(4);
                    mappingConfig.setProperty(prop);
                } else if (getter.getName().startsWith("is")) {
                    String prop = Character.toLowerCase(getter.getName().charAt(2)) + getter.getName().substring(3);
                    mappingConfig.setProperty(prop);
                }
                if (mappingConfig.getProperty() == null) {
                    LOGGER.info("No matching property for column '" + column + "' was found when auto-mapping the bean " + elementClass);
                }
                configs.add(mappingConfig);
            }
        }
        return configs;
    }

    /**
     * Generate dynamic configuration (used for quick mapping) from the
     * collection of result mapping configuration.
     * 
     * @param configs
     * @return dynamic configuration
     */
    private DynamicConfig generatePropertyMappers(List<PropertyMappingConfig> configs) {
        String[] groupBy = config.getGroupBy();
        List<PropertyMapper> mappers = new ArrayList<PropertyMapper>();
        List<NestedMapPropertyMapper> nestedMappers = new ArrayList<NestedMapPropertyMapper>();
        List<ValueGetter> keyGenerators = new ArrayList<ValueGetter>();
        for (PropertyMappingConfig mappingConfig : configs) {
            Type propertyType = introspectionFactory.getPropertyType(elementClass, mappingConfig.getProperty());
            Setter setter = introspectionFactory.buildSetter(elementClass, mappingConfig.getProperty());
            if (groupBy != null && contains(groupBy, mappingConfig.getProperty())) {
                ValueGetter keyGen = new ValueGetter(typeHandlerFactory.getTypeHandler(propertyType), mappingConfig);
                keyGenerators.add(keyGen);
            }
            if (mappingConfig.getSubselect() != null) {
                Type[] parameterTypes = mappingConfig.getSubselect().getParameterTypes();
                TypeHandler<?> typeHandler = typeHandlerFactory.getTypeHandler(parameterTypes[0]);
                mappers.add(new PropertyMapper(mappingConfig, setter, typeHandler));
            } else if (mappingConfig.getNestedMapConfig() != null) {
                Getter getter = introspectionFactory.buildGetter(elementClass, mappingConfig.getProperty());
                RowMapper nestedMap = new DefaultRowMapper(propertyType, mappingConfig.getNestedMapConfig(), introspectionFactory, typeHandlerFactory);
                nestedMappers.add(new NestedMapPropertyMapper(getter, setter, nestedMap, mappingConfig));
            } else {
                TypeHandler<?> typeHandler = typeHandlerFactory.getTypeHandler(propertyType);
                mappers.add(new PropertyMapper(mappingConfig, setter, typeHandler));
            }
        }
        DynamicConfig dc = new DynamicConfig();
        dc.mappers = mappers.toArray(new PropertyMapper[mappers.size()]);
        dc.nestedMappers = nestedMappers.toArray(new NestedMapPropertyMapper[nestedMappers.size()]);
        dc.valueGetters = keyGenerators.toArray(new ValueGetter[keyGenerators.size()]);
        return dc;
    }

    private boolean contains(String[] array, String value) {
        for (String str : array) {
            if (str.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dynamic result map configuration. Used for quick mapping of the result
     * set.
     * 
     * @author Ivan Dubrov
     */
    private static class DynamicConfig {

        public PropertyMapper[] mappers;

        public ValueGetter[] valueGetters;

        public NestedMapPropertyMapper[] nestedMappers;

        DynamicConfig() {
        }
    }

    /**
     * Helper class for retrieving the data from result set for generating a row
     * key (used for grouping several result rows into one).
     * 
     * @author Ivan Dubrov
     */
    private static class ValueGetter {

        private TypeHandler<?> typeHandler;

        private PropertyMappingConfig config;

        private ValueGetter(TypeHandler<?> typeHandler, PropertyMappingConfig config) {
            this.typeHandler = typeHandler;
            this.config = config;
        }

        public Object getValue(ResultSet rs) throws SQLException {
            if (config.getColumnIndex() != 0) {
                return typeHandler.getValue(rs, config.getColumnIndex());
            }
            return typeHandler.getValue(rs, config.getColumn());
        }
    }
}
