package net.recaptcha.jca.cci;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import javax.resource.cci.ConnectionSpec;

/**
 * Implements <code>ConnectionSpec</code> in the JCA specification.
 * @author Marcus Andersson (lucaf3rr@gmail.com)
 */
public class ConnectionSpecImpl implements ConnectionSpec {

    private final Log log = LogFactory.getLog(ConnectionSpecImpl.class);

    private String recaptchaHostName;

    private String recaptchaPrivateKey;

    /**
     * Public default constructor.
     */
    public ConnectionSpecImpl() {
        log.trace("ENTER/EXIT: <constructor>");
    }

    /**
     * Accessor for property <code>recaptchaPrivateKey</code>.
     * @return A private key
     */
    public String getRecaptchaPrivateKey() {
        if (log.isTraceEnabled()) {
            log.trace("ENTER/EXIT: getRecaptchaPrivateKey()[" + recaptchaPrivateKey + "]");
        }
        return recaptchaPrivateKey;
    }

    /**
     * Mutator for property <code>recaptchaPrivateKey</code>.
     * @param recaptchaPrivateKey A private key
     */
    public void setRecaptchaPrivateKey(String recaptchaPrivateKey) {
        if (log.isTraceEnabled()) {
            log.trace("ENTER/EXIT: setRecaptchaPrivateKey(String [" + recaptchaPrivateKey + "])");
        }
        this.recaptchaPrivateKey = recaptchaPrivateKey;
    }

    /**
     * Accessor for property <code>recaptchaHostName</code>.
     * @return A host name
     */
    public String getRecaptchaHostName() {
        if (log.isTraceEnabled()) {
            log.trace("ENTER/EXIT: getRecaptchaHostName()[" + recaptchaHostName + "]");
        }
        return recaptchaHostName;
    }

    /**
     * Mutator for property <code>recaptchaHostName</code>.
     * @param recaptchaHostName A host name
     */
    public void setRecaptchaHostName(String recaptchaHostName) {
        if (log.isTraceEnabled()) {
            log.trace("ENTER/EXIT: setRecaptchaHostName(String [" + recaptchaHostName + "])");
        }
        this.recaptchaHostName = recaptchaHostName;
    }

    /**
     * Overrides default method with a simple <code>String</code> representation of
     * this object's state.
     * @return A String representation of this object
     */
    public String toString() {
        return "recaptchaPrivateKey:=[" + getRecaptchaPrivateKey() + "]  recaptchaHostName:=[" + getRecaptchaHostName() + "]";
    }
}
