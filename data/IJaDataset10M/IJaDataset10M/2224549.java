package com.ivis.xprocess.ui.tables.columns.definition.task;

import com.ivis.xprocess.core.SchedulingType;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.ITask;
import com.ivis.xprocess.ui.datawrappers.project.ProjectAvailabilityWrapper;
import com.ivis.xprocess.ui.datawrappers.project.RequiredResourceRoleWrapper;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.tables.columns.definition.InstantSaveColumn;
import com.ivis.xprocess.ui.wizards.csvimport.CSVUtil;
import com.ivis.xprocess.ui.wizards.csvimport.ImportCSVException;
import com.ivis.xprocess.util.NumberUtils;

public class TaskSizeColumn extends InstantSaveColumn {

    @Override
    public String getValueAsText(Object element) {
        Xtask task = extractTask(element);
        if (task == null) {
            return "";
        }
        double size = 0;
        if (task.isDesignatedAsParent() && !task.isTopDown()) {
            size = task.getSizeRolledUpFromChildren();
        } else {
            size = task.getSize();
        }
        if ((task.getSchedulingType() == SchedulingType.OVERHEAD) && (size <= 0.0)) {
            return "-";
        }
        return NumberUtils.format(size, 1);
    }

    @Override
    public int compare(Object arg0, Object arg1) {
        if (arg0 instanceof ProjectAvailabilityWrapper) {
            return -1;
        }
        if (arg1 instanceof ProjectAvailabilityWrapper) {
            return 1;
        }
        if (!(arg0 instanceof ITask)) {
            return 1;
        }
        if (!(arg1 instanceof ITask)) {
            return 1;
        }
        ITask task1 = (ITask) arg0;
        ITask task2 = (ITask) arg1;
        return new Double(task1.getTask().getSize()).compareTo(task2.getTask().getSize());
    }

    @Override
    public boolean internalSave(Object object, String text) {
        boolean canSave = true;
        double newSize = 0;
        try {
            newSize = Double.parseDouble(text);
        } catch (NumberFormatException numberFormatException) {
            canSave = false;
        }
        if (!canSave) {
            return false;
        }
        if (object instanceof RequiredResourceRoleWrapper) {
            RequiredResourceRoleWrapper requiredResourceRoleWrapper = (RequiredResourceRoleWrapper) object;
            requiredResourceRoleWrapper.getLocalTransientTaskFor().setSize(newSize);
            return super.internalSave(object, text);
        }
        doSave(object, text);
        return true;
    }

    public Double getSizeFromText(String text) {
        return Double.parseDouble(text);
    }

    @Override
    public void saveChanges(Object object, String text) {
        if (object instanceof IElementWrapper) {
            IElementWrapper elementWrapper = (IElementWrapper) object;
            if (object instanceof ITask) {
                Xtask task = ((ITask) object).getTask();
                Double size = getSizeFromText(text);
                task.setSize(size);
            }
            ChangeEventFactory.startChangeRecording(elementWrapper);
            ChangeEventFactory.addPropertyChange(elementWrapper, Xtask.SIZE);
            ChangeEventFactory.addChange(elementWrapper, ChangeEvent.FIELDS_CHANGED);
            ChangeEventFactory.saveChanges();
            ChangeEventFactory.stopChangeRecording();
        }
    }

    @Override
    public boolean isEditable(Object element) {
        Xtask task = extractTask(element);
        if ((task == null) || (task.getSchedulingType() == SchedulingType.OVERHEAD)) {
            return false;
        }
        if (task.isDesignatedAsParent() && !task.isTopDown()) {
            return false;
        }
        return true;
    }

    @Override
    public Xelement[] importSetValue(Object object, String... params) throws ImportCSVException {
        if (!(object instanceof Xtask)) {
            throw new ImportCSVException("Element imported is not a Task");
        }
        Xtask task = (Xtask) object;
        String text = params[0];
        if (text.length() == 0) {
            return new Xelement[] {};
        }
        try {
            double importedSize = Double.parseDouble(text);
            task.setSize(importedSize);
        } catch (NumberFormatException numberFormatException) {
            throw CSVUtil.createCSVException(task, "Unable to set the size of the Task because the Size: '" + text + "' cannot be converted into a double");
        }
        return new Xelement[] {};
    }
}
