package net.sf.xmlprocessor.xml.xsd.instances;

/**
 * <code>XSCheckResult</code> contains the results returned by the various
 * <code>accept</code> methods. The <code>XSCheckResult</code> contains the
 * outcome of the <code>accept</code> method and a description.
 * 
 * @author Emond Papegaaij
 */
public class XSCheckResult {

    private String reason;

    private boolean valid;

    private XSInstance acceptingInstance;

    /**
	 * Creates a new <code>XSCheckResult</code> with a description.
	 * 
	 * @param valid The outcome of the check.
	 * @param reason The reason for the given outcome.
	 */
    public XSCheckResult(boolean valid, String reason) {
        this.reason = reason;
        this.valid = valid;
    }

    /**
	 * Creates a new <code>XSCheckResult</code> without a description.
	 * 
	 * @param acceptingInstance The instance accepting the check.
	 */
    public XSCheckResult(XSInstance acceptingInstance) {
        this.reason = "Valid Result";
        this.valid = true;
        this.acceptingInstance = acceptingInstance;
    }

    /**
	 * Creates a new <code>XSCheckResult</code> without a description.
	 * 
	 * @param valid The outcome of the check.
	 */
    public XSCheckResult(boolean valid) {
        this(valid, (valid) ? "Valid Result" : "Invalid Result");
    }

    /**
	 * Returns the reason for the outcome.
	 * 
	 * @return The reason for the outcome.
	 */
    public String getReason() {
        return reason;
    }

    /**
	 * Returns the outcome of the check.
	 * 
	 * @return the outcome of the check.
	 */
    public boolean isValid() {
        return valid;
    }

    /**
	 * Overrides the current accepting instance. When <code>isValid</code> is
	 * false, nothing is done and a subsequent call to
	 * <code>getAcceptingInstance</code> will still return null.
	 * 
	 * @param acceptingInstance The new accepting instance.
	 */
    public void setAcceptingInstance(XSInstance acceptingInstance) {
        if (isValid()) {
            this.acceptingInstance = acceptingInstance;
        }
    }

    /**
	 * Returns the instance accepting the check.
	 * 
	 * @return The instance accepting the check.
	 */
    public XSInstance getAcceptingInstance() {
        return acceptingInstance;
    }

    /**
	 * Returns a string representation of the <code>XSCheckResult</code> that
	 * is useful for debugging.
	 * 
	 * @return A string representation of the <code>XSCheckResult</code>.
	 */
    public String toString() {
        return "XSCheckResult(" + valid + ", " + reason + ")";
    }
}
