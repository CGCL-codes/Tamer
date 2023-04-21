package jeeves.exceptions;

public abstract class BadInputEx extends JeevesClientEx {

    public BadInputEx(String message, Object object) {
        super(message, object);
        id = "bad-input";
    }
}
