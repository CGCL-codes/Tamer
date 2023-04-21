package com.javapathfinder.vjp.config;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import com.javapathfinder.vjp.VJP;
import com.javapathfinder.vjp.config.editors.ModePropertyEditorComposite;
import com.javapathfinder.vjp.config.editors.ModePropertyFileDialog;
import com.javapathfinder.vjp.config.tree.ModePropertyTree;
import com.javapathfinder.vjp.config.tree.TreeProject;
import com.javapathfinder.vjp.verify.VerifyJob;

/**
 * This is the main Configuration that VJP creates for the user. It lists all
 * of the Mode Property Confguration files found along with an editor to 
 * modify them.
 * 
 * @author Sandro Badame
 */
public class LaunchDialog extends TitleAreaDialog implements SelectionListener {

    private static final ImageRegistry imageRegistry = new ImageRegistry();

    private static final String titleImagePath = "images/vjptitle.jpg";

    static {
        imageRegistry.put(titleImagePath, ImageDescriptor.createFromURL(VJP.getResourceURL(titleImagePath)));
    }

    private static final Point DEFAULT_INITIAL_SIZE = new Point(1000, 620);

    private static final int[] DEFAULT_SASH_WEIGHTS = new int[] { 2, 7 };

    private ModePropertyTree fileTree;

    private ModePropertyEditorComposite editor;

    private ConfigFileBar fileBar;

    private Composite infoPanel;

    private Button verifyRun;

    private Button verifyStep;

    private Button close;

    /**
   * Constructs this dialog with this shell specified.
   * @param parentShell the parent shell for this dialog
   */
    public LaunchDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /**
   * Creates the contents of the dialog
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
    protected Control createDialogArea(Composite parent) {
        Composite dialogComp = (Composite) super.createDialogArea(parent);
        setDialogTitleInfo();
        SashForm sash = new SashForm(dialogComp, SWT.HORIZONTAL);
        GridData gd = new GridData(GridData.FILL_BOTH);
        sash.setLayoutData(gd);
        createFileTreeArea(sash);
        infoPanel = new Composite(sash, SWT.BORDER);
        infoPanel.setLayout(new FillLayout());
        sash.setWeights(DEFAULT_SASH_WEIGHTS);
        return dialogComp;
    }

    private Composite createFileTreeArea(Composite parent) {
        Composite top = new Composite(parent, SWT.NULL);
        top.setLayout(new FormLayout());
        fileBar = new ConfigFileBar(top, SWT.NULL);
        Composite tree = createModePropertyTree(top);
        fileBar.getNewFileButton().addSelectionListener(this);
        fileBar.getDeleteFileButton().addSelectionListener(this);
        FormData layoutData = new FormData();
        layoutData.top = new FormAttachment(0, 0);
        layoutData.left = new FormAttachment(0, 5);
        layoutData.right = new FormAttachment(100, -5);
        fileBar.setLayoutData(layoutData);
        layoutData = new FormData();
        layoutData.top = new FormAttachment(fileBar, 3);
        layoutData.left = new FormAttachment(0, 5);
        layoutData.right = new FormAttachment(100, -5);
        layoutData.bottom = new FormAttachment(100, 5);
        tree.setLayoutData(layoutData);
        return top;
    }

    private Composite createModePropertyTree(Composite parent) {
        Composite top = new Composite(parent, SWT.BORDER);
        top.setLayout(new FillLayout());
        fileTree = new ModePropertyTree(top);
        fileTree.addSelectionChangedListener(new ModePropertyTreeListener());
        return top;
    }

    private void clearInfoPanel() {
        for (Control child : infoPanel.getChildren()) child.dispose();
    }

    /**
   * @return the panel that displays informaton about the currently selected
   *         tree item.
   */
    public Composite getInfoPanel() {
        return infoPanel;
    }

    private void setDialogTitleInfo() {
        getShell().setText("Verify");
        setTitleImage(imageRegistry.get(titleImagePath));
        setTitle("Create, manage and verify configurations");
        setMessage("Verify a Java application");
    }

    /**
   * Creates the Verify and Cancel buttons for the button bar
   * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
   */
    public void createButtonsForButtonBar(Composite parent) {
        parent.setLayout(new FormLayout());
        verifyRun = new Button(parent, SWT.NULL);
        verifyRun.setText("Run Verify");
        verifyRun.addSelectionListener(this);
        verifyRun.setToolTipText("Completely runs through the verification of this program");
        verifyStep = new Button(parent, SWT.NULL);
        verifyStep.setText("Step Verify");
        verifyStep.addSelectionListener(this);
        verifyStep.setToolTipText("Step through the verification of this program");
        close = new Button(parent, SWT.NULL);
        close.setText("Close");
        FormData formData = new FormData();
        formData.right = new FormAttachment(close, -10);
        formData.bottom = new FormAttachment(100, -10);
        verifyRun.setLayoutData(formData);
        formData = new FormData();
        formData.right = new FormAttachment(verifyRun, -10);
        formData.bottom = new FormAttachment(100, -10);
        verifyStep.setLayoutData(formData);
        formData = new FormData();
        formData.right = new FormAttachment(100, -10);
        formData.bottom = new FormAttachment(100, -10);
        close.setLayoutData(formData);
        close.addSelectionListener(this);
    }

