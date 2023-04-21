package org.plugger.ui.viewmodel;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ListModel;
import org.plugger.infrastructure.domainbase.EntityBase;
import org.plugger.infrastructure.repositorybase.IWorkspace;
import org.plugger.ui.base.*;

/**
 * An abstract List Controller.
 *
 * @author "Antonio Begue"
 * @version $Revision: 1.0 $
 */
public abstract class ListableViewModel<T extends EntityBase> extends ViewModelBase {

    private IWorkspace ws;

    private List<T> entitiesList;

    private SelectionInList<T> entitiesView;

    private ArrayListModel<T> managedEntities;

    private DelegateAction editAction;

    private DelegateAction newAction;

    private DelegateAction deleteAction;

    private DelegateAction copyAction;

    protected ListableViewModel() {
        this(null);
    }

    protected ListableViewModel(IView view) {
        super(view);
        this.editAction = new DelegateAction("Edit", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editCommandHandler(e);
            }
        });
        this.newAction = new DelegateAction("New", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newCommandHandler(e);
            }
        });
        this.deleteAction = new DelegateAction("Delete", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteCommandHandler(e);
            }
        });
        this.copyAction = new DelegateAction("Copy", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                copyCommandHandler(e);
            }
        });
    }

    protected void initialize() {
        populateList();
    }

    protected void populateList() {
        this.entitiesList = this.generateEntitiesList();
        if (this.entitiesView == null) {
            this.entitiesView = new SelectionInList<T>(createManagedEntitiesList());
        } else {
            this.entitiesView.setListModel(createManagedEntitiesList());
        }
    }

    private ListModel createManagedEntitiesList() {
        managedEntities = new ArrayListModel<T>(this.getEntitiesList());
        return managedEntities;
    }

    protected abstract List<T> generateEntitiesList();

    protected abstract void editCurrentEntity();

    protected abstract T buildNewEntity();

    protected abstract T copyCurrentEntity();

    protected abstract boolean deleteCurrentEntity();

    public void refresh() {
        T currentEnt = getEntitiesView().getSelection();
        populateList();
        if (currentEnt != null) {
            for (int i = 0; i < getEntitiesView().getSize(); i++) {
                if (getEntitiesView().getElementAt(i).getKey().equals(currentEnt.getKey())) {
                    currentEnt = getEntitiesView().getElementAt(i);
                }
            }
        }
        getEntitiesView().setSelection(currentEnt);
        getEntitiesView().fireSelectedContentsChanged();
    }

    /**
     * @return the currentEntity
     */
    public T getCurrentEntity() {
        return getEntitiesView().getSelection();
    }

    /**
     * @param currentEntity the currentEntity to set
     */
    public void setCurrentEntity(T currentEntity) {
        getEntitiesView().setSelection(currentEntity);
    }

    /**
     * @return the entitiesView
     */
    public SelectionInList<T> getEntitiesView() {
        return entitiesView;
    }

    /**
     * @return the entitiesList
     */
    public List<T> getEntitiesList() {
        return entitiesList;
    }

    /**
     * @return the editAction
     */
    public DelegateAction getEditAction() {
        return editAction;
    }

    /**
     * @return the newAction
     */
    public DelegateAction getNewAction() {
        return newAction;
    }

    /**
     * @return the deleteAction
     */
    public DelegateAction getDeleteAction() {
        return deleteAction;
    }

    /**
     * @return the copyAction
     */
    public DelegateAction getCopyAction() {
        return copyAction;
    }

    /**
     * @return the ws
     */
    public IWorkspace getWs() {
        return ws;
    }

    /**
     * @param ws the ws to set
     */
    public void setWs(IWorkspace ws) {
        this.ws = ws;
    }

    /**
     * @return the managedEntities
     */
    public ArrayListModel<T> getManagedEntities() {
        return managedEntities;
    }

    public void editCommandHandler(ActionEvent e) {
        editCurrentEntity();
        getEntitiesView().fireSelectedContentsChanged();
    }

    public void newCommandHandler(ActionEvent e) {
        T newEntity = this.buildNewEntity();
        if (newEntity == null) {
            return;
        }
        getManagedEntities().add(newEntity);
        getEntitiesView().fireSelectedContentsChanged();
        entitiesView.setSelection(newEntity);
    }

    public void copyCommandHandler(ActionEvent e) {
        T newEntity = this.copyCurrentEntity();
        if (newEntity == null) {
            return;
        }
        this.getManagedEntities().add(newEntity);
        this.setCurrentEntity(null);
        getEntitiesView().fireSelectedContentsChanged();
        entitiesView.setSelection(newEntity);
    }

    public void deleteCommandHandler(ActionEvent e) {
        boolean resp = deleteCurrentEntity();
        if (this.getCurrentEntity() == null || resp == false) {
            return;
        }
        this.managedEntities.remove(this.getCurrentEntity());
        this.setCurrentEntity(null);
        getEntitiesView().fireSelectedContentsChanged();
    }
}
