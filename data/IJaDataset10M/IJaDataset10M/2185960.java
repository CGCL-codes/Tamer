package org.jofc.itf;

import org.jofc.bean.ChartTable;

/**
 * 通过实现这个接口来达到使用外部缓存的目的，可以根据实际情况来编写，比如本地文件或者Memcached等
 * 
 * @author 张啸 (zhangxiao@xiaomi.com):
 * @version 创建时间：2011-8-19 下午02:10:37
 */
public interface JOFCDataCache {

    ChartTable getQueryResultFromCache(String key);

    void putQueryResultInCache(String key, ChartTable ct);

    void deleteQueryResultFromCache(String key);
}
