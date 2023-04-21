package ggc.core.data.errors;

public class DateTimeError extends Error {

    /**
     * Eclipse says this class is serializable and therefore should have this
     * field.
     */
    static final long serialVersionUID = 1L;

    public DateTimeError(String msg) {
        super(msg);
    }
}
