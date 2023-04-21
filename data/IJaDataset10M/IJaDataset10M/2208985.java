package org.atlantal.impl.app.rundata;

import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.app.app.Application;
import org.atlantal.api.app.basket.Basket;
import org.atlantal.api.app.rundata.RunData;
import org.atlantal.api.app.rundata.RunDataService;
import org.atlantal.api.app.user.UserManager;
import org.atlantal.utils.AtlantalMap;

/**
 * Titre :        Atlantal Framework
 * Description :
 * Copyright :    Copyright (c) 2002
 * Soci�t� :      Mably Multimedia
 * @author Fran�ois Masurel
 * @version 1.0
 */
public abstract class AbstractRunData implements RunData {

    private static final Logger LOGGER = Logger.getLogger(AbstractRunData.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private Application application;

    private RunDataService service;

    private Map baskets = null;

    /**
     * Constructeur
     */
    protected AbstractRunData() {
    }

    /**
     * Constructeur
     * @param application application
     * @param service Service
     */
    protected AbstractRunData(Application application, RunDataService service) {
        this.application = application;
        this.service = service;
    }

    /**
     *
     */
    protected void initSpecific() {
        this.baskets = new AtlantalMap();
    }

    /**
     *
     */
    public void init() {
        this.initSpecific();
    }

    /**
     * @return Application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * R�cup�ration du UserManager
     * @return User manager
     */
    public UserManager getUserManager() {
        return this.service.getUserManager();
    }

    /**
     * @return baskets
     */
    public Map getBaskets() {
        return baskets;
    }

    /**
     * @param baskets
     */
    protected void setBaskets(Map baskets) {
        this.baskets = baskets;
    }

    /**
     * @param name name
     * @return basket
     */
    public Basket getBasket(String name) {
        return (Basket) baskets.get(name);
    }

    /**
     * @param name name
     * @param basket basket
     */
    public void setBasket(String name, Basket basket) {
        baskets.put(name, basket);
    }
}
