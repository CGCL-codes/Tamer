package org.scrinch.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.scrinch.model.WorkType;
import org.scrinch.model.WorkTypeFactory;

public class WorkTypeComboBoxModel implements WorkTypeFactory.Listener, ComboBoxModel {

    private List<WorkType> workTypeList = new ArrayList<WorkType>();

    private WorkType selectedWork;

    private List<ListDataListener> listDataListenerList = new ArrayList<ListDataListener>();

    public void workTypeListChanged() {
        updateWorkTypeList();
        notifyListeners();
    }

    private void updateWorkTypeList() {
        workTypeList.clear();
        workTypeList.addAll(WorkTypeFactory.getInstance().getWorkTypeList());
    }

    public WorkTypeComboBoxModel() {
        updateWorkTypeList();
        WorkTypeFactory.getInstance().addListener(this);
    }

    public void releaseAllResources() {
        WorkTypeFactory.getInstance().removeListener(this);
    }

    public void setSelectedItem(Object object) {
        try {
            this.selectedWork = (WorkType) object;
        } catch (Exception e) {
            this.selectedWork = null;
        }
    }

    public Object getElementAt(int i) {
        return workTypeList.get(i);
    }

    public void removeListDataListener(ListDataListener listDataListener) {
        listDataListenerList.remove(listDataListener);
    }

    public void addListDataListener(ListDataListener listDataListener) {
        listDataListenerList.add(listDataListener);
    }

    private void notifyListeners() {
        for (ListDataListener listener : listDataListenerList) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, listDataListenerList.size() - 1));
        }
    }

    public int getSize() {
        return this.workTypeList.size();
    }

    public Object getSelectedItem() {
        return selectedWork;
    }
}
