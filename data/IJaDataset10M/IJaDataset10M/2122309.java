package com.volantis.mcs.expression.functions.diselect;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.expression.functions.diselect.DIDeviceHeightFunction;
import com.volantis.mcs.expression.functions.diselect.DILengthFunction;

/**
 * Sub class which returns specific information about the DILengthFunction
 * for use in the test cases defined in the parent class.
 */
public class DIDeviceHeightFunctionTestCase extends DILengthFunctionTestAbstract {

    public DILengthFunction getFunction() {
        return new DIDeviceHeightFunction();
    }

    public String[] getPolicyNames() {
        return new String[] { DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS, DevicePolicyConstants.ACTUAL_HEIGHT_IN_MM, DevicePolicyConstants.USABLE_HEIGHT_IN_PIXELS };
    }
}
