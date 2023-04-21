package com.definity.toolkit.xml.converter;

import java.io.Serializable;
import java.util.Map.Entry;
import com.definity.toolkit.domain.DynamicDomain;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DynamicDomainConverter implements Converter {

    @Override
    @SuppressWarnings({ "rawtypes" })
    public boolean canConvert(Class type) {
        return DynamicDomain.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        DynamicDomain<?> domain = (DynamicDomain<?>) object;
        writer.addAttribute("type", domain.getClass().getName());
        DynamicDomain<Serializable> metadata = domain.getMetadata();
        writer.startNode("metadata");
        for (Entry<String, Object> e : metadata.getProperties().entrySet()) {
            writer.startNode("attr");
            writer.addAttribute("name", e.getKey());
            writer.addAttribute("type", ((Class<?>) e.getValue()).getName());
            writer.endNode();
        }
        writer.endNode();
        for (Entry<String, Object> entry : domain.getProperties().entrySet()) {
            if (entry.getValue() == null) continue;
            writer.startNode(entry.getKey());
            context.convertAnother(entry.getValue());
            writer.endNode();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        DynamicDomain<Serializable> domain;
        String domainType = reader.getAttribute("type");
        try {
            Class<?> classType = Class.forName(domainType);
            domain = (DynamicDomain<Serializable>) classType.newInstance();
        } catch (ClassNotFoundException e) {
            domain = new DynamicDomain<Serializable>();
        } catch (InstantiationException e) {
            domain = new DynamicDomain<Serializable>();
        } catch (IllegalAccessException e) {
            domain = new DynamicDomain<Serializable>();
        }
        DynamicDomain<Serializable> metadata = new DynamicDomain<Serializable>();
        reader.moveDown();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String name = reader.getAttribute("name");
            String type = reader.getAttribute("type");
            Class<?> value;
            try {
                value = Class.forName(type);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            metadata.setProperty(name, value);
            reader.moveUp();
        }
        reader.moveUp();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String name = reader.getNodeName();
            Object value = context.convertAnother(reader.getValue(), (Class<?>) metadata.getProperty(name));
            domain.setProperty(name, value);
            reader.moveUp();
        }
        return domain;
    }
}
