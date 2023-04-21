package com.ecmdeveloper.plugin.java.wizard;

/**
 * @author Ricardo.Belfor
 *
 */
public class NewEventActionClassWizard extends NewClassWizard {

    @Override
    public void addPages() {
        newClassTypePage = new NewEventActionClassTypePage();
        addPage(newClassTypePage);
        newClassTypePage.init(getSelection());
    }
}
