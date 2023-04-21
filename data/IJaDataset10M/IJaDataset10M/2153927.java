package com.thinkive.business.stock.service;

import java.util.ArrayList;
import java.util.List;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.StringHelper;
import com.thinkive.plat.service.BaseService;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2007
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-6-6
 * ����ʱ��: 9:52:08
 */
public class ExpertService extends BaseService {

    /**
	 * ���ר����Ϣ
	 *
	 * @param data
	 */
    public void add(DataRow data) {
        getJdbcTemplate().insert("T_B_EXPERT", data);
    }

    /**
	 * ����ר����Ϣ
	 *
	 * @param data
	 */
    public void update(DataRow data) {
        int expId = data.getInt("expid");
        getJdbcTemplate().update("T_B_EXPERT", data, "ExpId", new Integer(expId));
    }

    /**
	 * ɾ��ר����Ϣ
	 *
	 * @param expId
	 */
    public void delte(int expId) {
        getJdbcTemplate().delete("T_B_EXPERT", "ExpId", new Integer(expId));
    }

    /**
	 * ���ר��ID��������Ӧ��ר����Ϣ
	 *
	 * @param expId
	 * @return
	 */
    public DataRow findExpertById(int expId) {
        String sql = "select * from T_B_EXPERT where expid=?";
        ArrayList argList = new ArrayList();
        argList.add(new Integer(expId));
        return getJdbcTemplate().queryMap(sql, argList.toArray());
    }

    /**
	 * 
	 * ����:  ��ѯ����ר��  
	 * ����:	 liwei
	 * ��������: 2010-1-22
	 * ����ʱ��: ����12:18:03
	 * @return List
	 * @throws
	 */
    public List findExpertList() {
        String sql = "select a.*,b.last_time from t_b_expert a,t_user b where a.expid = b.user_id  order by ExpId desc";
        return getJdbcTemplate().query(sql);
    }

    /**
	 * ��ҳ��ѯר����Ϣ
	 *
	 * @param curPage   ��ǰ�ڼ�ҳ
	 * @param rowOfPage ÿҳ��������¼
	 * @return
	 */
    public DBPage getPageData(int curPage, int rowOfPage, String keyword, String salesName, String tiaojian) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select a.*,b.last_time from t_b_expert a,t_user b where a.expid = b.user_id ");
        ArrayList argList = new ArrayList();
        if (!StringHelper.isEmpty(keyword)) {
            sqlBuf.append(" and a.name like ? ");
            argList.add("%" + keyword + "%");
        }
        if (!StringHelper.isEmpty(salesName)) {
            sqlBuf.append(" and a.DeptName = ? ");
            argList.add(salesName);
        }
        if (!StringHelper.isEmpty(tiaojian)) {
            sqlBuf.append(" order by b.last_time desc ");
        } else {
            sqlBuf.append(" order by ExpId desc ");
        }
        return getJdbcTemplate().queryPage(sqlBuf.toString(), argList.toArray(), curPage, rowOfPage);
    }

    /**
	 * ������
	 *

	 * @return
	 */
    public List getGreateElx(String keyword, String salesName, String tiaojian) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select a.*,b.last_time from t_b_expert a,t_user b where a.expid = b.user_id ");
        ArrayList argList = new ArrayList();
        if (!StringHelper.isEmpty(keyword)) {
            sqlBuf.append(" and a.name like ? ");
            argList.add("%" + keyword + "%");
        }
        if (!StringHelper.isEmpty(salesName)) {
            sqlBuf.append(" and a.DeptName = ? ");
            argList.add(salesName);
        }
        if (!StringHelper.isEmpty(tiaojian)) {
            sqlBuf.append(" order by b.last_time desc ");
        } else {
            sqlBuf.append(" order by ExpId desc ");
        }
        return getJdbcTemplate().query(sqlBuf.toString(), argList.toArray());
    }

    /**
	 * ����ר������״̬
	 *
	 0Ϊ���ߣ�1Ϊ����
	 */
    public void getUpdateOnlineType(DataRow data, String type) {
        int expId = data.getInt("expid");
        data.set("onlinetype", type);
        getJdbcTemplate().update("T_B_EXPERT", data, "ExpId", new Integer(expId));
    }
}
