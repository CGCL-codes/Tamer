package simplespider.simplespider.enity;

public class Link {

    public static final String GET_NEXT = "LINK_GET_NEXT";

    private Long id = null;

    private String url = null;

    private boolean done = false;

    private int errors = 0;

    public Link() {
    }

    public Link(final String url) {
        this.url = url;
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isDone() {
        return this.done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Link [id:" + this.id + ",url:\"" + this.url + "\",done:" + this.done + "]";
    }

    public int getErrors() {
        return this.errors;
    }

    public void setErrors(final int errors) {
        this.errors = errors;
    }
}
