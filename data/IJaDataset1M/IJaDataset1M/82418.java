package com.wgo.bpot.server.persist;

import com.wgo.bpot.common.transport.servicefacade.UniqueValueFactory;

public abstract class BasicUniqueValueFactory implements UniqueValueFactory {

    private Class uniqueValueClass;

    private PersistService persistService;

    public PersistService getPersistService() {
        return persistService;
    }

    public void setPersistService(PersistService persistService) {
        this.persistService = persistService;
    }

    public Class getUniqueValueClass() {
        return uniqueValueClass;
    }

    public void setUniqueValueClass(Class uniqueValueClass) {
        this.uniqueValueClass = uniqueValueClass;
    }
}
