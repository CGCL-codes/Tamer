package net.nohaven.proj.javeau.fs.exceptions;

public class EntityNotFoundException extends Exception {

    private static final long serialVersionUID = -90533249446961287L;

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String arg0) {
        super(arg0);
    }

    public EntityNotFoundException(Throwable arg0) {
        super(arg0);
    }

    public EntityNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
