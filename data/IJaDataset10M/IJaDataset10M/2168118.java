package org.jprovocateur.serializer.converters.extended;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.jprovocateur.serializer.converters.ConversionException;
import org.jprovocateur.serializer.converters.Converter;
import org.jprovocateur.serializer.converters.MarshallingContext;
import org.jprovocateur.serializer.converters.UnmarshallingContext;
import org.jprovocateur.serializer.io.HierarchicalStreamReader;
import org.jprovocateur.serializer.io.HierarchicalStreamWriter;
import org.jprovocateur.serializer.mapper.DynamicProxyMapper;
import org.jprovocateur.serializer.mapper.Mapper;

/**
 * Converts a dynamic proxy to XML, storing the implemented
 * interfaces and handler.
 *
 * @author Joe Walnes
 */
public class DynamicProxyConverter implements Converter {

    private ClassLoader classLoader;

    private Mapper mapper;

    public DynamicProxyConverter(Mapper mapper) {
        this(mapper, DynamicProxyConverter.class.getClassLoader());
    }

    public DynamicProxyConverter(Mapper mapper, ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.mapper = mapper;
    }

    public boolean canConvert(Class type) {
        return type.equals(DynamicProxyMapper.DynamicProxy.class) || Proxy.isProxyClass(type);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(source);
        addInterfacesToXml(source, writer);
        writer.startNode("handler");
        String attributeName = mapper.aliasForSystemAttribute("class");
        if (attributeName != null) {
            writer.addAttribute(attributeName, mapper.serializedClass(invocationHandler.getClass()));
        }
        context.convertAnother(invocationHandler);
        writer.endNode();
    }

    private void addInterfacesToXml(Object source, HierarchicalStreamWriter writer) {
        Class[] interfaces = source.getClass().getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class currentInterface = interfaces[i];
            writer.startNode("interface");
            writer.setValue(mapper.serializedClass(currentInterface));
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        List interfaces = new ArrayList();
        InvocationHandler handler = null;
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String elementName = reader.getNodeName();
            if (elementName.equals("interface")) {
                interfaces.add(mapper.realClass(reader.getValue()));
            } else if (elementName.equals("handler")) {
                String attributeName = mapper.aliasForSystemAttribute("class");
                if (attributeName != null) {
                    Class handlerType = mapper.realClass(reader.getAttribute(attributeName));
                    handler = (InvocationHandler) context.convertAnother(null, handlerType);
                }
            }
            reader.moveUp();
        }
        if (handler == null) {
            throw new ConversionException("No InvocationHandler specified for dynamic proxy");
        }
        Class[] interfacesAsArray = new Class[interfaces.size()];
        interfaces.toArray(interfacesAsArray);
        return Proxy.newProxyInstance(classLoader, interfacesAsArray, handler);
    }
}
