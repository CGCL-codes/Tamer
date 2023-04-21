package eip.chapter5.messagefilter.impl;

import eip.chapter5.messagefilter.DeclarationMessage;
import eip.chapter5.messagefilter.PaymentService;

public class PaymentServiceImpl implements PaymentService {

    public void processDeclaration(DeclarationMessage msg) {
        System.out.println("PaymentService: Processed declaration message: " + msg);
    }
}
