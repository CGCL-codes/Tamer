package com.emental.mindraider.ui.outline.treetable;

import javax.swing.tree.DefaultMutableTreeNode;
import com.emental.mindraider.ui.outline.NotebookOutlineNode;

/**
 * Entry represents a resource's outline. It contains a URL, a user definable
 * string, and two dates, one giving the date the URL was last visited and the
 * other giving the date the bookmark was created.
 * 
 * @author Martin Dvorak
 */
public class NotebookOutlineEntry extends DefaultMutableTreeNode implements NotebookOutlineNode {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    public String uri;

    private String label;

    private String annotation;

    private String created;

    /**
     * Constructor.
     */
    public NotebookOutlineEntry(String uri, String label, String annotation, String created) {
        this.label = label;
        this.annotation = annotation;
        this.created = created;
        this.uri = uri;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public String toString() {
        return getLabel();
    }

    public String getUri() {
        return uri;
    }
}
