package uips.tree.inner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uips.tree.inner.interfaces.IBehaviorInn;
import uips.tree.inner.interfaces.IContainerInn;
import uips.tree.inner.interfaces.IElementInn;
import uips.tree.inner.interfaces.ILayoutInn;
import uips.tree.inner.interfaces.IPositionInn;
import uips.tree.inner.interfaces.IPropertiesInn;
import uips.tree.inner.interfaces.IPropertyInn;
import uips.tree.inner.interfaces.IStyleInn;

public class ContainerInn implements IContainerInn {

    private IPositionInn position;

    private List<ILayoutInn> layout;

    private List<IElementInn> element;

    private List<IContainerInn> container;

    private List<IBehaviorInn> behavior;

    private IStyleInn style;

    private String id;

    private String clazz;

    private String model;

    private String validator;

    private String children;

    private Map<String, IPropertyInn> property;

    @Override
    public List<ILayoutInn> getLayout() {
        if (this.layout == null) {
            this.layout = new ArrayList<ILayoutInn>();
        }
        return this.layout;
    }

    @Override
    public List<IElementInn> getElement() {
        if (this.element == null) {
            this.element = new ArrayList<IElementInn>();
        }
        return this.element;
    }

    @Override
    public List<IContainerInn> getContainer() {
        if (this.container == null) {
            this.container = new ArrayList<IContainerInn>();
        }
        return this.container;
    }

    @Override
    public List<IBehaviorInn> getBehavior() {
        if (this.behavior == null) {
            this.behavior = new ArrayList<IBehaviorInn>();
        }
        return this.behavior;
    }

    @Override
    public IPropertyInn getProperty(String name) {
        if (this.property == null) {
            this.property = new HashMap<String, IPropertyInn>();
            return null;
        }
        if (name == null) {
            return null;
        }
        return this.property.get(name);
    }

    @Override
    public void addProperty(IPropertyInn propertyInn) {
        if (this.property == null) {
            this.property = new HashMap<String, IPropertyInn>();
        }
        if (propertyInn == null || propertyInn.getName() == null) {
            return;
        }
        this.property.put(propertyInn.getName(), propertyInn);
    }

    @Override
    public void addProperties(IPropertiesInn propertiesInn) {
        if (this.property == null) {
            this.property = new HashMap<String, IPropertyInn>();
        }
        if (propertiesInn == null) {
            return;
        }
        List<PropertyInn> properties = PropertiesToPropertyConv.convertPropertiesToProperty((PropertiesInn) propertiesInn);
        for (PropertyInn propertyInn : properties) {
            this.property.put(propertyInn.getName(), propertyInn);
        }
    }

    @Override
    public Map<String, IPropertyInn> getPropertyMap() {
        if (this.property == null) {
            this.property = new HashMap<String, IPropertyInn>();
        }
        return this.property;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String value) {
        this.id = value;
    }

    @Override
    public String getClazz() {
        if (this.clazz == null) {
            return "public.container";
        }
        return this.clazz;
    }

    @Override
    public void setClazz(String value) {
        this.clazz = value;
    }

    @Override
    public String getModel() {
        return this.model;
    }

    @Override
    public void setModel(String value) {
        this.model = value;
    }

    @Override
    public String getValidator() {
        return this.validator;
    }

    @Override
    public void setValidator(String value) {
        this.validator = value;
    }

    @Override
    public String getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(String value) {
        this.children = value;
    }

    /**
     * @return the position
     */
    @Override
    public IPositionInn getPosition() {
        return this.position;
    }

    /**
     * @param position the position to set
     */
    @Override
    public void setPosition(IPositionInn position) {
        this.position = position;
    }

    /**
     * @return the style
     */
    @Override
    public IStyleInn getStyle() {
        return this.style;
    }

    /**
     * @param style the style to set
     */
    @Override
    public void setStyle(IStyleInn style) {
        this.style = style;
    }
}
