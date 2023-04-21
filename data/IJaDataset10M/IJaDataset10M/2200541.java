package com.gjzq.webpart;

import java.util.List;
import org.apache.log4j.Logger;
import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.plat.service.BaseService;

/**
 * 
 * ����: ��Ƹ���
 * ��Ȩ: Copyright (c) 2010
 * ��˾: ˼�ϿƼ� 
 * ����: ��ѫ
 * �汾: 1.0 
 * ��������: 2011-4-25 
 * ����ʱ��: ����09:35:06
 */
public class JobService extends BaseService {

    private static Logger logger = Logger.getLogger(JobService.class);

    /**
	 * 
	 * �����������ĿID��ѯ��Ƹ��Ϣ
	 * ���ߣ���ѫ
	 * ʱ�䣺2011-4-25 ����09:39:58
	 * @param catalogId
	 * @return
	 */
    public List getJobInfo() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query("SELECT * FROM T_B_JOB WHERE Initialized=1 AND (CLOSED=0 OR CLOSED IS NULL) AND substr(EXPIREDATE,0,10) >= TO_CHAR(SYSDATE,'yyyy-mm-dd') ");
    }
}
