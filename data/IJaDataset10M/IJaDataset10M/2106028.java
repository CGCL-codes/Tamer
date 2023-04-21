package org.richfaces.tarkus.fmk.gui.bean;

import java.util.List;
import javax.persistence.EntityExistsException;
import org.richfaces.component.html.HtmlModalPanel;
import org.richfaces.tarkus.fmk.gui.filter.FilterComponent;
import org.richfaces.tarkus.fmk.gui.filter.Filterable;
import org.richfaces.tarkus.fmk.gui.popup.ValidationPopup;
import org.richfaces.tarkus.fmk.gui.table.SelectableData;
import org.richfaces.tarkus.fmk.gui.table.Table;

/**
 * AbstractListBean
 * 
 * @version $Revision: 10343 $
 * @author hbourdin, last modified by $Author: A-SIS\aselva $
 * @param <T>
 */
public abstract class AbstractListBean<T> extends AbstractPageBean implements Filterable {

    private HtmlModalPanel modelPanelPopUp;

    /** backingBeanName */
    protected String backingBeanName;

    /** table */
    protected Table<T> table;

    /** editBean */
    private AbstractDetailBean<T> editBean;

    /** Data to delete */
    private T dataToDelete;

    /** filterComponent */
    protected FilterComponent filterComponent;

    /**
   * AbstractSortableListBean
   * 
   * @param backingBeanName
   */
    protected AbstractListBean(String backingBeanName) {
        super();
        this.backingBeanName = backingBeanName;
        table = new Table<T>(backingBeanName + ".table", backingBeanName + ".dataList", getMsg());
        addColumnDefinition();
    }

    /**
   * AbstractSortableListBean
   * 
   * @param backingBeanName
   * @param defaultSortColumn
   * @param defaultAscending
   */
    protected AbstractListBean(String backingBeanName, String defaultSortColumn, boolean defaultAscending) {
        super();
        this.backingBeanName = backingBeanName;
        table = new Table<T>(backingBeanName + ".table", backingBeanName + ".dataList", defaultSortColumn, defaultAscending, getMsg(), true);
        addColumnDefinition();
    }

    /**
   * setEditBean
   * 
   * @param editBean
   *          the editBean to set
   */
    public void setEditBean(AbstractDetailBean<T> editBean) {
        this.editBean = editBean;
    }

    /**
   * getTable
   * 
   * @return Table<T>
   */
    public Table<T> getTable() {
        return table;
    }

    /**
   * getSelectedData
   * 
   * @return T
   */
    private T getSelectedData() {
        return table.getSelectedData();
    }

    /**
   * confirmDelete
   */
    public void confirmDelete() {
        if (!isReadonly()) {
            dataToDelete = getSelectedData();
        }
    }

    public HtmlModalPanel getModelPanelPopUp() {
        ValidationPopup popup = getValidationPopup();
        popup.open(getElMsg("deletePopup_deleteText", "Do you confirm the deletion ?"), "#{" + backingBeanName + ".validateDelete}", "#{" + backingBeanName + ".validationPopup.close}");
        return popup.getModelPanel();
    }

    public void setModelPanelPopUp(HtmlModalPanel modelPanelPopUp) {
        this.modelPanelPopUp = modelPanelPopUp;
    }

    /**
   * validateDelete
   */
    public void validateDelete() {
        if (!isReadonly()) {
            delete();
        }
    }

    /**
   * Delete a data.<br>
   * <br>
   * Delete the stored data if there's one, or else the selected one.
   * 
   * @return String
   */
    private String delete() {
        if (isReadonly()) {
            return "";
        }
        if (dataToDelete == null) {
            return "";
        }
        T data = dataToDelete;
        dataToDelete = null;
        try {
            delete(data);
            table.clearDataModel(false);
        } catch (RuntimeException re) {
            if (isConstraintError(re.getCause())) {
                addMessage("listForm", "errConstraint", "Could not delete : constraint error.");
                return "";
            }
            throw re;
        } catch (Exception e) {
            addMessage("listForm", e.getMessage());
        }
        return "";
    }

    /**
   * create
   * 
   * @return String
   */
    @SuppressWarnings("unchecked")
    public String create() {
        String navigationRule = "";
        if (!isReadonly() && editBean instanceof AbstractEditBean) {
            navigationRule = ((AbstractEditBean) editBean).create();
        }
        return navigationRule;
    }

    /**
   * isConstraintError
   * 
   * @param cause
   * @return boolean
   */
    private boolean isConstraintError(Throwable cause) {
        boolean errConstraint = false;
        if (cause instanceof EntityExistsException) {
            errConstraint = true;
        } else if (cause != null && cause.getCause() instanceof EntityExistsException) {
            errConstraint = true;
        }
        return errConstraint;
    }

    private int selectedIndex = -1;

    /**
   * edit
   * 
   * @return String
   */
    public String edit() {
        T selectedData = getSelectedData();
        editBean.setKey(getEditKey(selectedData));
        selectedIndex = table.getSelectedIndex();
        return "edit";
    }

    /**
   * previous
   * 
   * @return String
   */
    @SuppressWarnings("unchecked")
    public void previous() {
        if (selectedIndex > 0) {
            List<SelectableData<T>> datas = (List<SelectableData<T>>) table.getDataModel().getWrappedData();
            selectedIndex--;
            SelectableData<T> editData = datas.get(selectedIndex);
            editBean.setKey(getEditKey(editData.getData()));
        }
    }

    /**
   * next
   * 
   * @return String
   */
    @SuppressWarnings("unchecked")
    public void next() {
        List<SelectableData<T>> datas = (List<SelectableData<T>>) table.getDataModel().getWrappedData();
        if (selectedIndex < (datas.size() - 1)) {
            selectedIndex++;
            SelectableData<T> editData = datas.get(selectedIndex);
            editBean.setKey(getEditKey(editData.getData()));
        }
    }

    /**
   * getDataList
   * 
   * @return List<T>
   */
    public abstract List<T> getDataList();

    /**
   * delete
   * 
   * @param t
   */
    protected abstract void delete(T t);

    /**
   * getEditKey
   * 
   * @param t
   * @return Object
   */
    protected abstract Object getEditKey(T t);

    /**
   * Methode definissant les colonnes de la table... definition faite et
   * appellant les methodes addButtonColumn, addLinkColumn et/ou addTextColumn
   */
    protected abstract void addColumnDefinition();

    /**
   * @param filterComponent
   *          the FilterComponent instance to set
   */
    public void setFilterComponent(FilterComponent filterComponent, String filterId) {
        this.filterComponent = filterComponent;
        table.clearDataModel();
    }

    /**
   * @return the filterComponent
   */
    public FilterComponent getFilterComponent() {
        return filterComponent;
    }

    public String refresh() {
        table.clearDataModel();
        return "";
    }
}
