package com.ken.dgking.handler;

import java.util.Vector;
import com.ken.dgking.model.Customer;

public interface CustomerServicesHandler {

    /**
	 * ����ݿ�������¿ͻ�
	 * @param customer ��װ�õĿͻ�
	 * @return ִ�н��
	 */
    boolean addCustomer(Customer customer);

    /**
	 * ����ݿ���ɾ��ָ���ͻ�����Ϣ
	 * @param id ��ɾ��ͻ��ı��
	 * @return ִ�н��
	 */
    boolean deleteCustomer(String id);

    /**
	 * ��ѯ��ݿ������������Ŀͻ�
	 * @param field ��ѯ���ֶ�
	 * @param value �����ֵ
	 * @return ��ѯ���
	 */
    Vector<Customer> searchCustomer(String field, String value);

    /**
	 * ���¿ͻ���Ϣ
	 * @param customer ��װ�õĿͻ�����Ϣ
	 */
    boolean modifyCustomer(Customer customer);

    /**
	 * ��ȡ���пͻ���Ϣ
	 * @return �ͻ�����
	 */
    Vector<Customer> getAllCustomer();

    /**
     * �ж��û��Ƿ����
     * @param id ��ѯ���û��㻹
     * @return ��ѯ���
     */
    boolean isExited(String id);

    /**
     * ��ȡ�ض��ͻ�����Ϣ
     * @param id �ͻ����
     * @return ��ѯ���
     */
    Customer getCustomerInfo(String id);
}
