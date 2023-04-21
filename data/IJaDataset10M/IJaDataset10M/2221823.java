package com.gjzq.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.util.StringHelper;
import com.thinkive.plat.service.BaseService;

public class EmailManageService extends BaseService {

    private static Logger logger = Logger.getLogger(EmailQueueService.class);

    public DBPage getPageData(int curPage, int rowOfPage, String name, String type, String siteNo) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select a.*,b.branchname,c.description  from T_B_EMAIL_RECEIVE a left join t_b_branch b on a.branch_no=b.branchno inner join t_common_template c on a.email_plate=c.name where 1=1 ");
        ArrayList argList = new ArrayList();
        if (!StringHelper.isEmpty(name)) {
            sqlBuf.append(" AND a.receive_name LIKE ? ");
            argList.add("%" + name + "%");
        }
        if (!StringHelper.isEmpty(type)) {
            sqlBuf.append(" AND a.type = ?");
            argList.add(type);
        }
        if (StringHelper.isNotEmpty(siteNo)) {
            sqlBuf.append(" AND a.SITENO = ?");
            argList.add(siteNo);
        }
        sqlBuf.append("ORDER BY a.createdtime,a.id DESC");
        return getJdbcTemplate().queryPage(sqlBuf.toString(), argList.toArray(), curPage, rowOfPage);
    }

    /**
	* �����������˱�
	*
	* @param dataRow
	*/
    public int add(DataRow dataRow) {
        try {
            JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
            String emailReceiveid = this.getSeqValue("T_B_EMAIL_RECEIVE");
            if (emailReceiveid != null && emailReceiveid.length() != 0) {
                dataRow.set("id", emailReceiveid);
                jdbcTemplate.insert("T_B_EMAIL_RECEIVE", dataRow);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * ������������˱�
	 *
	 * @param data
	 */
    public void update(DataRow data) {
        int id = data.getInt("id");
        getJdbcTemplate().update("T_B_EMAIL_RECEIVE", data, "id", new Integer(id));
    }

    /**
	 * ɾ����������˱�
	 *
	 * @param issueId
	 */
    public void delte(int id) {
        getJdbcTemplate().delete("T_B_EMAIL_RECEIVE", "id", new Integer(id));
    }

    /**
	 * ɾ��������������˱�
	 */
    public void deleteAll() {
        getJdbcTemplate().update("delete from T_B_EMAIL_RECEIVE");
    }

    /**
	 * �����ϢID��������Ӧ����Ϣ
	 *
	 * @param issueId
	 * @return
	 */
    public DataRow findEmailReceiveById(int id) {
        String sql = "select * from T_B_EMAIL_RECEIVE where id=?";
        ArrayList argList = new ArrayList();
        argList.add(new Integer(id));
        return getJdbcTemplate().queryMap(sql, argList.toArray());
    }

    /**
	 * �鿴����branchno
	 */
    public List findAllBranchNo() {
        String sql = "select branchno,branchname from t_b_Branch";
        return getJdbcTemplate().query(sql);
    }

    /**
	 * �鿴����t_common_template 
	 */
    public List findAllCommTemplate() {
        String sql = "SELECT NAME,Description from t_common_template";
        return getJdbcTemplate().query(sql);
    }
}
