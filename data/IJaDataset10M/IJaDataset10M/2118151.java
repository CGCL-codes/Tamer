package org.ufacekit.core.dbel.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.ufacekit.core.ubean.databinding.observables.UBeansObservables;
import org.eclipse.ufacekit.ui.core.UIElement;
import org.ufacekit.core.dbel.Path;
import org.ufacekit.core.dbel.ui.DBELElement;
import org.ufacekit.core.dbel.ui.DBELPropertiesContainer;
import org.ufacekit.core.dbel.ui.form.UIDBELForm;

public abstract class AbstractDBELElement implements DBELElement {

    private UIElement element;

    private DBELPropertiesContainer container;

    private Map<String, UIDBELForm> formBindings = null;

    public AbstractDBELElement(UIElement element, DBELPropertiesContainer container) {
        this.element = element;
        this.container = container;
        if (this.container instanceof InternalDBELPropertiesContainer) ((InternalDBELPropertiesContainer) this.container).setDBELELement(this);
    }

    public UIElement getUIElement() {
        return element;
    }

    public DBELPropertiesContainer getContainer() {
        return container;
    }

    protected UIDBELForm newForm() {
        return container.newForm();
    }

    public UIDBELForm getForm(String property) {
        if (formBindings == null) {
            formBindings = new HashMap<String, UIDBELForm>();
        }
        UIDBELForm form = formBindings.get(property);
        if (form == null) {
            form = newForm();
            formBindings.put(property, form);
        }
        return form;
    }

    public void bindForms() {
        if (formBindings == null) return;
        for (Iterator iterator = formBindings.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, UIDBELForm> entry = (Map.Entry<String, UIDBELForm>) iterator.next();
            bindForm(entry.getValue(), entry.getKey());
        }
    }

    public void bindWithProperty(String property, Object source, Path path) {
        try {
            int targetFeatureId = Integer.parseInt(property);
            UIElement targetElement = getUIElement();
            String segment = path.firstSegment();
            int sourceFeatureId = Integer.parseInt(segment);
            UIElement sourceElement = (UIElement) source;
            DataBindingContext context = newForm().getDatabindingContext();
            Realm realm = context.getValidationRealm();
            context.bindValue(UBeansObservables.observeValue(realm, targetElement, targetFeatureId), UBeansObservables.observeValue(realm, sourceElement, sourceFeatureId), null, null);
        } catch (Throwable e) {
        }
    }

    public abstract void bindForm(UIDBELForm form, String property);
}
