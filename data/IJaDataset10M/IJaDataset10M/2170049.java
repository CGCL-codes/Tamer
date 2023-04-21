package net.sf.kmoviecataloger.core;

/**
 * Simple wrapper for String, in order to maintain a better control over the labels
 * a movie has. It allows for better search capabilities and management.
 *
 * Label is a simple support class.
 *
 * @author sergiolopes
 */
public class Label {

    private long id;

    private String text;

    public Label() {
    }

    public Label(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Label)) {
            return false;
        }
        Label o = (Label) other;
        return this.id == o.id && this.text.equals(other);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 19 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }
}
