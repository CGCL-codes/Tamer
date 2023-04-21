package fr.gestionimmo.exception;

public class ManagedBeanException extends Exception {

    private static final long serialVersionUID = 735059199938238410L;

    public ManagedBeanException(Exception e) {
        super(e);
    }
}
