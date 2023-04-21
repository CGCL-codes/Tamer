package org.torweg.pulse.component.shop.payment;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Christian Schatt
 * @version $Revision$
 */
@XmlRootElement(name = "wirecard-eft-payment-response")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
public final class WirecardEFTPaymentResponse implements IWirecardPaymentResponse {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = 3575099259948988689L;

    /**
	 * The <code>WirecardEFTPaymentRequest</code>.
	 */
    @XmlElement(name = "payment-request", required = true)
    private WirecardEFTPaymentRequest paymentRequest = null;

    /**
	 * The <code>PaymentStatus</code>.
	 */
    @XmlElement(name = "payment-status", required = true)
    private PaymentStatus paymentStatus = null;

    /**
	 * The error message.
	 */
    @XmlElement(name = "error-message")
    private String errorMessage = null;

    /**
	 * The (self) generated transaction id.
	 */
    @XmlElement(name = "transaction-id", required = true)
    private String transactionId = null;

    /**
	 * The GuWID ("Globally unique Wirecard ID") received from Wirecard.
	 */
    @XmlElement(name = "guwid", required = true)
    private String guWID = null;

    /**
	 * the no-argument constructor.
	 */
    protected WirecardEFTPaymentResponse() {
        super();
    }

    /**
	 * Returns the <code>WirecardEFTPaymentRequest</code>.
	 * 
	 * @return the <code>WirecardEFTPaymentRequest</code>
	 */
    public WirecardEFTPaymentRequest getPaymentRequest() {
        return this.paymentRequest;
    }

    /**
	 * Sets the <code>WirecardEFTPaymentRequest</code>.
	 * 
	 * @param request
	 *            the <code>WirecardEFTPaymentRequest</code> to set
	 * @return the <code>WirecardEFTPaymentResponse</code> itself
	 */
    protected WirecardEFTPaymentResponse setPaymentRequest(final WirecardEFTPaymentRequest request) {
        this.paymentRequest = request;
        return this;
    }

    /**
	 * Returns the <code>PaymentStatus</code>.
	 * 
	 * @return the <code>PaymentStatus</code>
	 */
    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    /**
	 * Sets the <code>PaymentStatus</code>.
	 * 
	 * @param status
	 *            the <code>PaymentStatus</code> to set
	 * @return the <code>WirecardEFTPaymentResponse</code> itself
	 */
    protected WirecardEFTPaymentResponse setPaymentStatus(final PaymentStatus status) {
        this.paymentStatus = status;
        return this;
    }

    /**
	 * Returns the error message.
	 * 
	 * @return the error message
	 */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
	 * Sets the error message.
	 * 
	 * @param error
	 *            the error message to set
	 * @return the <code>WirecardEFTPaymentResponse</code> itself
	 */
    protected WirecardEFTPaymentResponse setErrorMessage(final String error) {
        this.errorMessage = error;
        return this;
    }

    /**
	 * Returns the (self) generated transaction id.
	 * 
	 * @return the transaction id
	 */
    public String getTransactionId() {
        return this.transactionId;
    }

    /**
	 * Sets the (self) generated transaction id.
	 * 
	 * @param id
	 *            the transaction id to set
	 * @return the <code>WirecardEFTPaymentResponse</code> itself
	 */
    protected WirecardEFTPaymentResponse setTransactionId(final String id) {
        this.transactionId = id;
        return this;
    }

    /**
	 * Returns the GuWID ("Globally unique Wirecard ID") received from Wirecard.
	 * 
	 * @return the GuWID
	 */
    public String getGuWID() {
        return this.guWID;
    }

    /**
	 * Sets the GuWID ("Globally unique Wirecard ID") received from Wirecard.
	 * 
	 * @param id
	 *            the GuWID to set
	 * @return the <code>WirecardEFTPaymentResponse</code> itself
	 */
    protected WirecardEFTPaymentResponse setGuWID(final String id) {
        this.guWID = id;
        return this;
    }
}
