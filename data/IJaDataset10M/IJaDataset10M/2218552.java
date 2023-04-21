package com.cosmos.acacia.crm.gui;

import java.util.UUID;
import java.util.List;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.swingbinding.JTableBinding;
import com.cosmos.acacia.crm.data.ClassifierGroup;
import com.cosmos.acacia.gui.AbstractTablePanel;
import com.cosmos.acacia.gui.AcaciaTable;
import com.cosmos.beansbinding.EntityProperties;
import com.cosmos.swingb.DialogResponse;

/**
 *
 * @author Bozhidar Bozhanov
 */
public class ClassifierGroupsListPanel extends AbstractTablePanel<ClassifierGroup> {

    /** Creates new form ClassifierGroupsListPanel */
    public ClassifierGroupsListPanel(UUID parentDataObjectId) {
        super(parentDataObjectId);
    }

    private BindingGroup classifierGroupsBindingGroup;

    private List<ClassifierGroup> classifierGroups;

    @Override
    protected void initData() {
        super.initData();
        this.setVisible(Button.Classify, false);
        classifierGroupsBindingGroup = new BindingGroup();
        AcaciaTable classifierGroupsTable = getDataTable();
        JTableBinding tableBinding = classifierGroupsTable.bind(classifierGroupsBindingGroup, getClassifierGroups(), getClassifierGroupEntityProperties());
        classifierGroupsBindingGroup.bind();
        classifierGroupsTable.setEditable(false);
    }

    protected List<ClassifierGroup> getClassifierGroups() {
        if (classifierGroups == null) {
            classifierGroups = getClassifiersManager().getClassifierGroups();
        }
        return classifierGroups;
    }

    protected EntityProperties getClassifierGroupEntityProperties() {
        return getClassifiersManager().getClassifierGroupEntityProperties();
    }

    protected int deleteClassifierGroup(ClassifierGroup classifierGroup) {
        return getClassifiersManager().deleteClassifierGroup(classifierGroup);
    }

    @Override
    @Action
    public void selectAction() {
        super.selectAction();
    }

    @Override
    protected boolean deleteRow(ClassifierGroup rowObject) {
        if (rowObject != null) {
            deleteClassifierGroup((ClassifierGroup) rowObject);
            return true;
        }
        return false;
    }

    @Override
    protected ClassifierGroup modifyRow(ClassifierGroup rowObject) {
        if (rowObject != null) {
            ClassifierGroupPanel classifierGroupPanel = new ClassifierGroupPanel((ClassifierGroup) rowObject);
            DialogResponse response = classifierGroupPanel.showDialog(this);
            if (DialogResponse.SAVE.equals(response)) {
                return (ClassifierGroup) classifierGroupPanel.getSelectedValue();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Task refreshAction() {
        Task t = super.refreshAction();
        if (classifierGroupsBindingGroup != null) classifierGroupsBindingGroup.unbind();
        classifierGroups = null;
        initData();
        return t;
    }

    @Override
    protected ClassifierGroup newRow() {
        if (canNestedOperationProceed()) {
            ClassifierGroupPanel classifierGroupPanel = new ClassifierGroupPanel(getParentDataObjectId());
            DialogResponse response = classifierGroupPanel.showDialog(this);
            if (DialogResponse.SAVE.equals(response)) {
                return (ClassifierGroup) classifierGroupPanel.getSelectedValue();
            }
        }
        return null;
    }

    @Override
    public boolean canCreate() {
        return true;
    }

    @Override
    public boolean canModify(ClassifierGroup rowObject) {
        return true;
    }

    @Override
    public boolean canDelete(ClassifierGroup rowObject) {
        if (isSystemAdministrator()) return true;
        ClassifierGroup classifierGroup = (ClassifierGroup) rowObject;
        if (classifierGroup.getIsSystemGroup()) return false;
        return true;
    }
}
