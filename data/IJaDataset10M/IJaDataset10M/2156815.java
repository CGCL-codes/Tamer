package self.micromagic.eterna.model;

import self.micromagic.eterna.digester.ConfigurationException;

public interface CheckOperator {

    boolean check(Object value1, Object value2) throws ConfigurationException;
}
