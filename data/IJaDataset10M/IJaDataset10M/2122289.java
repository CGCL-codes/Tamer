package com.google.code.hibernate.rest.representation.xml;

import static com.google.code.hibernate.rest.internal.InternalPreconditions.checkNotNull;
import com.google.code.hibernate.rest.EntityManager;
import com.google.code.hibernate.rest.representation.AbstractXStreamPepresentor;
import com.google.code.hibernate.rest.representation.XStreamBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.AbstractTreeMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.core.TreeUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * @author wangzijian
 * 
 */
public class XMLRepresentor extends AbstractXStreamPepresentor {

    public static final String XMLNS_XLINK = "xmlns:xlink";

    public static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";

    public static final String XLINK_HREF = "xlink:href";

    private static final String CONTENT_TYPE = "text/xml";

    private static final String ENCODING = "UTF-8";

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";

    private final EntityManager entityManager;

    private final XStream xStream;

    public XMLRepresentor(EntityManager entityManager) {
        this.entityManager = checkNotNull(entityManager, "entityManager");
        this.xStream = new XStreamBuilder().driver(new DomDriver(ENCODING)).strategy(new EntityMarshallingStrategy()).alias(entityManager.getSimpleEntityNames()).build();
    }

    @Override
    protected XStream xStream() {
        return xStream;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    protected String marshal(Object root, String baseURL) {
        return HEADER + super.marshal(root, baseURL);
    }

    private class EntityMarshallingStrategy extends AbstractTreeMarshallingStrategy {

        @Override
        protected TreeUnmarshaller createUnmarshallingContext(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
            return new EntityXMLUnmarshaller(root, reader, converterLookup, mapper, entityManager);
        }

        @Override
        protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
            return new EntityXMLMarshaller(writer, converterLookup, mapper, entityManager);
        }
    }
}
