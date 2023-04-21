package com.cosylab.vdct.model.impl;

import com.cosylab.vdct.model.Edge;
import java.util.ArrayList;
import java.util.Collection;
import com.cosylab.vdct.model.Model;
import com.cosylab.vdct.model.ModelEvent;
import com.cosylab.vdct.model.ModelListener;
import com.cosylab.vdct.model.Node;
import com.cosylab.vdct.model.NodeDescriptor;
import com.cosylab.vdct.model.Pin;
import com.cosylab.vdct.model.ModelEvent.ModelEventDescriptor;
import com.cosylab.vdct.model.property.Property;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic node implementation class. It means to care about events.
 *
 * @author Jernej Kamenik, Janez Golob
 */
public abstract class AbbstractNode extends ModelEventSourceSupport implements Node {

    protected ModelListener listener = new ModelListener() {

        public void modelChanged(ModelEvent e) {
            fireModelChangeEvent(e);
        }

        ;
    };

    private Collection<Pin> pins = new ArrayList<Pin>();

    private Map<String, Pin> pinMap = new HashMap<String, Pin>();

    private NodeDescriptor nodeDescriptor;

    private final Model model;

    private PropertyProviderImpl delegate = new PropertyProviderImpl();

    private String name;

    private final String type;

    private Point position = new Point(-1, -1);

    /**
         * ArrayNodeImpl constructor.
         * @param descriptor
         * @param parent
         */
    public AbbstractNode(Model parent, String name, String type, NodeDescriptor desc) {
        if (parent == null) {
            throw new NullPointerException();
        }
        if (name == null) {
            throw new NullPointerException();
        }
        if (type == null) {
            throw new NullPointerException();
        }
        if (desc == null) {
            throw new NullPointerException();
        }
        this.model = parent;
        this.name = name;
        this.type = type;
        this.nodeDescriptor = desc;
    }

    /**
         * @see com.cosylab.vdct.model.Node#addPin(com.cosylab.vdct.model.Pin)
         */
    public void addPin(Pin pin) {
        if (pin == null) {
            throw new NullPointerException();
        }
        if (pins.contains(pin)) {
            return;
        }
        pins.add(pin);
        pinMap.put(pin.getUniqueName(), pin);
        pin.addListener(listener);
        fireModelChangeEvent(new ModelEvent(this, ModelEventDescriptor.NODE_PIN_ADDED, pin, pin));
    }

    /**
         * @see com.cosylab.vdct.model.Node#getModel()
         */
    public Model getModel() {
        return model;
    }

    /**
         * @see com.cosylab.vdct.model.Node#getNodeDescriptor()
         */
    public NodeDescriptor getNodeDescriptor() {
        return nodeDescriptor;
    }

    /**
         * @see com.cosylab.vdct.model.Node#getPins()
         */
    public Collection<Pin> getPins() {
        return new ArrayList<Pin>(pins);
    }

    /**
         * @see com.cosylab.vdct.model.Node#removeAllPins()
         */
    public void removeAllPins() {
        Collection<Pin> retVal = new ArrayList<Pin>(pins);
        for (Pin pin : retVal) {
            for (Edge edge : pin.getEdges()) {
                edge.getModel().removeEdge(edge);
            }
            pin.removeListener(listener);
            pin.removeAllEdges();
        }
        pins.clear();
        pinMap.clear();
        fireModelChangeEvent(new ModelEvent(this, ModelEventDescriptor.NODE_PINS_REMOVED, retVal, retVal));
    }

    /**
         * @see com.cosylab.vdct.model.Node#removePin(com.cosylab.vdct.model.Pin)
         */
    public void removePin(Pin pin) {
        if (pin == null) {
            throw new NullPointerException();
        }
        if (pins.contains(pin)) {
            for (Edge edge : pin.getEdges()) {
                edge.getModel().removeEdge(edge);
            }
            pin.removeListener(listener);
            pins.remove(pin);
            pinMap.remove(pin.getUniqueName());
            fireModelChangeEvent(new ModelEvent(this, ModelEventDescriptor.NODE_PIN_REMOVED, pin, pin));
        }
    }

    /**
         * @see com.cosylab.vdct.model.Node#findPin(java.lang.String)
         */
    public Pin findPin(String pinName) {
        if (pinName == null) {
            throw new NullPointerException();
        }
        return pinMap.get(pinName);
    }

    public void setProperty(Property property) {
        if (property == null) {
            throw new NullPointerException();
        }
        Property oldProp = getProperty(property.getName());
        delegate.setProperty(property);
        fireModelChangeEvent(new ModelEvent(this, ModelEventDescriptor.PROPERTY_CHANGED, property, oldProp));
    }

    public Collection<Property> getProperties() {
        return delegate.getProperties();
    }

    public Property getProperty(String propertyName) {
        return delegate.getProperty(propertyName);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String newName) {
        if (newName == null || newName.length() == 0) {
            throw new NullPointerException();
        }
        if (newName.equals(name)) {
            return;
        }
        String oldName = name;
        this.name = newName;
        fireModelChangeEvent(new ModelEvent(this, ModelEventDescriptor.IDENTIFIABLE_NAME_CHANGED, newName, oldName));
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void setPosition(Point position) {
        if (position == null) {
            throw new IllegalArgumentException("position should not be null");
        }
        Point oldPosition = this.position;
        this.position = position;
        fireModelChangeEvent(new ModelEvent(this, ModelEventDescriptor.MODEL_NODE_MOVED, position, oldPosition));
    }
}
