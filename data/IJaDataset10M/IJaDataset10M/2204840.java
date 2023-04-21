package net.sf.buildbox.devmodel.model;

import java.util.Date;
import net.sf.buildbox.devmodel.Identifiable;

/**
 * Comment entity. Comments for a discussion which can be attached to various contexts, like:
 * - vcs location
 * - concrete commit
 * - one file in a commit
 * - one line in a file in specific revision
 * - project
 */
public class Comment extends Identifiable {

    private Date timestamp;

    /**
     * structured and highly formalized identification, suitable for path-based filtering (exact, prefix, contains)
     */
    private String topicUri;

    /**
     * if the text refers also to another context, it can be specified here
     */
    private String secondarySubject;

    /**
     * OBSOLETE/NORMAL/IMPORTANT
     */
    private String status;

    private String author;

    /**
     * message of up to 140 chars
     */
    private String text;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopicUri() {
        return topicUri;
    }

    public void setTopicUri(String topicUri) {
        this.topicUri = topicUri;
    }

    public String getSecondarySubject() {
        return secondarySubject;
    }

    public void setSecondarySubject(String secondarySubject) {
        this.secondarySubject = secondarySubject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        final Comment comment = (Comment) o;
        if (!author.equals(comment.author)) return false;
        if (secondarySubject != null ? !secondarySubject.equals(comment.secondarySubject) : comment.secondarySubject != null) {
            return false;
        }
        if (!status.equals(comment.status)) return false;
        if (!text.equals(comment.text)) return false;
        if (!topicUri.equals(comment.topicUri)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = topicUri.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + (secondarySubject != null ? secondarySubject.hashCode() : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Comment");
        sb.append("{topicUri='").append(topicUri).append('\'');
        sb.append(", secondarySubject='").append(secondarySubject).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", timestamp='").append(timestamp).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
