package mudownmanager.backend;

public class PropertiesAccessExcpetion extends Exception {

    private static final long serialVersionUID = -4440549501624146561L;

    public PropertiesAccessExcpetion() {
        super();
    }

    public PropertiesAccessExcpetion(String message) {
        super(message);
    }

    public PropertiesAccessExcpetion(Throwable cause) {
        super(cause);
    }

    public PropertiesAccessExcpetion(String message, Throwable cause) {
        super(message, cause);
    }
}
