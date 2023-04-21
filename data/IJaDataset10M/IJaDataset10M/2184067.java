package eduburner.search;

import java.util.Date;

/**
 * DTO for search result
 * use dozer to map result?
 * @author zhangyf@gmail.com
 *
 */
public class EntryWrapper {

    private String title;

    private String content;

    private String author;

    private Date published;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }
}
