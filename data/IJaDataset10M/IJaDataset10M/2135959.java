package org.hlj.commons.cache;

import java.util.List;
import java.util.Map;

/**
 * 所有缓存的基础接口
 * @author WD
 * @since JDK5
 * @version 1.0 2009-09-04
 */
public interface Cache<K, V> {

    /**
	 * 获得所有缓存Map
	 * @return Map
	 */
    Map<K, V> getCaches();

    /**
	 * 设置所有缓存Map
	 * @param caches 缓存
	 * @return Map
	 */
    Map<K, V> setCaches(Map<K, V> caches);

    /**
	 * 获得所有缓存Key的List
	 * @return List
	 */
    List<K> getKeys();

    /**
	 * 获得一个缓存数据
	 * @param key 缓存Key
	 * @return 缓存Value
	 */
    V get(K key);

    /**
	 * 添加缓存
	 * @param key 缓存的Key
	 * @param value 缓存的Value
	 * @return 缓存的Value
	 */
    V add(K key, V value);

    /**
	 * 添加缓存
	 * @param caches 缓存
	 * @return 添加的缓存
	 */
    Map<K, V> add(Map<K, V> caches);

    /**
	 * 更新缓存
	 * @param key 缓存Key
	 * @param value 缓存Value
	 * @return 缓存的Value
	 */
    V ref(K key, V value);

    /**
	 * 更新缓存
	 * @param cachees 缓存
	 * @return 更新的缓存
	 */
    Map<K, V> ref(Map<K, V> caches);

    /**
	 * 删除缓存
	 * @param key 缓存Key
	 * @return 缓存的Value
	 */
    V remove(K key);

    /**
	 * 删除缓存
	 * @param keys 缓存Key
	 * @return value列表
	 */
    List<V> remove(List<K> keys);

    /**
	 * 删除缓存
	 * @param cachees 缓存
	 * @return 删除的缓存
	 */
    List<V> remove(Map<K, V> caches);

    /**
	 * 删除size个缓存 重Key缓存List尾部开始删除 如果size大于缓存大小 清除全部缓存
	 * @param size 删除的缓存个数
	 * @return value列表
	 */
    List<V> removeOfSize(int size);

    /**
	 * 获得缓存大小
	 * @return 缓存大小
	 */
    int getSize();

    /**
	 * 获得实际缓存的大小
	 * @return 实际缓存的大小
	 */
    int getCacheSize();

    /**
	 * 设置缓存大小 如果size小于1 缓存全部数据
	 * @param size 缓存大小
	 */
    void setSize(int size);

    /**
	 * 清除所有缓存
	 */
    void clear();

    /**
	 * 修整缓存 检查缓存是否小于size,小于删除多余的缓存.
	 * 数据和Key是否相同,如果数据有Key没有,给Key添加缓存,如果Key有,数据没有,删除Key
	 */
    void trim();
}
