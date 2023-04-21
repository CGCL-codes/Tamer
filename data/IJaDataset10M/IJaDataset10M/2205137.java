package net.adrianromero.tpv.payment;

import net.adrianromero.tpv.forms.*;

public class PaymentGatewayFac {

    /** Creates a new instance of PaymentGatewayFac */
    private PaymentGatewayFac() {
    }

    public static PaymentGateway getPaymentGateway(String sReader, AppProperties app) {
        if ("external".equals(sReader)) {
            return new PaymentGatewayExt();
        } else if ("SECPay".equals(sReader)) {
            return new PaymentGatewaySECPay(app);
        } else if ("AuthorizeNet".equals(sReader)) {
            return new PaymentGatewayAuthorizeNet(app);
        } else {
            return null;
        }
    }
}
