package org.ikasan.framework.component.serialisation;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Ikasan Development Team
 *
 */
public class RouteConverter implements Converter {

    /**
     * Default constructor.
     */
    public RouteConverter() {
    }

    public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
        Route route = (Route) obj;
        if (route.getSourceSystem() != null && route.getSourceSystem().trim().length() > 0) {
            writer.addAttribute("sourceSystem", route.getSourceSystem());
        }
        if (route.getTargetSystems() != null && route.getTargetSystems().size() > 0) {
            context.convertAnother(route.getTargetSystems());
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Route route = new Route();
        String nodeName = null;
        String attributeValue = reader.getAttribute("sourceSystem");
        if (attributeValue != null && attributeValue.trim().length() > 0) {
            route.setSourceSystem(attributeValue);
        }
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            nodeName = reader.getNodeName();
            if (nodeName.equals("TargetSystem")) {
                TargetSystem targetSystem = (TargetSystem) context.convertAnother(route, TargetSystem.class);
                route.addTargetSystem(targetSystem);
            }
            reader.moveUp();
        }
        return route;
    }

    /**
     * NOTE:  Parent class is JDK 1.4 based, so have to suppress this warning
     * @param type 
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean canConvert(Class type) {
        return type.equals(Route.class);
    }
}
