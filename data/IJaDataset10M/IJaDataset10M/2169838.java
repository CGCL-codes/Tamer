package com.comarch.depth.project.workingsets;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.fieldassist.FieldAssistColors;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetPage;
import com.comarch.depth.helper.ResourceHelper;
import com.comarch.depth.share.FragmentEditorInput;
import de.tu.depth.fragments.Fragment;
import de.tu.depth.fragments.Project;
import depth.IRepository;
import depth.RepositoryFactory;

/**
 * A resource working set page allows the user to edit an 
 * existing working set and create a new working set.
 * <p>
 * Working set elements are presented as a simple resource tree.
 * </p>
 * 
 * @since 2.0
 */
public class WorkingSetPage extends WizardPage implements IWorkingSetPage {

    private static final int SIZING_SELECTION_WIDGET_WIDTH = 50;

    private static final int SIZING_SELECTION_WIDGET_HEIGHT = 200;

    private Text text;

    private CheckboxTreeViewer tree;

    private IWorkingSet workingSet;

    private boolean firstCheck = false;

    /**
     * Creates a new instance of the receiver.
     */
    public WorkingSetPage() {
        super("depthWorkingSetPage", "Depth Working Set", ResourceHelper.getImageDescriptor("export_big.png"));
        setDescription("Enter a working set name and select the working set resources");
    }

    /**
     * Overrides method in WizardPage.
     * 
     * @see org.eclipse.jface.wizard.WizardPage#createControl(Composite)
     */
    public void createControl(Composite parent) {
        Font font = parent.getFont();
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        setControl(composite);
        Label label = new Label(composite, SWT.WRAP);
        label.setText("Working Set Name");
        GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        label.setLayoutData(data);
        label.setFont(font);
        text = new Text(composite, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        text.setFont(font);
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
        text.setFocus();
        text.setBackground(FieldAssistColors.getRequiredFieldBackgroundColor(text));
        label = new Label(composite, SWT.WRAP);
        label.setText("Working Set Contents");
        data = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        label.setLayoutData(data);
        label.setFont(font);
        tree = new CheckboxTreeViewer(composite);
        tree.setUseHashlookup(true);
        final ITreeContentProvider treeContentProvider = new WSContentProvider();
        tree.setContentProvider(treeContentProvider);
        tree.setLabelProvider(new WSLabelProvider());
        tree.setInput(RepositoryFactory.getRepository());
        data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
        tree.getControl().setLayoutData(data);
        tree.getControl().setFont(font);
        tree.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                handleCheckStateChange(event);
            }
        });
        tree.addTreeListener(new ITreeViewerListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
            }

            public void treeExpanded(TreeExpansionEvent event) {
            }
        });
        Composite buttonComposite = new Composite(composite, SWT.NONE);
        buttonComposite.setLayout(new GridLayout(2, false));
        buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        Button selectAllButton = new Button(buttonComposite, SWT.PUSH);
        selectAllButton.setText("Select All");
        selectAllButton.setToolTipText("Select All");
        selectAllButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent selectionEvent) {
                tree.setCheckedElements(treeContentProvider.getElements(tree.getInput()));
                validateInput();
            }
        });
        selectAllButton.setFont(font);
        setButtonLayoutData(selectAllButton);
        Button deselectAllButton = new Button(buttonComposite, SWT.PUSH);
        deselectAllButton.setText("Deselect All");
        deselectAllButton.setToolTipText("Deselect All");
        deselectAllButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent selectionEvent) {
                tree.setCheckedElements(new Object[0]);
                validateInput();
            }
        });
        deselectAllButton.setFont(font);
        setButtonLayoutData(deselectAllButton);
        initializeCheckedState();
        if (workingSet != null) {
            text.setText(workingSet.getName());
        }
        setPageComplete(false);
    }

    private void findCheckedResources(List<FragmentEditorInput> checkedResources, IRepository repository) {
        List<Project> allProjects = repository.getProjects();
        for (Project project : allProjects) {
            if (tree.getChecked(project)) {
                checkedResources.add(new FragmentEditorInput(project));
            }
        }
    }

    /**
     * Implements IWorkingSetPage.
     * 
     * @see org.eclipse.ui.dialogs.IWorkingSetPage#finish()
     */
    public void finish() {
        List<FragmentEditorInput> resources = new ArrayList<FragmentEditorInput>();
        findCheckedResources(resources, (IRepository) tree.getInput());
        if (workingSet == null) {
            IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
            workingSet = workingSetManager.createWorkingSet(getWorkingSetName(), (IAdaptable[]) resources.toArray(new IAdaptable[resources.size()]));
        } else {
            workingSet.setName(getWorkingSetName());
            workingSet.setElements((IAdaptable[]) resources.toArray(new IAdaptable[resources.size()]));
        }
    }

    /**
     * Implements IWorkingSetPage.
     * 
     * @see org.eclipse.ui.dialogs.IWorkingSetPage#getSelection()
     */
    public IWorkingSet getSelection() {
        return workingSet;
    }

    /**
     * Returns the name entered in the working set name field.
     * 
     * @return the name entered in the working set name field.
     */
    private String getWorkingSetName() {
        return text.getText();
    }

    /**
     * Called when the checked state of a tree item changes.
     * 
     * @param event the checked state change event.
     */
    private void handleCheckStateChange(final CheckStateChangedEvent event) {
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {

            public void run() {
                Fragment resource = (Fragment) event.getElement();
                tree.setGrayed(resource, false);
                validateInput();
            }
        });
    }

    /**
     * Sets the checked state of tree items based on the initial 
     * working set, if any.
     */
    private void initializeCheckedState() {
        if (workingSet == null) {
            return;
        }
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {

            public void run() {
                IAdaptable[] items = workingSet.getElements();
                for (IAdaptable adaptable : items) {
                    if (adaptable instanceof FragmentEditorInput) {
                        tree.setChecked(((FragmentEditorInput) adaptable).getFragment(), true);
                    }
                }
            }
        });
    }

    /**
     * Implements IWorkingSetPage.
     * 
     * @see org.eclipse.ui.dialogs.IWorkingSetPage#setSelection(IWorkingSet)
     */
    public void setSelection(IWorkingSet workingSet) {
        if (workingSet == null) {
            throw new IllegalArgumentException("Working set must not be null");
        }
        this.workingSet = workingSet;
        if (getShell() != null && text != null) {
            firstCheck = true;
            initializeCheckedState();
            text.setText(workingSet.getName());
        }
    }

    /**
     * Validates the working set name and the checked state of the 
     * resource tree.
     */
    private void validateInput() {
        String errorMessage = null;
        String infoMessage = null;
        String newText = text.getText();
        if (newText.equals(newText.trim()) == false) {
            errorMessage = "The name must not have a leading or trailing whitespace.";
        } else if (firstCheck) {
            firstCheck = false;
            return;
        }
        if (newText.equals("")) {
            errorMessage = "The name must not be empty.";
        }
        if (errorMessage == null && (workingSet == null || newText.equals(workingSet.getName()) == false)) {
            IWorkingSet[] workingSets = PlatformUI.getWorkbench().getWorkingSetManager().getWorkingSets();
            for (int i = 0; i < workingSets.length; i++) {
                if (newText.equals(workingSets[i].getName())) {
                    errorMessage = "A working set with the same name already exists.";
                }
            }
        }
        if (infoMessage == null && tree.getCheckedElements().length == 0) {
            infoMessage = "No project selected.";
        }
        setMessage(infoMessage, INFORMATION);
        setErrorMessage(errorMessage);
        setPageComplete(errorMessage == null);
    }
}
