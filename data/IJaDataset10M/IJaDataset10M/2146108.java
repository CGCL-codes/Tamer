package net.sf.crudelia.cfg.tasks.commons;

import net.sf.hibernate.Hibernate;
import net.sf.crudelia.CrudeliaException;
import org.apache.commons.beanutils.PropertyUtils;

/**
 */
public class Fetch {

    private static final String FETCH_THIS_NAME = "this";

    private String name;

    public void fetch(Object objToInit) throws CrudeliaException {
        try {
            if (FETCH_THIS_NAME.equals(name)) {
                Hibernate.initialize(objToInit);
            } else {
                Hibernate.initialize(PropertyUtils.getProperty(objToInit, name));
            }
        } catch (Exception e) {
            throw new CrudeliaException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string;
    }
}
