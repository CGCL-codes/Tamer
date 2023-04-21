package org.sqlanyware.sqlwclient.api.data.user;

import java.util.LinkedList;
import java.util.List;

public class History {

    private String id;

    private User user;

    private List<HistoryLine> lines = new LinkedList<HistoryLine>();

    public List<HistoryLine> getLines() {
        return this.lines;
    }

    protected void setLines(final List<HistoryLine> lines) {
        if (null != lines) {
            this.lines = lines;
        } else {
            this.lines = new LinkedList<HistoryLine>();
        }
    }

    protected String getId() {
        return id;
    }

    protected void setId(final String id) {
        this.id = id;
    }

    /**
	 * Gets the user associated to this history.
	 * 
	 * @return The user associated to this history
	 */
    public User getUser() {
        return this.user;
    }

    /**
	 * Sets the user associated to this history.
	 * 
	 * @param user
	 */
    public void setUser(final User user) {
        this.user = user;
    }
}
