package cx.prutser.nonogram;

public class LowerLimitExceededException extends ArrayIndexOutOfBoundsException {

    static final long serialVersionUID = -8155290603300998014L;

    public LowerLimitExceededException() {
        super();
    }

    public LowerLimitExceededException(int index) {
        super(index);
    }

    public LowerLimitExceededException(String s) {
        super(s);
    }
}
