package com.alisoft.xplatform.asf.cache;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.alisoft.xplatform.asf.cache.memcached.MemcacheStats;
import com.alisoft.xplatform.asf.cache.memcached.MemcacheStatsSlab;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedResponse;

/**
 * Memcached Cache�Ľӿڶ���
 * @author wenchu.cenwc<wenchu.cenwc@alibaba-inc.com>
 *
 */
public interface IMemcachedCache extends ICache<String, Object> {

    /**
	 * ����memcache�Ľ���Ƶ����ɵ�������ʧ����˲��ñ���cache���memcache�ķ�ʽ
	 * @param key
	 * @param ���ػ���ʧЧʱ�䵥λ��
	 * @return
	 */
    public Object get(String key, int localTTL);

    /**
	 * ��ȡ���keys��Ӧ��ֵ
	 * @param keys
	 * @return
	 */
    public Object[] getMultiArray(String[] keys);

    /**
	 * ��ȡ���keys��Ӧ��key&value Entrys
	 * @param keys
	 * @return
	 */
    public Map<String, Object> getMulti(String[] keys);

    /**
	 * key���Ӧ����һ����������ʵ������inc������
	 * @param key
	 * @param inc
	 * @return
	 */
    public long incr(String key, long inc);

    /**
	 * key���Ӧ����һ����������ʵ�ּ���decr������
	 * @param key
	 * @param decr
	 * @return
	 */
    public long decr(String key, long decr);

    /**
	 * key���Ӧ����һ����������ʵ������inc������
	 * @param key
	 * @param inc
	 * @return
	 */
    public long addOrIncr(String key, long inc);

    /**
	 * key���Ӧ����һ����������ʵ�ּ���decr������
	 * @param key
	 * @param decr
	 * @return
	 */
    public long addOrDecr(String key, long decr);

    /**
	 * �洢������
	 * @param key
	 * @param count
	 */
    public void storeCounter(String key, long count);

    /**
	 * ��ȡ�Ĵ�����-1��ʾ������
	 * @param key
	 */
    public long getCounter(String key);

    /**
	 * ����ӿڷ��ص�Key������fastģʽ��
	 * ��ô���ص�key�����Ѿ���������ʧЧ���������ڴ��л��кۼ�������Ƿ�fastģʽ����ô�ͻᾫȷ���أ�����Ч�ʽϵ�
	 * @param �Ƿ���Ҫȥ����key�Ƿ����
	 * @return
	 */
    public Set<String> keySet(boolean fast);

    /**
	 * ͳ�Ʒ�������Slab�����
	 * @return
	 */
    public MemcacheStatsSlab[] statsSlabs();

    /**
	 * ͳ��Memcacheʹ�õ����
	 * @return
	 */
    public MemcacheStats[] stats();

    /**
	 * ͳ��Items�Ĵ洢���
	 * @param servers
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public Map statsItems();

    /**
	 * ͳ��Cache����Ӧʱ��
	 * @return
	 */
    public MemcachedResponse statCacheResponse();

    /**
	 * ����ͳ��ʱ�䣬��λΪ��
	 * @param checkInterval
	 */
    public void setStatisticsInterval(long checkInterval);

    /**
	 * �������,ǰ����key��������memcache�У����򱣴治�ɹ�
	 * @param key
	 * @param value
	 * @return
	 */
    public boolean add(String key, Object value);

    /**
	 * ��������Ч�ڵ���ݣ�ǰ����key��������memcache�У����򱣴治�ɹ�
	 * @param key
	 * @param value
	 * @param ��Ч��
	 * @return
	 */
    public boolean add(String key, Object value, Date expiry);

    /**
	 * �������,ǰ����key���������memcache�У����򱣴治�ɹ�
	 * @param key
	 * @param value
	 * @return
	 */
    public boolean replace(String key, Object value);

    /**
	 * ��������Ч�ڵ���ݣ�ǰ����key���������memcache�У����򱣴治�ɹ�
	 * @param key
	 * @param value
	 * @param ��Ч��
	 * @return
	 */
    public boolean replace(String key, Object value, Date expiry);

    /**
	 * �첽������ݣ���ǰ�������أ��Ժ�������
	 * @param key
	 * @param value
	 */
    public void asynPut(String key, Object value);

    /**
	 * �첽�ۼ�������������֤�ۼ��ɹ�
	 * @param key
	 * @param decr
	 */
    public void asynAddOrDecr(String key, long decr);

    /**
	 * �첽�ۼӼ�����������֤�ۼӳɹ�
	 * @param key
	 * @param incr
	 */
    public void asynAddOrIncr(String key, long incr);

    /**
	 * �첽�ۼ�������������֤�ۼ��ɹ�
	 * @param key
	 * @param decr
	 */
    public void asynDecr(String key, long decr);

    /**
	 * �첽�ۼӼ�����������֤�ۼӳɹ�
	 * @param key
	 * @param incr
	 */
    public void asynIncr(String key, long incr);

    /**
	 * �첽�洢������,����֤����ɹ�
	 * @param key
	 * @param count
	 */
    public void asynStoreCounter(String key, long count);
}
