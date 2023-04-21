package com.prolix.editor.interaction.commands.operations.modify;

import java.util.List;
import org.eclipse.gef.commands.Command;
import com.prolix.editor.interaction.model.test.McTestInteraction;
import com.prolix.editor.interaction.model.test.TestInteraction;
import com.prolix.editor.interaction.operation.Operation;
import com.prolix.editor.interaction.operation.basic.BasicOperationWrite;
import com.prolix.editor.interaction.operation.test.TestOperationWrite;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class ModifyOperationWrite extends Command {

    private Operation operation;

    private String text;

    private String oldText;

    private List oldQuestions;

    private List newQuestions;

    private int oldAnzQuestions;

    private int newAnzQuestions;

    private int oldAnzAnswers;

    private int newAnzAnswers;

    private ModifyOperationWrite(Operation operation) {
        super("Modify: " + operation.getName());
        this.operation = operation;
        oldText = getOperationText();
    }

    public ModifyOperationWrite(BasicOperationWrite operation) {
        this((Operation) operation);
    }

    public ModifyOperationWrite(TestOperationWrite operation) {
        this((Operation) operation);
    }

    private String getOperationText() {
        if (operation instanceof BasicOperationWrite) return ((BasicOperationWrite) operation).getText();
        return ((TestOperationWrite) operation).getText();
    }

    private void setOperationText(String text) {
        if (operation instanceof BasicOperationWrite) ((BasicOperationWrite) operation).setText(text); else ((TestOperationWrite) operation).setText(text);
    }

    private void setInteractionQuestions(List questions) {
        if (questions == null) {
            return;
        }
        if (this.operation.getInteraction() instanceof TestInteraction) {
            ((TestInteraction) this.operation.getInteraction()).setTestQuestions(questions);
        }
    }

    private void setInteractionAnzQuestions(int anzQuestions, int anzAnswers) {
        if (anzQuestions <= 0) {
            return;
        }
        if (this.operation.getInteraction() instanceof TestInteraction) {
            ((TestInteraction) this.operation.getInteraction()).setAnzQuestion(anzQuestions);
            if (anzAnswers > 0) {
                ((McTestInteraction) this.operation.getInteraction()).setAnzAnswers(anzAnswers);
            }
        }
    }

    /**
	 * @param text
	 *           the text to set
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * @param oldQuestions
	 *           the oldQuestions to set
	 */
    public void setOldQuestions(List oldQuestions) {
        this.oldQuestions = oldQuestions;
    }

    /**
	 * @param newQuestions
	 *           the newQuestions to set
	 */
    public void setNewQuestions(List newQuestions) {
        this.newQuestions = newQuestions;
    }

    /**
	 * @param oldAnzQuestions
	 *           the oldAnzQuestions to set
	 */
    public void setOldAnzQuestions(int oldAnzQuestions) {
        this.oldAnzQuestions = oldAnzQuestions;
    }

    /**
	 * @param newAnzQuestions
	 *           the newAnzQuestions to set
	 */
    public void setNewAnzQuestions(int newAnzQuestions) {
        this.newAnzQuestions = newAnzQuestions;
    }

    /**
	 * @param oldAnzAnswers
	 *           the oldAnzAnswers to set
	 */
    public void setOldAnzAnswers(int oldAnzAnswers) {
        this.oldAnzAnswers = oldAnzAnswers;
    }

    /**
	 * @param newAnzAnswers
	 *           the newAnzAnswers to set
	 */
    public void setNewAnzAnswers(int newAnzAnswers) {
        this.newAnzAnswers = newAnzAnswers;
    }

    public boolean canExecute() {
        return text != null && !text.trim().isEmpty();
    }

    public void execute() {
        setOperationText(text);
        setInteractionQuestions(this.newQuestions);
        setInteractionAnzQuestions(this.newAnzQuestions, this.newAnzAnswers);
    }

    public void redo() {
        execute();
    }

    public void undo() {
        setOperationText(oldText);
        setInteractionQuestions(this.oldQuestions);
        setInteractionAnzQuestions(this.oldAnzQuestions, this.oldAnzAnswers);
    }
}
