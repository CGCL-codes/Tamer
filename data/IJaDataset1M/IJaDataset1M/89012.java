package org.fantasy.common.compile;

import java.util.Map;

/**
 *  Sql构造器
 * @author: 王文成
 * @version: 1.0
 * @since 2010-5-12
 */
public interface SqlBuilder {

    /**
     * Sql构造器
     * @param map
     * @return
     * @throws Exception
     */
    String getSql(Map map) throws Exception;
}
