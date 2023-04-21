package org.jprovocateur.serializer.core;

import org.jprovocateur.serializer.converters.ConverterLookup;
import org.jprovocateur.serializer.io.HierarchicalStreamReader;
import org.jprovocateur.serializer.io.HierarchicalStreamWriter;
import org.jprovocateur.serializer.mapper.Mapper;

public class TreeMarshallingStrategy extends AbstractTreeMarshallingStrategy {

    protected TreeUnmarshaller createUnmarshallingContext(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
        return new TreeUnmarshaller(root, reader, converterLookup, mapper);
    }

    protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
        return new TreeMarshaller(writer, converterLookup, mapper);
    }
}
