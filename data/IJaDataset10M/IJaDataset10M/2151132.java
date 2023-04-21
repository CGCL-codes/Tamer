package org.monet.setup.core.producers;

import java.util.HashMap;
import org.monet.kernel.exceptions.SystemException;
import org.monet.setup.core.constants.ErrorCode;

public class ProducersFactory {

    private static ProducersFactory oInstance;

    private HashMap<String, Class<?>> hmProducers;

    private ProducersFactory() {
        this.hmProducers = new HashMap<String, Class<?>>();
        this.registerProducers();
    }

    private Boolean registerProducers() {
        return true;
    }

    public static synchronized ProducersFactory getInstance() {
        if (oInstance == null) oInstance = new ProducersFactory();
        return oInstance;
    }

    public Object get(String sType) {
        Class<?> cProducer;
        Producer oProducer = null;
        try {
            cProducer = (Class<?>) this.hmProducers.get(sType);
            oProducer = (Producer) cProducer.newInstance();
        } catch (NullPointerException oException) {
            throw new SystemException(ErrorCode.PRODUCERS_FACTORY, sType, oException);
        } catch (Exception oException) {
            throw new SystemException(ErrorCode.PRODUCERS_FACTORY, sType, oException);
        }
        return oProducer;
    }

    public Boolean register(String sType, Class<?> cProducer) throws IllegalArgumentException {
        if ((cProducer == null) || (sType == null)) {
            return false;
        }
        this.hmProducers.put(sType, cProducer);
        return true;
    }
}
