package org.peertrust.modeler.policysystem.gui.control;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.gui.providers.PSModelObjectComboContentProvider;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.checks.CheckOverridingRulePoliciesNeq;

/**
 * Provide a panel for editing an overriding model object.
 * 
 * @author Patrice Congo
 */
public class PSOverriddingRuleEditControl implements PSModelObjectEditControl {

    /**
	 * to cache localy the parent composite that holds
	 * all editor controls
	 */
    private Composite parent;

    /**
	 * The top container panel for the editor controls
	 */
    private Composite top;

    /** 
	 * The ps overridding rule to edit
	 */
    private PSOverridingRule overridingRule;

    /**
	 * The field edito used to edit the overridding rule 
	 * model object label
	 */
    private StringFieldEditor labelFieldEditor;

    private ComboViewer overriderCombo;

    private ComboViewer overriddenCombo;

    /** the change comit button*/
    private Button setButton;

    /**
	 * boolean that control the creation of the set button
	 */
    private boolean doCreateSetButtion = false;

    /**
	 * Boolean that control the creation of the header
	 */
    private boolean showHeader = true;

    /**
	 * The logger for the PSPolicyOverriddingRuleEditControl Class
	 */
    private static Logger logger = Logger.getLogger(PSOverriddingRuleEditControl.class);

    /** 
	 * the selected new replacement rule model object 
	 * for the overridden rule
	 */
    private PSPolicy selectedOverridden = null;

    private PSPolicy selectedOverriddenOld = null;

    /** 
	 * the selected new replacement rule model object 
	 * for the overridder rule
	 */
    private PSPolicy selectedOverridder = null;

    private PSPolicy selectedOverridderOld = null;

    private PolicySystemRDFModel psModel = PolicySystemRDFModel.getInstance();

    public PSOverriddingRuleEditControl() {
        this(false, true);
    }

    public PSOverriddingRuleEditControl(boolean doCreateSetButtion, boolean showHeader) {
        this.doCreateSetButtion = doCreateSetButtion;
        this.showHeader = showHeader;
    }

