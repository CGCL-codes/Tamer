package org.fantasy.common.compile;

/**
 * @rem:编译接口
 * @author: 王文成
 * @version: 1.0
 * @since 2010-5-11
 */
public interface SqlCompiler {

    /**
     * 编译sqlCode，返回SqlBuilder
     * @param sqlCode
     * @return
     * @throws Exception
     */
    public SqlBuilder compile(String sqlCode) throws Exception;
}
