package com.kkshop.gwt.widget.wizard.impl.stackpanel;

import java.util.Iterator;
import java.util.Vector;
import com.google.gwt.user.client.ui.Widget;
import com.kkshop.gwt.widget.wizard.Wizard;
import com.kkshop.gwt.widget.wizard.WizardPage;
import com.kkshop.gwt.widget.wizard.WizardWidget;
import com.kkshop.gwt.widget.wizard.impl.AbstractWizardWidget;

public class StackPanelWizardWidget extends AbstractWizardWidget implements WizardWidget {

    Vector wizardPageWrapper = new Vector();

    protected boolean showOnlyCurrentPage = false;

    public StackPanelWizardWidget(Wizard wizard) {
        super(wizard);
    }

    public void setShowOnlyCurrentPage(boolean showOnlyCurrentPage) {
        this.showOnlyCurrentPage = showOnlyCurrentPage;
    }

    protected ExtendedStackPanel panel;

    public Widget createWidget() {
        super.preCreateWidget();
        panel = new ExtendedStackPanel();
        WizardPageWrapper wrapper = getNextWizardPageWrapper((WizardPage) null);
        panel.showStack(0);
        panel.setActive(0);
        currentPage = wrapper.getWizardPage();
        wrapper.setActive(true);
        return panel;
    }

    public void updateButtons() {
        for (Iterator i = wizardPageWrapper.iterator(); i.hasNext(); ) {
            WizardPageWrapper wrapper = (WizardPageWrapper) i.next();
            wrapper.updateButtons();
        }
    }

    public void updateMessage() {
        for (Iterator i = wizardPageWrapper.iterator(); i.hasNext(); ) {
            WizardPageWrapper wrapper = (WizardPageWrapper) i.next();
            wrapper.updateMessage();
        }
    }

    protected WizardPageWrapper getNextWizardPageWrapper(WizardPageWrapper wizardPageWrapper) {
        return getNextWizardPageWrapper(wizardPageWrapper == null ? null : wizardPageWrapper.getWizardPage());
    }

    protected WizardPageWrapper getNextWizardPageWrapper(WizardPage page) {
        page = getWizard().getNextWizardPage(page);
        return getWrapperForWizardPage(page);
    }

    protected WizardPageWrapper getWrapperForWizardPage(WizardPage page) {
        if (page == null) {
            return null;
        }
        for (Iterator i = wizardPageWrapper.iterator(); i.hasNext(); ) {
            WizardPageWrapper tmp = (WizardPageWrapper) i.next();
            if (tmp.getWizardPage() == page) {
                return tmp;
            }
        }
        WizardPageWrapper wrapper = new WizardPageWrapper(this, page);
        wizardPageWrapper.add(wrapper);
        panel.add(wrapper.createWidget(), wrapper.getWizardPage().getName());
        return wrapper;
    }

    public void handleNextClick() {
        if (!currentPage.canFlipToNextPage()) {
            return;
        }
        WizardPageWrapper wrapper = getNextWizardPageWrapper(currentPage);
        activateWizardPage(wrapper);
    }

    public void handlePrevClick() {
        WizardPageWrapper wrapper = getWrapperForWizardPage(wizard.getPrevWizardPage(currentPage));
        activateWizardPage(wrapper);
    }

    public void handleFinishClick() {
        if (!currentPage.canFlipToNextPage()) {
            return;
        }
        super.handleFinishClick();
    }

    protected void activateWizardPage(WizardPageWrapper wrapper) {
        if (wrapper == null) {
            return;
        }
        WizardPageWrapper currentWrapper = getWrapperForWizardPage(currentPage);
        if (currentWrapper != null) {
            currentWrapper.setActive(false);
        }
        if (showOnlyCurrentPage) {
            panel.hideStack(wizard.getIndexOfPage(currentPage));
        }
        currentPage = wrapper.getWizardPage();
        int index = wizard.getIndexOfPage(currentPage);
        panel.showStack(index);
        panel.setActive(index);
        wrapper.setActive(true);
        panel.scrollToStack(index);
    }
}
