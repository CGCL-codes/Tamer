package org.jcr_blog.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
public class Page implements Serializable, Stageable, Auditable, NamedResource {

    private static final long serialVersionUID = 8191220683487413614L;

    private String id;

    private String title;

    private Date modificationDate;

    private String modificationUser;

    private String resourceName;

    private String text;

    private Stage stage;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Date getModificationDate() {
        return this.modificationDate;
    }

    @Override
    public void setModificationDate(Date date) {
        this.modificationDate = date;
    }

    @Override
    public void setModificationUser(String username) {
        this.modificationUser = username;
    }

    @Override
    public String getModificationUser() {
        return this.modificationUser;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Page other = (Page) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if (this.modificationDate != other.modificationDate && (this.modificationDate == null || !this.modificationDate.equals(other.modificationDate))) {
            return false;
        }
        if ((this.modificationUser == null) ? (other.modificationUser != null) : !this.modificationUser.equals(other.modificationUser)) {
            return false;
        }
        if ((this.resourceName == null) ? (other.resourceName != null) : !this.resourceName.equals(other.resourceName)) {
            return false;
        }
        if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 31 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 31 * hash + (this.modificationDate != null ? this.modificationDate.hashCode() : 0);
        hash = 31 * hash + (this.modificationUser != null ? this.modificationUser.hashCode() : 0);
        hash = 31 * hash + (this.resourceName != null ? this.resourceName.hashCode() : 0);
        hash = 31 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }
}
