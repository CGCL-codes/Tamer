package fi.hip.gb.gridlib.gridsp.userbase;

import org.hibernate.*;
import org.hibernate.cfg.*;
import java.io.*;
import java.util.List;
import java.util.Date;
import org.jboss.portal.core.impl.user.UserPrefSet;
import java.io.File;

/**
 * This class represents a JBoss portal user.
 * @author Henri Mikkonen
 * @version $Id: $
 */
public class PortalUser {

    private static final SessionFactory factory;

    private static final String hbrConfig = "portal.userdatabase.hibernate.cfg.xml";

    private static final String hbrMappings = "UserImpl.hbm.xml";

    private Integer uid;

    private String username;

    private UserPrefSet rootPreferenceSet;

    private String givenName;

    private String familyName;

    private String password;

    private String realEmail;

    private String fakeEmail;

    private Date registrationDate;

    private boolean viewRealEmail;

    private boolean enabled;

    static {
        try {
            InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(hbrMappings);
            Configuration cfg = new Configuration().configure(hbrConfig);
            cfg.addInputStream(ins);
            factory = cfg.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static final ThreadLocal session = new ThreadLocal();

    /**
     * Gets current Hibernate session.
     * @return Hibernate session.
     * @throws HibernateException If something goes wrong.
     */
    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s == null) {
            s = factory.openSession();
            session.set(s);
        }
        return s;
    }

    /**
     * Closes the current Hibernate session.
     * @throws HibernateException If something goes wrong.
     */
    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s != null) s.close();
        session.set(null);
    }

    /**
     * Constructor. No operation.
     */
    public PortalUser() {
    }

    private Object getUserbaseElement(String key, String value) {
        Session session = currentSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from fi.hip.gb.gridlib.gridsp.userbase.PortalUser as user where user." + key + " = '" + value + "'");
        Object result = query.uniqueResult();
        tx.commit();
        closeSession();
        return result;
    }

    /**
     * Gets the user password.
     * @param identifier The user identifier as <code>String</code>.
     * @return The user password as <code>String</code>.
     */
    public String getUserPassword(String identifier) {
        PortalUser ub = (PortalUser) this.getUserbaseElement("uid", identifier);
        if (ub != null) {
            return ub.getPassword();
        } else {
            return null;
        }
    }

    /**
     * Gets the user by user name.
     * @param name The name of the user.
     * @return The user, <code>null</code> if it does not exist.
     */
    public PortalUser getUserByName(String name) {
        PortalUser ub = (PortalUser) this.getUserbaseElement("username", name);
        if (ub != null) {
            return ub;
        } else {
            return null;
        }
    }

    /**
     * Sets the user database identifier. Required by Hibernate.
     * @param intgr The user database identifier as <code>Integer</code>.
     */
    public void setUid(Integer intgr) {
        this.uid = intgr;
    }

    /**
     * Gets the user database identifier. Required by Hibernate.
     * @return The user database identifier as <code>Integer</code>.
     */
    public Integer getUid() {
        return this.uid;
    }

    /**
     * Sets the username. Required by Hibernate.
     * @param name The username as <code>String</code>.
     */
    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * Gets the username. Required by Hibernate.
     * @return The username as <code>String</code>.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the root preference set. Required by Hibernate.
     * @param prefset The root preference set as <code>UserPrefSet</code>.
     */
    public void setRootPreferenceSet(UserPrefSet prefset) {
        this.rootPreferenceSet = prefset;
    }

    /**
     * Gets the root preference set. Required by Hibernate.
     * @return The root preference set as <code>UserPrefSet</code>.
     */
    public UserPrefSet getRootPreferenceSet() {
        return this.rootPreferenceSet;
    }

    /**
     * Sets the user's given name. Required by Hibernate.
     * @param name The given name as <code>String</code>.
     */
    public void setGivenName(String name) {
        this.givenName = name;
    }

    /**
     * Gets the user's given name. Required by Hibernate.
     * @return The given name as <code>String</code>.
     */
    public String getGivenName() {
        return this.givenName;
    }

    /**
     * Sets the user's family name. Required by Hibernate.
     * @param name The family name as <code>String</code>.
     */
    public void setFamilyName(String name) {
        this.familyName = name;
    }

    /**
     * Gets the user's family name. Required by Hibernate.
     * @return The family name as <code>String</code>.
     */
    public String getFamilyName() {
        return this.familyName;
    }

    /**
     * Sets the user's password. Required by Hibernate.
     * @param pwd The password as <code>String</code>.
     */
    public void setPassword(String pwd) {
        this.password = pwd;
    }

    /**
     * Gets the user's password. Required by Hibernate.
     * @return The password as <code>String</code>.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the user's real Email address. Required by Hibernate.
     * @param mail The mail address as <code>String</code>.
     */
    public void setRealEmail(String mail) {
        this.realEmail = mail;
    }

    /**
     * Gets the user's real Email address. Required by Hibernate.
     * @return The mail address as <code>String</code>.
     */
    public String getRealEmail() {
        return this.realEmail;
    }

    /**
     * Sets the user's fake Email address. Required by Hibernate.
     * @param mail The mail address as <code>String</code>.
     */
    public void setFakeEmail(String mail) {
        this.fakeEmail = mail;
    }

    /**
     * Gets the user's fake Email address. Required by Hibernate.
     * @return The mail address as <code>String</code>.
     */
    public String getFakeEmail() {
        return this.fakeEmail;
    }

    /**
     * Sets the user's registration date. Required by Hibernate.
     * @param date The registration date as <code>Date</code>.
     */
    public void setRegistrationDate(Date date) {
        this.registrationDate = date;
    }

    /**
     * Gets the user's registration date. Required by Hibernate.
     * @return The registration date as <code>Date</code>.
     */
    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    /**
     * Sets Email view authorization. Required by Hibernate.
     * @param view The authorization as <code>boolean</code>.
     */
    public void setViewRealEmail(boolean view) {
        this.viewRealEmail = view;
    }

    /**
     * Gets Email view authorization. Required by Hibernate.
     * @return The authorization as <code>boolean</code>.
     */
    public boolean getViewRealEmail() {
        return this.viewRealEmail;
    }

    /**
     * Sets the account validity. Required by Hibernate.
     * @param enable The account validity as <code>boolean</code>.
     */
    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    /**
     * Gets the account validity. Required by Hibernate.
     * @return <code>true</code> if enabled, <code>false</code> otherwise.
     */
    public boolean getEnabled() {
        return this.enabled;
    }
}
