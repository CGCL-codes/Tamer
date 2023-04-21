package net.sourceforge.copernicus.client.controller.editparts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.copernicus.client.model.Operation;
import net.sourceforge.copernicus.client.model.gef.GefInstance;
import net.sourceforge.copernicus.client.view.figures.OperationsCompartmentFigure;
import org.eclipse.draw2d.IFigure;

public class InstanceOperationsCompartmentEditPart extends AbstractCompartmentEditPart {

    @Override
    protected IFigure createFigure() {
        return new OperationsCompartmentFigure(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(GefInstance.PROPERTY_OPERATIONS_COLLAPSED)) {
            refresh();
        }
    }

    @Override
    protected void refreshVisuals() {
        OperationsCompartmentFigure figure = (OperationsCompartmentFigure) getFigure();
        GefInstance model = (GefInstance) getParent().getModel();
        figure.setModel(model);
    }

    @Override
    protected List<?> getModelChildren() {
        GefInstance model = (GefInstance) getParent().getModel();
        if (!model.isOperationsCollapsed()) {
            ArrayList<Operation> ret = new ArrayList<Operation>();
            Iterator<Operation> iter = model.getOperations();
            while (iter.hasNext()) ret.add(iter.next());
            return ret;
        }
        return Collections.EMPTY_LIST;
    }
}
