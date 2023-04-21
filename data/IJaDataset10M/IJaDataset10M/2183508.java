package br.com.settech.querycreator;

/**
 * The main exception that should be use to throws exception.
 * @author Alberto Pc
 *
 */
@SuppressWarnings("serial")
public class AdvancedFilterException extends RuntimeException {

    public AdvancedFilterException() {
        super();
    }

    public AdvancedFilterException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AdvancedFilterException(String arg0) {
        super(arg0);
    }

    public AdvancedFilterException(Throwable arg0) {
        super(arg0);
    }
}
