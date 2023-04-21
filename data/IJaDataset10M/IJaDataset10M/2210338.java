package com.gjzq.service;

import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.StringHelper;
import com.thinkive.plat.service.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2007
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-6-15
 * ����ʱ��: 14:58:06
 */
public class RegionService extends BaseService {

    /**
     * ���Ӫҵ���������Ϣ
     *
     * @param data
     */
    public void add(DataRow data) {
        String id = getSeqValue("T_B_REGION");
        data.set("id", id);
        if (StringHelper.isBlank(data.getString("ordernum"))) {
            data.set("ordernum", id);
        }
        getJdbcTemplate().insert("T_B_REGION", data);
    }

    /**
     * ����Ӫҵ���������Ϣ
     *
     * @param data
     */
    public void update(DataRow data) {
        int id = data.getInt("id");
        getJdbcTemplate().update("T_B_REGION", data, "id", new Integer(id));
    }

    /**
     * ɾ��Ӫҵ���������Ϣ
     *
     * @param id
     */
    public void delte(int id) {
        getJdbcTemplate().delete("T_B_REGION", "id", new Integer(id));
    }

    /**
     * ���Ӫҵ������ID��������Ӧ��Ӫҵ��������Ϣ
     *
     * @param id
     * @return
     */
    public DataRow findRegionById(int id) {
        String sql = "select * from T_B_REGION where id=?";
        ArrayList argList = new ArrayList();
        argList.add(new Integer(id));
        return getJdbcTemplate().queryMap(sql, argList.toArray());
    }

    /**
     * ���Ӫҵ��������ƣ�������Ӧ��Ӫҵ��������Ϣ
     *
     * @param Regionname
     * @return
     */
    public List findRegionByRegionname(String Regionname) {
        String sql = "select * from T_B_REGION where Regionname like '%" + Regionname + "%'";
        return getJdbcTemplate().query(sql);
    }

    /**
     * ���Ӫҵ������id��������Ӧ��Ӫҵ����Ϣ
     *
     * @param Regionname
     * @return
     */
    public List findBranchByRegion(int region) {
        String sql = "select * from t_b_branch where region= ?";
        ArrayList argList = new ArrayList();
        argList.add(region);
        return getJdbcTemplate().query(sql, argList.toArray());
    }

    /**
     * ��ѯ���е�Ӫҵ���������Ϣ
     *
     * @return
     */
    public DBPage getPageData(int curPage, int rowOfPage, String regionname) {
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select * from T_B_REGION where 1=1");
        if (StringHelper.isNotEmpty(regionname)) {
            sqlBuf.append(" and regionname = ? ");
            argList.add(regionname);
        }
        sqlBuf.append(" order by ordernum asc");
        return getJdbcTemplate().queryPage(sqlBuf.toString(), argList.toArray(), curPage, rowOfPage);
    }

    /**
     * �ж��������Ƿ��Ѿ�����
     *
     * @param branchNo
     * @return
     */
    public boolean isIdExist(String ID) {
        String sql = "select id from T_B_REGION where id=?";
        ArrayList argList = new ArrayList();
        argList.add(ID);
        return (getJdbcTemplate().queryMap(sql, argList.toArray()) != null);
    }

    public boolean isIdExist(String ID, String siteNo) {
        String sql = "select id from T_B_REGION where 1=1";
        ArrayList argList = new ArrayList();
        if (StringHelper.isNotEmpty(ID)) {
            sql += " and id=?";
            argList.add(ID);
        }
        if (StringHelper.isNotEmpty(siteNo)) {
            sql += " and siteno=?";
            argList.add(siteNo);
        }
        return (getJdbcTemplate().queryMap(sql, argList.toArray()) != null);
    }
}
