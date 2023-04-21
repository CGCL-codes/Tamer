package cn.hrmzone.test;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

/**
 * ���ڸ��model��hbm�����ļ�����ݿ��д������Ӧ�ı�
 * �������JUnit�����в��ԡ�
 * @author hrmzone.cn
 *
 * 
 */
public class ExportDB {

    @Test
    public void createTable() {
        Configuration cfg = new Configuration().configure();
        SchemaExport export = new SchemaExport(cfg);
        export.create(true, true);
    }
}
