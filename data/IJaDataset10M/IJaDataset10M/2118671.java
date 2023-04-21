package com.ken.dgking.handler;

import java.util.Vector;
import com.ken.dgking.model.PortIn;

public interface PortInServicesHandle {

    /**
	 * ����µĽ����¼
	 * @param portIn ��װ�õ�PortIn����
	 * @return ִ�н��
	 */
    boolean addPortIn(PortIn portIn);

    /**
	 * ��ȡ���еĽ����Ϣ
	 * @return �����Ϣ����
	 */
    Vector<PortIn> getAllPortIn();

    /**
	 * ��ѯ��ݿ������������Ľ����¼
	 * @param field ��ѯ���ֶ�
	 * @param value �����ֵ
	 * @return ��ѯ���
	 */
    Vector<PortIn> searchPortIn(String field, String value);

    /**
	 * ��ѯ��ݿ������������Ľ����¼
	 * @param beginTime ��ѯ�Ŀ�ʼʱ��
	 * @param endTime  ��ѯ�Ľ���ʱ��
	 * @return ��ѯ���
	 */
    Vector<PortIn> searchPortInByTime(String beginTime, String endTime);

    /**
	 * �жϽ������Ƿ����
	 * @param id 
	 * @return
	 */
    boolean isExited(String id);
}
