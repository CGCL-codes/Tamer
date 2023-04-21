package org.dengues.warehouse.viewers.script;

import java.util.ArrayList;
import java.util.List;
import org.dengues.commons.utils.DenguesTextUtil;
import org.dengues.core.warehouse.ENodeCategoryName;
import org.dengues.core.warehouse.ENodeStatus;
import org.dengues.designer.ui.DesignerUIPlugin;
import org.dengues.model.component.IMPORTType;
import org.dengues.model.warehouse.ScriptsStorage;
import org.dengues.warehouse.i18n.Messages;
import org.dengues.warehouse.viewers.wizards.AbstractStorageWizardPage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2008-1-7 qiang.zhang $
 * 
 */
public class NewJavaScriptsPage extends AbstractStorageWizardPage {

    private String processName;

    private String processComment;

    private IStatus nameStatus;

    private ScriptsStorage storage;

    private IPath path;

    private boolean isActived;

    private List<IMPORTType> imports = new ArrayList<IMPORTType>();

    /**
     * Qiang.Zhang.Adolf@gmail.com NewJavaScriptsPage constructor comment.
     * 
     * @param destinationPath
     */
    public NewJavaScriptsPage(IPath destinationPath) {
        this();
        this.path = destinationPath;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com NewJavaScriptsPage constructor comment.
     */
    private NewJavaScriptsPage() {
        super(Messages.getString("NewProcessWizardPage.0"));
        nameStatus = createOkStatus();
        setTitle(Messages.getString("NewJavaScriptsWizard.ctitle"));
        setDescription(Messages.getString("NewJavaScriptsWizard.ctitle"));
    }

    public NewJavaScriptsPage(ScriptsStorage storage, boolean isActived) {
        super(Messages.getString("NewProcessWizardPage.0"));
        nameStatus = createOkStatus();
        setTitle(Messages.getString("NewJavaScriptsWizard.mtitle"));
        setDescription(Messages.getString("NewJavaScriptsWizard.mtitle"));
        this.storage = storage;
        this.isActived = isActived;
        EList<IMPORTType> imports2 = storage.getImports();
        if (imports2 != null) {
            for (IMPORTType type : imports2) {
                imports.add(type);
            }
        }
    }

    private Text nameText;

    private Text commentText;

    private JarListTableViewer jarListViewer;

    public void createControl(Composite parent) {
        Composite supcontainer = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        supcontainer.setLayout(layout);
        Composite container = new Composite(supcontainer, SWT.NONE);
        layout = new GridLayout(2, false);
        container.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        container.setLayoutData(gridData);
        Label nameLab = new Label(container, SWT.NONE);
        nameLab.setText(Messages.getString("NewProcessWizardPage.Label.Name"));
        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (storage != null) {
            processName = DenguesTextUtil.getNotNullString(storage.getName());
            nameText.setText(processName);
        }
        Label commentLab = new Label(container, SWT.NONE);
        commentLab.setText(Messages.getString("NewProcessWizardPage.Label.Comment"));
        commentText = new Text(container, SWT.BORDER | SWT.MULTI);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 60;
        commentText.setLayoutData(gridData);
        if (storage != null) {
            processComment = DenguesTextUtil.getNotNullString(storage.getComment());
            commentText.setText(processComment);
        }
        createJarList(supcontainer);
        setControl(supcontainer);
        setPageComplete(false);
        addListeners();
        checkFieldsValue();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "createJarList".
     * 
     * @param container
     */
    private void createJarList(Composite container) {
        Composite composite = new Composite(container, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        jarListViewer = new JarListTableViewer(composite, imports);
        jarListViewer.initTableInput();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "checkFieldsValue".
     */
    private void checkFieldsValue() {
        if (isActived) {
            nameText.setEnabled(false);
            commentText.setEnabled(false);
            jarListViewer.setEnabled(false);
            setPageComplete(false);
            setErrorMessage(Messages.getString("NewJavaScriptsPage.activated"));
        } else {
            if (nameText.getText().length() == 0) {
                nameStatus = new Status(IStatus.ERROR, DesignerUIPlugin.PLUGIN_ID, IStatus.ERROR, Messages.getString("NewJavaScriptsPage.empty"), null);
            } else {
                boolean n = storage != null && storage.getName().equals(nameText.getText());
                if (!n && isNameAlreadyUsed(DenguesTextUtil.getNotNullString(nameText.getText()), ENodeCategoryName.SCRIPTS)) {
                    nameStatus = new Status(IStatus.ERROR, DesignerUIPlugin.PLUGIN_ID, IStatus.ERROR, Messages.getString("NewJavaScriptsPage.exist"), null);
                } else {
                    nameStatus = createOkStatus();
                }
            }
            setMessage(nameStatus);
            boolean b = nameStatus.getSeverity() != IStatus.ERROR;
            setPageComplete(b);
            if (b) {
                processName = nameText.getText();
            }
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "addListeners".
     */
    private void addListeners() {
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }
        });
        commentText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                processComment = commentText.getText();
            }
        });
    }

    /**
     * zx Comment method "getComment".
     * 
     * @return
     */
    public String getStorageComment() {
        return this.processComment;
    }

    public String getStorageName() {
        return this.processName;
    }

    public String getStorageStatus() {
        return ENodeStatus.NORMAL.getId();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "setMessage".
     * 
     * @param status
     */
    private void setMessage(IStatus status) {
        if (IStatus.ERROR == status.getSeverity()) {
            setErrorMessage(status.getMessage());
            setMessage("");
        } else {
            setMessage(status.getMessage());
            setErrorMessage(null);
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "createOkStatus".
     * 
     * @return
     */
    private static IStatus createOkStatus() {
        return new Status(IStatus.OK, DesignerUIPlugin.PLUGIN_ID, IStatus.OK, "", null);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getImports".
     * 
     * @return
     */
    public List<IMPORTType> getImports() {
        return imports;
    }
}