    /**
   * Called when a button is selected.
   * (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
    public void widgetSelected(SelectionEvent e) {
        if (e.widget.equals(verifyRun) || e.widget.equals(verifyStep)) handleVerify(e.widget.equals(verifyStep)); else if (e.widget.equals(close)) cancelPressed(); else if (e.widget.equals(fileBar.getNewFileButton())) handleNewFile(); else if (e.widget.equals(fileBar.getDeleteFileButton())) handleDeleteFile();
    }

    protected Point getInitialSize() {
        return DEFAULT_INITIAL_SIZE;
    }

    private void refreshInfoPanel() {
        getInfoPanel().layout(true);
        getInfoPanel().redraw();
    }

    private class ModePropertyTreeListener implements ISelectionChangedListener {

        public void selectionChanged(SelectionChangedEvent event) {
            Object selection = ((TreeSelection) event.getSelection()).getFirstElement();
            clearInfoPanel();
            if (selection instanceof IFile) {
                TreePath tp = (TreePath) ((TreeSelection) event.getSelection()).getPathsFor(selection)[0].getParentPath();
                TreeProject treeProject = (TreeProject) (tp.getLastSegment());
                editor = new ModePropertyEditorComposite(getInfoPanel(), treeProject.java_project, (IFile) selection);
            } else {
                editor = null;
                new VJPInfoComposite(getInfoPanel(), SWT.NULL);
            }
            refreshInfoPanel();
        }
    }

    /**
   * Handles the verify action. Checks whether is can be done or not.
   */
    private void handleVerify(boolean step) {
        if (!shouldVerify()) return;
        TreeSelection s = ((TreeSelection) (fileTree.getSelection()));
        TreeProject p = (TreeProject) s.getPaths()[0].getFirstSegment();
        Object o = s.getFirstElement();
        if (!(o instanceof IFile)) return;
        VerifyJob.verify((IFile) o, p.java_project, step);
        close();
    }

    private boolean shouldVerify() {
        if (VerifyJob.isRunning()) {
            new MessageDialog(getShell(), "Program being verified.", null, "Only one program can be verified at a time.", MessageDialog.ERROR, new String[] { "OK" }, 0).open();
            return false;
        }
        if (!editor.isDirty()) return true;
        MessageDialog dialog = new MessageDialog(getShell(), "Save Changes?", null, "Before the program can be verified changes made to the configuration must first be saved.", MessageDialog.ERROR, new String[] { "&Save New Properties and Verify", "&Revert to Old Properties and Verify", "&Cancel" }, 2);
        int option = dialog.open();
        if (option == 0) editor.saveProperties(); else if (option == 1) editor.revertProperties(); else return false;
        return true;
    }

    private void handleNewFile() {
        ModePropertyFileDialog dialog = new ModePropertyFileDialog(getShell());
        IFile file = dialog.getFile();
        if (file == null) return;
        IProject project = dialog.getFileProject();
        if (project == null) {
            new MessageDialog(getShell(), "Invalid Mode Property File location", null, "Mode Property Files can only be stored within a project.", MessageDialog.ERROR, new String[] { "OK" }, 0).open();
            return;
        }
        if (file.exists()) {
            new MessageDialog(getShell(), "File already exists.", null, "This file location chosen already exists.", MessageDialog.ERROR, new String[] { "OK" }, 0).open();
            return;
        }
        try {
            file.getLocation().toFile().getParentFile().mkdirs();
            file.getLocation().toFile().createNewFile();
            file.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (Exception e) {
            VJP.logError("File could not be created or refreshed", e);
        }
        updateTree();
    }

    private void handleDeleteFile() {
        Object selection = ((TreeSelection) fileTree.getSelection()).getFirstElement();
        if (!(selection instanceof IFile)) return;
        IFile file = (IFile) selection;
        int option = new MessageDialog(getShell(), "Delete " + file.getName() + "?", null, "Are you sure you want to delete " + "the configuration file: " + file.getName() + "?", MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1).open();
        if (option != 0) return;
        try {
            file.delete(true, true, null);
        } catch (CoreException e) {
            VJP.logError("Could not delete file.", e);
        }
        updateTree();
    }

    /**
   * Rebuilds the updateTree.
   */
    public void updateTree() {
        fileTree.refresh();
    }

    /**
   * @return the file tree that displays all of the configuration files
   *         in the workspace. 
   */
    public ModePropertyTree getTree() {
        return fileTree;
    }

    public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
    }
}
