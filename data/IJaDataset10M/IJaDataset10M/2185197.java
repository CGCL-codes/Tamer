package com.antilia.web.button;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import com.antilia.web.ajax.AntiliaAjaxCallDecorator;
import com.antilia.web.ajax.IDialogFinder;
import com.antilia.web.dialog.IDialogScope;
import com.antilia.web.dialog.IVeilScope;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reirn70@gmail.com)
 */
public abstract class AntiliaAjaxFormComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior implements IDialogFinder {

    private static final long serialVersionUID = 1L;

    public IDialogScope findParentDialog() {
        return (IDialogScope) getComponent().findParent(IDialogScope.class);
    }

    public IVeilScope findVeilScope() {
        return getComponent().findParent(IVeilScope.class);
    }

    public Component getDefiningComponent() {
        return getComponent();
    }

    private IAjaxCallDecorator decorator;

    /**
	 * @param event
	 */
    public AntiliaAjaxFormComponentUpdatingBehavior(String event) {
        super(event);
    }

    @Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        if (decorator == null) decorator = new AntiliaAjaxCallDecorator(this);
        return decorator;
    }
}
