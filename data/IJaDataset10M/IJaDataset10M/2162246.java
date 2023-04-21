package org.universa.tcc.gemda.web.component;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public abstract class AbstractFormPanel<T> extends Panel {

    private static final String FORM_MARKUP_ID = "form";

    public AbstractFormPanel(String id) {
        super(id);
        add(new Form<T>(FORM_MARKUP_ID));
    }

    public <E> AbstractFormPanel<T> add(FormComponent<E> formComponent) {
        getForm().add(formComponent);
        return this;
    }

    private Form<?> getForm() {
        return ((Form<?>) get(FORM_MARKUP_ID));
    }
}
