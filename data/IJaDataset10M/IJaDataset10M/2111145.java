package org.telscenter.sail.webapp.domain.brainstorm;

import java.io.Serializable;
import org.telscenter.sail.webapp.domain.Run;
import org.telscenter.sail.webapp.domain.project.Project;

/**
 * @author patrick lawler
 * @version $Id:$
 */
public class CreateSingleChoiceBrainstormParameters implements Serializable {

    private Run run;

    private Project project;

    private String question;

    private String choices;

    private String correctChoice;

    private DisplayNameOption displayNameOption;

    private boolean isGated;

    private boolean isRichTextEditorAllowed;

    private Questiontype questionType;

    private static final long serialVersionUID = 1L;

    /**
	 * @return the run
	 */
    public Run getRun() {
        return run;
    }

    /**
	 * @param run the run to set
	 */
    public void setRun(Run run) {
        this.run = run;
    }

    /**
	 * @return the project
	 */
    public Project getProject() {
        return project;
    }

    /**
	 * @param project the project to set
	 */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
	 * @return the correctQuestion
	 */
    public String getCorrectChoice() {
        return correctChoice;
    }

    /**
	 * @param correctQuestion the correctQuestion to set
	 */
    public void setCorrectChoice(String correctChoice) {
        this.correctChoice = correctChoice;
    }

    /**
	 * @return the displayNameOption
	 */
    public DisplayNameOption getDisplayNameOption() {
        return displayNameOption;
    }

    /**
	 * @param displayNameOption the displayNameOption to set
	 */
    public void setDisplayNameOption(DisplayNameOption displayNameOption) {
        this.displayNameOption = displayNameOption;
    }

    /**
	 * @return the isGated
	 */
    public boolean isGated() {
        return isGated;
    }

    /**
	 * @param isGated the isGated to set
	 */
    public void setGated(boolean isGated) {
        this.isGated = isGated;
    }

    /**
	 * @return the isRichTextEditorAllowed
	 */
    public boolean isRichTextEditorAllowed() {
        return isRichTextEditorAllowed;
    }

    /**
	 * @param isRichTextEditorAllowed the isRichTextEditorAllowed to set
	 */
    public void setRichTextEditorAllowed(boolean isRichTextEditorAllowed) {
        this.isRichTextEditorAllowed = isRichTextEditorAllowed;
    }

    /**
	 * @return the questionType
	 */
    public Questiontype getQuestionType() {
        return questionType;
    }

    /**
	 * @param questionType the questionType to set
	 */
    public void setQuestionType(Questiontype questionType) {
        this.questionType = questionType;
    }

    /**
	 * @return the serialVersionUID
	 */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
	 * @return the question
	 */
    public String getQuestion() {
        return question;
    }

    /**
	 * @param question the question to set
	 */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
	 * @return the choices
	 */
    public String getChoices() {
        return choices;
    }

    /**
	 * @param choices the choices to set
	 */
    public void setChoices(String choices) {
        this.choices = choices;
    }
}
