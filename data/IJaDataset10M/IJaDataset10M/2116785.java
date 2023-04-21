package com.scs.base.dao;

public abstract class DaoFactory {

    private static Object initLock = new Object();

    private static String className = "com.scs.base.dao.impl.hibernate.HibernateDAOFactory";

    public abstract ManageUserDao createAccountDAO();

    public static Dao getDao(String daoClassName) {
        Dao daoImplObject = null;
        Class daoImplClass;
        try {
            daoImplClass = Class.forName("com.scs.base.dao.impl.hibernate." + daoClassName + "HibernateImpl");
            daoImplObject = (Dao) daoImplClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return daoImplObject;
    }
}
