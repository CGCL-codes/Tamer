package ag.ion.bion.workbench.office.editor.ui.wizards.document;

import ag.ion.noa4e.ui.FormBorderPainter;
import ag.ion.noa4e.ui.NOAUIPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import java.io.File;

/**
 * Wizard page in order to define the location and name
 * for a new OpenOffice.org document.
 * 
 * @author Andreas Br�ker
 * @version $Revision$
 */
public class NewDocumentWizardDefinePage extends WizardPage implements IWizardPage {

    /** Name of the page. */
    public static final String PAGE_NAME = "ag.ion.bion.workbench.office.editor.ui.wizards.document.NewDocumentWizardDefinePage";

    private IStructuredSelection structuredSelection = null;

    private Text textFolder = null;

    private Text textName = null;

    private Text textTemplatePath = null;

    private Label labelTemplatePath = null;

    private Button buttonBrowseTemplatePath = null;

    private Button buttonUseTemplate = null;

    private TreeViewer treeViewer = null;

    private IContainer targetContainer = null;

    private String[] validTemplateExtensions = null;

    private String documentFileExtension = null;

    private String templateFileExtension = null;

    private String currentTemplatePath = null;

    private Button buttonConstructTemplate = null;

    private boolean isWorkspaceValid = true;

    private boolean supportTemplates = true;

    /**
   * Constructs new NewDocumentWizardDefinePage.
   * 
   * @param structuredSelection structured selection to be used
   * 
   * @author Andreas Br�ker
   */
    protected NewDocumentWizardDefinePage(IStructuredSelection structuredSelection) {
        super(PAGE_NAME);
        this.structuredSelection = structuredSelection;
    }

    /**
   * Sets document file extension.
   * 
   * @param documentFileExtension document file extension to be used
   * 
   * @author Andreas Br�ker
   */
    public void setDocumentFileExtension(String documentFileExtension) {
        this.documentFileExtension = documentFileExtension;
    }

    /**
   * Sets template file extension to be used.
   * 
   * @param templateFilExtension template file extension to be used
   * 
   * @author Andreas Br�ker
   */
    public void setTemplateFileExtension(String templateFilExtension) {
        this.templateFileExtension = templateFilExtension;
    }

    /**
   * Sets valid template extensions.
   * 
   * @param validTemplateExtensions valid template extensions
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    public void setValidTemplateExtensions(String[] validTemplateExtensions) {
        this.validTemplateExtensions = validTemplateExtensions;
    }

    /**
   * Sets information whether the wizard supports templates. The default
   * valus is <code>true</code>.
   * 
   * @param supportTemplates information whether the wizard supports templates
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    public void setSupportTemplates(boolean supportTemplates) {
        this.supportTemplates = supportTemplates;
    }

    /**
   * Returns name for the new document (with extension). Returns null
   * if a name for the new document is not available.
   * 
   * @return name for the new document (with extension) or null
   * if a name for the new document is not available
   * 
   * @author Andreas Br�ker
   */
    public String getDocumentName() {
        String documentName = textName.getText();
        boolean useDocumentFileExtension = false;
        if (templateFileExtension != null) {
            if (!buttonConstructTemplate.getSelection()) {
                useDocumentFileExtension = true;
            } else {
                if (templateFileExtension != null) {
                    if (!documentName.endsWith("." + templateFileExtension)) documentName += "." + templateFileExtension;
                }
            }
        } else useDocumentFileExtension = true;
        if (useDocumentFileExtension) {
            if (documentFileExtension != null) {
                if (!documentName.endsWith("." + documentFileExtension)) documentName += "." + documentFileExtension;
            }
        }
        return documentName;
    }

    /**
   * Returns container for the new document. Returns null
   * if a container for a new document is not available.
   * 
   * @return container for the new document or null
   * if a container for a new document is not available
   * 
   * @author Andreas Br�ker
   */
    public IContainer getDocumentContainer() {
        return targetContainer;
    }

