package br.ufpe.cin.stp.mass.model.session;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import br.ufpe.cin.stp.mass.persistence.Persistent;

/**
 * The Survey Session is used to indicate the state of questions. Date, results and
 * other global information is concentrated on the session.
 * @author Marcello Sales Jr.
 * @version 1.0
 * @created 24-jul-2004 18:18:44
 */
public abstract class AbstractSession extends Persistent implements Session {

    /**
     * <code>title</code> is the title of the session.
     */
    private String title;

    /**
	 * The questions of the questionary
	 */
    protected List questions;

    /**
	 * <code>answers</code> is the answers of the listeners.
	 */
    protected List answers;

    /**
	 * <code>opened</code> defines if the session is available.
	 */
    private boolean opened;

    /**
	 * <code>startDate</code> is the start date of the session
	 */
    private GregorianCalendar date;

    /**
	 * Creates a session with the initial date.
	 * @created 24/07/2004 22:30:58
	 */
    public AbstractSession(String title) {
        this.title = title;
        this.answers = new Vector();
        this.questions = new Vector();
        this.opened = true;
        this.date = new GregorianCalendar();
    }

    /**
	 * The questions of the application
	 */
    public Iterator getQuestions() {
        return this.questions.iterator();
    }

    public Iterator getAnswers() {
        return this.answers.iterator();
    }

    public int numberOfAnswers() {
        return this.answers.size();
    }

    /**
	 * Adds an answer to the session from the listeners.
	 * @param answer The answer of the session
	 */
    public void addAnswer(Answer answer) throws SessionClosedException {
        if (this.opened) this.answers.add(answer); else throw new SessionClosedException("This session is already closed and impossible to receive votes.", this);
    }

    /**
	 * The start day of the session
	 */
    public GregorianCalendar getTime() {
        return this.date;
    }

    /**
     * @return Returns the title.
     * @created 25/07/2004 08:56:26
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title The title to set.
     * @created 25/07/2004 08:56:26
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns if the session is still available
     * @created 25/07/2004 08:56:26
     */
    public boolean isOpened() {
        return this.opened;
    }

    /**
     * @return Returns the date of the survey.
     * @created 25/07/2004 08:56:26
     */
    public GregorianCalendar getDate() {
        return this.date;
    }

    /**
     * Adds a question to the session.
     * @param question
     * @created 25/07/2004 09:16:19
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void closeSession() {
        this.opened = false;
    }
}
