package net.sf.gm.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <code>GMThreadSnapshot</code>
 * 
 * This is a snapshot of a Thread. It includes the HTML data.
 * @author tzellman
 * 
 */
public class GMThreadSnapshot implements Serializable {

    /**
     * Gets or sets the thread identifier.
     */
    private String threadID = "";

    /**
     * Gets or sets the 'thread is read' flag.
     */
    private boolean read = false;

    /**
     * Gets or sets the 'thread is starred' flag.
     */
    private boolean starred = false;

    /**
     * Gets or sets the HTML-formatted thread date.
     */
    private String dateHtml = "";

    /**
     * Gets or sets the HTML-formatted thread author(s) text.
     */
    private String authorsHtml = "";

    /**
     * Gets or sets the flags String (unknown).
     */
    private String flags = "";

    /**
     * Gets or sets the thread subject.
     */
    private String subjectHtml = "";

    /**
     * Gets or sets the thread snippet.
     */
    private String snippetHtml = "";

    /**
     * Gets or sets a list of zero or more categories in which the thread is
     * classified.
     */
    private Collection<String> categories = new ArrayList<String>();

    /**
     * Gets or sets the HTML-formatted 'thread has attachment' text (contains an
     * IMG tag).
     */
    private String attachHtml = "";

    /**
     * Gets or sets the thread-message link identifier.
     */
    private String matchingMessageID = "";

    public GMThreadSnapshot() {
    }

    /**
     * @return Returns the attachHtml.
     */
    public String getAttachHtml() {
        return attachHtml;
    }

    /**
     * @param attachHtml
     *            The attachHtml to set.
     */
    public void setAttachHtml(String attachHtml) {
        this.attachHtml = attachHtml;
    }

    /**
     * @return Returns the authorsHtml.
     */
    public String getAuthorsHtml() {
        return authorsHtml;
    }

    /**
     * @param authorsHtml
     *            The authorsHtml to set.
     */
    public void setAuthorsHtml(String authorsHtml) {
        this.authorsHtml = authorsHtml;
    }

    /**
     * @return Returns the categories.
     */
    public Iterable<String> getCategories() {
        return categories;
    }

    /**
     * @param categories
     *            The categories to set.
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * @return Returns the dateHtml.
     */
    public String getDateHtml() {
        return dateHtml;
    }

    /**
     * @param dateHtml
     *            The dateHtml to set.
     */
    public void setDateHtml(String dateHtml) {
        this.dateHtml = dateHtml;
    }

    /**
     * @return Returns the flags.
     */
    public String getFlags() {
        return flags;
    }

    /**
     * @param flags
     *            The flags to set.
     */
    public void setFlags(String flags) {
        this.flags = flags;
    }

    /**
     * @return Returns the matchingMessageID.
     */
    public String getMatchingMessageID() {
        return matchingMessageID;
    }

    /**
     * @param matchingMessageID
     *            The matchingMessageID to set.
     */
    public void setMatchingMessageID(String matchingMessageID) {
        this.matchingMessageID = matchingMessageID;
    }

    /**
     * @return Returns the read.
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read
     *            The read to set.
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return Returns the snippetHtml.
     */
    public String getSnippetHtml() {
        return snippetHtml;
    }

    /**
     * @param snippetHtml
     *            The snippetHtml to set.
     */
    public void setSnippetHtml(String snippetHtml) {
        this.snippetHtml = snippetHtml;
    }

    /**
     * @return Returns the starred.
     */
    public boolean isStarred() {
        return starred;
    }

    /**
     * @param starred
     *            The starred to set.
     */
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    /**
     * @return Returns the subjectHtml.
     */
    public String getSubjectHtml() {
        return subjectHtml;
    }

    /**
     * @param subjectHtml
     *            The subjectHtml to set.
     */
    public void setSubjectHtml(String subjectHtml) {
        this.subjectHtml = subjectHtml;
    }

    /**
     * @return Returns the threadID.
     */
    public String getThreadID() {
        return threadID;
    }

    /**
     * @param threadID
     *            The threadID to set.
     */
    public void setThreadID(String threadID) {
        this.threadID = threadID;
    }

    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof GMThreadSnapshot)) return false;
        GMThreadSnapshot castOther = (GMThreadSnapshot) other;
        return new EqualsBuilder().append(threadID, castOther.threadID).append(read, castOther.read).append(starred, castOther.starred).append(dateHtml, castOther.dateHtml).append(authorsHtml, castOther.authorsHtml).append(flags, castOther.flags).append(subjectHtml, castOther.subjectHtml).append(snippetHtml, castOther.snippetHtml).append(categories, castOther.categories).append(attachHtml, castOther.attachHtml).append(matchingMessageID, castOther.matchingMessageID).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(threadID).append(read).append(starred).append(dateHtml).append(authorsHtml).append(flags).append(subjectHtml).append(snippetHtml).append(categories).append(attachHtml).append(matchingMessageID).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("threadID", threadID).append("read", read).append("starred", starred).append("dateHtml", dateHtml).append("authorsHtml", authorsHtml).append("flags", flags).append("subjectHtml", subjectHtml).append("snippetHtml", snippetHtml).append("categories", categories).append("attachHtml", attachHtml).append("matchingMessageID", matchingMessageID).toString();
    }
}
