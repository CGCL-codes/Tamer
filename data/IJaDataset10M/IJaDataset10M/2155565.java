package org.broadleafcommerce.vendor.cybersource.service;

import org.broadleafcommerce.vendor.cybersource.service.payment.message.CyberSourcePaymentRequest;
import org.broadleafcommerce.vendor.cybersource.service.payment.message.CyberSourcePaymentResponse;
import org.broadleafcommerce.profile.vendor.service.exception.PaymentException;

/**
 * 
 * @author jfischer
 *
 */
public interface CyberSourcePaymentService extends CyberSourceService {

    public CyberSourcePaymentResponse process(CyberSourcePaymentRequest paymentRequest) throws PaymentException;
}
