package org.databene.benerator;

/**
 * Indicates an error in Benerator execution, for example raised by an &lt;error&gt; element.<br/><br/>
 * Created: 12.01.2011 09:13:11
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class BeneratorError extends RuntimeException {

    private static final long serialVersionUID = 4922982624810176934L;

    protected int code;

    public BeneratorError(String s, Throwable throwable, int code) {
        super(s, throwable);
        this.code = code;
    }

    public BeneratorError(String s, int code) {
        super(s);
        this.code = code;
    }

    public BeneratorError(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
