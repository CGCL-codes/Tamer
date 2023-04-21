package net.sf.orcc.backends.options;

import java.util.ArrayList;
import java.util.List;
import net.sf.orcc.ui.OrccActivator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * 
 * Class that add a Browse file input into backend options.
 * 
 * @author J�r�me Gorin
 * 
 */
public class BrowseFileOption implements ModifyListener, BackendOption {

    /**
	 * Button browse connected with the option
	 */
    private Button buttonBrowse;

    /**
	 * Text connected with the interface
	 */
    private String caption;

    /**
	 * Extension of files selectable
	 */
    private String extension;

    /**
	 * Font connected with the option
	 */
    private Font font;

    /**
	 * group connected with the option
	 */
    private Group group;

    /**
	 * Label connected with the option
	 */
    private Label lbl;

    /**
	 * Name of the option
	 */
    private String option;

    /**
	 * indicate if this option is mandatory
	 */
    private boolean required;

    /**
	 * Text connected with the option
	 */
    private Text text;

    /**
	 * Value of the option
	 */
    private String value;

    /**
	 * indicate if the demanded file is located in the workspace
	 */
    private boolean workspace;

    /**
	 * BrowseFileOption constructor
	 * 
	 * @param option
	 *            Name of the option
	 * @param caption
	 *            Caption associated to input file interface
	 * @param required
	 *            Indicate if this information is mandatory
	 * @param defaultVal
	 *            Default value text of the Text
	 * @param extension
	 *            File extension for restricting selection
	 */
    public BrowseFileOption(String option, String caption, boolean required, boolean workspace, String defaultVal, String extension) {
        this.option = option;
        this.caption = caption;
        this.required = required;
        this.value = defaultVal;
        this.extension = extension;
        this.workspace = workspace;
    }

    /**
	 * Creates the interface of the BrowseFile text into the given group
	 * 
	 * @param font
	 *            Font used in the interface
	 * @param group
	 *            Group to add the input file interface
	 */
    private void createBrowseFile() {
        GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
        data.horizontalSpan = 3;
        group.setLayoutData(data);
        lbl = new Label(group, SWT.LEFT);
        lbl.setFont(font);
        lbl.setText(caption);
        data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        lbl.setLayoutData(data);
        text = new Text(group, SWT.BORDER | SWT.SINGLE);
        text.setFont(font);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        text.setLayoutData(data);
        text.setText(value);
        text.addModifyListener(this);
        buttonBrowse = new Button(group, SWT.PUSH);
        buttonBrowse.setFont(font);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false);
        buttonBrowse.setLayoutData(data);
        buttonBrowse.setText("&Browse...");
        buttonBrowse.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (workspace) {
                    editWorkspace(group.getShell(), text);
                } else {
                    editExternalPath(group.getShell(), text);
                }
            }
        });
    }

    /**
	 * Dispose option elements
	 */
    @Override
    public void dispose() {
        if (text != null) {
            text.dispose();
            buttonBrowse.dispose();
            lbl.dispose();
        }
    }

    /**
	 * Creates the FileDialog of the browseFiles button
	 * 
	 * @param shell
	 *            Instance of the windows manager
	 * @param text
	 *            Text of input file interface
	 */
    private void editExternalPath(Shell shell, Text text) {
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
        if (extension != null) {
            fileDialog.setFilterExtensions(new String[] { extension });
        }
        text.setText(fileDialog.open());
    }

    /**
	 * Creates the ElementTreeSelectionDialog of the browseFiles button
	 * 
	 * @param shell
	 *            Instance of the windows manager
	 * @param text
	 *            Text of input file interface
	 */
    private void editWorkspace(Shell shell, Text text) {
        ElementTreeSelectionDialog tree = new ElementTreeSelectionDialog(shell, WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(), new WorkbenchContentProvider());
        tree.setAllowMultiple(false);
        tree.setInput(ResourcesPlugin.getWorkspace().getRoot());
        IFile file = getFileFromText(text);
        if (file != null) {
            tree.setInitialSelection(file);
        }
        tree.setMessage("Please select an existing file:");
        tree.setTitle("Choose an existing file");
        tree.setValidator(new ISelectionStatusValidator() {

            @Override
            public IStatus validate(Object[] selection) {
                if (selection.length == 1) {
                    if (selection[0] instanceof IFile) {
                        IFile file = (IFile) selection[0];
                        if (extension != null) {
                            if (file.getFileExtension().equals(extension)) {
                                return new Status(IStatus.OK, OrccActivator.PLUGIN_ID, "");
                            } else {
                                return new Status(IStatus.ERROR, OrccActivator.PLUGIN_ID, "Selected file must be an " + extension + " file.");
                            }
                        } else {
                            return new Status(IStatus.OK, OrccActivator.PLUGIN_ID, "");
                        }
                    }
                }
                return new Status(IStatus.ERROR, OrccActivator.PLUGIN_ID, "Only files can be selected, not folders nor projects");
            }
        });
        if (tree.open() == Window.OK) {
            file = (IFile) tree.getFirstResult();
            text.setText(file.getLocation().toOSString());
        }
    }

    /**
	 * Returns an IFile instance of the focused file in text
	 * 
	 * @param text
	 *            Text containing the file
	 * 
	 * @return an IFile instance of focused file
	 */
    private IFile getFileFromText(Text text) {
        String value = text.getText();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IFile file = root.getFileForLocation(new Path(value));
        return file;
    }

    /**
	 * Returns the option name
	 * 
	 * @return a String containing the option name
	 */
    public String[] getOption() {
        List<String> options = new ArrayList<String>();
        options.add(option);
        return options.toArray(new String[] {});
    }

    /**
	 * Returns the value of the option
	 * 
	 * @return a String containing the value
	 */
    public String[] getValue() {
        List<String> values = new ArrayList<String>();
        values.add(value);
        return values.toArray(new String[] {});
    }

    /**
	 * Tests if the option is valid
	 * 
	 * @return a boolean representing the validation of the option
	 */
    public boolean isValid() {
        if (required) {
            return !value.equals("");
        }
        return true;
    }

    /**
	 * Modify listener on events of text
	 * 
	 * @param e
	 *            a ModifyEvent containing event from the text
	 * 
	 */
    @Override
    public void modifyText(ModifyEvent e) {
        if (workspace) {
            IFile file = getFileFromText(text);
            if (!file.toString().equals("")) {
                value = text.getText();
            }
        } else {
            value = text.getText();
        }
        group.redraw();
    }

    /**
	 * Apply option to the specificied ILaunchConfigurationWorkingCopy * @param
	 * configuration ILaunchConfigurationWorkingCopy of configuration tab
	 */
    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    }

    @Override
    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * Show the interface on the selected group
	 * 
	 * @param font
	 *            Font used in the interface
	 * @param group
	 *            Group to add the input file interface
	 */
    @Override
    public void show(Font font, Group group) {
        this.font = font;
        this.group = group;
        createBrowseFile();
    }
}
