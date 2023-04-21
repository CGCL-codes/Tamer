package net.sourceforge.c4jplugin.internal.ui.preferences;

import net.sourceforge.c4jplugin.internal.decorators.C4JDecorator;
import net.sourceforge.c4jplugin.internal.ui.text.UIMessages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class C4JDecoratorPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, SelectionListener {

    private IDecoratorManager decoManager = PlatformUI.getWorkbench().getDecoratorManager();

    private Button buttonContractedClasses;

    private Button buttonContractedMethods;

    private Button buttonContracts;

    private Button buttonContractMethods;

    private Button buttonTL;

    private Button buttonBL;

    private Button buttonTR;

    private Button buttonBR;

    @Override
    protected Control createContents(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 40;
        comp.setLayout(gridLayout);
        Label labelDeco = new Label(comp, SWT.NONE);
        Composite compButtons = new Composite(comp, SWT.NONE);
        gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        compButtons.setLayout(gridLayout);
        buttonContractedClasses = new Button(compButtons, SWT.CHECK);
        buttonContracts = new Button(compButtons, SWT.CHECK);
        buttonContractedMethods = new Button(compButtons, SWT.CHECK);
        buttonContractMethods = new Button(compButtons, SWT.CHECK);
        Label labelPos = new Label(comp, SWT.NONE);
        GridData gridData = new GridData();
        gridData.verticalIndent = 15;
        labelPos.setLayoutData(gridData);
        Composite compPos = new Composite(comp, SWT.NONE);
        gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        compPos.setLayout(gridLayout);
        buttonTL = new Button(compPos, SWT.RADIO);
        buttonTR = new Button(compPos, SWT.RADIO);
        buttonBL = new Button(compPos, SWT.RADIO);
        buttonBR = new Button(compPos, SWT.RADIO);
        labelDeco.setText(UIMessages.PreferencesDecorations_decoHeader);
        buttonContractedClasses.setText(UIMessages.PreferencesDecorations_decoContractedClasses);
        buttonContractedMethods.setText(UIMessages.PreferencesDecorations_decoContractedMethods);
        buttonContracts.setText(UIMessages.PreferencesDecorations_decoContracts);
        buttonContractMethods.setText(UIMessages.PreferencesDecorations_decoContractMethods);
        labelPos.setText(UIMessages.PreferencesDecorations_posHeader);
        buttonTL.setText(UIMessages.PreferencesDecorations_posUL);
        buttonBL.setText(UIMessages.PreferencesDecorations_posLL);
        buttonTR.setText(UIMessages.PreferencesDecorations_posUR);
        buttonBR.setText(UIMessages.PreferencesDecorations_posLR);
        buttonContractedClasses.addSelectionListener(this);
        buttonContractedMethods.addSelectionListener(this);
        buttonContracts.addSelectionListener(this);
        buttonContractMethods.addSelectionListener(this);
        buttonTL.addSelectionListener(this);
        buttonTR.addSelectionListener(this);
        buttonBL.addSelectionListener(this);
        buttonBR.addSelectionListener(this);
        buttonContracts.setSelection(C4JPreferences.getDecorateContracts());
        buttonContractMethods.setSelection(C4JPreferences.getDecorateContractMethods());
        buttonContractedClasses.setSelection(C4JPreferences.getDecorateContractedClasses());
        buttonContractedMethods.setSelection(C4JPreferences.getDecorateContractedMethods());
        getButtonPosition(C4JPreferences.getDecorationPosition()).setSelection(true);
        return comp;
    }

    public void init(IWorkbench workbench) {
    }

    @Override
    protected void performDefaults() {
        buttonContracts.setSelection(C4JPreferences.getDefaultDecorateContracts());
        buttonContractMethods.setSelection(C4JPreferences.getDefaultDecorateContractMethods());
        buttonContractedClasses.setSelection(C4JPreferences.getDefaultDecorateContractedClasses());
        buttonContractedMethods.setSelection(C4JPreferences.getDefaultDecorateContractedMethods());
        getButtonPosition(C4JPreferences.getDefaultDecorationPosition()).setSelection(true);
        super.performDefaults();
    }

    @Override
    public boolean performOk() {
        C4JPreferences.setDecorateContracts(buttonContracts.getSelection());
        C4JPreferences.setDecorateContractMethods(buttonContractMethods.getSelection());
        C4JPreferences.setDecorateContractedClasses(buttonContractedClasses.getSelection());
        C4JPreferences.setDecorateContractedMethods(buttonContractedMethods.getSelection());
        int pos = C4JPreferences.getDecorationPosition();
        if (buttonTL.getSelection()) pos = IDecoration.TOP_LEFT; else if (buttonBL.getSelection()) pos = IDecoration.BOTTOM_LEFT; else if (buttonTR.getSelection()) pos = IDecoration.TOP_RIGHT; else if (buttonBR.getSelection()) pos = IDecoration.BOTTOM_RIGHT;
        C4JPreferences.setDecorationPosition(pos);
        if (!buttonContractedClasses.getSelection() && !buttonContractedMethods.getSelection() && !buttonContracts.getSelection() && !buttonContractMethods.getSelection()) {
            try {
                decoManager.setEnabled(C4JDecorator.ID, false);
            } catch (CoreException e) {
            }
        } else {
            ((C4JDecorator) decoManager.getBaseLabelProvider(C4JDecorator.ID)).refreshAll();
        }
        return super.performOk();
    }

    public void setVisible(boolean visible) {
        widgetSelected(null);
        super.setVisible(visible);
    }

    private Button getButtonPosition(int pos) {
        switch(pos) {
            case IDecoration.BOTTOM_LEFT:
                return buttonBL;
            case IDecoration.TOP_LEFT:
                return buttonTL;
            case IDecoration.BOTTOM_RIGHT:
                return buttonBR;
            case IDecoration.TOP_RIGHT:
                return buttonTR;
        }
        return buttonTL;
    }

    private boolean isPreferencesChanged() {
        if (buttonContractedClasses.getSelection() != C4JPreferences.getDecorateContractedClasses()) return true;
        if (buttonContractedMethods.getSelection() != C4JPreferences.getDecorateContractedMethods()) return true;
        if (buttonContracts.getSelection() != C4JPreferences.getDecorateContracts()) return true;
        if (buttonContractMethods.getSelection() != C4JPreferences.getDecorateContractMethods()) return true;
        int pos = C4JPreferences.getDecorationPosition();
        if (!getButtonPosition(pos).getSelection()) return true;
        return false;
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    public void widgetSelected(SelectionEvent e) {
        if (decoManager.getEnabled(C4JDecorator.ID)) {
            if (buttonContractedClasses.getSelection() || buttonContractedMethods.getSelection() || buttonContracts.getSelection() || buttonContractMethods.getSelection()) setMessage(null); else if (isPreferencesChanged()) {
                setMessage(UIMessages.PreferencesDecorations_infoMsg_willDisableDeco, INFORMATION);
            }
        } else if (isPreferencesChanged()) {
            setMessage(UIMessages.PreferencesDecorations_warningMsg_disabledDeco, WARNING);
        } else setMessage(null);
    }
}
