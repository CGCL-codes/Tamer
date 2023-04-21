package com.ecmdeveloper.plugin.search.commands;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.ecmdeveloper.plugin.search.model.IQueryField;
import com.ecmdeveloper.plugin.search.model.InTest;
import com.ecmdeveloper.plugin.search.model.QueryComponent;
import com.ecmdeveloper.plugin.search.wizards.InTestWizard;

/**
 * @author ricardo.belfor
 *
 */
public class EditInTestCommand extends EditQueryComponentCommand {

    private IQueryField previousField;

    private IQueryField newField;

    private Object previousValue;

    private Object newValue;

    public EditInTestCommand(QueryComponent queryComponent) {
        super(queryComponent);
        setLabel("Edit In Test");
    }

    @Override
    public void execute() {
        Shell shell = Display.getCurrent().getActiveShell();
        InTest inTest = getInTest();
        InTestWizard wizard = getInTestWizard(inTest);
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
        if (dialog.open() == Dialog.OK) {
            previousField = inTest.getField();
            previousValue = inTest.getValue();
            newField = wizard.getField();
            newValue = wizard.getValue();
            redo();
        }
    }

    private InTestWizard getInTestWizard(InTest inTest) {
        InTestWizard wizard = new InTestWizard(queryComponent.getQuery());
        wizard.setSelection(inTest.getField());
        wizard.setValue(inTest.getValue());
        return wizard;
    }

    private InTest getInTest() {
        return (InTest) queryComponent;
    }

    @Override
    public void redo() {
        InTest inTest = getInTest();
        inTest.setField(newField);
        inTest.setValue(newValue);
    }

    @Override
    public boolean canUndo() {
        return previousField != null;
    }

    @Override
    public void undo() {
        InTest inTest = getInTest();
        inTest.setField(previousField);
        inTest.setValue(previousValue);
    }
}
