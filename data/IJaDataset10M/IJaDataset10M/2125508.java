package org.tamacat.dao.impl;

import org.tamacat.dao.event.DaoEvent;

public class DaoEventImpl implements DaoEvent {

    private final Class<?> callerDao;

    private String query;

    private int result;

    public DaoEventImpl(Class<?> callerDao) {
        this.callerDao = callerDao;
    }

    public DaoEventImpl(Class<?> callerDao, String query) {
        this.callerDao = callerDao;
        this.query = query;
    }

    public Class<?> getCallerDao() {
        return callerDao;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
