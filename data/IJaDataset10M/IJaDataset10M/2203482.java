package com.gever.goa.dailyoffice.targetmng.dao;

/**
 * Title: ���Ŀ����󹤳���
 * Description: ���Ŀ����󹤳���
 * Copyright: Copyright (c) 2004
 * Company: �������
 * @author Hu.Walker
 * @version 1.0
 */
public abstract class YearTargetFactory {

    private static String className = "com.gever.goa.dailyoffice.targetmng.dao.impl.DefaultYearTargetFactory";

    private static YearTargetFactory factory = null;

    public YearTargetFactory() {
    }

    /**
   *@return com.gever.sysman.privilege.dao.PrivilegeFactory
   *@roseuid 40BAB9B00167
   */
    public static synchronized YearTargetFactory getInstance() {
        if (factory == null) {
            try {
                Class c = Class.forName(className);
                factory = (YearTargetFactory) c.newInstance();
            } catch (Exception e) {
                System.err.println("Failed to load PrivilegeFactory class " + className);
                e.printStackTrace();
                return null;
            }
        }
        return factory;
    }

    public abstract YearTargetDao createYearTarget(String dbData);
}
