package com.icteam.fiji.security.jaas;

import javax.security.auth.login.LoginException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;

public class FIJIAdvancedLoginModule extends org.jboss.security.auth.spi.DatabaseServerLoginModule {

    private int maxRetries;

    private int maxUnusageDays;

    private String resetCounterQuery;

    private String incrementCounterQuery;

    private String getUtenStatusQuery;

    private String myDsJndiName;

    private String lockAccountQuery;

    private final int ACTIVE_UTEN_CODE = 1;

    private final int TIP_BLK_MAX_GG_INUTILIZZO = 2;

    private final int TIP_BLK_MAX_TENTATIVI_ERR = 3;

    private String getUserName() throws LoginException {
        String[] tmp = getUsernameAndPassword();
        if (tmp != null && tmp.length >= 1) {
            log.debug("login " + tmp[0]);
            return tmp[0];
        } else return null;
    }

    public boolean login() throws LoginException {
        UtenStatus utenStatus = getUtenStatus();
        if (!utenStatus.isActive()) {
            super.loginOk = false;
            throw new FailedLoginException("Account Locked: user is not active");
        }
        if (utenStatus.getRetries() >= maxRetries) {
            super.loginOk = false;
            lockAccount(TIP_BLK_MAX_TENTATIVI_ERR);
            throw new FailedLoginException("Account Locked: max retries reached");
        }
        if (utenStatus.getUnusageDays() >= maxUnusageDays) {
            super.loginOk = false;
            lockAccount(TIP_BLK_MAX_GG_INUTILIZZO);
            throw new FailedLoginException("Account Locked: max unusage days reached");
        }
        boolean res = false;
        try {
            res = super.login();
            logAttempt(res);
            if (res) resetCounter();
            return res;
        } catch (LoginException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            logAttempt(false);
            throw e;
        }
    }

    /**
	 * Counts previous failed login attempts
	 * 
	 * @return
	 * @throws LoginException
	 */
    private UtenStatus getUtenStatus() throws LoginException {
        UtenStatus u = new UtenStatus();
        log.debug("getUtenStatus start");
        String username = getUserName();
        if (username == null || username.equals("")) return u;
        Connection con = null;
        ResultSet rs = null;
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(myDsJndiName);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(getUtenStatusQuery);
            ps.setString(1, username);
            rs = ps.executeQuery();
            log.debug("getUtenStatus executeQuery");
            if (rs.next()) {
                u.setRetries(rs.getInt(1));
                u.setUnusageDays(rs.getInt(2));
                u.setActive(rs.getInt(3) == ACTIVE_UTEN_CODE);
                log.debug("Trovato utente: " + username + " - retries: " + u.getRetries() + " - active: " + u.isActive());
            } else {
                u.setRetries(Integer.MAX_VALUE);
                u.setUnusageDays(Integer.MAX_VALUE);
                u.setActive(false);
                log.debug("NON trovato utente: " + username + " - retries: " + u.getRetries() + " - active: " + u.isActive());
            }
            return u;
        } catch (NamingException e) {
            log.error("Unexpected error", e);
            return new UtenStatus(Integer.MAX_VALUE, Integer.MAX_VALUE, false);
        } catch (SQLException e) {
            log.error("Query failed", e);
            return new UtenStatus(Integer.MAX_VALUE, Integer.MAX_VALUE, false);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Throwable e) {
                log.error("Error closing connection", e);
            }
        }
    }

    /**
	 * Logs login attempt
	 * 
	 * @param success
	 * @throws LoginException
	 */
    private void logAttempt(boolean success) throws LoginException {
        Connection con = null;
        try {
            if (!success) {
                incrementCounter();
                if (getUtenStatus().getRetries() >= maxRetries) {
                    log.info("Utente bloccato per superamento tentativi errati (" + maxRetries + ")");
                    lockAccount(TIP_BLK_MAX_TENTATIVI_ERR);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Throwable e) {
                    log.error("Error closing connection", e);
                }
            }
        }
    }

    /**
	 * Increments failures counter
	 * 
	 * @throws LoginException
	 */
    private void incrementCounter() throws LoginException {
        String username = getUserName();
        if (username == null || username.equals("")) return;
        Connection con = null;
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(myDsJndiName);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(incrementCounterQuery);
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (NamingException e) {
            log.error("Unexpected error", e);
        } catch (SQLException e) {
            log.error("Query failed", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Throwable e) {
                log.error("Error closing connection", e);
            }
        }
    }

    /**
	 * Locks user account
	 * 
	 * @throws LoginException
	 */
    private void lockAccount(int tipBlk) throws LoginException {
        String username = getUserName();
        if (username == null || username.equals("")) return;
        Connection con = null;
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(myDsJndiName);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(lockAccountQuery);
            ps.setInt(1, tipBlk);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (NamingException e) {
            log.error("Unexpected error", e);
        } catch (SQLException e) {
            log.error("Query failed", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Throwable e) {
                log.error("Error closing connection", e);
            }
        }
    }

    /**
	 * Resets failures counter
	 * 
	 * @throws LoginException
	 */
    private void resetCounter() throws LoginException {
        String username = getUserName();
        if (username == null || username.equals("")) return;
        Connection con = null;
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(myDsJndiName);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(resetCounterQuery);
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (NamingException e) {
            log.error("Unexpected error", e);
        } catch (SQLException e) {
            log.error("Query failed", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Throwable e) {
                log.error("Error closing connection", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
        super.initialize(subject, callbackHandler, sharedState, options);
        log.debug("Initializing LoginModule");
        try {
            String flag = (String) options.get("maxRetries");
            maxRetries = Integer.valueOf(flag).intValue();
            resetCounterQuery = (String) options.get("resetCounterQuery");
            incrementCounterQuery = (String) options.get("incrementCounterQuery");
            getUtenStatusQuery = (String) options.get("getUtenStatusQuery");
            lockAccountQuery = (String) options.get("lockAccountQuery");
            myDsJndiName = (String) options.get("dsJndiName");
            String maxUnusageDaysStr = (String) options.get("maxUnusageDays");
            try {
                maxUnusageDays = Integer.valueOf(maxUnusageDaysStr).intValue();
                log.debug("maxUnusageDays = " + maxUnusageDays);
            } catch (Exception ignore) {
                log.debug("maxUnusageDays unlimited");
                maxUnusageDays = 0;
            }
        } catch (Throwable e) {
            log.error("Error initializing", e);
        }
        log.debug("LoginModule initialized");
    }

    class UtenStatus {

        private boolean active = true;

        private int retries = 0;

        private int unusageDays = 0;

        public UtenStatus() {
        }

        public UtenStatus(int retries, int unusageDays, boolean active) {
            this.retries = retries;
            this.unusageDays = unusageDays;
            this.active = active;
        }

        public int getRetries() {
            return retries;
        }

        public void setRetries(int reties) {
            this.retries = reties;
        }

        public int getUnusageDays() {
            return unusageDays;
        }

        public void setUnusageDays(int unusageDays) {
            this.unusageDays = unusageDays;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
