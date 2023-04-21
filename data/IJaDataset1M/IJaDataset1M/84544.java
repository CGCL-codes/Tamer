package com.volantis.mcs.expression.functions.diselect;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.NumericValue;
import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;

/**
 * This Function retrieves the number of entries in the colour lookup table for
 * the device making the request. If the repository contains no entry for this
 * then the supplied default value will be returned. If this default value is
 * not valid, zero will be returned.
 */
public class DIColorIndexFunction extends ZeroMandatoryArgumentsExpressionFunction {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(DIColorIndexFunction.class);

    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER = new DefaultValueProvider();

    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    protected Value execute(ExpressionContext expressionContext, DevicePolicyValueAccessor accessor, Value defaultValue) {
        Value value = null;
        ExpressionFactory factory = expressionContext.getFactory();
        String palette = accessor.getDependentPolicyValue(DevicePolicyConstants.PALETTE);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Size of colour palette (from repository):" + palette);
        }
        try {
            value = factory.createIntValue(Integer.parseInt(palette));
        } catch (NumberFormatException e) {
            LOGGER.info("function-bad-parameter-value", new Object[] { palette, DevicePolicyConstants.PALETTE });
        }
        if (value == null) {
            if (defaultValue instanceof NumericValue) {
                value = defaultValue;
            } else {
                if (defaultValue != null) {
                    LOGGER.info("function-bad-default-value", "NumericValue");
                }
                value = new SimpleIntValue(factory, 0);
            }
        }
        return value;
    }

    protected String getFunctionName() {
        return "cssmq-color-index";
    }
}
