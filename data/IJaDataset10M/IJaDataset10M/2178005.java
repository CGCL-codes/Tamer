package finderXMLObject.Exception;

;

public class MultipleObjectFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public MultipleObjectFoundException() {
        super();
    }

    public MultipleObjectFoundException(String message) {
        super(message);
    }
}
