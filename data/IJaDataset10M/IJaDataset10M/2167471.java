package com.cosmos.acacia.crm.gui;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JTableBinding;
import com.cosmos.acacia.crm.data.Classifier;
import com.cosmos.acacia.crm.data.DataObjectType;
import com.cosmos.acacia.gui.AbstractTablePanel;
import com.cosmos.acacia.gui.AcaciaTable;
import com.cosmos.beansbinding.EntityProperties;

/**
 *
 * @author Bozhidar Bozhanov
 */
public class DataObjectTypesListPanel extends AbstractTablePanel<DataObjectType> {

    /** Creates new form DataObjectTypesListPanel */
    public DataObjectTypesListPanel() {
        super();
        postInitData();
    }

    /** Creates new form DataObjectTypesListPanel */
    public DataObjectTypesListPanel(UUID parentDataObjectId, Classifier classifier) {
        super(parentDataObjectId);
        this.classifier = classifier;
        postInitData();
    }

    private BindingGroup dataObjectTypesBindingGroup;

    private List<DataObjectType> dataObjectTypes;

    private DataObjectType dataObjectType;

    private Classifier classifier;

    @Override
    protected void initData() {
        super.initData();
        setVisible(Button.Select, false);
    }

    protected void postInitData() {
        dataObjectTypesBindingGroup = new BindingGroup();
        AcaciaTable dataObjectTypesTable = getDataTable();
        JTableBinding tableBinding = dataObjectTypesTable.bind(dataObjectTypesBindingGroup, getDataObjectTypes(), getDataObjectTypeEntityProperties());
        List<DataObjectType> l = ObservableCollections.observableList(getDataObjectTypes());
        dataObjectTypesBindingGroup.bind();
        dataObjectTypesTable.setEditable(false);
    }

    protected List<DataObjectType> getDataObjectTypes() {
        if (dataObjectTypes == null) {
            dataObjectTypes = shortenDataObjectTypeNames(getClassifiersManager().getDataObjectTypes(classifier));
        }
        return dataObjectTypes;
    }

    public static List<DataObjectType> shortenDataObjectTypeNames(List<DataObjectType> list) {
        List<DataObjectType> result = new ArrayList<DataObjectType>(list.size());
        for (DataObjectType dot : list) {
            String name = new String(dot.getDataObjectType());
            dot.setDataObjectType(name.replaceAll(DataObjectType.class.getPackage().getName() + "\\.", ""));
            result.add(dot);
        }
        return result;
    }

    public DataObjectType getDataObjectType() {
        return dataObjectType;
    }

    public void setDataObjectType(DataObjectType dataObjectType) {
        this.dataObjectType = dataObjectType;
    }

    protected EntityProperties getDataObjectTypeEntityProperties() {
        return getClassifiersManager().getDataObjectTypeEntityProperties();
    }

    protected void deleteDataObjectType(DataObjectType dataObjectType) {
        getClassifiersManager().removeDataObjectTypeConstraint(classifier, dataObjectType);
    }

    @Override
    @Action
    public void selectAction() {
        super.selectAction();
    }

    @Override
    protected boolean deleteRow(DataObjectType rowObject) {
        if (rowObject != null) {
            deleteDataObjectType(rowObject);
            return true;
        }
        return false;
    }

    @Override
    protected DataObjectType modifyRow(DataObjectType rowObject) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Task refreshAction() {
        Task t = super.refreshAction();
        if (dataObjectTypesBindingGroup != null) dataObjectTypesBindingGroup.unbind();
        dataObjectTypes = null;
        postInitData();
        return t;
    }

    @Override
    protected DataObjectType newRow() {
        return null;
    }

    @Override
    public boolean canCreate() {
        return true;
    }

    @Override
    public boolean canModify(DataObjectType rowObject) {
        return true;
    }

    @Override
    public boolean canDelete(DataObjectType rowObject) {
        return true;
    }
}
