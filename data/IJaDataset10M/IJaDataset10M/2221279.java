package jeeves.exceptions;

public abstract class NotAllowedEx extends JeevesClientEx {

    public NotAllowedEx(String message, Object object) {
        super(message, object);
        id = "not-allowed";
    }
}
