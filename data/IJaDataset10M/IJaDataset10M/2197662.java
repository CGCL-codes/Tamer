package at.redcross.tacos.web.persistence;

import java.util.Map;
import at.redcross.tacos.dbal.manager.DbalResources;

public class WebDbalResources extends DbalResources {

    private static WebDbalResources instance;

    private String persistenceUnit;

    private WebDbalResources() {
    }

    /**
     * Sets the name of the persistence unit that will be used to create the
     * manager factory.
     * <p>
     * Please note that this call must be done <b>before</tt> the factory is
     * created otherwise some kind of unchecked exception will be thrown.
     * </p>
     * 
     * @param persistenceUnit
     */
    protected void setPersistenceUnit(String persistenceUnit) {
        if (lazyFactory.get() != null) {
            throw new IllegalStateException("Container factory already initialized");
        }
        this.persistenceUnit = persistenceUnit;
    }

    /**
     * Creates or returns the shared {@code WebDbalResources} instance.
     * 
     * @return the shared instance.
     */
    public static synchronized WebDbalResources getInstance() {
        if (instance == null) {
            instance = new WebDbalResources();
        }
        return instance;
    }

    @Override
    protected void initResources() {
        System.setProperty(AUDIT_CLASSNAME, WebAuditListener.class.getName());
    }

    @Override
    protected void initFactory(Map<String, String> map) {
        map.put("hibernate.ejb.interceptor", WebHistoryInterceptor.class.getName());
    }

    @Override
    protected String getPersistenceUnit() {
        if (System.getProperty(PERSISTENCE_UNIT) != null) {
            return System.getProperty(PERSISTENCE_UNIT);
        }
        return persistenceUnit;
    }
}
