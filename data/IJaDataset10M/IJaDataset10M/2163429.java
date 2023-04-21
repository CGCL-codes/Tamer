package jgnash.ui.wizards.imports.jgnash;

import jgnash.ui.wizards.file.NewFileOne;

public class ImportOne extends NewFileOne {

    /**
     * toString must return a valid description for this page that will
     * appear in the task list of the WizardDialog
     */
    @Override
    public String toString() {
        return "2. " + rb.getString("Title.DatabaseCfg");
    }
}
