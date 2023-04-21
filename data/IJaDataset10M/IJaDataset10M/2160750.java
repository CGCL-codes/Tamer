package com.gjzq.wappart.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.plat.service.BaseService;

/**
 * 
 * ����: �����Ϣ
 * ��Ȩ: Copyright (c) 2010
 * ��˾: ˼�ϿƼ� 
 * ����: ��ѫ
 * �汾: 1.0 
 * ��������: 2011-6-10 
 * ����ʱ��: ����09:35:06
 */
public class ADService extends BaseService {

    private static Logger logger = Logger.getLogger(ADService.class);

    /**
	 * 
	 * �������������ѯ���
	 * ���ߣ���ѫ
	 * ʱ�䣺2011-6-13 ����09:39:58
	 * @return
	 */
    public List getADGroupInfo(int rows, String groupId) {
        List argList = new ArrayList();
        String sql = "SELECT * FROM T_AD A INNER JOIN T_AD_GROUP B ON A.GROUP_ID=B.ID WHERE  ( (FILE_STATE = 1 AND START_TIME<=TO_CHAR(SYSDATE,'YYYY-MM-DD') AND END_TIME>TO_CHAR(SYSDATE,'YYYY-MM-DD')) OR ( FILE_STATE=1 AND END_TIME  IS NULL)) AND B.ID = ? ORDER BY END_TIME DESC,A.ORDERLINE DESC";
        argList.add(groupId);
        if (rows != 0) return getJdbcTemplate().query(sql, argList.toArray(), rows); else return getJdbcTemplate().query(sql, argList.toArray());
    }

    /**
	 * 
	 * ��������Ĺ��״̬
	 * ���ߣ���ѫ
	 * ʱ�䣺2011-6-13 ����09:39:58
	 * @return
	 */
    public void upAdFileState(DataRow data) {
        String sql = "UPDATE T_AD SET FILE_STATE = 0 WHERE AD_ID = ?";
        getJdbcTemplate().update(sql, new Object[] { data.getString("ad_id") });
    }
}
