package org.fantasy.common.db.center;

import org.fantasy.common.db.Query;
import java.util.Map;

public interface QryCenterFactory {

    /**
     * 返回QryService接口
     * 
     * @param serviceName
     * @param paramMap
     * @return
     */
    QryCenter getQryCenter(String name);

    /**
     * 返回QryService接口
     * 
     * @param serviceName
     * @param paramMap
     * @return
     */
    QryCenter getQryCenter(String name, Map<String, Object> paramMap);

    /**
     * 返回Query接口
     * 
     * @param serviceName
     * @param paramMap
     * @return
     */
    public Query getQuery(String sql);

    /**
     * 返回Query接口
     * 
     * @param serviceName
     * @param paramMap
     * @return
     */
    public Query getQuery(String sql, Map<String, Object> paramMap);
}
