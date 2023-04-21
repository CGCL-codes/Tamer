package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ContextConverter implements Converter {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return Context.class.isAssignableFrom(type);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        ModelId modelId = (ModelId) ModelIdConverter.INSTANCE.unmarshal(reader, context);
        return ContextFactory.getInstance().get(modelId);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        ModelIdConverter.INSTANCE.marshal(((Context) source).getModelId(), writer, context);
    }
}
