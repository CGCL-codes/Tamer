package com.vangent.hieos.empi.config;

import com.vangent.hieos.empi.distance.DistanceFunction;
import com.vangent.hieos.empi.exception.EMPIException;

/**
 *
 * @author Bernie Thuman
 */
public class DistanceFunctionConfig extends FunctionConfig {

    /**
     *
     * @return
     */
    public DistanceFunction getDistanceFunction() {
        return (DistanceFunction) this.getFunction();
    }

    /**
     *
     * @return
     * @throws EMPIException
     */
    @Override
    public FunctionConfig copyNoParameters() throws EMPIException {
        DistanceFunctionConfig copyFunctionConfig = new DistanceFunctionConfig();
        super.copyNoParameters(copyFunctionConfig);
        return copyFunctionConfig;
    }
}
