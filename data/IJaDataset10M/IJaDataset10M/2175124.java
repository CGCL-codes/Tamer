package org.conann.example;

import org.conann.bindings.LoggedIn;
import org.conann.instrument.WebBeanUtil;
import org.conann.model.User;
import org.conann.model.services.AbstractPaymentProcessor;
import org.conann.model.services.ChequePaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChequePaymentProcessorIMPL extends AbstractPaymentProcessor {

    private static final Logger log = LoggerFactory.getLogger(ChequePaymentProcessor.class);

    @LoggedIn
    private User user;

    public ChequePaymentProcessorIMPL() {
        _initialize();
    }

    private void _initialize() {
        WebBeanUtil.initialize(this);
    }

    public void pay() {
        log.info("Paid by cheque");
    }

    public static void main(String[] args) {
        ChequePaymentProcessorIMPL paymentProcessor = new ChequePaymentProcessorIMPL();
    }
}
