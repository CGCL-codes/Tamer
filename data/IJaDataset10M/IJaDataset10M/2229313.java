package net.sf.rcpforms.experimenting.rcp.extension.interfaces;

import net.sf.rcpforms.modeladapter.configuration.IRangeAdapter;

public interface IRangeAdapterFactory {

    public IRangeAdapter createRangeAdapter(Object modelBean, Class<?> propertyType, String propertyName);
}
