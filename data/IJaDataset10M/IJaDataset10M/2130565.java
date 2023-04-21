package com.volantis.devrep.repository.impl.devices.policy.values;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.PolicyDescriptorAccessor;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.BooleanPolicyType;
import com.volantis.mcs.devices.policy.types.CompositePolicyType;
import com.volantis.mcs.devices.policy.types.IntPolicyType;
import com.volantis.mcs.devices.policy.types.OrderedSetPolicyType;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.RangePolicyType;
import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.mcs.devices.policy.types.SimplePolicyType;
import com.volantis.mcs.devices.policy.types.StructurePolicyType;
import com.volantis.mcs.devices.policy.types.TextPolicyType;
import com.volantis.mcs.devices.policy.types.UnorderedSetPolicyType;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.mcs.devices.policy.values.SimplePolicyValue;
import com.volantis.synergetics.log.LogDispatcher;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A factory that can generate {@link PolicyValue}s using device policy value
 * information from {@link DefaultDevice} and the device policy meta data from
 * {@link com.volantis.mcs.devices.policy.PolicyDescriptor}.
 */
public class DefaultPolicyValueFactory extends PolicyValueFactory {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(DefaultPolicyValueFactory.class);

    /**
     * The object to use to retrieve {@link PolicyDescriptor} objects.
     */
    private PolicyDescriptorAccessor policyDescriptorAccessor;

    /**
     * Initialize.
     *
     * @param policyDescriptorAccessor The object to use to access
     * {@link PolicyDescriptor}s. May not be null.
     */
    public DefaultPolicyValueFactory(PolicyDescriptorAccessor policyDescriptorAccessor) {
        if (policyDescriptorAccessor == null) {
            throw new IllegalArgumentException("Cannot be null: policyDescriptorAccessor");
        }
        this.policyDescriptorAccessor = policyDescriptorAccessor;
    }

    public PolicyValue createPolicyValue(DefaultDevice device, String policyName) {
        if (device == null) {
            throw new IllegalArgumentException("Cannot be null: device");
        }
        if (policyName == null) {
            throw new IllegalArgumentException("Cannot be null: policyName");
        }
        PolicyValue value = null;
        try {
            PolicyDescriptor descriptor = policyDescriptorAccessor.getPolicyDescriptor(policyName, Locale.getDefault());
            if (descriptor != null) {
                PolicyType type = descriptor.getPolicyType();
                if (type instanceof StructurePolicyType) {
                    value = createStructuredPolicyValue(device, policyName, (StructurePolicyType) type);
                } else {
                    try {
                        String valueString = device.getComputedPolicyValue(policyName);
                        if (valueString != null) {
                            value = createPolicyValue(valueString, type);
                        }
                    } catch (DeviceRepositoryException e) {
                        logger.error("unexpected-exception", e.getCause());
                    }
                }
            } else {
                logger.warn("cannot-retrieve-policy-info", new Object[] { policyName });
            }
        } catch (DeviceRepositoryException dre) {
            logger.warn("cannot-retrieve-policy-info", new Object[] { policyName }, dre);
        }
        return value;
    }

    /**
     * Given a policy value, and the known type of that value, create a
     * <code>PolicyValue</code> instance of the correct type and return it.
     *
     * <strong>
     * It should be noted that the <code>PolicyValue</code> returned should
     * be an immutable object.
     * </strong>
     *
     * @param policyValue The value to create a correctly typed instance of
     *                    <code>PolicyValue</code> around.
     * @param type        The type of the value.
     *
     * @return An initialized PolicyValue containing the value provided
     *         expressed as the correct type.
     * @throws DeviceRepositoryException if the given value is not of the
     * specified policy type.
     */
    private PolicyValue createPolicyValue(String policyValue, PolicyType type) throws DeviceRepositoryException {
        PolicyValue value = null;
        if (type instanceof SimplePolicyType) {
            value = createSimplePolicyValue(policyValue, (SimplePolicyType) type);
        } else if (type instanceof CompositePolicyType) {
            if (type instanceof OrderedSetPolicyType) {
                OrderedSetPolicyType orderedSetType = (OrderedSetPolicyType) type;
                value = new DefaultOrderedSetPolicyValue(orderedSetType.getMemberPolicyType(), policyValue);
            } else if (type instanceof UnorderedSetPolicyType) {
                UnorderedSetPolicyType unorderedSetType = (UnorderedSetPolicyType) type;
                value = new DefaultUnorderedSetPolicyValue(unorderedSetType.getMemberPolicyType(), policyValue);
            }
        } else {
            logger.warn("policy-type-unknown", new Object[] { type });
        }
        return value;
    }

