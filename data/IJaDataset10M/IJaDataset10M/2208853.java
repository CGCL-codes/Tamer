package org.fantasy.common.compile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 编译中心
 * @author: 王文成
 * @version: 1.0
 * @since 2010-5-15
 */
public class CompileCenter {

    @SuppressWarnings("all")
    private static final Log log = LogFactory.getLog(CompileCenter.class);

    /**
	 * SqlBuilder对象池
	 */
    private Map<String, SqlBuilder> pool = new HashMap<String, SqlBuilder>();

    /**
	 * 自定义编译器
	 */
    private SqlCompiler compiler;

    public void setCompiler(SqlCompiler compiler) {
        this.compiler = compiler;
    }

    /**
	 * 执行编译返回SQL
	 * 
	 * @param queryId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
    public String compile(String key, Map<String, Object> paramMap) throws Exception {
        throw new Exception("[" + key + "]不支持该方法 !");
    }

    /**
	 * 执行编译返回SQL
	 * 
	 * @param sqlName
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
    public String compileSql(String key, String sql, Map<String, Object> paramMap) throws Exception {
        if (pool.containsKey(key)) {
            return pool.get(key).getSql(paramMap);
        } else {
            SqlBuilder builder = compiler.compile(sql);
            pool.put(key, builder);
            return builder.getSql(paramMap);
        }
    }

    /**
	 * 重新加载Service
	 * 
	 * @param id
	 * @throws Exception
	 */
    public void reload(String key) throws Exception {
        pool.remove(key);
    }

    /**
	 * 重新加载Service
	 * 
	 * @param sqlId
	 * @throws Exception
	 */
    public void reload(List<String> keys) throws Exception {
        for (String key : keys) {
            reload(key);
        }
    }

    /**
	 * 重新加载所有
	 * 
	 * @throws Exception
	 */
    public void reloadAll() throws Exception {
        pool.clear();
    }

    /**
	 * 根据ServiceName返回SqlBuilder
	 * 
	 * @throws Exception
	 */
    public SqlBuilder getSqlBuilder(String key) {
        return pool.get(key);
    }
}
