package org.nomadpim.core.ui.views;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.util.properties.Property;

public abstract class EntityView<T extends IEntity> extends ViewPart implements IModelProvider {

    private Map<Property, Text> components = new HashMap<Property, Text>();

    private T model;

    protected void addProperty(Text descriptionField, Property property) {
    }

    public void dispose() {
        for (Text text : components.values()) {
            text.dispose();
        }
        super.dispose();
    }

    /**
     * Template Method that is called after <code>setModel</code> and should
     * return the title of this component for the new model.
     */
    protected abstract String getComponentTitle();

    public final T getModel() {
        return model;
    }

    public Text getPropertyComponent(Property property) {
        return components.get(property);
    }

    public void setModel(T newModel) {
        this.model = newModel;
        for (Map.Entry<Property, Text> entry : components.entrySet()) {
            entry.getValue().setText((String) model.get(entry.getKey()));
        }
        setPartName(getComponentTitle());
    }
}
