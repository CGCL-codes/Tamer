package org.xmi.uml.model;

import org.xmi.graph.model.AbstractCompartmentItem;
import org.xmi.infoset.XMIStructure;
import org.xmi.infoset.ext.Diagram;
import org.xmi.infoset.ext.ModelPersistence;
import org.xmi.infoset.ext.Shape;

public class Property extends AbstractCompartmentItem implements Shape {

    private final L1.Property property;

    private Diagram diagram;

    public Property(UMLDiagram diagram, L1.Property property) {
        super((XMIStructure) property);
        this.property = property;
        this.diagram = diagram;
    }

    public void setModelId(String modelId) {
    }

    public void setBounds(int[] bounds) {
        this.bounds = bounds;
    }

    @ModelPersistence
    public String getModelId() {
        return ((XMIStructure) property).getXmiId();
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public Object getModelElement() {
        return property;
    }

    public Integer getLower() {
        return property.getLower();
    }

    public Long getUpper() {
        return property.getUpper();
    }

    public String getType() {
        return property.getDatatype().getName();
    }

    private int[] bounds;

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @ModelPersistence
    public int[] getBounds() {
        return bounds;
    }

    @ModelPersistence
    public String getId() {
        return id;
    }

    public String getModelNamespace() {
        return ((XMIStructure) property).getXmiNamespace();
    }

    public String getName() {
        return property.getName();
    }

    public void setName(String name) {
        property.setName(name);
    }

    public void setModelNamespace(String ns) {
        ((XMIStructure) property).setXmiNamespace(ns);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Operation.formatVisibilityKind(property.getVisibility()));
        if (property.getIsDerived()) {
            buffer.append(" / ");
        }
        buffer.append(property.getName());
        buffer.append(" : ");
        buffer.append(" ").append(property.getType().getName());
        if (property.getDefault() != null) {
            buffer.append(" = ").append(property.getDefault());
        }
        if (property.getIsReadOnly()) {
            buffer.append(" readOnly");
        } else if (property.getIsDerivedUnion()) {
            buffer.append(" union");
        } else if (property.getIsOrdered()) {
            buffer.append(" ordered");
        }
        return buffer.toString();
    }
}
