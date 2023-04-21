package org.sqlanyware.struts.forms;

import org.apache.struts.action.ActionForm;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class QuizForm extends ActionForm {

    /**
    * Returns the answer.
    * @return String
    */
    public String getAnswer() {
        return m_sAnswer;
    }

    /**
    * Returns the name.
    * @return String
    */
    public String getName() {
        return m_sName;
    }

    /**
    * Returns the question.
    * @return String
    */
    public String getQuestion() {
        return m_sQuestion;
    }

    /**
    * Sets the answer.
    * @param answer The answer to set
    */
    public void setAnswer(String answer) {
        this.m_sAnswer = answer;
    }

    /**
    * Sets the name.
    * @param name The name to set
    */
    public void setName(String name) {
        this.m_sName = name;
    }

    /**
    * Sets the question.
    * @param question The question to set
    */
    public void setQuestion(String question) {
        this.m_sQuestion = question;
    }

    protected String m_sName;

    protected String m_sAnswer;

    protected String m_sQuestion;
}
