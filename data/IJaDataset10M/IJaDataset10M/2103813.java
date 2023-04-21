package org.mxeclipse.business.basic;

import java.util.ArrayList;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.mxeclipse.model.MxTreeAttribute;
import org.mxeclipse.model.MxTreeBusiness;
import org.mxeclipse.model.MxTreeType;
import org.mxeclipse.views.IModifyable;
import org.eclipse.swt.widgets.Combo;

/**
 * <p>Title: MxAttributeBasicComposite</p>
 * <p>Description: TODO class description?</p>
 * <p>Company: ABB Switzerland</p>
 * @author CHTIILI
 * @version 1.0
 */
public class MxTypeBasicComposite extends MxBusinessBasicComposite {

    MxTreeType businessType;

    IModifyable view;

    private Label lblName = null;

    private Text txtName = null;

    private Label lblDescription = null;

    private Text txtDescription = null;

    private Label lblType = null;

    private Text txtType = null;

    private Label lblHidden = null;

    private Button chkHidden = null;

    private Button chkAbstract = null;

    private Label lblAbstract = null;

    private Label lblParentType = null;

    private Combo cmbParentType = null;

    /**
	 * TODO MxAttributeBasicComposite constructor description?
	 * @param parent
	 * @param style
	 */
    public MxTypeBasicComposite(Composite parent, int style, IModifyable view, MxTreeBusiness businessType) {
        super(parent, style);
        this.view = view;
        initialize();
        initializeContent(businessType);
    }

    private void initialize() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.verticalAlignment = GridData.CENTER;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.verticalAlignment = GridData.CENTER;
        GridData gridData1 = new GridData();
        gridData1.widthHint = 70;
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        gridData.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        lblName = new Label(this, SWT.NONE);
        lblName.setText("Name");
        lblName.setLayoutData(gridData1);
        txtName = new Text(this, SWT.BORDER);
        txtName.setLayoutData(gridData);
        txtName.addKeyListener(new ModifySetter(view));
        lblDescription = new Label(this, SWT.NONE);
        lblDescription.setText("Description");
        txtDescription = new Text(this, SWT.BORDER);
        txtDescription.setLayoutData(gridData2);
        txtDescription.addKeyListener(new ModifySetter(view));
        lblType = new Label(this, SWT.NONE);
        lblType.setText("Type");
        txtType = new Text(this, SWT.BORDER);
        txtType.setEnabled(false);
        txtType.setLayoutData(gridData3);
        txtType.addKeyListener(new ModifySetter(view));
        lblHidden = new Label(this, SWT.NONE);
        lblHidden.setText("Hidden");
        chkHidden = new Button(this, SWT.CHECK);
        lblAbstract = new Label(this, SWT.NONE);
        lblAbstract.setText("Abstract");
        chkAbstract = new Button(this, SWT.CHECK);
        lblParentType = new Label(this, SWT.NONE);
        lblParentType.setText("Parent Type");
        chkAbstract.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                MxTypeBasicComposite.this.view.setModified(true);
            }
        });
        chkHidden.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                MxTypeBasicComposite.this.view.setModified(true);
            }
        });
        this.setLayout(gridLayout);
        createCmbParentType();
        setSize(new Point(300, 200));
    }

    @Override
    public void initializeContent(MxTreeBusiness selectedBusiness) {
        try {
            this.businessType = (MxTreeType) selectedBusiness;
            txtName.setText(businessType.getName());
            txtDescription.setText(businessType.getDescription());
            chkHidden.setSelection(businessType.isHidden());
            chkAbstract.setSelection(businessType.isAbstractType());
            ArrayList<MxTreeType> allTypes = MxTreeType.getAllTypes(false);
            String[] sAllTypes = new String[allTypes.size() + 1];
            sAllTypes[0] = "";
            String parentName = (businessType.getParentType(false) != null ? businessType.getParentType(false).getName() : "");
            int selIndex = 0;
            for (int i = 1; i < sAllTypes.length; i++) {
                sAllTypes[i] = allTypes.get(i - 1).getName();
                if (sAllTypes[i].equals(parentName)) {
                    selIndex = i;
                }
            }
            cmbParentType.setItems(sAllTypes);
            cmbParentType.select(selIndex);
        } catch (Exception e) {
            Status status = new Status(IStatus.ERROR, "MxEclipse", 0, e.getMessage(), e);
            ErrorDialog.openError(this.getShell(), "Error when trying to initialize data in the basic form", "Error when trying to initialize data in the basic form", status);
        }
    }

    @Override
    public void storeData() {
        try {
            businessType.setName(txtName.getText());
            businessType.setDescription(txtDescription.getText());
            businessType.setHidden(chkHidden.getSelection());
            businessType.setAbstractType(chkAbstract.getSelection());
            businessType.setParentType(cmbParentType.getSelectionIndex() > 0 ? cmbParentType.getItem(cmbParentType.getSelectionIndex()) : null);
        } catch (Exception e) {
            Status status = new Status(IStatus.ERROR, "MxEclipse", 0, e.getMessage(), e);
            ErrorDialog.openError(this.getShell(), "Error when trying to store the data to matrix", "Error when trying to store the data to matrix", status);
        }
    }

    class ModifySetter implements org.eclipse.swt.events.KeyListener {

        private IModifyable view;

        public ModifySetter(IModifyable view) {
            this.view = view;
        }

        public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
        }

        public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
            this.view.setModified(true);
        }
    }

    /**
	 * This method initializes cmbParentType
	 *
	 */
    private void createCmbParentType() {
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.verticalAlignment = GridData.CENTER;
        cmbParentType = new Combo(this, SWT.NONE);
        cmbParentType.setLayoutData(gridData4);
    }
}
