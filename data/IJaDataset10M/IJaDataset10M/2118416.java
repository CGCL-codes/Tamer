package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * An Action to add client dependencies to some modelelement.
 *
 * @author Michiel
 */
public class ActionAddSupplierDependencyAction extends AbstractActionAddModelElement2 {

    /**
     * The constructor.
     */
    public ActionAddSupplierDependencyAction() {
        super();
        setMultiSelect(true);
    }

    protected void doIt(Collection selected) {
        Set oldSet = new HashSet(getSelected());
        for (Object supplier : oldSet) {
            if (oldSet.contains(supplier)) {
                oldSet.remove(supplier);
            } else {
                Model.getCoreFactory().buildDependency(supplier, getTarget());
            }
        }
        Collection toBeDeleted = new ArrayList();
        Collection c = Model.getFacade().getSupplierDependencies(getTarget());
        for (Object dependency : c) {
            if (oldSet.containsAll(Model.getFacade().getClients(dependency))) {
                toBeDeleted.add(dependency);
            }
        }
        ProjectManager.getManager().getCurrentProject().moveToTrash(toBeDeleted);
    }

    protected List getChoices() {
        List ret = new ArrayList();
        Object model = ProjectManager.getManager().getCurrentProject().getModel();
        if (getTarget() != null) {
            ret.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model, "org.omg.uml.foundation.core.ModelElement"));
            ret.remove(getTarget());
        }
        return ret;
    }

    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-supplier-dependency");
    }

    protected List getSelected() {
        List v = new ArrayList();
        Collection c = Model.getFacade().getSupplierDependencies(getTarget());
        for (Object supplierDependency : c) {
            v.addAll(Model.getFacade().getClients(supplierDependency));
        }
        return v;
    }
}
