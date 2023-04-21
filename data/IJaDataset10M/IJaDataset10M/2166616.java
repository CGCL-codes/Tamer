package com.gjzq.service;

import java.util.ArrayList;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.StringHelper;
import com.thinkive.plat.service.BaseService;

/**
 * ����:  ȯ���ʸ��ҵ
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ��ѫ
 * �汾:	 1.0
 * ��������: 2011-4-13
 * ����ʱ��: ����14:20:34
 */
public class QualificationService extends BaseService {

    /**
	 * 
	 * ����:  ���ȯ���ʸ��ҵ֤��
	 * ����:	 ��ѫ
	 * ��������: 2011-4-13
	 * ����ʱ��: ����14:23:21
	 * @param data
	 */
    public void add(DataRow data) {
        getJdbcTemplate().insert("T_GJZQ_STOCK_CERTIFICATE", data);
    }

    /**
	 * 
	 * ����: �޸�ȯ���ʸ�֤��
	 * ����:	 ��ѫ	
	 * ��������: 2011-4-13
	 * ����ʱ��: ����14:30:21
	 * @param data
	 */
    public void edit(DataRow data) {
        getJdbcTemplate().update("T_GJZQ_STOCK_CERTIFICATE", data, "job_number", data.getString("job_number"));
    }

    /**
	 * ɾ��ȯ�̴�ҵ�ʸ�֤����Ϣ
	 * ����:	 ��ѫ
	 * ��������: 2011-4-13
	 * ����ʱ��: ����15:35:21
	 * @param ID
	 */
    public void delte(String jobnumber) {
        getJdbcTemplate().delete("T_GJZQ_STOCK_CERTIFICATE", "job_number", jobnumber);
    }

    /**
	 * 
	 * ����: ���id��ѯ��ҵ�ʸ�֤����Ϣ
	 * ����:	 ��ѫ
	 * ��������: 2011-4-13
	 * ����ʱ��: ����15:36:21
	 * @param data
	 */
    public DataRow findQualificationId(String jobnumber) {
        return getJdbcTemplate().queryMap("select * from T_GJZQ_STOCK_CERTIFICATE where job_number = ?", new Object[] { jobnumber });
    }

    /**
	 * 
	 * ����: ���֤���Ų�ѯ��ҵ�ʸ�֤����Ϣ
	 * ����:	 ��ѫ
	 * ��������: 2011-4-13
	 * ����ʱ��: ����15:36:21
	 * @param data
	 */
    public DataRow findCertificate(String certificate) {
        return getJdbcTemplate().queryMap("SELECT * FROM T_GJZQ_STOCK_CERTIFICATE WHERE CERTIFICATE = ?", new Object[] { certificate });
    }

    /**
	 * ��ҳ��ѯȯ�̴�ҵ�ʸ�֤����Ϣ
	 * ����:	 ��ѫ
	 * ��������: 2011-4-13
	 * ����ʱ��: ����15:37:21
	 * @param curPage    ��ǰ�ڼ�ҳ
	 * @param rowOfPage  ÿҳ��������¼
	 * @param clientname �ͻ���ƣ�ģ��ƥ��
	 * @param type       ҵ������
	 * @param status     ״̬
	 * @return
	 */
    public DBPage getPageData(int curPage, int rowOfPage, String clientname) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT * FROM T_GJZQ_STOCK_CERTIFICATE WHERE 1=1 ");
        ArrayList argList = new ArrayList();
        if (!StringHelper.isEmpty(clientname)) {
            sqlBuf.append(" AND NAME LIKE ? ");
            argList.add("%" + clientname + "%");
        }
        sqlBuf.append(" ORDER BY JOB_NUMBER");
        return getJdbcTemplate().queryPage(sqlBuf.toString(), argList.toArray(), curPage, rowOfPage);
    }
}
