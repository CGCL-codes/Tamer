package net.sourceforge.epoint;

/**
 * <code>Exception</code> indicating provable fraudulent issuer behavior
 *
 * @version 0.1
 * @author <a href="mailto:nagydani@users.sourceforge.net">Daniel A. Nagy</a>
 */
public abstract class FraudException extends java.lang.Exception {

    private Issuer issuer;

    /**
     * @return the fraudulent <code>Issuer</code>
     */
    public Issuer getIssuer() {
        return issuer;
    }

    /**
     * @param msg explaination
     * @param i fraudulent <code>Issuer</code>
     */
    public FraudException(String msg, Issuer i) {
        super(msg);
        issuer = i;
    }

    /**
     * @return an array of certificates proving the fraud
     */
    public abstract EPointCertificate[] getProof();
}
