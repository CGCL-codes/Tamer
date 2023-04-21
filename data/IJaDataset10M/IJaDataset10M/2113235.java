package com.ken.dgking.handler;

import java.util.Vector;
import com.ken.dgking.model.SalesBack;

public interface SalesBackServicesHandler {

    /**
	 * ����ݿ�������µ��˻���¼
	 * @param SalesBack ��װ�õ�SalesBack����
	 * @return ִ�н��
	 */
    boolean addSalesBack(SalesBack salesBack);

    /**
	 * ��ȡ���е������˻���Ϣ
	 * @return �����˻���Ϣ����
	 */
    Vector<SalesBack> getAllSalesBack();

    /**
	 * ��ѯ��ݿ������������������˻���¼
	 * @param field ��ѯ���ֶ�
	 * @param value �����ֵ
	 * @return ��ѯ���
	 */
    Vector<SalesBack> searchSalesBack(String field, String value);

    /**
	 * ��ѯ��ݿ������������������˻���¼
	 * @param beginTime ��ѯ�Ŀ�ʼʱ��
	 * @param endTime  ��ѯ�Ľ���ʱ��
	 * @return ��ѯ���
	 */
    Vector<SalesBack> searchSalesBackByTime(String beginTime, String endTime);

    /**
	 * �ж��˻�����Ƿ����
	 * @param id 
	 * @return
	 */
    boolean isExited(String id);
}
