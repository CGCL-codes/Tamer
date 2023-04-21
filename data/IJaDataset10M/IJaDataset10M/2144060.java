package org.argouml.core.propertypanels.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Action;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.util.PathComparator;

/**
 * The combobox model for the parameter belonging to some TemplateParameter.
 */
class UMLTemplateParameterParameterComboBoxModel extends UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -1295242702819038923L;

    private static final Logger LOG = Logger.getLogger(UMLTemplateParameterParameterComboBoxModel.class);

    /**
     * Constructor for UMLStructuralFeatureTypeComboBoxModel.
     */
    public UMLTemplateParameterParameterComboBoxModel(Object target) {
        super(target, "parameter", true);
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAModelElement(element);
    }

    @SuppressWarnings("unchecked")
    protected void buildModelList() {
        Set<Object> elements = new TreeSet<Object>(new PathComparator());
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p == null) {
            return;
        }
        for (Object model : p.getUserDefinedModelList()) {
            elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model, Model.getMetaTypes().getModelElement()));
        }
        elements.addAll(p.getProfileConfiguration().findByMetaType(Model.getMetaTypes().getClassifier()));
        setElements(elements);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void buildMinimalModelList() {
        Collection list = new ArrayList(1);
        Object element = getSelectedModelElement();
        LOG.info("The selected element is " + element);
        if (element != null) {
            LOG.info("The selected element is " + Model.getFacade().getName(element));
        }
        if (element != null) {
            list.add(element);
        }
        setElements(list);
    }

    @Override
    protected boolean isLazy() {
        return true;
    }

    protected Object getSelectedModelElement() {
        Object o = null;
        if (getTarget() != null) {
            o = Model.getFacade().getParameter(getTarget());
        }
        return o;
    }

    public Action getAction() {
        return null;
    }
}
