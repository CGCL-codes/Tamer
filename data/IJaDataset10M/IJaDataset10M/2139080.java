package com.smb.MMUtil.handler.createORM;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.smb.MMUtil.pojo.CreateORMMappingFiles;
import com.smb.MMUtil.pojo.CreateORMPojo;
import com.smb.MMUtil.pojo.MySQLShowColumns;

public class CreateORMFileFactory {

    private static Log logger = LogFactory.getLog(CreateORMFileFactory.class);

    private static CreateHibernateHelper ceateHibernateHelper = new CreateHibernateHelper();

    private static CreateIbatisHelper createIbatisHelper = new CreateIbatisHelper();

    @SuppressWarnings("unchecked")
    public CreateORMPojo factoryCreateORMPojo(String host, String dbName, String user, String pswd, String tabNames[], String packName, String createORMID, List tables) throws Exception {
        CreateORMPojo ORMPojo = null;
        if (createORMID.equals("hibernate")) {
            logger.info(createORMID);
            ORMPojo = new CreateORMPojo();
            ORMPojo.setCreateType(createORMID);
            ORMPojo.setSpringFile("");
            String ormCFGFile = ceateHibernateHelper.HibernateCFGFile(host, dbName, user, pswd, tabNames, packName);
            ORMPojo.setOrmCFGFile(ormCFGFile);
            List ormMappingFiles = new ArrayList();
            for (int i = 0; i < tables.size(); i++) {
                CreateORMMappingFiles ormMappingFile = ceateHibernateHelper.HibernateHBMFile(tabNames[i], packName, (List<MySQLShowColumns>) tables.get(i));
                ormMappingFiles.add(ormMappingFile);
            }
            ORMPojo.setOrmMappingFiles(ormMappingFiles);
        } else if (createORMID.equals("ibatis")) {
            logger.info(createORMID);
            ORMPojo = new CreateORMPojo();
            String ormCFGFile = createIbatisHelper.getiBATISMapFile(host, dbName, user, pswd, tabNames, packName);
            ORMPojo.setOrmCFGFile(ormCFGFile);
            ORMPojo.setCreateType(createORMID);
            List ormMappingFiles = new ArrayList();
            for (int i = 0; i < tables.size(); i++) {
                String ormMappingFile = createIbatisHelper.iBATISPojoMAPXMLFile(tabNames[i], packName);
                ormMappingFiles.add(ormMappingFile);
            }
            ORMPojo.setOrmMappingFiles(ormMappingFiles);
            ORMPojo.setSpringFile("");
        } else if (createORMID.equals("hibernateSpring")) {
            logger.info(createORMID);
            ORMPojo = new CreateORMPojo();
            String springFile = ceateHibernateHelper.HibernateSpringFile(host, dbName, user, pswd, tabNames, packName);
            ORMPojo.setSpringFile(springFile);
            ORMPojo.setCreateType(createORMID);
            ORMPojo.setOrmCFGFile("");
            List ormMappingFiles = new ArrayList();
            for (int i = 0; i < tables.size(); i++) {
                CreateORMMappingFiles ormMappingFile = ceateHibernateHelper.HibernateHBMFile(tabNames[i], packName, (List<MySQLShowColumns>) tables.get(i));
                ormMappingFiles.add(ormMappingFile);
            }
            ORMPojo.setOrmMappingFiles(ormMappingFiles);
        } else if (createORMID.equals("ibatisSpring")) {
            logger.info(createORMID);
            ORMPojo = new CreateORMPojo();
            ORMPojo.setCreateType(createORMID);
            String ormCFGFile = createIbatisHelper.getSpringSQLMAPFile(host, dbName, user, pswd, tabNames, packName);
            ORMPojo.setOrmCFGFile(ormCFGFile);
            List ormMappingFiles = new ArrayList();
            for (int i = 0; i < tables.size(); i++) {
                String ormMappingFile = createIbatisHelper.iBATISPojoMAPXMLFile(tabNames[i], packName);
                ormMappingFiles.add(ormMappingFile);
            }
            ORMPojo.setOrmMappingFiles(ormMappingFiles);
            String springFile = createIbatisHelper.getSpringApplicationContextFile(host, dbName, user, pswd);
            ORMPojo.setSpringFile(springFile);
        }
        return ORMPojo;
    }
}
