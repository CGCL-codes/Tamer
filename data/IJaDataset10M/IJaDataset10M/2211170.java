package com.idna.trace.core.logging;

import java.util.Map;
import org.apache.commons.collections.Bag;
import com.idna.trace.utils.parameters.consumer.ParametersConsumer;

public interface Logging {

    void log(ParametersConsumer params, String action, Bag statsBag, Map<String, Object> clientParams);
}
