package uk.ac.lkl.migen.system.cdst.ui;

import javax.swing.JPanel;
import uk.ac.lkl.migen.system.cdst.model.TeacherModel;
import uk.ac.lkl.migen.system.task.TaskIdentifier;

/**
 * A tool in the Teacher Assistance Tools that reads information from 
 * the teacher model. 
 * 
 * @author sergut
 *
 */
public abstract class TeacherToolView extends JPanel {

    /**
     * The model where the indicators for the students is stored.
     */
    private TeacherModel teacherModel;

    /**
     * The task currently selected. 
     */
    private TaskIdentifier currentTaskId;

    public TeacherToolView(TeacherModel teacherModel) {
        this.teacherModel = teacherModel;
        this.currentTaskId = null;
    }

    public TeacherModel getTeacherModel() {
        return teacherModel;
    }

    protected abstract void doSetMaxTime(Long timestamp);

    /**
     * Used by the container to set the task of this tool.
     * 
     * @param taskId the task identifier
     */
    final void setTask(TaskIdentifier taskId) {
        this.currentTaskId = taskId;
        doSetTask(taskId);
    }

    protected abstract void doSetTask(TaskIdentifier taskId);

    public TaskIdentifier getCurrentTaskId() {
        return currentTaskId;
    }
}
