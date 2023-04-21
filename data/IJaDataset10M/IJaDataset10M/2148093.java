package com.corratech.opensuite.aspect;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.HibernateException;
import com.corratech.opensuite.api.SessionManager;
import com.corratech.opensuite.exceptions.BaseException;
import com.corratech.opensuite.persist.hibernate.AbstractSessionManager;
import com.corratech.opensuite.services.OpensuiteServiceFacade;

/**
 * @author ubegun
 * 
 */
public class TMAdviserAspect {

    private static final Logger log = Logger.getLogger(TMAdviserAspect.class);

    public void readStart(ProceedingJoinPoint call) throws HibernateException, SQLException, BaseException {
        log.debug("Start read only transaction");
        this.getSessionManager().startTransaction(true);
    }

    public void readNormalEnd(ProceedingJoinPoint call) {
        SessionManager sessionManager = this.getSessionManager();
        try {
            sessionManager.commit();
            sessionManager.closeSession();
        } catch (BaseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readEndException(ProceedingJoinPoint call) {
        SessionManager sessionManager = this.getSessionManager();
        try {
            sessionManager.rollback();
            sessionManager.closeSession();
        } catch (BaseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rwStart(ProceedingJoinPoint call) throws HibernateException, SQLException, BaseException {
        log.debug("Start r/w transaction");
        this.getSessionManager().startTransaction(false);
    }

    public void rwNormalEnd(ProceedingJoinPoint call) {
        SessionManager sessionManager = this.getSessionManager();
        try {
            sessionManager.commit();
            sessionManager.closeSession();
        } catch (BaseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rwExceptionEnd(ProceedingJoinPoint call) {
        SessionManager sessionManager = this.getSessionManager();
        try {
            sessionManager.rollback();
            sessionManager.closeSession();
        } catch (BaseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AbstractSessionManager getSessionManager() {
        return (AbstractSessionManager) OpensuiteServiceFacade.getInstance().getSessionManager();
    }
}
