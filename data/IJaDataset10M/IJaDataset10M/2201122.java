package org.torweg.pulse.component.shop.checkout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.torweg.pulse.configuration.Configuration;

/**
 * The {@code InvoicePaymentControllerConfiguration}.
 * 
 * @author Christian Schatt
 * @version $Revision$
 */
@XmlRootElement(name = "invoice-payment-controller-configuration")
@XmlAccessorType(XmlAccessType.NONE)
public class InvoicePaymentControllerConfiguration extends Configuration {

    /**
	 * The {@code serialVersionUID}.
	 */
    private static final long serialVersionUID = -8734588811653074677L;

    /**
	 * The no-argument constructor used by JAXB.
	 */
    @Deprecated
    protected InvoicePaymentControllerConfiguration() {
        super();
    }
}
