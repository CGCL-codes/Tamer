package restlet.description.convert;

public class Handler {

    String path;

    boolean active;

    public Handler() {
    }

    public Handler(String path, boolean active) {
        this.path = path;
        this.active = active;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
