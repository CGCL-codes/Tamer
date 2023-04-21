package yaw.core.uml.adapter;

public class ModelError extends Exception {

    private static final long serialVersionUID = 1L;

    public ModelError() {
        super();
    }

    public ModelError(String msg) {
        super(msg);
    }

    public ModelError(Throwable e) {
        super(e);
    }

    public ModelError(String msg, Throwable e) {
        super(msg, e);
    }
}
