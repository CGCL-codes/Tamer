package net.martinimix.service.payment.provider.cybersource;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import net.martinimix.service.payment.CreditCardTransactionFailedException;
import net.martinimix.service.payment.provider.cybersource.context.CyberSourceConfiguration;
import net.martinimix.service.payment.provider.cybersource.context.CyberSourceServiceRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cybersource.ws.client.Client;
import com.cybersource.ws.client.ClientException;
import com.cybersource.ws.client.FaultException;

/**
 * Provides an abstract CyberSource implementation of credit card processor.
 * 
 * <p>
 * This class provides convenience methods common to all CyberSource order processors.
 * 
 * </p>
 * 
 * @author Scott Rossillo
 *
 */
public abstract class AbstractCyberSourcePaymentProcessor {

    private static final Log log = LogFactory.getLog(AbstractCyberSourcePaymentProcessor.class);

    protected CyberSourceConfiguration cyberSourceConfiguration;

    /**
	 * Creates a new abstract CyberSource credit card processor.
	 */
    protected AbstractCyberSourcePaymentProcessor() {
    }

    /**
	 * Logs a CyberSource message.
	 */
    private void logMessage(Map message) {
        StringBuffer buf = new StringBuffer(message.size() * 200);
        String key;
        for (Iterator it = message.keySet().iterator(); it.hasNext(); ) {
            key = (String) it.next();
            buf.append("\t[" + key + "] -> [" + message.get(key) + "]\n");
        }
        log.debug(buf.toString());
    }

    /**
	 * Runs a CyberSource transaction.
	 * 
	 * @param serviceRequest the request for CyberSource services
	 * 
	 * @return a <code>Map</code> containing CyberSource's response to the given
	 * <code>serviceRequest</code>
	 * 
	 * @throws CreditCardTransactionFailedException if CyberSource payment service throws
	 * a <code>FaultException</code> or a <code>ClientException</code>
	 */
    protected final Map runTransaction(CyberSourceServiceRequest serviceRequest) {
        if (cyberSourceConfiguration == null) {
            throw new IllegalStateException("CyberSource configuration must be set before running a transaction!");
        }
        if (serviceRequest == null) {
            throw new IllegalArgumentException("[CyberSourceServiceRequest] cannot be null!");
        }
        return this.runTransaction(serviceRequest.toMap(), cyberSourceConfiguration.toProperties());
    }

    /**
	 * Runs a CyberSource transaction.
	 * 
	 * @param serviceRequest a <code>Map</code> containing the request for CyberSource services
	 * @param cyberSourceConfigurationProps the request <code>Properties</code> to pass to
	 * CyberSource's client
	 * 
	 * @return a <code>Map</code> containing CyberSource's response to the given
	 * <code>serviceRequest</code>
	 * 
	 * @throws CreditCardTransactionFailedException if CyberSource payment service throws
	 * a <code>FaultException</code> or a <code>ClientException</code>
	 */
    protected final Map runTransaction(Map serviceRequest, Properties cyberSourceConfigurationProps) {
        Map response;
        if (log.isDebugEnabled()) {
            log.debug("CyberSource request:");
            logMessage(serviceRequest);
        }
        try {
            response = Client.runTransaction(serviceRequest, cyberSourceConfigurationProps);
        } catch (FaultException e) {
            throw new CreditCardTransactionFailedException("Unable to process transaction: " + e.getMessage(), e);
        } catch (ClientException e) {
            throw new CreditCardTransactionFailedException("Unable to process transaction: " + e.getMessage(), e);
        }
        if (log.isDebugEnabled()) {
            log.debug("CyberSource response:");
            logMessage(response);
        }
        return response;
    }

    /**
	 * Returns the configuration for this credit card service.
	 * 
	 * @return the <code>CyberSourceConfiguration</code> for this service
	 */
    public CyberSourceConfiguration getCyberSourceConfiguration() {
        return cyberSourceConfiguration;
    }

    /**
	 * Sets the configuration for this credit card service.
	 * 
	 * @param cyberSourceConfiguration the <code>CyberSourceConfiguration</code>
	 * for this service
	 */
    public void setCyberSourceConfiguration(CyberSourceConfiguration cyberSourceConfiguration) {
        this.cyberSourceConfiguration = cyberSourceConfiguration;
    }
}
