package net.sourceforge.copernicus.client.controller.editparts.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;
import net.sourceforge.copernicus.client.model.Instance;
import net.sourceforge.copernicus.client.model.gef.GefSchema;

public class SchemaTreeEditPart extends AbstractTreeEditPart {

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(GefSchema.PROPERTY_DISPLAYED_INSTANCES)) {
            refresh();
        }
    }

    @Override
    protected List<Instance> getModelChildren() {
        return ((GefSchema) getModel()).getDisplayedInstances();
    }
}
