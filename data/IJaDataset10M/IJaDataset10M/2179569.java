package jp.ekasi.pms.model.command;

import jp.ekasi.common.util.DateUtil;
import jp.ekasi.pms.model.Task;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * �I������X�V����R�}���h. �J�n��Ɗ�Ԃ����ɏI������X�V���܂��B
 * 
 * @author Yuusuke Hikime
 */
public class TaskFinishUpdateCommand extends AbstractCommand {

    protected EditingDomain editingDomain;

    protected Task task;

    protected long finishTimeInMillis;

    /**
	 * �R���X�g���N�^.<br>
	 * 
	 * @param editingDomain
	 * @param task
	 */
    public TaskFinishUpdateCommand(EditingDomain editingDomain, Task task) {
        super();
        this.editingDomain = editingDomain;
        this.task = task;
    }

    public void execute() {
        calcurationFinishTimeInMillis();
        task.getFinish().setTimeInMillis(finishTimeInMillis);
    }

    public void redo() {
        execute();
    }

    @Override
    protected boolean prepare() {
        return task != null;
    }

    /**
	 * @return the editingDomain
	 */
    public EditingDomain getEditingDomain() {
        return editingDomain;
    }

    /**
	 * @param editingDomain
	 *            the editingDomain to set
	 */
    public void setEditingDomain(EditingDomain editingDomain) {
        this.editingDomain = editingDomain;
    }

    /**
	 * @return the task
	 */
    public Task getTask() {
        return task;
    }

    /**
	 * @param task
	 *            the task to set
	 */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
	 * FIXME �I������v�Z����B
	 */
    protected void calcurationFinishTimeInMillis() {
        finishTimeInMillis = task.getStart().getTimeInMillis() + (task.getDuration() * DateUtil.HOUR);
    }
}
