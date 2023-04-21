package net.sourceforge.myvd.protocol.ldap.mina.ldap.exception;

import javax.naming.InvalidNameException;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.message.ResultCodeEnum;

/**
 * A subclass of InvalidNameException designed to hold an unequivocal LDAP
 * result code.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev: 437007 $
 */
public class LdapInvalidNameException extends InvalidNameException implements LdapException {

    static final long serialVersionUID = 1922458280238058561L;

    /** the LDAP resultCode this exception is associated with */
    private final ResultCodeEnum resultCode;

    /**
     * Creates an Eve NamingException.
     * 
     * @param resultCode
     *            the LDAP resultCode this exception is associated with
     */
    public LdapInvalidNameException(ResultCodeEnum resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * Creates an Eve NamingException.
     * 
     * @param explanation
     *            an explanation for the failure
     * @param resultCode
     *            the LDAP resultCode this exception is associated with
     */
    public LdapInvalidNameException(String explanation, ResultCodeEnum resultCode) {
        super(explanation);
        this.resultCode = resultCode;
    }

    /**
     * Gets the LDAP resultCode this exception is associated with.
     * 
     * @return the LDAP resultCode this exception is associated with
     */
    public ResultCodeEnum getResultCode() {
        return resultCode;
    }
}
