package org.torweg.pulse.component.cms;

import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.util.io.FastStringWriter;

/**
 * Wraps a {@code Throwable} as
 * {@code AbstractCMSFormProcessorMailTaskEmailData} for usage with the
 * {@code  CMSFormProcessorMailTaskResult}.
 * 
 * @author Daniel Dietz
 * @version $Revision: 2017 $
 */
@XmlRootElement(name = "cms-form-processor-mail-task-email-error")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class CMSFormProcessorMailTaskEmailError extends AbstractCMSFormProcessorMailTaskEmailData<CMSFormProcessorMailTaskEmailError> {

    /**
	 * The throwable.
	 */
    @XmlTransient
    private Throwable throwable;

    /**
	 * Default constructor.
	 */
    @Deprecated
    protected CMSFormProcessorMailTaskEmailError() {
        super();
    }

    /**
	 * Creates a new {@code CMSFormProcessorMailTaskEmailError} with the given
	 * {@code Throwable}.
	 * 
	 * @param t
	 *            the {@code Throwable}
	 */
    protected CMSFormProcessorMailTaskEmailError(final Throwable t) {
        super();
        this.throwable = t;
    }

    /**
	 * Not implemented.
	 * 
	 * @param request
	 *            the current {@code ServiceRequest}
	 * 
	 * @return NOTHING
	 * 
	 * @throws UnsupportedOperationException
	 *             always
	 * 
	 * @see org.torweg.pulse.component.cms.AbstractCMSFormProcessorMailTaskEmailData
	 *      #newEmailData(org.torweg.pulse.service.request.ServiceRequest)
	 */
    @Override
    public final CMSFormProcessorMailTaskEmailError newEmailData(final ServiceRequest request) {
        throw new UnsupportedOperationException("CMSFormProcessorMailTaskEmailError.newEmailData(request) is NOT supported.");
    }

    /**
	 * Returns the localized message of the {@code Throwable}.
	 * 
	 * @return the localized message
	 */
    @XmlElement(name = "localized-message")
    public final String getLocalizedMessage() {
        if (this.throwable == null) {
            return null;
        }
        return this.throwable.getLocalizedMessage();
    }

    /**
	 * Returns the stack trace.
	 * 
	 * @return the stack trace
	 * 
	 * @throws IOException
	 *             on IO errors
	 */
    @XmlElement(name = "stack-trace")
    public final String getStackTrace() throws IOException {
        if (this.throwable == null) {
            return null;
        }
        FastStringWriter writer = new FastStringWriter();
        PrintWriter out = new PrintWriter(writer);
        this.throwable.printStackTrace(out);
        return writer.toString();
    }
}
