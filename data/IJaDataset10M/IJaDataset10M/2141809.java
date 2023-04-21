package org.softmed.jops.fileloading.converters;

public interface IValueConverter {

    public Object fromInternalToExternal(Object internal);

    public Object fromExternalToInternal(Object external);
}
