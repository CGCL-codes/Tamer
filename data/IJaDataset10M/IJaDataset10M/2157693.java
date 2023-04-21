package org.mariella.persistence.annotations.mapping_builder;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import org.mariella.persistence.database.BigDecimalConverter;
import org.mariella.persistence.database.BooleanAsNumberConverter;
import org.mariella.persistence.database.Converter;
import org.mariella.persistence.database.DateConverter;
import org.mariella.persistence.database.EnumConverter;
import org.mariella.persistence.database.IntegerConverter;
import org.mariella.persistence.database.LongConverter;
import org.mariella.persistence.database.StringConverter;
import org.mariella.persistence.database.TimestampConverter;
import org.mariella.persistence.schema.ScalarPropertyDescription;

public class ConverterRegistryImpl implements ConverterRegistry {

    public static interface ConverterFactory {

        public Converter<?> createConverter(ScalarPropertyDescription propertyDescription, DatabaseTableInfo tableInfo, DatabaseColumnInfo columnInfo);
    }

    public static class ConverterFactoryImpl implements ConverterFactory {

        private final Converter<?> converter;

        public ConverterFactoryImpl(Converter<?> converter) {
            super();
            this.converter = converter;
        }

        @Override
        public Converter<?> createConverter(ScalarPropertyDescription propertyDescription, DatabaseTableInfo tableInfo, DatabaseColumnInfo columnInfo) {
            return converter;
        }
    }

    private Map<String, Converter<?>> namedConverters = new HashMap<String, Converter<?>>();

    private Map<Integer, Map<Class<?>, ConverterFactory>> converterFactories = new HashMap<Integer, Map<Class<?>, ConverterFactory>>();

    public ConverterRegistryImpl() {
        super();
        registerConverterFactory(Types.VARCHAR, String.class, new ConverterFactoryImpl(StringConverter.Singleton));
        registerConverterFactory(Types.LONGVARCHAR, String.class, new ConverterFactoryImpl(StringConverter.Singleton));
        registerConverterFactory(Types.CLOB, String.class, new ConverterFactoryImpl(StringConverter.Singleton));
        registerNumericConverters(Types.INTEGER);
        registerNumericConverters(Types.DECIMAL);
        registerNumericConverters(Types.NUMERIC);
        registerConverterFactory(Types.FLOAT, BigDecimal.class, new ConverterFactoryImpl(BigDecimalConverter.Singleton));
        registerConverterFactory(Types.DATE, Timestamp.class, new ConverterFactoryImpl(TimestampConverter.Singleton));
        registerConverterFactory(Types.TIMESTAMP, Timestamp.class, new ConverterFactoryImpl(TimestampConverter.Singleton));
        registerConverterFactory(Types.DATE, Date.class, new ConverterFactoryImpl(DateConverter.Singleton));
        registerConverterFactory(Types.DATE, java.util.Date.class, new ConverterFactoryImpl(DateConverter.Singleton));
        registerConverterFactory(Types.TIMESTAMP, Date.class, new ConverterFactoryImpl(DateConverter.Singleton));
    }

    private void registerNumericConverters(int type) {
        registerConverterFactory(type, Integer.class, new ConverterFactoryImpl(IntegerConverter.Singleton));
        registerConverterFactory(type, int.class, new ConverterFactoryImpl(IntegerConverter.Singleton));
        registerConverterFactory(type, Long.class, new ConverterFactoryImpl(LongConverter.Singleton));
        registerConverterFactory(type, long.class, new ConverterFactoryImpl(LongConverter.Singleton));
        registerConverterFactory(type, BigDecimal.class, new ConverterFactoryImpl(BigDecimalConverter.Singleton));
        registerConverterFactory(type, boolean.class, new ConverterFactoryImpl(BooleanAsNumberConverter.Singleton));
        registerConverterFactory(type, Boolean.class, new ConverterFactoryImpl(BooleanAsNumberConverter.Singleton));
    }

    public void registerConverterFactory(int sqlType, Class<?> propertyType, ConverterFactory converterFactory) {
        Map<Class<?>, ConverterFactory> map = converterFactories.get(sqlType);
        if (map == null) {
            map = new HashMap<Class<?>, ConverterFactory>();
            converterFactories.put(sqlType, map);
        }
        map.put(propertyType, converterFactory);
    }

    public void registerConverter(String converterName, Converter<?> converter) {
        namedConverters.put(converterName, converter);
    }

    @Override
    public Converter<?> getNamedConverter(String converterName) {
        Converter<?> converter = namedConverters.get(converterName);
        if (converter == null) {
            throw new IllegalArgumentException("Unknown converter named '" + converterName + "'!");
        } else {
            return converter;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Converter<?> getConverterForColumn(ScalarPropertyDescription propertyDescription, DatabaseTableInfo tableInfo, DatabaseColumnInfo columnInfo) {
        ConverterFactory factory = null;
        Map<Class<?>, ConverterFactory> map = converterFactories.get(columnInfo.getType());
        if (map != null) {
            factory = map.get(propertyDescription.getPropertyDescriptor().getPropertyType());
            if (factory != null) {
                Converter<?> converter = factory.createConverter(propertyDescription, tableInfo, columnInfo);
                if (converter != null) {
                    return converter;
                }
            }
        }
        if (columnInfo.getType() == Types.VARCHAR && propertyDescription.getPropertyDescriptor().getPropertyType().isEnum()) {
            return new EnumConverter(propertyDescription.getPropertyDescriptor().getPropertyType());
        }
        throw new IllegalArgumentException("Cannot create converter for property " + propertyDescription.getClassDescription().getClassName() + "." + propertyDescription.getPropertyDescriptor().getName());
    }
}
