package org.sidora.core.util;

/**
 * AbstractFactory
 * @author Enric Tartera, Juan Manuel Gimeno, Roger Masgoret
 * @version 1.0
 */
public abstract class AbstractFactory {

    protected abstract Object create(ParameterCollector param);

    public static final Object createObject(ParameterCollector param) throws Exception {
        String id = (String) param.get("factoryid");
        return FactoryPool.getFactory(id).create(param);
    }
}
