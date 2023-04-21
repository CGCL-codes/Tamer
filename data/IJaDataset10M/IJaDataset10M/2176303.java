package com.gjzq.pojo;

import java.io.Serializable;

/**
 * ����: ��վ�û���Ϣ��pojo
 * ��Ȩ: Copyright (c) 2010
 * ��˾: ˼�ϿƼ� 
 * ����:���� 
 * �汾: 1.0 
 * ��������: 2011-6-28 
 * ����ʱ��: ����08:49:27
 */
public class AccountInfo implements Serializable {

    private String userId;

    private String name;

    private String loginId;

    private String clientId;

    /**
	 * 
	 * @������
	 * @���ߣ����
	 * @ʱ�䣺2011-6-28 ����10:33:47
	 * @return
	 */
    public String getUserId() {
        return userId;
    }

    /**
	 * 
	 * @������
	 * @���ߣ����
	 * @ʱ�䣺2011-6-28 ����10:33:43
	 * @param userId
	 */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
	 * ������crm�пͻ���Ż�ȡ(rowId)
	 * ���ߣ�����
	 * ʱ�䣺2011-6-28 ����09:06:07
	 * @return
	 */
    public String getClientId() {
        return clientId;
    }

    /**
	 * ������crm�пͻ��������(rowId)
	 * ���ߣ�����
	 * ʱ�䣺2011-6-28 ����09:05:52
	 * @param clientId
	 */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
	 * ������crm�е�¼�Ż�ȡ
	 * ���ߣ�����
	 * ʱ�䣺2011-6-28 ����09:02:44
	 * @return
	 */
    public String getLoginId() {
        return loginId;
    }

    /**
	 * ������crm�е�¼������
	 * ���ߣ�����
	 * ʱ�䣺2011-6-28 ����09:02:17
	 * @param loginId
	 */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
	 * ��������վ�õ��ı����ȡ
	 * ���ߣ�����
	 * ʱ�䣺2011-6-28 ����09:01:04
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * ��������վ�õ��ı�������
	 * ���ߣ�����
	 * ʱ�䣺2011-6-28 ����09:00:53
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }
}
