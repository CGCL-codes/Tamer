package mipt.gui.data.choice.tree;

import mipt.gui.data.choice.DataListChooser;
import mipt.gui.data.choice.event.DataChoiceEvent;
import mipt.gui.data.choice.event.DataListChoiceEvent;
import mipt.data.Data;
import mipt.data.choice.event.DataListSelector;
import mipt.data.choice.event.DataSelectListener;
import mipt.data.event.*;
import mipt.common.NotifiableListenerList;

/**
 * @author: Administrator
 */
public class TreeSelector implements DataListChooser, DataListSelector {

    protected NotifiableListenerList listenerList = new NotifiableListenerList();

    protected TreeManager manager = null;

    protected boolean isSilent = false;

    /**
 * TreeSelector constructor comment.
 */
    public TreeSelector(TreeManager manager) {
        super();
        this.manager = manager;
        manager.getTree().addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent event) {
                if (isSilent) return;
                fireSelected();
            }
        });
    }

    /**
 * 
 */
    public void addSelectListener(DataSelectListener listener) {
        listenerList.add(DataSelectListener.class, listener);
    }

    /**
 * 
 */
    public void chooseAll() {
        setChosenList(manager.getModel().getAllList());
    }

    /**
 * calls the method of treeModel nodechanged(Node)
 */
    public void dataChanged(Data dataToUpdate) {
        javax.swing.tree.DefaultMutableTreeNode treeNodeToUpdate = ((TreeChoiceModel) manager.getModel()).getNodeWith(dataToUpdate);
        if (treeNodeToUpdate == null) return;
        isSilent = true;
        manager.getTreeModel().nodeChanged(treeNodeToUpdate);
        isSilent = false;
    }

    /**
 * notifies listners about selection
 */
    public void fireSelected() {
        if (listenerList.getListenerCount() == 0) return;
        DataChoiceEvent choiceEvent = null;
        if (manager.isSingleChoiceMode()) {
            choiceEvent = new DataChoiceEvent(manager, getChosenData());
        } else {
            choiceEvent = new DataListChoiceEvent(manager, getChosenList());
        }
        listenerList.notifyListeners(DataSelectListener.class, "selected", choiceEvent, DataEvent.class);
    }

    /**
 * getChosenCount method comment.
 */
    public int getChosenCount() {
        return manager.getTree().getSelectionCount();
    }

    /**returns one of the following objects:          need to be tested :))
  //       - selected data;
  //       - first selected data if more than 1 selected;
  //       - null if there is no selected data
 * getChosenData method comment.
 */
    public Data getChosenData() {
        javax.swing.tree.TreePath path = manager.getTree().getSelectionPath();
        if (path == null) return null;
        return manager.getData(path);
    }

    /**
 * analoguous to getChosenData.
 */
    public Data[] getChosenList() {
        javax.swing.tree.TreePath[] treePath = manager.getTree().getSelectionPaths();
        if (treePath == null || treePath.length == 0) return new Data[0];
        mipt.data.impl.DataList dataToReturn = new mipt.data.impl.DataList();
        for (int i = 0; i < treePath.length; i++) {
            dataToReturn.add(manager.getData(treePath[i]));
        }
        return dataToReturn.toArray();
    }

    /**
 * makes the node that consists of dataToView visible!
 */
    public void makeVisible(Data dataToView) {
        manager.getTree().scrollPathToVisible(manager.getTree().pathOf(dataToView));
    }

    /**
 * removes all listners
 */
    public void removeSelectListeners() {
        listenerList.remove(DataSelectListener.class);
    }

    /**
 * selectAll method comment.
 */
    public void selectAll() {
        this.chooseAll();
        fireSelected();
    }

    /**
 * selectData method comment.
 */
    public void selectData(Data dataToSelect) {
        setChosenData(dataToSelect);
        fireSelected();
    }

    /**
 * selectList method comment.
 */
    public void selectList(Data[] dataToSelect) {
        setChosenList(dataToSelect);
        fireSelected();
    }

    /**
 * invokes tree method setSelectionPath with node which consists of DataToChoose
 */
    public void setChosenData(Data dataToChoose) {
        isSilent = true;
        manager.getTree().setSelectionPath(dataToChoose == null ? null : manager.getTree().pathOf(dataToChoose));
        isSilent = false;
    }

    /**
 * if datachoose.length == 0 then clearAllselecion
 If manager Is single mode selection then the first element will be selected
 If manager Is not single selection then all elements will be selected
 */
    public void setChosenList(Data[] dataToChoose) {
        isSilent = true;
        manager.getTree().clearSelection();
        for (int i = 0; i < dataToChoose.length; i++) {
            manager.getTree().addSelectionPath(manager.getTree().pathOf(dataToChoose[i]));
        }
        isSilent = false;
    }
}
