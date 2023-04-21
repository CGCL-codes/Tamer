package com.xy.sframe.component.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xy.sframe.component.log.ILogger;
import com.xy.sframe.component.log.LoggerFactory;
import com.xy.sframe.component.ormap.tablestruct.ITableLoader;
import com.xy.sframe.component.ormap.tablestruct.RePoolTable;
import com.xy.sframe.component.ormap.tablestruct.ReTableMap;
import com.xy.sframe.component.ormap.tablestruct.TableLoaderFactory;
import com.xy.sframe.component.xml.XMLDataObject;

/**
 * @author luwenpeng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableStructConfig {

    private static ILogger logger = LoggerFactory.getDefaultLog();

    private static final String CONFIG_NAME = "��ṹ����";

    public static void load(String configFiles) {
        logger.info("=================================");
        logger.info("��ʼ��ʼ����ṹ�����ļ�");
        XMLDataObject xdo = XMLDataObject.parseFile(configFiles);
        if (xdo.hasNode("tablestruct")) {
            xdo.rootScrollTo("tablestruct");
            loadTableStruct(xdo);
        } else {
            logger.info("�����ļ���û��tablestruct��û�м��ر�ṹ");
        }
        logger.info("��ʼ����ṹ�����ļ�����");
        logger.info("=================================");
    }

    public static void loadTableStruct(XMLDataObject xdo) {
        try {
            for (int row = 0; row < xdo.getRowCount(); row++) {
                RePoolTable pool = new RePoolTable();
                pool.setPoolName(xdo.getItemString(row, "poolname"));
                pool.setDbType(xdo.getItemString(row, "dbtype"));
                XMLDataObject schemaXdo = xdo.getXDO("pool", row);
                schemaXdo.rootScrollTo("schemas");
                for (int i = 0; i < schemaXdo.getColumnCount(); i++) {
                    pool.getSchemas().add(schemaXdo.getItemString(0, i));
                }
                ReTableMap tableMap = new ReTableMap();
                tableMap.setPoolref(pool);
                pool.setTableMap(tableMap);
                RePoolTable.poolTableMap.put(pool.getPoolName(), pool);
            }
            for (Iterator it = RePoolTable.poolTableMap.keySet().iterator(); it.hasNext(); ) {
                String poolName = (String) it.next();
                logger.info("LoadTableStruct pool name = " + poolName);
                RePoolTable pool = (RePoolTable) RePoolTable.poolTableMap.get(poolName);
                ITableLoader loader = TableLoaderFactory.getTableLoader(pool.getDbType());
                for (int i = 0; i < pool.getSchemas().size(); i++) {
                    logger.info("LoadTableStruct schema: " + pool.getSchemas().get(i));
                    loader.load(pool.getPoolName(), (String) pool.getSchemas().get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
