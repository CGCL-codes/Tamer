package com.gjzq.webpart;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.util.StringHelper;
import com.thinkive.plat.service.BaseService;

/**
 * 
 * ����: ��ȡ������Ϣ
 * ��Ȩ: Copyright (c) 2010
 * ��˾: ˼�ϿƼ� 
 * ����: ��ѫ
 * �汾: 1.0 
 * ��������: 2011-4-25 
 * ����ʱ��: ����09:35:06
 */
public class ArticleService extends BaseService {

    private static Logger logger = Logger.getLogger(ArticleService.class);

    /**
	 * 
	 * �����������ĿID��ѯһ�����µ���Ϣ
	 * ���ߣ���ѫ
	 * ʱ�䣺2011-4-25 ����09:39:58
	 * @param catalogId
	 * @return
	 */
    public DataRow getArticInfo(int catalogId) {
        String sql = "SELECT A.*,B.CONTENT FROM  T_ARTICLE A ,T_ARTICLE_CONTENT B WHERE A.ARTICLE_ID = B.ARTICLE_ID AND A.CATALOG_ID = ? AND STATE = 3 ORDER BY CREATE_DATE DESC";
        return getJdbcTemplate().queryMap(sql, new Object[] { catalogId });
    }

    /**
	 * 
	 * �����������ĿID��ѯ������Ϣ
	 * ���ߣ���ѫ
	 * ʱ�䣺2011-4-25 ����09:39:58
	 * @param catalogId
	 * @return
	 */
    public List getArticList(int catalogId, int rows) {
        String sql = "SELECT * FROM  T_ARTICLE A WHERE A.CATALOG_ID = ? AND A.STATE = 3 ORDER BY CREATE_DATE DESC";
        return getJdbcTemplate().query(sql, new Object[] { catalogId }, rows);
    }

    /**
	 * 
	 * �����������ĿID��ѯ��������Ŀ�Ͷ���������Ŀ����������²������±��ⲻ�ظ�
	 * ���ߣ���ѫ
	 * ʱ�䣺2011-4-25 ����09:39:58
	 * @param catalogId
	 * @return
	 */
    public List getChildArticleList(int catalogId, int rows) {
        String sql = "SELECT A.*,B.CONTENT FROM  (SELECT TITLE,URL,ARTICLE_ID,CREATE_DATE,LINK_URL,TYPE,BRIEF FROM (SELECT T.*, ROW_NUMBER() OVER(PARTITION BY TITLE ORDER BY CREATE_DATE DESC) RN FROM T_ARTICLE T " + " WHERE T.CATALOG_ID IN (SELECT CATALOG_ID FROM T_CATALOG WHERE PARENT_ID = ? ) ORDER BY CREATE_DATE DESC) WHERE RN=1 AND STATE = 3 ) A" + " LEFT JOIN T_ARTICLE_CONTENT B ON A.ARTICLE_ID = B.ARTICLE_ID";
        return getJdbcTemplate().query(sql, new Object[] { new Integer(catalogId) }, rows);
    }

    /**
	 * 
	 * ��������ݶ����ĿID��ѯ��Ŀ����������²������±��ⲻ�ظ�
	 * ���ߣ���ѫ
	 * ʱ�䣺2011-4-25 ����09:39:58
	 * @param catalogId
	 * @return
	 */
    public List getChildArticleList(String catalogId[], int rows) {
        StringBuffer sql = new StringBuffer();
        sql.append("select title,url,article_id,create_date,link_url,type from (SELECT T.*, ROW_NUMBER() OVER(PARTITION BY TITLE ORDER BY create_date desc) RN FROM T_ARTICLE T  where t.catalog_id IN (");
        for (int i = 0; i < catalogId.length; i++) {
            if (i == 0) {
                sql.append(catalogId[i]);
            } else {
                sql.append("," + catalogId[i]);
            }
        }
        sql.append(" ) order by create_date desc) where rn=1 and state = 3");
        return getJdbcTemplate().query(sql.toString(), rows);
    }

    /**
	 * 
	 * @��������ȡ������Ϣ
	 * @���ߣ���ѫ
	 * @ʱ�䣺2011-4-5 ����07:15:28
	 * @param catalogId ��ĿID
	 * @return
	 */
    public List findArticleListByid(String catalogId) {
        return findArticleListByid(catalogId, 0);
    }

    /**
	 * 
	 * @��������ȡ������Ϣ
	 * @���ߣ���ѫ
	 * @ʱ�䣺2011-4-5 ����07:15:28
	 * @param catalogId ��ĿID
	 * @param rows ���صļ�¼�� 0Ϊ��������
	 * @return
	 */
    public List findArticleListByid(String catalogId, int rows) {
        List argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT TITLE,URL,LINK_URL,CREATE_DATE,TYPE FROM T_ARTICLE WHERE STATE = 3 ");
        if (!StringHelper.isEmpty(catalogId)) {
            sqlBuf.append(" AND CATALOG_ID = ? ");
            argList.add(catalogId);
        }
        sqlBuf.append("ORDER BY CREATE_DATE DESC");
        if (rows > 0) {
            return getJdbcTemplate().query(sqlBuf.toString(), argList.toArray(), rows);
        } else {
            return getJdbcTemplate().query(sqlBuf.toString(), argList.toArray());
        }
    }
}
