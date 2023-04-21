package org.weras.commons.file.jbfs.xml;

import org.weras.commons.file.jbfs.Configuration;
import org.weras.commons.file.jbfs.FileStorage;
import org.weras.commons.file.jbfs.Path;
import org.weras.commons.file.jbfs.storages.LocalSystemFileStorage;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ConfigurationXmlConverter implements Converter {

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Configuration c = new Configuration();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            process(c, reader, context);
            reader.moveUp();
        }
        return c;
    }

    private void process(Configuration c, HierarchicalStreamReader reader, UnmarshallingContext context) {
        String tag = reader.getNodeName();
        if (tag.equalsIgnoreCase(XmlAlias.STORES)) {
            processStores(c, reader, context);
        } else if (tag.equalsIgnoreCase(XmlAlias.PATHS)) {
            processPaths(c, reader, context);
        }
    }

    private void processStores(Configuration c, HierarchicalStreamReader reader, UnmarshallingContext context) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String tag = reader.getNodeName();
            if (tag.equalsIgnoreCase(XmlAlias.LOCAL_DISK_STORE)) {
                c.addStore((FileStorage) context.convertAnother(c, LocalSystemFileStorage.class));
            }
            reader.moveUp();
        }
    }

    private void processPaths(Configuration c, HierarchicalStreamReader reader, UnmarshallingContext context) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            c.addPath((Path) context.convertAnother(c, Path.class));
            reader.moveUp();
        }
    }

    @SuppressWarnings("unchecked")
    public boolean canConvert(Class clazz) {
        return Configuration.class.isAssignableFrom(clazz);
    }
}
