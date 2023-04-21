package org.tamacat.dao.impl;

import org.tamacat.dao.event.DaoEvent;
import org.tamacat.dao.event.DaoTransactionHandler;
import org.tamacat.log.LogFactory;

public class LoggingDaoTransactionHandler implements DaoTransactionHandler {

    @Override
    public void handleAfterCommit(DaoEvent event) {
        try {
            LogFactory.getLog(event.getCallerDao()).debug("commit.");
        } catch (Exception e) {
        }
    }

    @Override
    public void handleAfterRollback(DaoEvent event) {
        try {
            LogFactory.getLog(event.getCallerDao()).debug("rollback.");
        } catch (Exception e) {
        }
    }

    @Override
    public void handleBeforeCommit(DaoEvent event) {
    }

    @Override
    public void handleBeforeRollback(DaoEvent event) {
    }

    @Override
    public void handleException(DaoEvent event, Throwable cause) {
        try {
            LogFactory.getLog(event.getCallerDao()).error(event.getQuery(), cause);
        } catch (Exception e) {
        }
    }

    @Override
    public void handleRelease(DaoEvent event) {
        try {
            LogFactory.getLog(event.getCallerDao()).debug("release.");
        } catch (Exception e) {
        }
    }

    @Override
    public void handleTransantionEnd(DaoEvent event) {
        try {
            LogFactory.getLog(event.getCallerDao()).debug("end.");
        } catch (Exception e) {
        }
    }

    @Override
    public void handleTransantionStart(DaoEvent event) {
        try {
            LogFactory.getLog(event.getCallerDao()).debug("start.");
        } catch (Exception e) {
        }
    }
}
