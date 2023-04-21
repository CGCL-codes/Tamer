package net.sf.ww4j.struts2.action;

import net.sf.ww4j.Wizard;
import net.sf.ww4j.config.Ww4jSettings;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Dawid Walczak <dawid.m.walczak@gmail.com>
 *
 */
public class WizardActionSupport implements Preparable {

    /** Reference to wizard bean - set by spring */
    protected Wizard wizard;

    @SuppressWarnings("unchecked")
    public void prepare() throws Exception {
        if (wizard == null) {
            throw new IllegalStateException("Wizard bean not set.");
        }
        wizard.initialize();
        String attrName = Ww4jSettings.getInstance().getSessionAttributeName();
        ActionContext.getContext().getSession().put(attrName, wizard);
    }

    public String execute() throws Exception {
        return WizardStepActionSupport.CURRENT_ACTION;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public Wizard getWizard() {
        return wizard;
    }
}
