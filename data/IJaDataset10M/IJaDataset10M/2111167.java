package pedro.mda.model;

import pedro.system.PedroResources;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class RecordNameProvider implements Serializable, Cloneable {

    private transient ArrayList changeListeners;

    protected ListFieldModel containingListModel;

    protected String instanceIdentifier;

    protected boolean useInstanceIdentifier;

    protected String toolTipText;

    public RecordNameProvider() {
        changeListeners = new ArrayList();
        instanceIdentifier = PedroResources.EMPTY_STRING;
        useInstanceIdentifier = false;
        toolTipText = PedroResources.EMPTY_STRING;
    }

    public String getToolTip() {
        return toolTipText;
    }

    public abstract String getRecordClassName();

    public abstract String getDisplayName();

    /**
     * change listeners for this record.  The primary change listener is
     * pedro.view.NavigationTree but is generalised to accommodate a list
     */
    public ArrayList getChangeListeners() {
        return changeListeners;
    }

    public RecordModel getParentRecordModel() {
        if (containingListModel == null) {
            return null;
        }
        RecordModel parentRecordModel = containingListModel.getContainingRecord();
        return parentRecordModel;
    }

    public ListFieldModel getContainingListModel() {
        return containingListModel;
    }

    public boolean useInstanceIdentifier() {
        return useInstanceIdentifier;
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    public void setUseInstanceIdentifier(boolean useInstanceIdentifier) {
        this.useInstanceIdentifier = useInstanceIdentifier;
    }

    /**
	* this is an identifier supplied by the parent record.  It is used to 
	* help give the record model a unique display name should it not 
	* contain enough useful display name components to distinguish it in 
	* a list from other components
	*/
    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    /**
     * sets the list field that contains this model.  if the model is the
     * root item, then the list model would be null
     *
     * @param containingListModel the list field that contains this record
     */
    public void setContainingListModel(ListFieldModel containingListModel) {
        this.containingListModel = containingListModel;
    }

    public abstract void updateDisplayName();

    public abstract String computeDisplayName();

    /**
     * removes list of change listeners
     */
    public void clearChangeListeners() {
        changeListeners.clear();
    }

    /**
     * adds change listener.  Typically this is going to be
     * pedro.view.NavigationTree, but the class has been generalised
     * to accept more than one kind of listener
     */
    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    public void setChangeListener(ChangeListener changeListener) {
        clearChangeListeners();
        changeListeners.add(changeListener);
    }

    /**
     * Since I'm making the search results 'live', I'm going to need this
     * @param changeListener - listener to remove if it is registered
     */
    public void removeChangeListener(ChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    /**
     * sets the list of change listeners that watch this record model
     */
    public void setChangeListeners(ArrayList changeListeners) {
        this.changeListeners = changeListeners;
    }

    /**
     * method used to inform listeners of child creation
     */
    public void informListenersOfChildCreation() {
        notifyListeners(ChangeObject.OBJECT_CREATED);
    }

    /**
     * method used to inform listeners of child being deleted
     */
    public void informListenersOfDestruction() {
        notifyListeners(ChangeObject.OBJECT_DELETED);
    }

    /**
     * method used to inform listeners that the record has gained focus
     * this method is invoked when edit button in list field is pressed.
     */
    public void informListenersOfGainedFocus() {
        notifyListeners(ChangeObject.OBJECT_GAINS_FOCUS);
    }

    protected void notifyListeners(int changeType) {
        ChangeObject changeObject = new ChangeObject(this, changeType);
        fireChangeEvent(changeObject);
    }

    public void fireChangeEvent(ChangeObject changeObject) {
        ChangeEvent event = new ChangeEvent(changeObject);
        int numberOfChangeListeners = changeListeners.size();
        for (int i = 0; i < numberOfChangeListeners; i++) {
            ChangeListener currentChangeListener = (ChangeListener) changeListeners.get(i);
            currentChangeListener.stateChanged(event);
        }
    }

    public abstract Object clone();
}
