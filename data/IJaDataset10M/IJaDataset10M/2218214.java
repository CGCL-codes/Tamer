package org.mxeclipse.mxgraphclipse.dialog;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.mxeclipse.model.MxTreeAttribute;
import org.mxeclipse.model.MxTreeRelationship;
import org.mxeclipse.model.MxTreeType;
import org.mxeclipse.mxgraphclipse.util.ExportAdminObjects;
import org.mxeclipse.utils.IXMLPersistable;
import org.mxeclipse.utils.MxPersistUtils;

/**
 * <p>Title: CreateNewComposite</p>
 * <p>Description: TODO class description?</p>
 * <p>Company: ABB Switzerland</p>
 * @author CHTIILI
 * @version 1.0
 */
public class MatrixExportComposite extends Composite {

    private TabFolder tabExport = null;

    private ExportAdminObjects exportObjects;

    private MatrixExportOneAdminType typesComposite;

    private MatrixExportOneAdminType relationshipsComposite;

    private MatrixExportOneAdminType attributesComposite;

    private Button chkIncludeRelatedObjects = null;

    private Button chkIncludeParents = null;

    private boolean bNewExportObjects = false;

    /**
	 * TODO CreateNewComposite constructor description?
	 * @param parent
	 * @param style
	 */
    public MatrixExportComposite(Composite parent, int style, ExportAdminObjects exportObjects) {
        super(parent, style);
        this.exportObjects = exportObjects;
        if (exportObjects == null) {
            this.exportObjects = new ExportAdminObjects();
            this.bNewExportObjects = true;
        }
        initialize();
    }

    private void initialize() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        this.setLayout(gridLayout);
        this.setLayoutData(gridData);
        chkIncludeParents = new Button(this, SWT.CHECK);
        chkIncludeParents.setText("Include Parents");
        chkIncludeParents.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                exportObjects.setIncludeParents(chkIncludeParents.getSelection());
            }
        });
        chkIncludeRelatedObjects = new Button(this, SWT.CHECK);
        chkIncludeRelatedObjects.setText("Include Related Objects");
        chkIncludeRelatedObjects.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                exportObjects.setIncludeRelatedObjects(chkIncludeRelatedObjects.getSelection());
            }
        });
        createTabExport();
        this.setSize(new Point(300, 200));
    }

    public void initializeContent() {
        try {
            TabItem tabTypes = new TabItem(this.tabExport, SWT.NONE);
            tabTypes.setText("Types");
            typesComposite = new MatrixExportOneAdminType(tabExport, SWT.NONE);
            typesComposite.initCheckboxes(MxTreeType.getAllTypes(false));
            if (!bNewExportObjects) {
                typesComposite.setExportList(exportObjects.getTypes());
            }
            tabTypes.setControl(this.typesComposite);
            TabItem tabRelationships = new TabItem(tabExport, SWT.NONE);
            tabRelationships.setText("Relationships");
            relationshipsComposite = new MatrixExportOneAdminType(tabExport, SWT.NONE);
            relationshipsComposite.initCheckboxes(MxTreeRelationship.getAllRelationships(false));
            if (!bNewExportObjects) {
                relationshipsComposite.setExportList(exportObjects.getRelationships());
            } else {
                relationshipsComposite.selectAll();
            }
            tabRelationships.setControl(this.relationshipsComposite);
            TabItem tabAttribues = new TabItem(tabExport, SWT.NONE);
            tabAttribues.setText("Attributes");
            attributesComposite = new MatrixExportOneAdminType(tabExport, SWT.NONE);
            attributesComposite.initCheckboxes(MxTreeAttribute.getAllAttributes(false));
            if (!bNewExportObjects) {
                attributesComposite.setExportList(exportObjects.getAttributes());
            } else {
                attributesComposite.selectAll();
            }
            tabAttribues.setControl(this.attributesComposite);
            if (!bNewExportObjects) {
                this.chkIncludeParents.setSelection(exportObjects.isIncludeParents());
                this.chkIncludeRelatedObjects.setSelection(exportObjects.isIncludeRelatedObjects());
            } else {
                this.chkIncludeParents.setSelection(true);
                this.chkIncludeRelatedObjects.setSelection(true);
            }
        } catch (Exception ex) {
            MessageDialog.openInformation(this.getShell(), "Matrix Export", "Error when trying get all types/rels/attributes " + ex.getMessage());
        }
    }

    public ExportAdminObjects getExportObjects() {
        return exportObjects;
    }

    private void setExportObjects(ExportAdminObjects exportObjects) {
        if (exportObjects != null) {
            this.exportObjects = exportObjects;
            typesComposite.setExportList(exportObjects.getTypes());
            relationshipsComposite.setExportList(exportObjects.getRelationships());
            attributesComposite.setExportList(exportObjects.getAttributes());
            this.chkIncludeParents.setSelection(exportObjects.isIncludeParents());
            this.chkIncludeRelatedObjects.setSelection(exportObjects.isIncludeRelatedObjects());
        }
    }

    public boolean okPressed() {
        List<IXMLPersistable> lst = new ArrayList<IXMLPersistable>();
        lst.add(this.getExportObjects());
        MxPersistUtils.persistObjects(lst);
        return true;
    }

    /**
	 * This method initializes tabExport
	 *
	 */
    private void createTabExport() {
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessVerticalSpace = true;
        tabExport = new TabFolder(this, SWT.NONE);
        tabExport.setLayoutData(gridData);
    }
}
