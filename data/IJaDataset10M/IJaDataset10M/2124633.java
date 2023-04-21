package tei.cr.utils.merging.textMerge;

public class MergerException extends Exception {

    public MergerException(String message) {
        super(message);
    }

    public MergerException(String message, Throwable t) {
        super(message, t);
    }
}
