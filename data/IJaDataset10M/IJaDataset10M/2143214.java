package com.gjzq.webpart;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.plat.service.BaseService;

/**
 * 
 * ����: Ͷ���߽��� ��Ȩ: Copyright (c) 2010 ��˾: ˼�ϿƼ� ����: ��ѫ �汾: 1.0 ��������: 2011-4-25
 * ����ʱ��: ����09:35:06
 */
public class CatalogChildService extends BaseService {

    private static Logger logger = Logger.getLogger(CatalogChildService.class);

    /**
	 * 
	 * �����������ĿID��ѯ��������Ŀ ���ߣ���ѫ ʱ�䣺2011-4-25 ����09:39:58
	 * 
	 * @param catalogId
	 * @return
	 */
    public List getCatalogById(int catalogId, String siteno, int type) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "";
        if (type == 1) {
            sql = "SELECT CATALOG_ID,NAME,LINK_URL FROM T_CATALOG WHERE PARENT_ID = ? AND SITENO = ? AND STATE = 1 ORDER BY orderline ASC";
        } else {
            sql = "SELECT CATALOG_ID,NAME,LINK_URL FROM T_CATALOG WHERE PARENT_ID = ? AND SITENO = ? ORDER BY orderline ASC";
        }
        List list = jdbcTemplate.query(sql, new Object[] { new Integer(catalogId), siteno });
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            DataRow dataRow = (DataRow) iter.next();
            List childrenlist = null;
            if (type == 1) {
                childrenlist = jdbcTemplate.query("SELECT CATALOG_ID,NAME,LINK_URL FROM T_CATALOG WHERE PARENT_ID = ? AND SITENO = ? ORDER BY orderline ASC", new Object[] { dataRow.getInt("catalog_id"), siteno });
            } else {
                childrenlist = getCatalogById(dataRow.getInt("catalog_id"), siteno, type);
            }
            if (childrenlist.size() > 0) {
                dataRow.set("children", childrenlist);
            }
        }
        return list;
    }
}
