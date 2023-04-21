package jeeves.exceptions;

public abstract class JeevesServerEx extends JeevesException {

    public JeevesServerEx(String message, Object object) {
        super(message, object);
        id = "server";
        code = 500;
    }
}
