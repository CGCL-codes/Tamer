package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.TagList;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class TagListConverter implements SingleValueConverter {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class cls) {
        return TagList.class.isAssignableFrom(cls);
    }

    @Override
    public Object fromString(String value) {
        if (value == null || value.length() == 0) return new TagList();
        return TagList.fromString(value);
    }

    @Override
    public String toString(Object value) {
        return ((TagList) value).toString();
    }
}
