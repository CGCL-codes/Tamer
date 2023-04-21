package net.sourceforge.pmd.ui.properties;

import java.util.Collection;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.ui.PMDUiPlugin;
import net.sourceforge.pmd.ui.nls.StringKeys;
import net.sourceforge.pmd.ui.preferences.PMDPreferencePage;
import net.sourceforge.pmd.ui.preferences.RuleLabelProvider;
import net.sourceforge.pmd.ui.preferences.RuleSetContentProvider;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Property page to enable or disable PMD on a project
 * 
 * @author Philippe Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.1  2006/05/22 21:23:58  phherlin
 * Refactor the plug-in architecture to better support future evolutions
 *
 * Revision 1.19  2006/04/26 21:16:06  phherlin
 * Add the include derived files option
 *
 * Revision 1.18  2006/04/10 20:57:32  phherlin
 * Update to PMD 3.6
 *
 * Revision 1.17  2005/05/10 21:49:27  phherlin
 * Fix new violations detected by PMD 3.1
 *
 * Revision 1.16  2005/05/07 13:32:04  phherlin
 * Continuing refactoring
 * Fix some PMD violations
 * Fix Bug 1144793
 * Fix Bug 1190624 (at least try)
 *
 * Revision 1.15  2004/11/28 20:31:38  phherlin
 * Continuing the refactoring experiment
 *
 * Revision 1.14  2004/11/21 21:38:43  phherlin
 * Continue applying MVC.
 *
 * Revision 1.13  2004/11/18 23:54:27  phherlin
 * Refactoring to apply MVC.
 * The goal is to test the refactoring before a complete refactoring for all GUI
 *
 * Revision 1.12  2003/12/18 23:58:37  phherlin
 * Fixing malformed UTF-8 characters in generated xml files
 *
 * Revision 1.11  2003/11/30 22:57:44  phherlin
 * Merging from eclipse-v2 development branch
 *
 * Revision 1.9.2.6  2003/11/25 21:48:18  phherlin
 * Delete import javax.mail.Session !
 *
 * Revision 1.9.2.5  2003/11/07 14:33:57  phherlin
 * Implementing the "project ruleset" feature
 *
 * Revision 1.9.2.4  2003/11/04 16:27:19  phherlin
 * Refactor to use the adaptable framework instead of downcasting
 *
 * Revision 1.9.2.3  2003/11/04 13:26:38  phherlin
 * Implement the working set feature (working set filtering)
 *
 * Revision 1.9.2.2  2003/10/31 16:53:55  phherlin
 * Implementing lazy check feature
 *
 * Revision 1.9.2.1  2003/10/29 14:26:06  phherlin
 * Refactoring JDK 1.3 compatibility feature. Now use the compiler compliance option.
 *
 * Revision 1.9  2003/09/29 22:38:09  phherlin
 * Adding and implementing "JDK13 compatibility" property.
 *
 * Revision 1.8  2003/08/13 20:09:40  phherlin
 * Refactoring private->protected to remove warning about non accessible member access in enclosing types
 *
 * Revision 1.7  2003/07/07 19:27:52  phherlin
 * Making rules selectable from projects
 *
 * Revision 1.6  2003/07/01 20:22:16  phherlin
 * Make rules selectable from projects
 *
 * Revision 1.5  2003/06/30 20:16:06  phherlin
 * Redesigning plugin configuration
 *
 * Revision 1.4  2003/06/19 21:01:13  phherlin
 * Force a rebuild when PMD properties have changed
 *
 * Revision 1.3  2003/03/30 20:52:17  phherlin
 * Adding logging
 * Displaying error dialog in a thread safe way
 *
 */
public class PMDPropertyPage extends PropertyPage {

    private static final Logger log = Logger.getLogger(PMDPropertyPage.class);

    private PMDPropertyPageController controller;

    private PMDPropertyPageBean model;

    private Button enablePMDButton;

    protected TableViewer availableRulesTableViewer;

    private IWorkingSet selectedWorkingSet;

    private Label selectedWorkingSetLabel;

    private Button deselectWorkingSetButton;

    private Button includeDerivedFilesButton;

    protected Button ruleSetStoredInProjectButton;

