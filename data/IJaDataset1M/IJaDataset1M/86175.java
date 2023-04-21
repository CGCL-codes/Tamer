package com.csam.javabean2map.impl;

import com.csam.javabean2map.*;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is the default <CODE>GeneratorBuilder</CODE> implementation.
 *
 * @author Nathan Crause
 */
public class GeneratorBuilderImpl implements GeneratorBuilder {

    /** Creates a new instance of GeneratorBuilderImpl */
    public GeneratorBuilderImpl() {
    }

    public MappingGenerator load(InputStream inStream) throws MappingException {
        try {
            MappingGeneratorImpl result = new MappingGeneratorImpl();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setCoalescing(true);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inStream);
            Element root = document.getDocumentElement();
            NodeList mappings = root.getElementsByTagName("mapping");
            for (int i = 0; i < mappings.getLength(); ++i) {
                Element mapping = (Element) mappings.item(i);
                String name = mapping.getAttribute("id");
                String className = mapping.getAttribute("class");
                MappingEntry entry = new MappingEntry(getClass().forName(className));
                NodeList controls = mapping.getChildNodes();
                for (int j = 0; j < controls.getLength(); ++j) {
                    Element control = (Element) controls.item(j);
                    if (control.getTagName().equals("map")) {
                        String mappedName = control.getAttribute("name");
                        String propertyName = control.getAttribute("property");
                        entry.addDefinition(mappedName, propertyName);
                    } else if (control.getTagName().equals("premap") || control.getTagName().equals("postmap")) {
                        String processorClass = control.getAttribute("class");
                        Class processorClazz = getClass().forName(processorClass);
                        if (!ExtraProcessor.class.isAssignableFrom(processorClazz)) {
                            throw new MappingException("Processor class '" + processorClass + "'defined for '" + control.getTagName() + "' does not implement '" + ExtraProcessor.class.getName() + "'");
                        }
                        ExtraProcessor p = (ExtraProcessor) processorClazz.newInstance();
                        if (control.getTagName().equals("premap")) {
                            entry.setPremapper(p);
                        } else {
                            entry.setPostmapper(p);
                        }
                    }
                }
                result.addEntry(name, entry);
            }
            return result;
        } catch (Throwable thrown) {
            if (thrown instanceof MappingException) throw (MappingException) thrown;
            throw new MappingException(thrown);
        }
    }
}
