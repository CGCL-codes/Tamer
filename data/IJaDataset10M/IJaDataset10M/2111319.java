package com.amazon.merchants.transport.persistence;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.amazon.merchants.dao.DAOException;

/**
 * Copyright 2004 Amazon.com
 * 
 * Description:
 * 
 * @author hynoskij
 *
 */
public class TransportDAOConnectionFactory {

    private static final TransportDAOConnectionFactory _instance = new TransportDAOConnectionFactory();

    private static final Log log = LogFactory.getLog(TransportDAOConnectionFactory.class);

    private TransportDAOConnection holdOpen;

    private TransportDAOConnectionFactory() {
        try {
            holdOpen = new TransportDAOConnection();
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    try {
                        holdOpen.closeConnection();
                    } catch (Exception ex) {
                    }
                }
            });
        } catch (Exception ex) {
            log.error("Could not connect to database due to: " + ex.getMessage());
            log.debug(ExceptionUtils.getFullStackTrace(ex));
        }
    }

    public static TransportDAOConnectionFactory instance() {
        return _instance;
    }

    public com.amazon.merchants.dao.DAOConnection getDAOConnection() throws DAOException {
        return new TransportDAOConnection();
    }
}
