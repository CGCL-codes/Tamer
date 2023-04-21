package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;

/**
 * List model which implements allAvailableFeatures operation for a
 * ClassifierRole as described in the well formedness rules.
 * 
 * @since Oct 4, 2002
 * @author jaap.branderhorst@xs4all.nl
 * 
 */
class UMLClassifierRoleAvailableFeaturesListModel extends UMLModelElementListModel {

    /**
     * Constructor for UMLClassifierRoleAvailableFeaturesListModel.
     */
    public UMLClassifierRoleAvailableFeaturesListModel(final Object modelElement) {
        super();
        setTarget(modelElement);
    }

    protected void buildModelList() {
        setAllElements(Model.getCollaborationsHelper().allAvailableFeatures(getTarget()));
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e instanceof AddAssociationEvent) {
            if (e.getPropertyName().equals("base") && e.getSource() == getTarget()) {
                Object clazz = getChangedElement(e);
                addAll(Model.getFacade().getFeatures(clazz));
                Model.getPump().addModelEventListener(this, clazz, "feature");
            } else if (e.getPropertyName().equals("feature") && Model.getFacade().getBases(getTarget()).contains(e.getSource())) {
                addElement(getChangedElement(e));
            }
        } else if (e instanceof RemoveAssociationEvent) {
            if (e.getPropertyName().equals("base") && e.getSource() == getTarget()) {
                Object clazz = getChangedElement(e);
                Model.getPump().removeModelEventListener(this, clazz, "feature");
            } else if (e.getPropertyName().equals("feature") && Model.getFacade().getBases(getTarget()).contains(e.getSource())) {
                removeElement(getChangedElement(e));
            }
        } else {
            super.propertyChange(e);
        }
    }

    protected void setTarget(Object target) {
        assert (getTarget() == null);
        assert (Model.getFacade().isAElement(target));
        setListTarget(target);
        Collection bases = Model.getFacade().getBases(getTarget());
        Iterator it = bases.iterator();
        while (it.hasNext()) {
            Object base = it.next();
            Model.getPump().addModelEventListener(this, base, "feature");
        }
        Model.getPump().addModelEventListener(this, getTarget(), "base");
        removeAllElements();
        setBuildingModel(true);
        buildModelList();
        setBuildingModel(false);
        if (getSize() > 0) {
            fireIntervalAdded(this, 0, getSize() - 1);
        }
    }

    protected boolean isValidElement(Object element) {
        return false;
    }

    public void removeModelEventListener() {
        Collection bases = Model.getFacade().getBases(getTarget());
        Iterator it = bases.iterator();
        while (it.hasNext()) {
            Object base = it.next();
            Model.getPump().removeModelEventListener(this, base, "feature");
        }
        Model.getPump().removeModelEventListener(this, getTarget(), "base");
    }
}