    /**
     * Given a policy value, and the known simple type of that value, create a
     * <code>SimplePolicyValue</code> instance of the correct type and
     * return it.
     *
     * <strong>
     * It should be noted that the <code>PolicyValue</code> returned should
     * be an immutable object.
     * </strong>
     *
     * @param policyValue The value to create a correctly typed instance of
     *                    <code>SimplePolicyValue</code> around. Must not be
     *                    null.
     * @param type        The type of the value.
     *
     * @return An initialized SimplePolicyValue containing the value provided
     *         expressed as the correct type.
     * @throws DeviceRepositoryException if the given value is not of the
     * specified policy type.
     */
    private SimplePolicyValue createSimplePolicyValue(String policyValue, SimplePolicyType type) throws DeviceRepositoryException {
        SimplePolicyValue value = null;
        if (type instanceof BooleanPolicyType) {
            value = new DefaultBooleanPolicyValue(policyValue);
        } else if (type instanceof IntPolicyType) {
            if (policyValue.length() > 0) {
                value = new DefaultIntPolicyValue(policyValue);
            }
        } else if (type instanceof RangePolicyType) {
            if (policyValue.length() > 0) {
                value = new DefaultRangePolicyValue(policyValue);
            }
        } else if (type instanceof SelectionPolicyType) {
            value = new DefaultSelectionPolicyValue(policyValue);
        } else if (type instanceof TextPolicyType) {
            value = new DefaultTextPolicyValue(policyValue);
        } else {
            logger.warn("policy-type-unknown-simple", new Object[] { type });
        }
        return value;
    }

    /**
     * Given a policy name and associated device for a structured type, create
     * a <code>StructuredPolicyValue</code> instance of the correct type and
     * return it.
     *
     * <strong>
     * It should be noted that the <code>PolicyValue</code> returned should
     * be an immutable object.
     * </strong>
     *
     * @param device     The device on which the policies of interested are
     *                   registered.
     * @param policyName The policy for which the value should be retrieved
     * @param type       The type of the value, which has to be a structured
     *                   type.  This is necesary because of the extra
     *                   information it contains.
     *
     * @return An initialized PolicyValue containing the value provided
     *         expressed as the correct type.
     */
    private PolicyValue createStructuredPolicyValue(DefaultDevice device, String policyName, StructurePolicyType type) {
        DefaultStructurePolicyValue value = new DefaultStructurePolicyValue();
        Map fieldTypes = type.getFieldTypes();
        Set keys = fieldTypes.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            try {
                String fieldName = (String) i.next();
                String fullName = policyName + "." + fieldName;
                SimplePolicyValue fieldValue = null;
                PolicyType fieldType = (PolicyType) fieldTypes.get(fieldName);
                if (!(fieldType instanceof SimplePolicyType)) {
                    throw new IllegalStateException("Can only have simple types inside a structured type");
                }
                String valueString = device.getComputedPolicyValue(fullName);
                if (valueString != null) {
                    fieldValue = createSimplePolicyValue(valueString, (SimplePolicyType) fieldType);
                }
                value.addField(fieldName, fieldValue);
            } catch (DeviceRepositoryException dre) {
                logger.warn("structured-fields-no-info", new Object[] { policyName });
            }
        }
        value.complete();
        return value;
    }
}
