package com.liusoft.dlog4j.velocity;

import java.sql.DatabaseMetaData;
import java.util.Properties;
import com.liusoft.dlog4j.dao.DAO;

/**
 * ���ڻ�ȡDLOG4J����ʱ��Ϣ��toolbox
 * @author liudong
 */
public class DLOG_Snoop_VelocityTool {

    /**
	 * ��ȡ��ݿ��Ԫ��Ϣ
	 * @return
	 */
    public DatabaseMetaData getDatabaseMetadata() {
        return DAO.metadata();
    }

    public String env(String key) {
        return System.getProperty(key);
    }

    public Properties envs() {
        return System.getProperties();
    }
}
