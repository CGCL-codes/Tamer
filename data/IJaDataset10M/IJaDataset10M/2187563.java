package org.torweg.pulse.component.shop.checkout.processor;

import java.util.Set;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.bundle.stepwise.BasicActionProcessorConfiguration;
import org.torweg.pulse.bundle.stepwise.BasicActionProcessorResult;
import org.torweg.pulse.bundle.stepwise.IActionProcessor;
import org.torweg.pulse.bundle.stepwise.IActionProcessorConfiguration;
import org.torweg.pulse.component.shop.checkout.AbstractPaymentMethod;
import org.torweg.pulse.component.shop.checkout.AbstractShipmentMethod;
import org.torweg.pulse.component.shop.checkout.BasicShipmentMethodConfiguration;
import org.torweg.pulse.component.shop.checkout.CheckoutController;
import org.torweg.pulse.component.shop.checkout.CheckoutData;
import org.torweg.pulse.component.shop.checkout.CheckoutException;
import org.torweg.pulse.component.shop.checkout.Order;
import org.torweg.pulse.configuration.ConfigurationException;
import org.torweg.pulse.service.request.Command;
import org.torweg.pulse.service.request.ServiceRequest;

/**
 * Selects and sets the shipment method.
 * 
 * @author Christian Schatt
 * @version $Revision$
 */
public final class ShipmentMethodSelectionProcessor implements IActionProcessor {

    /**
	 * The configuration of the <code>ShipmentMethodSelectionProcessor</code>.
	 */
    private BasicActionProcessorConfiguration config = null;

    /**
	 * Selects and sets the shipment method.
	 * 
	 * @param bundle
	 *            the current <code>Bundle</code>
	 * @param request
	 *            the current <code>ServiceRequest</code>
	 * @return a <code>BasicActionProcessorResult</code>
	 * @throws ConfigurationException .
	 */
    public BasicActionProcessorResult execute(final Bundle bundle, final ServiceRequest request) {
        BasicActionProcessorResult result = new BasicActionProcessorResult();
        result.setActionProcessorClassName(getClass().getCanonicalName());
        Command command = request.getCommand();
        if (command.getParameter("saveShipmentMethod") == null) {
            result.setSuccess(validate(bundle, request));
            return result;
        }
        CheckoutData data = CheckoutController.getCheckoutData(bundle, request);
        String code = command.getParameter("shipmentMethodIdCode").getFirstValue();
        AbstractShipmentMethod shipment = null;
        for (BasicShipmentMethodConfiguration conf : data.getAvailableShipmentMethodConfigurations()) {
            if (conf.getIdCode().equals(code)) {
                try {
                    shipment = (AbstractShipmentMethod) Class.forName(conf.getShipmentMethodClassName()).newInstance();
                } catch (InstantiationException e) {
                    throw new ConfigurationException(e.getLocalizedMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new ConfigurationException(e.getLocalizedMessage(), e);
                } catch (ClassNotFoundException e) {
                    throw new ConfigurationException(e.getLocalizedMessage(), e);
                }
                shipment.init(conf);
                break;
            }
        }
        if (shipment == null) {
            throw new CheckoutException("The shipment method with the id code '" + code + "' is not available.");
        }
        Order order = data.getOrder();
        AbstractPaymentMethod payment = order.getPaymentMethod();
        if (payment != null) {
            Set<String> combPays = shipment.getCombinablePaymentMethodIdCodes();
            Set<String> combShips = payment.getCombinableShipmentMethodIdCodes();
            if ((!combPays.isEmpty() && !combPays.contains(payment.getIdCode())) || (!combShips.isEmpty() && !combShips.contains(shipment.getIdCode()))) {
                order.setPaymentMethod(null);
            }
        }
        order.setShipmentMethod(shipment);
        return result.setSuccess(true);
    }

    /**
	 * Validates the selection of the shipment method.
	 * 
	 * @param bundle
	 *            the current <code>Bundle</code>
	 * @param request
	 *            the current <code>ServiceRequest</code>
	 * @return <code>true</code> if the selection of the shipment method is
	 *         valid. Returns <code>false</code>, otherwise.
	 */
    public boolean validate(final Bundle bundle, final ServiceRequest request) {
        Order order = CheckoutController.getCheckoutData(bundle, request).getOrder();
        if (order == null) {
            return false;
        }
        AbstractShipmentMethod shipment = order.getShipmentMethod();
        if (shipment == null) {
            return false;
        }
        AbstractPaymentMethod payment = order.getPaymentMethod();
        if (payment == null) {
            return true;
        }
        Set<String> combPays = shipment.getCombinablePaymentMethodIdCodes();
        Set<String> combShips = payment.getCombinableShipmentMethodIdCodes();
        return (combPays.isEmpty() || combPays.contains(payment.getIdCode())) && (combShips.isEmpty() || combShips.contains(shipment.getIdCode()));
    }

    /**
	 * Indicates whether the execution of the related <code>ActionStep</code>
	 * should fail, if the <code>ShipmentMethodSelectionProcessor</code> did not
	 * execute successfully.
	 * 
	 * @return <code>true</code> if the execution of the related
	 *         <code>ActionStep</code> should fail, if the
	 *         <code>ShipmentMethodSelectionProcessor</code> did not execute
	 *         successfully. Returns <code>false</code>, otherwise.
	 */
    public boolean isFailOnErrors() {
        return this.config.isFailOnErrors();
    }

    /**
	 * Indicates whether the <code>ShipmentMethodSelectionProcessor</code>
	 * should be executed, even if the related <code>ActionStep</code> has
	 * already failed due to the failure of a previously executed
	 * <code>IActionProcessor</code>.
	 * 
	 * @return <code>true</code> if the
	 *         <code>ShipmentMethodSelectionProcessor</code> should be executed,
	 *         even if the related <code>ActionStep</code> has already failed
	 *         due to the failure of a previously executed
	 *         <code>IActionProcessor</code>. Returns <code>false</code>,
	 *         otherwise.
	 */
    public boolean isExecuteOnFailure() {
        return this.config.isExecuteOnFailure();
    }

    /**
	 * Initializes the <code>ShipmentMethodSelectionProcessor</code> with the
	 * given <code>IActionProcessorConfiguration</code>.
	 * 
	 * @param conf
	 *            the <code>IActionProcessorConfiguration</code>
	 * @return the <code>ShipmentMethodSelectionProcessor</code> itself
	 * @throws NullPointerException
	 *             if the given <code>IActionProcessorConfiguration</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the given <code>IActionProcessorConfiguration</code> is
	 *             not an instance of
	 *             <code>BasicActionProcessorConfiguration</code>.
	 */
    public ShipmentMethodSelectionProcessor initialize(final IActionProcessorConfiguration conf) {
        if (conf == null) {
            throw new NullPointerException(getClass().getCanonicalName() + ".config must not be null.");
        }
        if (!(conf instanceof BasicActionProcessorConfiguration)) {
            throw new IllegalArgumentException(getClass().getCanonicalName() + ".config must not be an instance of " + conf.getClass().getCanonicalName() + ".");
        }
        this.config = (BasicActionProcessorConfiguration) conf;
        return this;
    }
}
