package org.pprun.hjpetstore.web.rest.client;

import org.pprun.hjpetstore.domain.PaymentPartner;
import org.pprun.hjpetstore.domain.RSAKey;
import org.pprun.hjpetstore.persistence.jaxb.DecryptCardNumber;
import org.pprun.hjpetstore.persistence.jaxb.EncryptCardNumber;
import org.springframework.beans.factory.annotation.Required;

/**
 * A SecurityService REST client construct GET, POST request to call internal integration component - SecurityService.
 * <p>
 * All external method call will be through retry policy set in applicationContext, which is spring-batch based.
 *
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public interface SecurityServiceRestClient {

    DecryptCardNumber decryptCardNumber(String cardNumber);

    EncryptCardNumber encryptCardNumber(String cardNumber);

    EncryptCardNumber encryptCardNumberForPartner(String partnerName, String cardNumber);

    RSAKey getEnabledRSAKey();

    @Required
    PaymentPartner getPaymentPartner(String name);
}
