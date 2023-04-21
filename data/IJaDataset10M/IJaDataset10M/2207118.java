package br.ufpe.cin.stp.mass.model.session;

import java.util.List;
import java.util.Vector;
import br.ufpe.cin.stp.mass.persistence.Persistent;

/**
 * The answer is the response to a given Session. If it's a questionary,
 * the correct answers will be seen on each question item of the questions.
 * If it's a survey, answers will be always the identifiers of the Session.
 * @author Marcello Sales Jr.
 * @version 1.0
 * @created 24-jul-2004 18:18:44
 */
public class Answer extends Persistent {

    /**
     * <code>senderID</code> is the sender identification.
     */
    private String senderID;

    /**
     * <code>sessionID</code> is the session identification.
     */
    private String sessionID;

    /**
     * <code>itemsAnsweredID</code> is a list of the items that
     * the listener have selected.
     */
    private List itemsAnsweredID;

    /**
	 * Creates a new Answer.
	 * @param sessionID is the session identification.
	 * @param senderID is the sender identification.
	 * @created 25/07/2004 10:09:48
	 */
    public Answer(String sessionID, String senderID) {
        this.sessionID = sessionID;
        this.senderID = senderID;
        this.itemsAnsweredID = new Vector();
    }

    /**
	 * Adds a set of itemsID that are the chosen items of the
	 * session.
	 * @param itemsID
	 * @created 25/07/2004 10:06:29
	 */
    public void addAnswer(String[] itemsID) {
        for (int i = 0; i < itemsID.length; i++) {
            this.itemsAnsweredID.add(itemsID[i]);
        }
    }

    /**
     * @return Returns the itemsAnsweredID.
     * @created 25/07/2004 11:29:27
     */
    public String[] getItemsAnswered() {
        return (String[]) this.itemsAnsweredID.toArray(new String[0]);
    }

    /**
     * @return Returns the senderID.
     * @created 25/07/2004 11:29:27
     */
    public String getSenderID() {
        return this.senderID;
    }

    /**
     * @return Returns the sessionID.
     * @created 25/07/2004 11:29:27
     */
    public String getSessionID() {
        return this.sessionID;
    }
}
