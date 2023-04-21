package jp.ekasi.pms.ui.ganttchart.gef.command;

import java.util.Calendar;
import jp.ekasi.pms.model.ModelPackage;
import jp.ekasi.pms.model.Task;
import jp.ekasi.pms.model.command.TaskFinishUpdateCommand;
import jp.ekasi.pms.ui.gef.emf.GefEmfEditDomain;
import jp.ekasi.pms.ui.gef.emf.command.AbstractEmfCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;

/**
 * �^�X�N�̃o�[�`���[�g�𓮂��������̃R�}���h.
 * @author Yuusuke Hikime
 */
public class TaskbarMoveCommand extends AbstractEmfCommand {

    protected Task owner;

    protected Calendar value;

    /**
	 * �R���X�g���N�^.<br>
	 * @param editingDomain
	 */
    public TaskbarMoveCommand(GefEmfEditDomain editingDomain, Task owner, Calendar value) {
        super();
        this.editingDomain = editingDomain.getEmfEditingDomain();
        this.owner = owner;
        this.value = value;
    }

    @Override
    protected Command createEmfCommand() {
        CompoundCommand command = new CompoundCommand();
        command.append(org.eclipse.emf.edit.command.SetCommand.create(editingDomain, owner, ModelPackage.Literals.TASK__START, value));
        command.append(new TaskFinishUpdateCommand(editingDomain, owner));
        return command;
    }
}
