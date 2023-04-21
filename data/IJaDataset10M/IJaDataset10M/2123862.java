package org.t2framework.t2.format.amf.spi.impl;

import java.util.ArrayList;
import java.util.List;
import org.t2framework.commons.util.Assertion;
import org.t2framework.t2.contexts.WebConfiguration;
import org.t2framework.t2.format.amf.spi.AmfConverter;
import org.t2framework.t2.format.amf3.io.reader.converter.ReadValueConverter;
import org.t2framework.t2.format.amf3.io.reader.converter.impl.Map2ObjectConverter;
import org.t2framework.t2.format.amf3.io.reader.converter.impl.NaN2NullConverter;
import org.t2framework.t2.format.amf3.io.writer.converter.WriteValueConverter;
import org.t2framework.t2.format.amf3.io.writer.converter.impl.Null2NaNConverter;

public class AmfConverterImpl implements AmfConverter {

    protected List<ReadValueConverter<?>> readValueConverters = new ArrayList<ReadValueConverter<?>>();

    protected List<WriteValueConverter<?>> writeValueConverters = new ArrayList<WriteValueConverter<?>>();

    @Override
    public void initialize(WebConfiguration config) {
        initReadValueConverterList(config);
        initWriteValueConverterList(config);
    }

    protected void initReadValueConverterList(WebConfiguration config) {
        this.readValueConverters.add(new NaN2NullConverter());
        Map2ObjectConverter map2ObjectConverter = new Map2ObjectConverter();
        map2ObjectConverter.setReadValueConverters(readValueConverters);
        this.readValueConverters.add(map2ObjectConverter);
    }

    protected void initWriteValueConverterList(WebConfiguration config) {
        this.writeValueConverters.add(new Null2NaNConverter());
        this.writeValueConverters.add(new org.t2framework.t2.format.amf3.io.writer.converter.impl.DateConverter());
    }

    @Override
    public Object convertReadValue(Class<?> valueType, Object value) {
        for (ReadValueConverter<?> converter : this.readValueConverters) {
            if (converter.accept(value, valueType)) {
                return converter.convert(value, valueType);
            }
        }
        return value;
    }

    @Override
    public Object convertWriteValue(Class<?> valueType, Object value) {
        for (WriteValueConverter<?> converter : this.writeValueConverters) {
            if (converter.accept(valueType, value)) {
                return converter.convert(valueType, value);
            }
        }
        return value;
    }

    @Override
    public List<ReadValueConverter<?>> getReadValueConverters() {
        return this.readValueConverters;
    }

    public void setReadValueConverters(List<ReadValueConverter<?>> readValueConverters) {
        this.readValueConverters = readValueConverters;
    }

    @Override
    public List<WriteValueConverter<?>> getWriteValueConverters() {
        return this.writeValueConverters;
    }

    public void setWriteValueConverters(List<WriteValueConverter<?>> writeValueConverters) {
        this.writeValueConverters = writeValueConverters;
    }

    public void addReadValueConverter(ReadValueConverter<?> converter) {
        Assertion.notNull(converter);
        this.readValueConverters.add(converter);
    }

    public void addWriteValueConverter(WriteValueConverter<?> converter) {
        Assertion.notNull(converter);
        this.writeValueConverters.add(converter);
    }
}
