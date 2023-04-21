package org.broadleafcommerce.core.payment.service.module;

import javax.annotation.Resource;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.service.PaymentInfoService;

public abstract class AbstractModule implements PaymentModule {

    @Resource(name = "blPaymentInfoService")
    private PaymentInfoService paymentInfoService;

    protected PaymentResponseItem getNewResponseItem() {
        return paymentInfoService.createResponseItem();
    }
}
