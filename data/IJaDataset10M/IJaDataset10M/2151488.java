package org.gello.client.editors;

import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.gello.client.Application;
import org.gello.client.actions.fileEditor.CheckinFileEditorAction;
import org.gello.client.actions.fileEditor.CheckoutFileEditorAction;
import org.gello.client.actions.fileEditor.CompileFileEditorAction;
import org.gello.client.actions.fileEditor.SaveFileEditorAction;
import org.gello.client.actions.fileEditor.TestFileEditorAction;
import org.gello.client.actions.fileEditor.VersionInfoEditorAction;

public class FileEditor extends EditorPart {

    public static final String ID = "org.gello.client.editors.FileEditor";

    private Composite top = null;

    private Text fileData = null;

    private IToolBarManager toolBarManager;

    /**
	 * Save this editor's content.
	 * 
	 */
    @Override
    public void doSave(IProgressMonitor monitor) {
        ((SaveFileEditorAction) Application.getAction(SaveFileEditorAction.ID)).runOnEditor(this);
    }

    /**
	 * Not available.
	 * 
	 */
    @Override
    public void doSaveAs() {
    }

    /**
	 * Set the current set and input.  Also sets the part name to the file name.
	 * 
	 */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        this.setPartName(getFileName());
    }

    /**
	 * Returns if the editor input for the editor is editable.
	 * @return True if editable, false if not.
	 */
    private boolean isEditable() {
        return ((FileEditorInput) getEditorInput()).isEditable();
    }

    /**
	 * Gets a list of editor actions available from the GelloNode.
	 * @return A list of editor action ID's.
	 */
    private List<String> getEditorActions() {
        return ((FileEditorInput) getEditorInput()).getGelloNode().getEditorActions();
    }

    /**
	 * Returns whether an editor could possibly be editable (if someone
	 * else doesn't if checked out).
	 * @return True if potentially editable, false if not.
	 */
    private boolean isPotentiallyEditable() {
        return ((FileEditorInput) getEditorInput()).isPotentiallyEditable();
    }

    /**
	 * Get the name of the file being edited.
	 * @return String fileName.
	 */
    private String getFileName() {
        return ((FileEditorInput) getEditorInput()).getName();
    }

    /**
	 * Get the data of the file being edited.
	 * @return String fileData.
	 */
    private String getFileData() {
        return ((FileEditorInput) getEditorInput()).getData();
    }

    /**
	 * States whether or not the file has been edited 
	 * (not equal to the file data in the input).
	 * 
	 */
    @Override
    public boolean isDirty() {
        return (!getFileData().equals(fileData.getText()));
    }

    /**
	 * Not available.
	 * 
	 */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
	 * Creates the editor control.  It contains a toolbar with actions
	 * and an text editor.
	 * 
	 */
    @Override
    public void createPartControl(Composite parent) {
        top = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 2;
        gridLayout.marginWidth = 0;
        top.setLayout(gridLayout);
        GridData toolbarGrid = new GridData();
        toolbarGrid.horizontalAlignment = GridData.FILL;
        toolbarGrid.grabExcessHorizontalSpace = true;
        toolbarGrid.verticalAlignment = GridData.FILL;
        toolbarGrid.horizontalIndent = 2;
        toolbarGrid.verticalIndent = 2;
        ToolBar toolBar = new ToolBar(top, SWT.NONE);
        toolBar.setLayoutData(toolbarGrid);
        toolBarManager = new ToolBarManager(toolBar);
        ActionContributionItem checkoutFile = new ActionContributionItem(Application.getAction(CheckoutFileEditorAction.ID));
        checkoutFile.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        ActionContributionItem checkinFile = new ActionContributionItem(Application.getAction(CheckinFileEditorAction.ID));
        checkinFile.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        ActionContributionItem saveFile = new ActionContributionItem(Application.getAction(SaveFileEditorAction.ID));
        saveFile.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        ActionContributionItem compileFile = new ActionContributionItem(Application.getAction(CompileFileEditorAction.ID));
        compileFile.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        ActionContributionItem testFile = new ActionContributionItem(Application.getAction(TestFileEditorAction.ID));
        testFile.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        ActionContributionItem versionInfo = new ActionContributionItem(Application.getAction(VersionInfoEditorAction.ID));
        versionInfo.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        toolBarManager.add(checkoutFile);
        toolBarManager.add(checkinFile);
        toolBarManager.add(saveFile);
        toolBarManager.add(compileFile);
        toolBarManager.add(testFile);
        toolBarManager.add(versionInfo);
        toolBarManager.update(true);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        fileData = new Text(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        fileData.setLayoutData(gridData);
        fileData.setText(getFileData());
        fileData.setEditable(isEditable());
        fileData.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                firePropertyChange(IEditorPart.PROP_DIRTY);
                Application.getAction(SaveFileEditorAction.ID).setEnabled(isEditable() && isDirty());
            }
        });
    }

    /**
	 * Set the focus to the editor and refresh it.
	 * 
	 */
    @Override
    public void setFocus() {
        if (fileData != null && !fileData.isDisposed()) fileData.setFocus();
        refreshEditor();
    }

    /**
	 * Removes all toolbar items and adds the correct ones.
	 * Sets file data to its editable or not editable state.
	 * Enables or disables the toolbar items.
	 *
	 */
    public void refreshEditor() {
        toolBarManager.removeAll();
        for (int i = 0; i < getEditorActions().size(); i++) {
            ActionContributionItem item = new ActionContributionItem(Application.getAction(getEditorActions().get(i)));
            item.setMode(ActionContributionItem.MODE_FORCE_TEXT);
            toolBarManager.add(item);
        }
        toolBarManager.update(true);
        if (isEditable()) {
            fileData.setEditable(true);
            fileData.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            fileData.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        } else {
            fileData.setEditable(false);
            fileData.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            fileData.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        }
        firePropertyChange(IEditorPart.PROP_DIRTY);
        Application.getAction(CheckinFileEditorAction.ID).setEnabled(isEditable());
        Application.getAction(SaveFileEditorAction.ID).setEnabled(isEditable() && isDirty());
        Application.getAction(CheckoutFileEditorAction.ID).setEnabled(!isEditable() && isPotentiallyEditable());
    }

    /**
	 * Gets the file data.
	 * @return The input's fileData.
	 */
    public String getData() {
        return fileData.getText();
    }
}
