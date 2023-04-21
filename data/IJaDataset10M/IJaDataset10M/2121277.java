package com.cateshop;

/**
 * ��������������<code>(int)</code>��ͨ�÷���.
 * 
 * @author notXX
 * @param <bean>
 *            ��������Ե��������.
 */
public interface IntService<bean extends IntIdentifiable> extends BaseService<bean> {

    /**
     * ɾ�����.
     * 
     * @param id
     *            ����.
     */
    void delete(int id);

    /**
     * ������.
     * 
     * @param id
     *            ����.
     * @return ���.
     */
    bean get(int id);

    /**
     * �������.
     * 
     * @param bean
     *            ���.
     * @return ����.
     */
    int save(bean bean);
}
