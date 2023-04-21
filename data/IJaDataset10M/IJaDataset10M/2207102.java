package org.mxeclipse.dialogs;

import java.util.ArrayList;
import java.util.List;
import matrix.util.MatrixException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.mxeclipse.model.MxTreeDomainObject;
import org.mxeclipse.model.MxTreePolicy;
import org.mxeclipse.model.MxTreeType;
import org.mxeclipse.model.MxTreeVault;

/**
 * <p>Title: CreateNewComposite</p>
 * <p>Description: TODO class description?</p>
 * <p>Company: ABB Switzerland</p>
 * @author CHTIILI
 * @version 1.0
 */
public class CreateNewComposite extends Composite {

    private Label lblType = null;

    private Combo cmbType = null;

    private Label lblName = null;

    private Text txtName = null;

    private Label lblRevision = null;

    private Text txtRevision = null;

    private Label lblPolicy = null;

    private Combo cmbPolicy = null;

    private Label lblVault = null;

    private Combo cmbVault = null;

    private MxTreeDomainObject treeObject;

    private Label lblSequence = null;

    /**
	 * TODO CreateNewComposite constructor description?
	 * @param parent
	 * @param style
	 */
    public CreateNewComposite(Composite parent, int style) {
        super(parent, style);
        initialize();
        initializeContent();
    }

    private void initialize() {
        GridData gridData11 = new GridData();
        gridData11.grabExcessHorizontalSpace = true;
        gridData11.verticalAlignment = GridData.CENTER;
        gridData11.horizontalAlignment = GridData.FILL;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.horizontalSpan = 2;
        gridData1.verticalAlignment = GridData.CENTER;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        lblType = new Label(this, SWT.NONE);
        lblType.setText("Type");
        createCmbType();
        lblName = new Label(this, SWT.NONE);
        lblName.setText("Name");
        txtName = new Text(this, SWT.BORDER);
        txtName.setLayoutData(gridData1);
        lblRevision = new Label(this, SWT.NONE);
        lblRevision.setText("Revision");
        txtRevision = new Text(this, SWT.BORDER);
        lblSequence = new Label(this, SWT.NONE);
        lblSequence.setText("");
        lblSequence.setLayoutData(gridData11);
        lblPolicy = new Label(this, SWT.NONE);
        lblPolicy.setText("Policy");
        this.setLayout(gridLayout);
        createCmbPolicy();
        this.setSize(new Point(300, 142));
        lblVault = new Label(this, SWT.NONE);
        lblVault.setText("Vault");
        createCmbVault();
    }

    private void initializeContent() {
        try {
            cmbType.setItems(MxTreeType.getAllTypeNames(false));
            cmbVault.setItems(MxTreeVault.getAllVaultNames(false));
            for (int i = 0; i < cmbVault.getItems().length; i++) {
                if (cmbVault.getItem(i).equals("eService Production")) {
                    cmbVault.select(i);
                    cmbVault.setSelection(new Point(0, -1));
                }
            }
        } catch (Exception ex) {
            MessageDialog.openInformation(this.getShell(), "Create New", "Error when trying to get combo boxes values " + ex.getMessage());
        }
    }

    private void fillInPolicyCombo() {
        try {
            String type = cmbType.getText();
            MxTreeType oType = new MxTreeType(type);
            cmbPolicy.setItems(oType.getPolicyNames(false));
            if (cmbPolicy.getItemCount() > 0) {
                cmbPolicy.select(0);
            }
        } catch (Exception ex) {
            MessageDialog.openInformation(this.getShell(), "Create New", "Error when trying to get policy combo boxes values " + ex.getMessage());
        }
    }

    private void createCmbType() {
        GridData gridData4 = new GridData();
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.verticalAlignment = GridData.CENTER;
        gridData4.horizontalSpan = 2;
        gridData4.horizontalAlignment = GridData.FILL;
        cmbType = new Combo(this, SWT.NONE);
        cmbType.setLayoutData(gridData4);
        cmbType.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                fillInPolicyCombo();
                fillInRevision();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }

    /**
	 * This method initializes cmbPolicy
	 *
	 */
    private void createCmbPolicy() {
        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.verticalAlignment = GridData.CENTER;
        gridData2.horizontalSpan = 2;
        gridData2.horizontalAlignment = GridData.FILL;
        cmbPolicy = new Combo(this, SWT.NONE);
        cmbPolicy.setLayoutData(gridData2);
        cmbPolicy.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                fillInRevision();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }

    /**
	 * This method initializes cmbVault
	 *
	 */
    private void createCmbVault() {
        GridData gridData3 = new GridData();
        gridData3.grabExcessVerticalSpace = false;
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.verticalAlignment = GridData.CENTER;
        gridData3.horizontalSpan = 2;
        gridData3.grabExcessHorizontalSpace = true;
        cmbVault = new Combo(this, SWT.NONE);
        cmbVault.setLayoutData(gridData3);
    }

    private void fillInRevision() {
        try {
            if (cmbPolicy.getSelectionIndex() < 0) {
                return;
            }
            MxTreePolicy policy = new MxTreePolicy(cmbPolicy.getItem(cmbPolicy.getSelectionIndex()));
            policy.fillBasics();
            String seq = policy.getSequence();
            lblSequence.setText(seq);
            if (seq.indexOf(",") > 0) {
                this.txtRevision.setText(seq.substring(0, seq.indexOf(',')));
            } else {
                this.txtRevision.setText(seq);
            }
        } catch (Exception ex) {
            MessageDialog.openInformation(this.getShell(), "Create New", "Error when trying to get revision sequence: " + ex.getMessage());
        }
    }

    public boolean okPressed() {
        try {
            if (cmbType.getSelectionIndex() < 0) {
                MessageDialog.openInformation(this.getShell(), "Create New", "Please select a type!");
                return false;
            }
            if (txtName.getText().equals("")) {
                MessageDialog.openInformation(this.getShell(), "Create New", "Please select type in name!");
                return false;
            }
            if (cmbPolicy.getSelectionIndex() < 0) {
                MessageDialog.openInformation(this.getShell(), "Create New", "Please select a policy!");
                return false;
            }
            if (cmbVault.getSelectionIndex() < 0) {
                MessageDialog.openInformation(this.getShell(), "Create New", "Please select a vault!");
                return false;
            }
            treeObject = MxTreeDomainObject.createNewObject(cmbType.getItem(cmbType.getSelectionIndex()), txtName.getText(), txtRevision.getText(), cmbPolicy.getItem(cmbPolicy.getSelectionIndex()), cmbVault.getItem(cmbVault.getSelectionIndex()));
            return true;
        } catch (Exception ex) {
            MessageDialog.openInformation(this.getShell(), "Create New", "Error when trying to get create a new object: " + ex.getMessage());
            return false;
        }
    }

    public MxTreeDomainObject getNewObject() {
        return treeObject;
    }

    public void setTypes(String[] types) {
        this.cmbType.setItems(types);
        this.cmbPolicy.removeAll();
        this.txtRevision.setText("");
    }
}
