package com.antilia.web.crud;

import java.io.Serializable;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import com.antilia.web.button.AbstractButton;
import com.antilia.web.resources.DefaultStyle;
import com.antilia.web.utils.RequestUtils;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class NextRecordButton<E extends Serializable> extends AbstractButton {

    private static final long serialVersionUID = 1L;

    public NextRecordButton() {
        super("next", true);
    }

    @Override
    protected ResourceReference getImage() {
        if (RequestUtils.isBrowserIeExplorer6()) return DefaultStyle.IMG_NEXT_ENABLED;
        return DefaultStyle.IMG_NEXT_ENABLED_PNG;
    }

    @Override
    protected ResourceReference getDisabledImage() {
        if (RequestUtils.isBrowserIeExplorer6()) return DefaultStyle.IMG_NEXT_DISABLED;
        return DefaultStyle.IMG_NEXT_DISABLED_PNG;
    }

    @Override
    protected String getLabel() {
        return "Next";
    }

    @Override
    protected String getLabelKey() {
        return "NextRecordButton.label";
    }

    @Override
    public boolean isVisible() {
        EditPanel<E> component = findEditPanel();
        return component.getPageableProvider().size() > 1;
    }

    @Override
    public boolean isEnabled() {
        EditPanel<E> component = findEditPanel();
        return component.getPageableProvider().hasNext();
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        EditPanel<E> component = findEditPanel();
        component.getPageableProvider().next();
        target.addComponent((Component) component);
    }

    @Override
    public void onSubmit() {
    }

    @SuppressWarnings("unchecked")
    private EditPanel<E> findEditPanel() {
        return (EditPanel<E>) findParent(EditPanel.class);
    }
}
