package com.thinkive.business.other.service;

import java.util.ArrayList;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.StringHelper;
import com.thinkive.plat.service.BaseService;

/**
 * ����:  �˲���Ƹ
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ��ѫ
 * �汾:	 1.0
 * ��������: 2011-4-22
 * ����ʱ��: ����1:20:34
 */
public class JobManageService extends BaseService {

    /**
	 * ��ҳ��ѯ�˲���Ƹ��Ϣ
	 * ����:	 ��ѫ
	 * ��������: 2011-4-19
	 * ����ʱ��: ����16:21:21
	 * @param curPage
	 * @param rowOfPage
	 * @param clientname
	 * @param status
	 * @param siteNo
	 * @return
	 */
    public DBPage getPageData(int curPage, int rowOfPage, String position, String siteNo) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT * FROM T_B_JOB WHERE 1=1");
        ArrayList argList = new ArrayList();
        if (!StringHelper.isEmpty(position)) {
            sqlBuf.append(" AND POSITION LIKE ? ");
            argList.add("%" + position + "%");
        }
        if (StringHelper.isNotEmpty(siteNo)) {
            sqlBuf.append(" AND SITENO = ?");
            argList.add(siteNo);
        }
        sqlBuf.append(" ORDER BY JOBID DESC ");
        return getJdbcTemplate().queryPage(sqlBuf.toString(), argList.toArray(), curPage, rowOfPage);
    }

    /**
	 * 
	 * ����:  �����Ƹ��Ϣ
	 * ����:	 ��ѫ
	 * ��������: 2011-4-22
	 * ����ʱ��: ����1:23:21
	 * @param data
	 */
    public void add(DataRow data) {
        String id = getSeqValue("T_B_JOB");
        data.set("jobid", id);
        getJdbcTemplate().insert("T_B_JOB", data);
    }

    /**
	 * 
	 * ����:  �޸���Ƹ��Ϣ
	 * ����:	 ��ѫ
	 * ��������: 2011-4-22
	 * ����ʱ��: ����1:23:21
	 * @param data
	 */
    public void update(DataRow data) {
        getJdbcTemplate().update("T_B_JOB", data, "jobid", data.getString("jobid"));
    }

    /**
	 * ɾ����Ƹ��Ϣ
	 *
	 * @param issueId
	 */
    public void delte(int jobid) {
        getJdbcTemplate().delete("T_B_JOB", "jobid", new Integer(jobid));
    }

    /**
	 * ����ID��ѯ��Ϣ
	 * @param jobid
	 * @return
	 */
    public DataRow findJobById(String jobid) {
        String sql = "SELECT * FROM T_B_JOB WHERE JOBID = ?";
        return getJdbcTemplate().queryMap(sql, new Object[] { jobid });
    }
}
