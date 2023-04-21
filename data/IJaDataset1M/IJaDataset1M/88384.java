package info.nekonya.xml.beanReader.exceptions;

public class BeanExtractorException extends Exception {

    private static final long serialVersionUID = 6114721994323615059L;

    public BeanExtractorException() {
        super();
    }

    public BeanExtractorException(String desc, Throwable cause) {
        super(desc, cause);
    }

    public BeanExtractorException(String desc) {
        super(desc);
    }

    public BeanExtractorException(Throwable cause) {
        super(cause);
    }
}
