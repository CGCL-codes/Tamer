package ch.richfaces.wua.la.model.comment;

import java.util.Date;
import ch.richfaces.wua.la.util.Utils;

public class Comment {

    private String author;

    private Date time;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append("Author: ").append(author).append("\n");
        sb.append("Time: ").append(Utils.getFormatedDate(time)).append("\n");
        sb.append("Comment: ").append(content).append("\n");
        return sb.toString();
    }
}
