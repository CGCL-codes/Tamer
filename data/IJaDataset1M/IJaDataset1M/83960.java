package org.torweg.pulse.component.shop.checkout;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The basic configuration of a shipment method.
 * 
 * @author Christian Schatt
 * @version $Revision$
 */
@XmlRootElement(name = "basic-shipment-method-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
public class BasicShipmentMethodConfiguration implements Serializable {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = -9114198327374309046L;

    /**
	 * The canonical class name of the shipment method configured by this
	 * <code>BasicShipmentMethodConfiguration</code>.
	 */
    @XmlElement(name = "shipment-method-class-name", required = true)
    private String shipmentMethodClassName;

    /**
	 * The id code of the shipment method.
	 */
    @XmlElement(name = "id-code", required = true)
    private String idCode;

    /**
	 * The <code>ExtraCharge</code> of the shipment method or <code>null</code>,
	 * if the shipment method does no have an <code>ExtraCharge</code>.
	 */
    @XmlElement(name = "extra-charge")
    private ExtraCharge extraCharge = null;

    /**
	 * The minimal order value needed for the shipment method to be available or
	 * 0, if there is no minimal order value.
	 */
    @XmlElement(name = "min-order-value")
    private long minOrderValue = 0L;

    /**
	 * The maximal order value needed for the shipment method to be available or
	 * 0, if there is no maximal order value.
	 */
    @XmlElement(name = "max-order-value")
    private long maxOrderValue = 0L;

    /**
	 * A <code>Set</code> holding the id codes of the payment methods combinable
	 * with this shipment method or an empty <code>Set</code>, if the shipment
	 * method is combinable with any payment method.
	 */
    @XmlElementWrapper(name = "combinable-payment-method-id-codes")
    @XmlElement(name = "combinable-payment-method-id-code")
    private final Set<String> combinablePaymentMethodIdCodes = new HashSet<String>();

    /**
	 * A <code>Set</code> holding the names of the <code>Role</code>s granting
	 * access to this shipment method or an empty <code>Set</code>, if no
	 * <code>Role</code> is necessary to access this shipment method.
	 */
    @XmlElementWrapper(name = "role-names")
    @XmlElement(name = "role-name")
    private final Set<String> roleNames = new HashSet<String>();

    /**
	 * The no-argument constructor.
	 */
    protected BasicShipmentMethodConfiguration() {
        super();
    }

    /**
	 * The constructor that initializes the
	 * <code>BasicShipmentMethodConfiguration</code>'s values with those from
	 * the <code>BasicShipmentMethodConfigurationBuilder</code>.
	 * 
	 * @param builder
	 *            the <code>BasicShipmentMethodConfigurationBuilder</code>
	 */
    public BasicShipmentMethodConfiguration(final BasicShipmentMethodConfigurationBuilder builder) {
        super();
        setShipmentMethodClassName(builder.getShipmentMethodClassName());
        setIdCode(builder.getIdCode());
        setExtraCharge(builder.getExtraCharge());
        setMinOrderValue(builder.getMinOrderValue());
        setMaxOrderValue(builder.getMaxOrderValue());
        setCombinablePaymentMethodIdCodes(builder.getCombinablePaymentMethodIdCodes());
    }

    /**
	 * Returns the canonical class name of the shipment method configured by
	 * this <code>BasicShipmentMethodConfiguration</code>.
	 * 
	 * @return the canonical class name of the shipment method configured by
	 *         this <code>BasicShipmentMethodConfiguration</code>
	 */
    public final String getShipmentMethodClassName() {
        return this.shipmentMethodClassName;
    }

    /**
	 * Sets the canonical class name of the shipment method configured by this
	 * <code>BasicShipmentMethodConfiguration</code>.
	 * 
	 * @param name
	 *            the class name to set
	 * @return the <code>BasicShipmentMethodConfiguration</code> itself
	 */
    protected final BasicShipmentMethodConfiguration setShipmentMethodClassName(final String name) {
        this.shipmentMethodClassName = name;
        return this;
    }

    /**
	 * Returns the id code of the shipment method.
	 * 
	 * @return the id code
	 */
    public final String getIdCode() {
        return this.idCode;
    }

    /**
	 * Sets the id code of the shipment method.
	 * 
	 * @param code
	 *            the id code to set
	 * @return the <code>BasicShipmentMethodConfiguration</code> itself
	 */
    protected final BasicShipmentMethodConfiguration setIdCode(final String code) {
        this.idCode = code;
        return this;
    }

    /**
	 * Returns the <code>ExtraCharge</code> of the shipment method or
	 * <code>null</code>, if the shipment method does not have an
	 * <code>ExtraCharge</code>.
	 * 
	 * @return the <code>ExtraCharge</code> or <code>null</code>
	 */
    public final ExtraCharge getExtraCharge() {
        return this.extraCharge;
    }

    /**
	 * Sets the <code>ExtraCharge</code> of the shipment method.
	 * 
	 * @param extra
	 *            the <code>ExtraCharge</code> to set
	 * @return the <code>BasicShipmentMethodConfiguration</code> itself
	 */
    protected final BasicShipmentMethodConfiguration setExtraCharge(final ExtraCharge extra) {
        this.extraCharge = extra;
        return this;
    }

    /**
	 * Returns the minimal order value needed for the shipment method to be
	 * available or 0, if there is no minimal order value.
	 * 
	 * @return the minimal order value
	 */
    public final long getMinOrderValue() {
        return this.minOrderValue;
    }

    /**
	 * Sets the minimal order value needed for the shipment method to be
	 * available.
	 * 
	 * @param value
	 *            the minimal order value to set
	 * @return the <code>BasicShipmentMethodConfiguration</code> itself
	 */
    protected final BasicShipmentMethodConfiguration setMinOrderValue(final long value) {
        this.minOrderValue = value;
        return this;
    }

    /**
	 * Returns the maximal order value needed for the shipment method to be
	 * available or 0, if there is no maximal order value.
	 * 
	 * @return the maximal order value
	 */
    public final long getMaxOrderValue() {
        return this.maxOrderValue;
    }

    /**
	 * Sets the maximal order value needed for the shipment method to be
	 * available.
	 * 
	 * @param value
	 *            the maximal order value to set
	 * @return the <code>BasicShipmentMethodConfiguration</code> itself
	 */
    protected final BasicShipmentMethodConfiguration setMaxOrderValue(final long value) {
        this.maxOrderValue = value;
        return this;
    }

    /**
	 * Returns an unmodifiable <code>Set</code> holding the id codes of the
	 * payment methods combinable with this shipment method or an unmodifiable,
	 * empty <code>Set</code>, if the shipment method is combinable with any
	 * payment method.
	 * 
	 * @return an unmodifiable view of the id codes of the payment methods
	 *         combinable with this shipment method
	 */
    public final Set<String> getCombinablePaymentMethodIdCodes() {
        return Collections.unmodifiableSet(this.combinablePaymentMethodIdCodes);
    }

    /**
	 * Sets the id codes of the payment methods combinable with this shipment
	 * method. If the given <code>Collection</code> 'code' is <code>null</code>
	 * or empty, the shipment method will be combinable with any payment method.
	 * 
	 * @param codes
	 *            the id codes to set
	 * @return the <code>BasicShipmentMethodConfiguration</code> itself
	 */
    protected final BasicShipmentMethodConfiguration setCombinablePaymentMethodIdCodes(final Collection<? extends String> codes) {
        this.combinablePaymentMethodIdCodes.clear();
        this.combinablePaymentMethodIdCodes.addAll(codes);
        return this;
    }

    /**
	 * Returns an unmodifiable <code>Set</code> holding the names of the
	 * <code>Role</code>s granting access to this shipment method.
	 * 
	 * @return an unmodifiable view of the names of the <code>Role</code>s
	 *         granting access to this shipment method
	 */
    public final Set<String> getRoleNames() {
        return Collections.unmodifiableSet(this.roleNames);
    }

    /**
	 * Sets the names of the <code>Role</code>s granting access to this shipment
	 * method. If the given <code>Collection</code> 'names' is <code>null</code>
	 * or empty, no <code>Role</code> will be necessary to access this shipment
	 * method.
	 * 
	 * @param names
	 *            the <code>Role</code> names to set
	 * @return the <code>BasicShipmentMethodConfiguration</code> itself
	 */
    protected final BasicShipmentMethodConfiguration setRoleNames(final Collection<? extends String> names) {
        this.roleNames.clear();
        this.roleNames.addAll(names);
        return this;
    }
}
