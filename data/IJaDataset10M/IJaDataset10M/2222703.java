package tripleo.framework.svr.storage;

class ss_exception extends Exception {

    public ss_exception(String message) {
        super(message);
    }

    public ss_exception(Throwable acause) {
        super(acause);
    }
}
