package com.companyname.common;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import com.companyname.common.templet.DAOTestCaseTemplet;
import com.companyname.projectname.init.InitData;

/**
 * 广告网上交易平台dao单元测试用例模板
 *
 * @author amonlei
 *
 */
public abstract class CommonDAOTestCaseTemplet extends DAOTestCaseTemplet {

    static Logger logger = Logger.getLogger(CommonDAOTestCaseTemplet.class);

    private static ApplicationContext applicationContext4ProjectNameDao;

    public void setUp() {
        super.setUp(XmlConfigFiles4Spring.XML_FILES_4_DAO);
    }

    public void initSysframe() {
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext4ProjectNameDao;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        applicationContext4ProjectNameDao = applicationContext;
    }
}
