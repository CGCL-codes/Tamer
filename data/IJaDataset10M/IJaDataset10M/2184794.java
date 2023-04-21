package de.mindcrimeilab.xsanalyzer.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author: agony $
 * @version $Revision: 165 $
 * 
 */
public class MasterListDetailList {

    public static final String PC_MASTERLIST_LISTMODEL = "masterListListModel";

    public static final String PC_DETAILLIST_LISTMODEL = "detailListListModel";

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private final JList masterList;

    private final JList detailList;

    private ListModelFactory listModelFactory;

    public MasterListDetailList() {
        masterList = new JList();
        masterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        masterList.addListSelectionListener(new MasterListListSelectionListener());
        detailList = new JList();
    }

    /**
     * @return the masterList
     */
    public JList getMasterList() {
        return masterList;
    }

    /**
     * @return the detailList
     */
    public JList getDetailList() {
        return detailList;
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void setMasterListModel(ListModel model) {
        ListModel oldModel = masterList.getModel();
        masterList.setModel(model);
        changeSupport.firePropertyChange(PC_MASTERLIST_LISTMODEL, oldModel, model);
    }

    private void setDetailListModel(ListModel model) {
        ListModel oldModel = detailList.getModel();
        detailList.setModel(model);
        changeSupport.firePropertyChange(PC_DETAILLIST_LISTMODEL, oldModel, model);
    }

    /**
     * @return the listModelFactory
     */
    public ListModelFactory getListModelFactory() {
        return listModelFactory;
    }

    /**
     * @param listModelFactory
     *            the listModelFactory to set
     */
    public void setListModelFactory(ListModelFactory listModelFactory) {
        this.listModelFactory = listModelFactory;
    }

    /**
     * Internal class triggers updates of the list selections of the master list to the detail list
     * 
     * @author Michael Engelhardt<me@mindcrime-ilab.de>
     * @author $Author: agony $
     * @version $Revision: 165 $
     * 
     */
    private class MasterListListSelectionListener implements ListSelectionListener {

        private final ListModel NULL_MODEL = new DefaultListModel();

        @Override
        public void valueChanged(ListSelectionEvent evt) {
            JList list = (JList) evt.getSource();
            Object value = list.getSelectedValue();
            if (null != value) {
                ListModel model = listModelFactory.createListModel(value);
                setDetailListModel(model);
            } else {
                setDetailListModel(NULL_MODEL);
            }
        }
    }
}
