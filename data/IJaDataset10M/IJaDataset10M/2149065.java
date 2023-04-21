package com.global360.sketchpadbpmn.documents;

import java.util.ArrayList;
import javax.swing.*;
import com.global360.sketchpadbpmn.*;
import com.global360.sketchpadbpmn.i18n.Messages;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ProcessIdComboBoxModel extends DefaultComboBoxModel {

    private static final long serialVersionUID = 1L;

    class ProcessWrapper {

        private WorkflowProcess process = null;

        public ProcessWrapper(Object process) {
            setProcess((WorkflowProcess) process);
        }

        public ProcessWrapper(WorkflowProcess process) {
            setProcess(process);
        }

        public String toString() {
            if (this.process == WorkflowProcess.NULL) return Messages.getString("ProcessIdComboBoxModel.none"); else return this.process.getIdString();
        }

        public void setProcess(WorkflowProcess process) {
            this.process = process;
        }

        public WorkflowProcess getProcess() {
            return this.process;
        }
    }

    private ArrayList<WorkflowProcess> processList = null;

    public ProcessIdComboBoxModel(ArrayList<WorkflowProcess> processes) {
        this.processList = processes;
        super.addElement(new ProcessWrapper(WorkflowProcess.NULL));
        for (WorkflowProcess process : processes) {
            super.addElement(new ProcessWrapper(process));
        }
    }

    /**
   * Returns the value at the specified index.
   *
   * @param index the requested index
   * @return the value at <code>index</code>
   */
    public Object getElementAt(int index) {
        ProcessWrapper wrapper = (ProcessWrapper) super.getElementAt(index);
        return wrapper;
    }

    /**
   *
   * @return The selected item or <code>null</code> if there is no selection
   */
    public Object getSelectedItem() {
        Object object = super.getSelectedItem();
        ProcessWrapper wrapper = (ProcessWrapper) object;
        if (wrapper != null) {
            return wrapper;
        }
        return null;
    }

    /**
   * Set the selected item.
   *
   * @param anItem the list object to select or <code>null</code> to clear the selection
   */
    public void setSelectedItem(Object anItem) {
        int index = getIndexOf(anItem);
        if (index >= 0) {
            Object object = super.getElementAt(index);
            if (!(object instanceof ProcessWrapper)) {
                SketchpadApplication.log.error("Error.");
            }
            super.setSelectedItem(object);
        }
    }

    /**
   * Adds an item at the end of the model.
   *
   * @param obj the <code>Object</code> to be added
   */
    public void addElement(Object obj) {
        super.addElement(new ProcessWrapper(obj));
        this.processList.add((WorkflowProcess) obj);
    }

    /**
   * Adds an item at a specific index.
   *
   * @param obj the <code>Object</code> to be added
   * @param index location to add the object
   */
    public void insertElementAt(Object obj, int index) {
        super.insertElementAt(new ProcessWrapper(obj), index);
        this.processList.set(index, (WorkflowProcess) obj);
    }

    /**
   * Removes an item from the model.
   *
   * @param obj the <code>Object</code> to be removed
   */
    public void removeElement(Object obj) {
        int index = this.getIndexOf(obj);
        if (index >= 0) {
            removeElementAt(index);
        }
    }

    /**
   * Removes an item at a specific index.
   *
   * @param index location of object to be removed
   */
    public void removeElementAt(int index) {
        super.removeElementAt(index);
        this.processList.remove(index);
    }

    /**
   * Returns the index-position of the specified object in the list.
   *
   * @param anObject Object
   * @return an int representing the index position, where 0 is the first position
   */
    public int getIndexOf(Object targetObject) {
        WorkflowProcess target = null;
        if (targetObject instanceof ProcessWrapper) {
            target = ((ProcessWrapper) targetObject).getProcess();
        } else {
            target = (WorkflowProcess) targetObject;
        }
        if (target == WorkflowProcess.NULL) {
            return 0;
        }
        if (target == null) {
            return 0;
        }
        for (int i = 0; i < this.processList.size(); i++) {
            WorkflowProcess item = this.processList.get(i);
            if ((item == null) || (target == null) || (item.getId() == null) || (target.getId() == null)) {
                return -1;
            }
            if (item.getId().sameAs(target.getId())) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
   * Empties the list.
   *
   */
    public void removeAllElements() {
        super.removeAllElements();
        this.processList.clear();
    }

    /**
   * getSelectedProcess
   *
   * @return WorkflowProcess
   */
    public WorkflowProcess getSelectedProcess() {
        ProcessWrapper wrapper = (ProcessWrapper) super.getSelectedItem();
        if (wrapper != null) {
            return wrapper.getProcess();
        }
        return null;
    }
}
