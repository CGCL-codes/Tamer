package com.marslei.dao.impl;

import javax.jdo.PersistenceManagerFactory;

public class BaseDao {

    protected PersistenceManagerFactory pmf;

    public PersistenceManagerFactory getPmf() {
        return pmf;
    }

    public void setPmf(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }
}
