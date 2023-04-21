package de.grogra.ext.exchangegraph.nodes;

import java.util.List;
import de.grogra.ext.exchangegraph.xmlbeans.Property;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.objects.Cylinder;

public class XEGCylinder {

    public static void handleImportProperties(Node node, List<Property> properties, List<Property> handledProperties) {
        for (Property p : properties) {
            if (p.getName().equals("radius")) {
                ((Cylinder) node).setRadius(Float.valueOf(p.getValue()));
                handledProperties.add(p);
            } else if (p.getName().equals("height")) {
                ((Cylinder) node).setLength(Float.valueOf(p.getValue()));
                handledProperties.add(p);
            } else if (p.getName().equals("bottom_open")) {
                ((Cylinder) node).setBaseOpen(Boolean.valueOf(p.getValue()));
                handledProperties.add(p);
            } else if (p.getName().equals("top_open")) {
                ((Cylinder) node).setTopOpen(Boolean.valueOf(p.getValue()));
                handledProperties.add(p);
            }
        }
    }

    public static void handleExportProperties(Node node, de.grogra.ext.exchangegraph.xmlbeans.Node xmlNode) {
        Property xmlProperty = xmlNode.addNewProperty();
        xmlProperty.setName("radius");
        xmlProperty.setValue(String.valueOf(((Cylinder) node).getRadius()));
        xmlProperty = xmlNode.addNewProperty();
        xmlProperty.setName("height");
        xmlProperty.setValue(String.valueOf(((Cylinder) node).getLength()));
        xmlProperty = xmlNode.addNewProperty();
        xmlProperty.setName("top_open");
        xmlProperty.setValue(String.valueOf(((Cylinder) node).isTopOpen()));
        xmlProperty = xmlNode.addNewProperty();
        xmlProperty.setName("bottom_open");
        xmlProperty.setValue(String.valueOf(((Cylinder) node).isBaseOpen()));
    }
}
