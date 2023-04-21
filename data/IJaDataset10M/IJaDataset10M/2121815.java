package it.cspnet.sutri.core.src.sutri.core;

import it.cspnet.jpa.mapping.NaiveBrokerStrategy;
import it.cspnet.sutri.core.src.sutri.core.Suite;
import it.cspnet.sutri.core.src.sutri.core.Test;

@SuppressWarnings("unchecked")
public class SuiteBrokerStrategy extends NaiveBrokerStrategy {

    public static final String TIPO_TEST = "T";

    public static final String TIPO_SUITE = "S";

    private static final Class[] classes = new Class[] { Test.class, Suite.class };

    private static final String[] types = new String[] { TIPO_TEST, TIPO_SUITE };

    public SuiteBrokerStrategy() {
        super(classes, types);
    }
}
