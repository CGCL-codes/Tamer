package com.prolix.editor.main.workspace.reload.activities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import com.prolix.editor.main.workspace.reload.ReloadWorkspace;
import com.prolix.editor.main.workspace.reload.utils.ItemModelTypeEditor;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.IDataModelListener;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.components.activities.SupportActivity;
import uk.ac.reload.straker.datamodel.learningdesign.types.ItemModelType;
import uk.ac.reload.straker.ui.widgets.BannerCLabel;

/**
 * Support Activity Editor Panel
 * 
 * @author Phillip Beauvoir
 * @version $Id: SupportActivityEditorPanel.java,v 1.8 2006/07/10 11:50:36 phillipus Exp $
 */
public class SupportActivityEditorPanel extends ActivityEditorPanel {

    /**
     * Description Editor
     */
    private ItemModelTypeEditor _descriptionEditor;

    /**
     * Role Tree
     */
    private SupportActivityRoleSelectorTree _roleSelectorTree;

    private BannerCLabel _bannerRoles;

    private IDataModelListener _listener;

    /**
     * Constructor
     */
    public SupportActivityEditorPanel(ReloadWorkspace ldEditor, Composite parent) {
        super(ldEditor, parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, HELPID_LD_SUPPORTACTIVITY_EDITOR);
    }

    /**
     * Create the part
     */
    protected void setupView() {
        super.setupView();
        setupCommonFields();
        Label label = new Label(getChildPanel(), SWT.NULL);
        GridData gd = new GridData(GridData.BEGINNING);
        gd.horizontalSpan = 3;
        label.setLayoutData(gd);
        _descriptionEditor = new ItemModelTypeEditor(getLearningDesignEditor(), getChildPanel(), "Activity Description");
        label = new Label(getChildPanel(), SWT.NULL);
        gd = new GridData(GridData.BEGINNING);
        gd.horizontalSpan = 3;
        label.setLayoutData(gd);
        _bannerRoles = new BannerCLabel(getChildPanel(), "Roles", true);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        _bannerRoles.setLayoutData(gd);
        _roleSelectorTree = new SupportActivityRoleSelectorTree(getLearningDesignEditor(), getChildPanel(), SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gd.heightHint = 90;
        _roleSelectorTree.getControl().setLayoutData(gd);
        setupCompleteActivityChoices();
        setupOnCompletionPanel();
        setMinSize(getChildPanel().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        getChildPanel().setSize(getChildPanel().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        setupControlListeners();
    }

    private void setListener(LD_DataComponent component) {
        if (component == null) return;
        if (getDataComponent() != null && getDataComponent().equals(component)) return;
        try {
            if (getDataComponent() != null) getDataComponent().getDataModel().removeIDataModelListener(_listener);
        } catch (NullPointerException e) {
        }
        _listener = new IDataModelListener() {

            public void componentAdded(DataComponent component) {
            }

            public void componentChanged(DataComponent component) {
                if (getDataComponent() != null && getDataComponent().equals(component)) setDataComponent(getDataComponent());
            }

            public void componentMoved(DataComponent component) {
            }

            public void componentRemoved(DataComponent component) {
            }
        };
        component.getDataModel().addIDataModelListener(_listener);
    }

    /**
     * Set the DataComponent
     * @param component
     */
    public void setDataComponent(LD_DataComponent component) {
        setListener(component);
        super.setDataComponent(component);
        SupportActivity sa = (SupportActivity) component;
        _roleSelectorTree.setSupportActivityComponent(sa);
        ItemModelType descriptionType = sa.getActivityDescriptionType();
        _descriptionEditor.setItemModelType(descriptionType);
        initSetEnabled(true);
    }

    private void initSetEnabled(boolean enabled) {
        _roleSelectorTree.getTree().setEnabled(enabled);
        _descriptionEditor.setEnable(enabled);
    }

    /**
     * Dispose of stuff
     */
    public void dispose() {
        super.dispose();
        _descriptionEditor.dispose();
        _descriptionEditor = null;
        _bannerRoles.dispose();
        _bannerRoles = null;
        _roleSelectorTree.dispose();
        _roleSelectorTree = null;
    }
}
