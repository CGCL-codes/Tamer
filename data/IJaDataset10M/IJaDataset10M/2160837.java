package com.idna.riskengine;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import com.idna.riskengine.domain.RiskEngRequestData;
import com.idna.riskengine.domain.RiskEngineRequestIdentification;

public class IdentificationDataDecoratorImpl implements IdentificationDataDecorator {

    private String[] additionalMessageHeaders;

    @Override
    public RiskEngRequestData decorateWithIdentificationData(RiskEngineRequestIdentification requestIdData) {
        RiskEngRequestData data = new RiskEngRequestData(requestIdData.getSourceName());
        Map<String, Serializable> map = requestIdData.getRiskEngineRequestIdentificationAsMap();
        for (String decoratorKey : additionalMessageHeaders) {
            if (!map.keySet().contains(decoratorKey)) {
                throw new RuntimeException("Decoration Data " + decoratorKey + " Not Supported");
            }
            data.put(decoratorKey, map.get(decoratorKey));
        }
        return data;
    }

    public void setAdditionalMessageHeaders(String[] additionalMessageHeaders) {
        this.additionalMessageHeaders = additionalMessageHeaders;
    }
}
