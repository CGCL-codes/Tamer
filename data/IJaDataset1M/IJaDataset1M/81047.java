package hrc.tool.converter;

/**
 * beanת���쳣
 * @author hrc
 *
 */
@SuppressWarnings("serial")
public class ConvertException extends Exception {

    public ConvertException(Throwable throwable, String msg) {
        super(msg, throwable);
    }
}
