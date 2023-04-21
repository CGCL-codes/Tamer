package com.spring.hibernate.Seminars.factories.addressbook;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.spring.hibernate.Seminars.model.dao.addressbook.*;

/** 
 * DAO factory implementation 
 * author: auto-generated
 */
public class HibernateAddressbookDaoFactory implements ApplicationContextAware {

    /** Internal state */
    private ApplicationContext context = null;

    /** Placeholder for an instance */
    private static HibernateAddressbookDaoFactory instance;

    /** Return an instance of this class
	 * @return an instance of this class
	 */
    public static synchronized HibernateAddressbookDaoFactory getInstance() {
        if (instance == null) {
            instance = new HibernateAddressbookDaoFactory();
        }
        return instance;
    }

    /** Return an instance of the context
	 * @return an instance of this context
	 */
    public static ApplicationContext getContext() {
        return getInstance().context;
    }

    /**
	 * Returns a AddressesDao instance
	 * 
	 * @return a AddressesDao instance
	 */
    public static AddressesDao getAddressesDao() {
        return (AddressesDao) getContext().getBean("AddressesDao");
    }

    /**
	 * Returns a ContactMethodsDao instance
	 * 
	 * @return a ContactMethodsDao instance
	 */
    public static ContactMethodsDao getContactMethodsDao() {
        return (ContactMethodsDao) getContext().getBean("ContactMethodsDao");
    }

    /**
	 * Returns a ContactsDao instance
	 * 
	 * @return a ContactsDao instance
	 */
    public static ContactsDao getContactsDao() {
        return (ContactsDao) getContext().getBean("ContactsDao");
    }

    /**
	 * Returns a PermissionsDao instance
	 * 
	 * @return a PermissionsDao instance
	 */
    public static PermissionsDao getPermissionsDao() {
        return (PermissionsDao) getContext().getBean("PermissionsDao");
    }

    /**
	 * Returns a RolePermissionsDao instance
	 * 
	 * @return a RolePermissionsDao instance
	 */
    public static RolePermissionsDao getRolePermissionsDao() {
        return (RolePermissionsDao) getContext().getBean("RolePermissionsDao");
    }

    /**
	 * Returns a RolesDao instance
	 * 
	 * @return a RolesDao instance
	 */
    public static RolesDao getRolesDao() {
        return (RolesDao) getContext().getBean("RolesDao");
    }

    /**
	 * Returns a UserRolesDao instance
	 * 
	 * @return a UserRolesDao instance
	 */
    public static UserRolesDao getUserRolesDao() {
        return (UserRolesDao) getContext().getBean("UserRolesDao");
    }

    /**
	 * Returns a UsersDao instance
	 * 
	 * @return a UsersDao instance
	 */
    public static UsersDao getUsersDao() {
        return (UsersDao) getContext().getBean("UsersDao");
    }

    /**
	 * Returns a ZipcodesDao instance
	 * 
	 * @return a ZipcodesDao instance
	 */
    public static ZipcodesDao getZipcodesDao() {
        return (ZipcodesDao) getContext().getBean("ZipcodesDao");
    }

    /**
	 * Sets a Spring Application Context object
	 */
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        getInstance().context = context;
    }
}
