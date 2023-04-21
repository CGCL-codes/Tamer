package com.prolix.editor.oics.data;

import java.io.File;
import com.prolix.editor.oics.get.OICSGetFile;

public class TMEntry implements OICSEntry {

    private String id;

    private String title;

    private String summary;

    private String author;

    private String content;

    public TMEntry() {
        super();
    }

    /**
	 * @param id
	 * @param generator
	 * @param title
	 * @param summary
	 * @param author
	 * @param content
	 */
    public TMEntry(String id, String title, String summary, String author, String content) {
        super();
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.content = content;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id
	 *           the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title
	 *           the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return the summary
	 */
    public String getSummary() {
        return summary;
    }

    /**
	 * @param summary
	 *           the summary to set
	 */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
	 * @return the author
	 */
    public String getAuthor() {
        return author;
    }

    /**
	 * @param author
	 *           the author to set
	 */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
	 * @return the content
	 */
    public String getContent() {
        return content;
    }

    /**
	 * @param content
	 *           the content to set
	 */
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isValid() {
        if (getId() == null) {
            return false;
        }
        if (getContent() == null) {
            return false;
        }
        if (getTitle() == null) {
            return false;
        }
        return true;
    }

    public String toString() {
        String ret = "TMEntry(" + getId() + "): " + getTitle();
        ret += " " + getContent();
        return ret;
    }

    public File loadContentFile(String path) {
        return new OICSGetFile(getContent(), path).getFile();
    }

    public OICSLOMEntry getLOMData() {
        return null;
    }

    public boolean hasLOMData() {
        return false;
    }
}