    /**
     * @see PreferencePage#createContents(Composite)
     */
    protected Control createContents(final Composite parent) {
        log.info("PMD properties editing requested");
        this.controller = new PMDPropertyPageController(this.getShell());
        final IProject project = (IProject) this.getElement().getAdapter(IProject.class);
        this.controller.setProject(project);
        this.model = controller.getPropertyPageBean();
        Composite composite = null;
        noDefaultAndApplyButton();
        if ((project.isAccessible()) && (this.model != null)) {
            composite = new Composite(parent, SWT.NONE);
            final GridLayout layout = new GridLayout();
            composite.setLayout(layout);
            this.enablePMDButton = buildEnablePMDButton(composite);
            Label separator = new Label(composite, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
            GridData data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            separator.setLayoutData(data);
            this.includeDerivedFilesButton = buildIncludeDerivedFilesButton(composite);
            separator = new Label(composite, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            separator.setLayoutData(data);
            this.selectedWorkingSetLabel = buildSelectedWorkingSetLabel(composite);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            selectedWorkingSetLabel.setLayoutData(data);
            final Composite workingSetPanel = new Composite(composite, SWT.NONE);
            final RowLayout rowLayout = new RowLayout();
            rowLayout.type = SWT.HORIZONTAL;
            rowLayout.justify = true;
            rowLayout.pack = false;
            rowLayout.wrap = false;
            workingSetPanel.setLayout(rowLayout);
            buildSelectWorkingSetButton(workingSetPanel);
            this.deselectWorkingSetButton = buildDeselectWorkingSetButton(workingSetPanel);
            separator = new Label(composite, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.HORIZONTAL);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            separator.setLayoutData(data);
            buildLabel(composite, StringKeys.MSGKEY_PROPERTY_LABEL_SELECT_RULE);
            final Table availableRulesTable = buildAvailableRulesTableViewer(composite);
            data = new GridData();
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.FILL;
            data.heightHint = 50;
            availableRulesTable.setLayoutData(data);
            this.ruleSetStoredInProjectButton = buildStoreRuleSetInProjectButton(composite);
        } else {
            setValid(false);
        }
        log.debug("Property page created");
        return composite;
    }

    /**
     * Create the enable PMD checkbox
     * @param parent the parent composite
     */
    private Button buildEnablePMDButton(final Composite parent) {
        final Button button = new Button(parent, SWT.CHECK);
        button.setText(getMessage(StringKeys.MSGKEY_PROPERTY_BUTTON_ENABLE));
        button.setSelection(model.isPmdEnabled());
        return button;
    }

    /**
     * Create the include derived files checkbox
     * @param parent the parent composite
     */
    private Button buildIncludeDerivedFilesButton(final Composite parent) {
        final Button button = new Button(parent, SWT.CHECK);
        button.setText(getMessage(StringKeys.MSGKEY_PROPERTY_BUTTON_INCLUDE_DERIVED_FILES));
        button.setSelection(model.isIncludeDerivedFiles());
        return button;
    }

    /**
     * Create the checkbox for storing configuration in a project file
     * @param parent the parent composite
     */
    private Button buildStoreRuleSetInProjectButton(final Composite parent) {
        final Button button = new Button(parent, SWT.CHECK);
        button.setText(getMessage(StringKeys.MSGKEY_PROPERTY_BUTTON_STORE_RULESET_PROJECT));
        button.setSelection(model.isRuleSetStoredInProject());
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                final Table ruleTable = availableRulesTableViewer.getTable();
                ruleTable.setEnabled(!ruleSetStoredInProjectButton.getSelection());
            }
        });
        return button;
    }

    /**
     * Build a label
     */
    private Label buildLabel(final Composite parent, final String msgKey) {
        final Label label = new Label(parent, SWT.NONE);
        label.setText(msgKey == null ? "" : this.getMessage(msgKey));
        return label;
    }

    /**
     * Build a label
     */
    private Label buildSelectedWorkingSetLabel(final Composite parent) {
        this.selectedWorkingSet = model.getProjectWorkingSet();
        final Label label = new Label(parent, SWT.NONE);
        label.setText(this.selectedWorkingSet == null ? getMessage(StringKeys.MSGKEY_PROPERTY_LABEL_NO_WORKINGSET) : (getMessage(StringKeys.MSGKEY_PROPERTY_LABEL_SELECTED_WORKINGSET) + selectedWorkingSet.getName()));
        return label;
    }

    /**
     * Build rule table viewer
     */
    private Table buildAvailableRulesTableViewer(final Composite parent) {
        final int tableStyle = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK;
        availableRulesTableViewer = new TableViewer(parent, tableStyle);
        final Table ruleTable = availableRulesTableViewer.getTable();
        final TableColumn ruleNameColumn = new TableColumn(ruleTable, SWT.LEFT);
        ruleNameColumn.setResizable(true);
        ruleNameColumn.setText(getMessage(StringKeys.MSGKEY_PREF_RULESET_COLUMN_NAME));
        ruleNameColumn.setWidth(200);
        final TableColumn rulePriorityColumn = new TableColumn(ruleTable, SWT.LEFT);
        rulePriorityColumn.setResizable(true);
        rulePriorityColumn.setText(getMessage(StringKeys.MSGKEY_PREF_RULESET_COLUMN_PRIORITY));
        rulePriorityColumn.setWidth(110);
        final TableColumn ruleDescriptionColumn = new TableColumn(ruleTable, SWT.LEFT);
        ruleDescriptionColumn.setResizable(true);
        ruleDescriptionColumn.setText(getMessage(StringKeys.MSGKEY_PREF_RULESET_COLUMN_DESCRIPTION));
        ruleDescriptionColumn.setWidth(200);
        ruleTable.setLinesVisible(true);
        ruleTable.setHeaderVisible(true);
        this.availableRulesTableViewer.setContentProvider(new RuleSetContentProvider());
        this.availableRulesTableViewer.setLabelProvider(new RuleLabelProvider());
        this.availableRulesTableViewer.setColumnProperties(new String[] { PMDPreferencePage.PROPERTY_NAME, PMDPreferencePage.PROPERTY_PRIORITY, PMDPreferencePage.PROPERTY_DESCRIPTION });
        this.availableRulesTableViewer.setSorter(new ViewerSorter() {

            public int compare(Viewer viewer, Object e1, Object e2) {
                int result = 0;
                if ((e1 instanceof Rule) && (e2 instanceof Rule)) {
                    result = ((Rule) e1).getName().compareTo(((Rule) e2).getName());
                }
                return result;
            }

            public boolean isSorterProperty(Object element, String property) {
                return property.equals(PMDPreferencePage.PROPERTY_NAME);
            }
        });
        populateAvailableRulesTable();
        ruleTable.setEnabled(!model.isRuleSetStoredInProject());
        return ruleTable;
    }

    /**
     * Build the working set selection button
     * @param parent
     */
    private void buildSelectWorkingSetButton(final Composite parent) {
        final Button workingSetButton = new Button(parent, SWT.PUSH);
        workingSetButton.setText(getMessage(StringKeys.MSGKEY_PROPERTY_BUTTON_SELECT_WORKINGSET));
        workingSetButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                selectWorkingSet();
            }
        });
    }

    /**
     * Build the working set deselect button
     * @param parent
     */
    private Button buildDeselectWorkingSetButton(final Composite parent) {
        final Button button = new Button(parent, SWT.PUSH);
        button.setText(getMessage(StringKeys.MSGKEY_PROPERTY_BUTTON_DESELECT_WORKINGSET));
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                deselectWorkingSet();
            }
        });
        button.setEnabled(selectedWorkingSet != null);
        return button;
    }

    /**
     * Populate the rule table
     */
    private void populateAvailableRulesTable() {
        availableRulesTableViewer.setInput(controller.getAvailableRules());
        final RuleSet activeRuleSet = model.getProjectRuleSet();
        if (activeRuleSet != null) {
            final Collection activeRules = activeRuleSet.getRules();
            final TableItem[] itemList = availableRulesTableViewer.getTable().getItems();
            for (int i = 0; i < itemList.length; i++) {
                final Object rule = itemList[i].getData();
                if (activeRules.contains(rule)) {
                    itemList[i].setChecked(true);
                }
            }
        }
    }

    /**
     * User press OK Button
     */
    public boolean performOk() {
        log.info("Properties editing accepted");
        this.model.setPmdEnabled(this.enablePMDButton.getSelection());
        this.model.setProjectWorkingSet(this.selectedWorkingSet);
        this.model.setProjectRuleSet(this.getProjectRuleSet());
        this.model.setRuleSetStoredInProject(this.ruleSetStoredInProjectButton.getSelection());
        this.model.setIncludeDerivedFiles(this.includeDerivedFilesButton.getSelection());
        return controller.performOk();
    }

    /**
     * @see org.eclipse.jface.preference.IPreferencePage#performCancel()
     */
    public boolean performCancel() {
        log.info("Properties editing canceled");
        return super.performCancel();
    }

    /**
     * @return a RuleSet object from the selected table item
     */
    private RuleSet getProjectRuleSet() {
        final RuleSet ruleSet = new RuleSet();
        final TableItem[] rulesList = this.availableRulesTableViewer.getTable().getItems();
        for (int i = 0; i < rulesList.length; i++) {
            if (rulesList[i].getChecked()) {
                final Rule rule = (Rule) rulesList[i].getData();
                ruleSet.addRule(rule);
            }
        }
        return ruleSet;
    }

    /**
     * Help user to select a working set for PMD
     *
     */
    protected void selectWorkingSet() {
        log.info("Select working set");
        this.setSelectedWorkingSet(this.controller.selectWorkingSet(this.selectedWorkingSet));
    }

    /**
     * Help user to deselect a working set for PMD
     *
     */
    protected void deselectWorkingSet() {
        log.info("Deselect working set");
        setSelectedWorkingSet(null);
    }

    /**
     * Process the change of the selected working set
     * @param a newly selected working set
     */
    private void setSelectedWorkingSet(final IWorkingSet workingSet) {
        this.selectedWorkingSet = workingSet;
        this.selectedWorkingSetLabel.setText(this.selectedWorkingSet == null ? getMessage(StringKeys.MSGKEY_PROPERTY_LABEL_NO_WORKINGSET) : (getMessage(StringKeys.MSGKEY_PROPERTY_LABEL_SELECTED_WORKINGSET) + this.selectedWorkingSet.getName()));
        deselectWorkingSetButton.setEnabled(this.selectedWorkingSet != null);
    }

    /**
     * Helper method to shorten message access
     * @param key a message key
     * @return requested message
     */
    private String getMessage(final String key) {
        return PMDUiPlugin.getDefault().getStringTable().getString(key);
    }
}