    /**
	 * Creates the controls; use the provided composite as parent container
	 */
    public void createControl(Composite parent) {
        this.parent = parent;
        Label headerdd;
        top = new Composite(parent, SWT.LEFT);
        top.setLayout(new GridLayout());
        top.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite headerContainer = new Composite(top, SWT.LEFT);
        Composite panel = new Composite(top, SWT.LEFT);
        if (showHeader) {
            headerContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            headerContainer.setLayout(new GridLayout());
            headerdd = new Label(headerContainer, SWT.NONE);
            Label header = new Label(headerContainer, SWT.LEFT | SWT.BORDER | SWT.HORIZONTAL);
            header.setText("Edit Overridding Rule");
            header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            headerdd = new Label(headerContainer, SWT.NONE | SWT.SEPARATOR | SWT.HORIZONTAL);
            headerdd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        }
        panel.setLayoutData(new GridData(GridData.FILL_BOTH));
        labelFieldEditor = new StringFieldEditor("labelFieldEditor", "Label", panel);
        headerdd = new Label(panel, SWT.NONE);
        headerdd.setText("Overrider");
        overriderCombo = new ComboViewer(panel, SWT.FILL);
        overriderCombo.setContentProvider(new PSModelObjectComboContentProvider());
        overriderCombo.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        headerdd = new Label(panel, SWT.NONE);
        headerdd.setText("Overridden");
        overriddenCombo = new ComboViewer(panel);
        overriddenCombo.setContentProvider(new PSModelObjectComboContentProvider());
        overriddenCombo.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        headerdd = new Label(panel, SWT.NONE);
        if (doCreateSetButtion) {
            setButton = new Button(panel, SWT.NONE);
            setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            setButton.setText("set");
            setButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    saveEdit();
                }
            });
        }
    }

    /**
	 * @return the top editor control
	 */
    public Control getControl() {
        return top;
    }

    /**
	 * @return the rule which is being edited
	 */
    public PSOverridingRule getOverridingRule() {
        return overridingRule;
    }

    /**
	 * Sets a new model overriding rule to edit
	 * @param overridingRule -- a new overriding rule to edit
	 */
    public void setOverridingRule(PSOverridingRule overridingRule) {
        if (overridingRule == null) {
            return;
        }
        try {
            logger.info("new overridingRule:" + overridingRule);
            this.overridingRule = overridingRule;
            Collection policies = psModel.getPolicies();
            overriddenCombo.setInput(policies);
            overriderCombo.setInput(policies);
            PSPolicy psPol = overridingRule.getHasOverridden();
            selectedOverridden = psPol;
            selectedOverriddenOld = psPol;
            if (psPol != null) {
                overriddenCombo.setSelection(new StructuredSelection(psPol), true);
            } else {
            }
            psPol = overridingRule.getHasOverridder();
            selectedOverridder = psPol;
            selectedOverridderOld = psPol;
            if (psPol != null) {
                overriderCombo.setSelection(new StructuredSelection(psPol), true);
            } else {
            }
            labelFieldEditor.setStringValue(overridingRule.getLabel().getValue());
        } catch (Exception e) {
            logger.warn("error while setting rule", e);
        }
    }

    /**
	 * To get the label of the selected policy
	 * 
	 * @param comboViewer -- thecombo box holding the policies
	 * 
	 * @return the selected policy or null if nothing is selected
	 */
    private String getSelectedPolicylabel(ComboViewer comboViewer) {
        ISelection selection = comboViewer.getSelection();
        if (selection == null) {
            return null;
        } else if (selection instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) selection).getFirstElement();
            if (firstElement == null) {
                return null;
            } else if (firstElement instanceof PSModelObject) {
                PSModelLabel label = ((PSModelObject) firstElement).getLabel();
                if (label == null) {
                    return null;
                } else {
                    return label.getValue();
                }
            } else {
                return firstElement.toString();
            }
        } else {
            return selection.toString();
        }
    }

    /**
	 * @see org.peertrust.modeler.policysystem.gui.control.PSModelObjectEditControl#saveEdit()
	 */
    public int saveEdit() {
        try {
            if (overridingRule == null) {
                return PSModelObjectEditControl.SAVE_RESULT_FAILURE_NULL_POINTER;
            }
            String ruleLabel = labelFieldEditor.getStringValue();
            if (ruleLabel != null || !"".equals(ruleLabel)) {
                overridingRule.setLabel(ruleLabel);
            }
            IStructuredSelection sel = (IStructuredSelection) overriderCombo.getSelection();
            selectedOverridder = (PSPolicy) sel.getFirstElement();
            sel = (IStructuredSelection) overriddenCombo.getSelection();
            selectedOverridden = (PSPolicy) sel.getFirstElement();
            CheckOverridingRulePoliciesNeq check = new CheckOverridingRulePoliciesNeq(selectedOverridder, selectedOverridden);
            System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
            if (check.doCheck() == false) {
                PolicysystemPlugin.getDefault().showMessage("Overridding and overridden policies must not be equals");
                return PSModelObjectEditControl.SAVE_RESULT_FAILURE_INTEGRITY_CHECK;
            }
            if (selectedOverridden != selectedOverriddenOld) {
                if (selectedOverridden != null) {
                    overridingRule.setHasOverriden(selectedOverridden);
                } else {
                }
            }
            if (selectedOverridder != selectedOverridderOld) {
                if (selectedOverridder != null) {
                    overridingRule.setHasOverrider(selectedOverridder);
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PSModelObjectEditControl.SAVE_RESULT_OK;
    }

    /**
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
    public void doubleClick(DoubleClickEvent event) {
        ISelection sel = event.getSelection();
        if (sel == null) {
            return;
        }
        if (sel instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) sel;
            Object ele0 = ssel.getFirstElement();
            if (ele0 == null) {
                logger.warn("first selection is null");
                return;
            } else if (ele0 instanceof PSPolicy) {
                PSModelObjectEditDialog dlg = new PSModelObjectEditDialog(parent.getShell(), PSPolicy.class);
                dlg.create();
                dlg.setModelObject((PSPolicy) ele0);
                dlg.open();
            } else if (ele0 instanceof PSOverridingRule) {
                logger.warn("no impl for PSOverrindingrule");
            } else {
                logger.warn("Cannot handle this kind of selection element:" + ele0);
            }
        } else {
            logger.warn("Cannot handle this kind auf selection:" + sel);
        }
    }

    /**
	 * @see org.peertrust.modeler.policysystem.gui.control.PSModelObjectEditControl#dispose()
	 */
    public void dispose() {
    }

    /**
	 * @see org.peertrust.modeler.policysystem.gui.control.PSModelObjectEditControl#getModelObject()
	 */
    public PSModelObject getModelObject() {
        return this.getOverridingRule();
    }

    /**
	 * @see org.peertrust.modeler.policysystem.gui.control.PSModelObjectEditControl#setModelObject(org.peertrust.modeler.policysystem.model.abtract.PSModelObject)
	 */
    public void setModelObject(PSModelObject psModelObject) {
        if (psModelObject instanceof PSOverridingRule) {
            this.setOverridingRule((PSOverridingRule) psModelObject);
        }
    }
}