    /**
   * Returns path of the template. Returns null
   * if a template path is not available.
   * 
   * @return path of the template or null
   * if a template path is not available
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    public String getTemplatePath() {
        if (useTemplate()) {
            if (currentTemplatePath.length() == 0) return null; else return currentTemplatePath;
        }
        return null;
    }

    /**
   * Sets the visibility of this dialog page.
   *
   * @param visible <code>true</code> to make this page visible,
   *  and <code>false</code> to hide it
   *  
   * @author Andreas Br�ker
   */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            if (textFolder.getText().length() != 0) textName.setFocus(); else textFolder.setFocus();
            if (supportTemplates) initializeTemplatePath(structuredSelection);
        }
    }

    /**
   * Creates the top level control for this dialog
   * page under the given parent composite.
   *
   * @param parent the parent composite
   * 
   * @author Andreas Br�ker
   */
    public void createControl(Composite parent) {
        FormToolkit formToolkit = NOAUIPlugin.getFormToolkit();
        ScrolledForm scrolledForm = formToolkit.createScrolledForm(parent);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = IDialogConstants.VERTICAL_MARGIN;
        scrolledForm.getBody().setLayout(gridLayout);
        scrolledForm.getBody().setLayoutData(gridData);
        scrolledForm.getBody().setBackground(parent.getBackground());
        constructNameLocationSection(scrolledForm, formToolkit);
        setControl(scrolledForm);
        setPageComplete(false);
        initialize(structuredSelection);
    }

    /**
   * Constructs name and location section.
   * 
   * @param scrolledForm scrolled form to be used
   * @param formToolkit form toolkit to be used
   * 
   * @author Andreas Br�ker
   */
    private void constructNameLocationSection(ScrolledForm scrolledForm, FormToolkit formToolkit) {
        Section section = formToolkit.createSection(scrolledForm.getBody(), Section.DESCRIPTION | ExpandableComposite.CLIENT_INDENT);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        section.setText(Messages.NewDocumentWizardDefinePage_section_name_location_text);
        section.setDescription(Messages.NewDocumentWizardDefinePage_section_name_location_description);
        section.setLayoutData(gridData);
        section.setBackground(scrolledForm.getBody().getBackground());
        formToolkit.createCompositeSeparator(section);
        Composite client = formToolkit.createComposite(section);
        client.setBackground(section.getBackground());
        GridLayout layout = new GridLayout();
        client.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        Label folderLabel = formToolkit.createLabel(client, Messages.NewDocumentWizardDefinePage_label_folder_text);
        folderLabel.setBackground(client.getBackground());
        textFolder = formToolkit.createText(client, "");
        gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
        gridData.verticalAlignment = SWT.CENTER;
        gridData.widthHint = convertWidthInCharsToPixels(180);
        textFolder.setLayoutData(gridData);
        textFolder.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent modifyEvent) {
                checkPageState();
                IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(textFolder.getText()));
                if (resource instanceof IContainer) targetContainer = (IContainer) resource;
            }
        });
        Button browseLink = new Button(client, SWT.FLAT);
        browseLink.setText(Messages.NewDocumentWizardDefinePage_link_browse_text);
        browseLink.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent selectionEvent) {
                ContainerSelectionDialog containerSelectionDialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false, Messages.NewDocumentWizardDefinePage_dialog_container_message);
                if (containerSelectionDialog.open() == Window.OK) {
                    Object[] objects = containerSelectionDialog.getResult();
                    if (objects.length > 0) {
                        Object object = objects[0];
                        if (object instanceof IPath) {
                            textFolder.setText(((IPath) object).toString());
                        }
                    }
                }
            }
        });
        Label nameLabel = formToolkit.createLabel(client, Messages.NewDocumentWizardDefinePage_label_name_text);
        nameLabel.setBackground(client.getBackground());
        textName = formToolkit.createText(client, "");
        gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
        gridData.verticalAlignment = SWT.CENTER;
        gridData.widthHint = convertWidthInCharsToPixels(180);
        textName.setLayoutData(gridData);
        textName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent modifyEvent) {
                checkPageState();
            }
        });
        if (templateFileExtension != null) {
            buttonConstructTemplate = formToolkit.createButton(client, Messages.NewDocumentWizardDefinePage_button_as_template_text, SWT.CHECK);
            buttonConstructTemplate.setBackground(client.getBackground());
            gridData = new GridData();
            gridData.horizontalSpan = 3;
            buttonConstructTemplate.setLayoutData(gridData);
            buttonConstructTemplate.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent selectionEvent) {
                    if (textName.getText().length() != 0 || textFolder.getText().length() != 0) checkPageState();
                }
            });
        }
        if (supportTemplates) {
            Label separator = formToolkit.createSeparator(client, SWT.HORIZONTAL);
            gridData = new GridData(SWT.FILL, SWT.NONE, false, false);
            gridData.horizontalSpan = 3;
            separator.setLayoutData(gridData);
            buttonUseTemplate = formToolkit.createButton(client, Messages.NewDocumentWizardDefinePage_button_use_template_text, SWT.CHECK);
            buttonUseTemplate.setBackground(client.getBackground());
            gridData = new GridData();
            gridData.horizontalSpan = 3;
            buttonUseTemplate.setLayoutData(gridData);
            buttonUseTemplate.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent selectionEvent) {
                    buttonBrowseTemplatePath.setEnabled(buttonUseTemplate.getSelection());
                    labelTemplatePath.setEnabled(buttonUseTemplate.getSelection());
                    textTemplatePath.setEnabled(buttonUseTemplate.getSelection());
                    treeViewer.getTree().setEnabled(buttonUseTemplate.getSelection());
                    checkPageState();
                }
            });
            labelTemplatePath = formToolkit.createLabel(client, Messages.NewDocumentWizardDefinePage_label_template_path_text);
            labelTemplatePath.setBackground(client.getBackground());
            labelTemplatePath.setEnabled(false);
            textTemplatePath = formToolkit.createText(client, "");
            gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
            gridData.verticalAlignment = SWT.CENTER;
            gridData.widthHint = convertWidthInCharsToPixels(180);
            textTemplatePath.setLayoutData(gridData);
            textTemplatePath.setEnabled(false);
            textTemplatePath.setEditable(false);
            textTemplatePath.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent modifyEvent) {
                    checkPageState();
                }
            });
            buttonBrowseTemplatePath = new Button(client, SWT.FLAT);
            buttonBrowseTemplatePath.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
            buttonBrowseTemplatePath.setText(Messages.NewDocumentWizardDefinePage_link_browse_text);
            buttonBrowseTemplatePath.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent selectionEvent) {
                    FileDialog fileDialog = new FileDialog(getShell());
                    if (validTemplateExtensions != null) fileDialog.setFilterExtensions(toFilterExtension(validTemplateExtensions));
                    String templatePath = fileDialog.open();
                    if (templatePath != null) {
                        setTemplatePath(templatePath);
                    }
                }
            });
            buildWorkspaceTemplatesViewer(client);
        }
        FormBorderPainter.paintBordersFor(client);
        section.setClient(client);
    }

    /**
   * Initializes the page.
   * 
   * @param structuredSelection structured selection to be used
   * 
   * @author Andreas Br�ker
   */
    private void initialize(IStructuredSelection structuredSelection) {
        if (structuredSelection != null) {
            if (structuredSelection.size() > 1) return;
            Object object = structuredSelection.getFirstElement();
            if (object instanceof IResource) {
                IContainer container = null;
                if (object instanceof IContainer) container = (IContainer) object; else container = ((IResource) object).getParent();
                textFolder.setText(container.getFullPath().toOSString());
                targetContainer = container;
            }
        }
    }

    /**
   * Checks page state.
   * 
   * @author Andreas Br�ker
   */
    private void checkPageState() {
        String message = checkTemplatePath();
        if (message != null) {
            setMessage(message, ERROR);
            setPageComplete(false);
            return;
        }
        if (isWorkspaceValid) {
            String containerPath = textFolder.getText();
            String documentName = textName.getText();
            if (containerPath.length() == 0) {
                setMessage(Messages.NewDocumentWizardDefinePage_message_error_define_folder, ERROR);
                setPageComplete(false);
                return;
            } else {
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                IResource resource = root.findMember(new Path(containerPath));
                if (resource == null) {
                    setMessage(Messages.NewDocumentWizardDefinePage_message_error_folder_not_available, ERROR);
                    setPageComplete(false);
                    return;
                } else if (resource instanceof IWorkspaceRoot) {
                    setMessage(Messages.NewDocumentWizardDefinePage_message_error_folder_not_available, ERROR);
                    setPageComplete(false);
                    return;
                } else {
                    if (!(resource instanceof IContainer)) {
                        setMessage(Messages.NewDocumentWizardDefinePage_message_error_folder_not_available, ERROR);
                        setPageComplete(false);
                        return;
                    }
                }
            }
            if (documentName.length() == 0) {
                setMessage(null);
                setPageComplete(false);
                return;
            } else {
                char[] chars = documentName.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] == '\\' || chars[i] == '/' || chars[i] == ':' || chars[i] == '*' || chars[i] == '?' || chars[i] == '"' || chars[i] == '<' || chars[i] == '>' || chars[i] == '|') {
                        setMessage(NLS.bind(Messages.NewDocumentWizardDefinePage_message_error_character_invalid, new Character(chars[i])), ERROR);
                        setPageComplete(false);
                        return;
                    }
                }
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                IResource resource = root.findMember(new Path(containerPath));
                boolean useDocumentFileExtension = false;
                if (templateFileExtension != null) {
                    if (!buttonConstructTemplate.getSelection()) {
                        useDocumentFileExtension = true;
                    } else {
                        if (templateFileExtension != null) {
                            if (!documentName.endsWith("." + templateFileExtension)) documentName += "." + templateFileExtension;
                        }
                    }
                } else useDocumentFileExtension = true;
                if (useDocumentFileExtension) {
                    if (documentFileExtension != null) {
                        if (!documentName.endsWith("." + documentFileExtension)) documentName += "." + documentFileExtension;
                    }
                }
                File file = new File(resource.getLocation().toOSString() + File.separator + documentName);
                if (file.exists()) {
                    setMessage(Messages.NewDocumentWizardDefinePage_message_error_name_already_available, ERROR);
                    setPageComplete(false);
                    return;
                }
            }
            setMessage(null);
            setPageComplete(true);
        }
    }

    /**
   * Checks the template path.
   * 
   * @return returns message if the template path is not valid or null
   * if the template path is correct
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    private String checkTemplatePath() {
        if (!useTemplate()) return null;
        if (currentTemplatePath == null || currentTemplatePath.length() == 0) return null; else {
            File file = new File(currentTemplatePath);
            if (file.canRead()) {
                if (validTemplateExtensions != null) {
                    int index = currentTemplatePath.lastIndexOf(".");
                    if (index != -1 && currentTemplatePath.length() > index + 2) {
                        String extension = currentTemplatePath.substring(index + 1);
                        if (checkTemplateExtension(extension)) return null;
                    }
                    return Messages.NewDocumentWizardDefinePage_error_message_template_extension_invalid;
                }
                return null;
            } else return Messages.NewDocumentWizardDefinePage_error_message_template_read;
        }
    }

    /**
   * Converts the submitted valid template extensions
   * to filter extensions.
   * 
   * @param validTemplateExtensions valid template extensions to be converted
   * 
   * @return converted valid template extensions
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    private String[] toFilterExtension(String[] validTemplateExtensions) {
        String[] filterExtensions = new String[validTemplateExtensions.length];
        for (int i = 0, n = validTemplateExtensions.length; i < n; i++) {
            filterExtensions[i] = "*." + validTemplateExtensions[i];
        }
        return filterExtensions;
    }

    /**
   * Checks the submitted template extension.
   * 
   * @param extension template extension to be checked
   * 
   * @return <code>false</code> if the template extension is 
   * not valid and <code>true</code> otherwise
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    private boolean checkTemplateExtension(String extension) {
        if (extension == null) return false;
        for (int i = 0, n = validTemplateExtensions.length; i < n; i++) {
            if (extension.toLowerCase().equals(validTemplateExtensions[i].toLowerCase())) return true;
        }
        return false;
    }

    /**
   * Inits the template name.
   * 
   * @param structuredSelection structured selection to be used
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    private void initializeTemplatePath(IStructuredSelection structuredSelection) {
        Object object = structuredSelection.getFirstElement();
        if (object instanceof IFile) {
            IFile file = (IFile) object;
            if (checkTemplateExtension(file.getFileExtension())) {
                setTemplatePath(file.getRawLocation().toOSString());
            }
        }
    }

    /**
   * Builds viewer for templates within the workspace.
   * 
   * @param composite composite to be used
   * 
   * @author Andreas Br�ker
   * @date 02.07.2006
   */
    private void buildWorkspaceTemplatesViewer(Composite composite) {
        Tree tree = NOAUIPlugin.getFormToolkit().createTree(composite, SWT.FLAT);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 3;
        gridData.heightHint = (int) (Display.getCurrent().getActiveShell().getClientArea().height * 0.18);
        tree.setLayoutData(gridData);
        tree.setEnabled(false);
        treeViewer = new TreeViewer(tree);
        treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
        treeViewer.setLabelProvider(new WorkbenchLabelProvider());
        treeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
        treeViewer.addFilter(new ViewerFilter() {

            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IFile) {
                    IFile file = (IFile) element;
                    if (!checkTemplateExtension(file.getFileExtension())) {
                        return false;
                    }
                }
                return true;
            }
        });
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent selectionChangedEvent) {
                ISelection selection = selectionChangedEvent.getSelection();
                if (selection instanceof IStructuredSelection) {
                    Object object = ((IStructuredSelection) selection).getFirstElement();
                    if (object instanceof IFile) {
                        String templatePath = ((IFile) object).getRawLocation().toOSString();
                        setTemplatePath(templatePath);
                    }
                }
            }
        });
        treeViewer.setSelection(structuredSelection);
    }

    /**
   * Sets template path.
   * 
   * @param templatePath template path to be used
   * 
   * @author Andreas Br�ker
   * @date 03.07.2006
   */
    private void setTemplatePath(String templatePath) {
        if (templatePath != null) {
            currentTemplatePath = templatePath;
            textTemplatePath.setText(Dialog.shortenText(templatePath, textTemplatePath));
            textTemplatePath.setToolTipText(templatePath);
        }
    }

    /**
   * Returns information whether a template should
   * be used.
   * 
   * @return information whether a template should
   * be used
   * 
   * @author Andreas Br�ker
   * @date 03.07.2006
   */
    private boolean useTemplate() {
        if (buttonUseTemplate == null || !buttonUseTemplate.getSelection()) return false; else return true;
    }
}
