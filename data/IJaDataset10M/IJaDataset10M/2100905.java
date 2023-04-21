package com.jeecms.core.service;

/**
 * ͨ��ID����
 * 
 * <p>
 * ����hibernateֻ֧��ͨ��ID���һ�����󣬶���ͨ��Ψһ���Բ��Ҷ������á� Ϊ�ˣ��ṩͨ�����Բ���ID�Ĺ������档
 * </p>
 * 
 * @author liufang
 * 
 */
public interface IdCacheSvc extends CacheSvc {

    /**
	 * ���뻺��
	 * 
	 * @param id
	 * @param key
	 * @param otherKeys
	 *            ���KEY��'@'�������
	 */
    public void put(Long id, Object key, Object... otherKeys);

    /**
	 * ��û���
	 * 
	 * @param key
	 * @param otherKeys
	 * @return ��û���ֵ�����治�����򷵻�null
	 */
    public Long get(Object key, Object... otherKeys);

    /**
	 * �Ƴ��
	 * 
	 * @param key
	 * @param otherKeys
	 * @return �ɹ��Ƴ��true�����治���ڻ������������false��
	 */
    public boolean remove(Object key, Object... otherKeys);
}
