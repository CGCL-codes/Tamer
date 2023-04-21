package com.thinkive.business.other.service;

import java.util.ArrayList;
import java.util.List;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.StringHelper;
import com.thinkive.plat.service.BaseService;

public class HomeClientService extends BaseService {

    public DBPage getPageData(int curPage, int rowOfPage, String selectMethod, String v) {
        DBPage page = null;
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT C.*,B.BRANCHNAME FROM T_B_HOME_CLIENT C LEFT JOIN T_B_BRANCH B ON C.BRANCHNO = B.BRANCHNO WHERE 1=1 ");
        if (StringHelper.isNotEmpty(selectMethod) && StringHelper.isNotEmpty(v)) {
            sqlBuf.append(" AND " + selectMethod + " = ?");
            argList.add(v);
        }
        sqlBuf.append(" ORDER BY CREATE_DATE DESC");
        page = getJdbcTemplate().queryPage(sqlBuf.toString(), argList.toArray(), curPage, rowOfPage);
        return page;
    }

    public List getHomeClientData(String siteno, String login_id, String name, String state, String asset_account, String type, String isyg, String phone, String email) {
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT * FROM T_B_HOME_CLIENT WHERE 1=1 ");
        if (StringHelper.isNotEmpty(login_id)) {
            sqlBuf.append(" AND LOGIN_ID LIKE ?");
            argList.add("%" + login_id + "%");
        }
        if (StringHelper.isNotEmpty(name)) {
            sqlBuf.append(" AND NAME LIKE ?");
            argList.add("%" + name + "%");
        }
        if (StringHelper.isNotEmpty(state)) {
            sqlBuf.append(" AND STATE=?");
            argList.add(state);
        }
        if (StringHelper.isNotEmpty(asset_account)) {
            sqlBuf.append(" AND ASSET_ACCOUNT LIKE ?");
            argList.add("%" + asset_account + "%");
        }
        if (StringHelper.isNotEmpty(siteno)) {
            sqlBuf.append(" AND SITENO=?");
            argList.add(siteno);
        }
        if (StringHelper.isNotEmpty(type)) {
            sqlBuf.append(" AND TYPE=?");
            argList.add(type);
        }
        if (StringHelper.isNotEmpty(isyg)) {
            sqlBuf.append(" AND ISYG = ?");
            argList.add(isyg);
        }
        if (StringHelper.isNotEmpty(phone)) {
            sqlBuf.append(" AND PHONE LIKE ?");
            argList.add("%" + phone + "%");
        }
        if (StringHelper.isNotEmpty(email)) {
            sqlBuf.append(" AND EMAIL LIKE ?");
            argList.add("%" + email + "%");
        }
        sqlBuf.append(" ORDER BY CREATE_DATE DESC");
        return getJdbcTemplate().query(sqlBuf.toString(), argList.toArray());
    }

    public void update(DataRow homeClient) {
        getJdbcTemplate().update("T_B_HOME_CLIENT", homeClient, "ID", new Integer(homeClient.getString("id")));
    }

    public DataRow findClientById(String id) {
        DataRow data = null;
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT * FROM T_B_HOME_CLIENT WHERE 1=1");
        if (StringHelper.isNotBlank(id)) {
            sqlBuf.append(" AND passport_id=?");
            argList.add(id);
        }
        data = getJdbcTemplate().queryMap(sqlBuf.toString(), argList.toArray());
        return data;
    }

    public void close(int id) {
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("UPDATE T_B_HOME_CLIENT SET STATE=2 WHERE ID=?");
        argList.add(new Integer(id));
        getJdbcTemplate().update(sqlBuf.toString(), argList.toArray());
    }

    public void open(int id) {
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("UPDATE T_B_HOME_CLIENT SET STATE=1 WHERE ID=?");
        argList.add(new Integer(id));
        getJdbcTemplate().update(sqlBuf.toString(), argList.toArray());
    }

    public void modifyYg(int id, String state) {
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("UPDATE T_B_HOME_CLIENT SET ISYG=? WHERE ID=?");
        argList.add(state);
        argList.add(new Integer(id));
        getJdbcTemplate().update(sqlBuf.toString(), argList.toArray());
    }

    /**
	 * 
	 * �������༭�û���Ϣ
	 * ���ߣ����� liubao@thinkive.com
	 * 2009-4-17 ����16:37:15
	 * @return
	 */
    public void cancelBindById(String id) {
        ArrayList argList = new ArrayList();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("UPDATE T_B_HOME_CLIENT SET TYPE = 0,STATE = 2,ASSET_ACCOUNT = '' WHERE ID = ?");
        argList.add(new Integer(id));
        getJdbcTemplate().update(sqlBuf.toString(), argList.toArray());
    }
}
