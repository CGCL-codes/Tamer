package org.fantasy.common.compile;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bsh.EvalError;
import bsh.Interpreter;
import org.fantasy.common.util.Constants;
import org.fantasy.common.util.SQLBuilderException;

/**
 *  BSH方式编译
 * @author: 王文成
 * @version: 1.0
 * @since 2010-5-12
 */
public class SqlCompilerBSH implements SqlCompiler {

    @SuppressWarnings("all")
    private static final Log log = LogFactory.getLog(SqlCompilerBSH.class);

    /**
     * 编译返回SqlBuilder
     * @param sqlCode
     * @return
     * @throws Exception
     */
    public SqlBuilder compile(final String sqlCode) throws Exception {
        return new SqlBuilder() {

            public String getSql(Map map) throws Exception {
                try {
                    Interpreter interpreter = new Interpreter();
                    interpreter.set(Constants.SQL_BUILDER_MAP_NAME, map);
                    interpreter.eval(sqlCode);
                    Object sql = interpreter.get(Constants.SQL_BUILDER_BUFF_NAME);
                    if (sql != null) {
                        return sql.toString();
                    } else {
                        throw new SQLBuilderException("变量sql未找到，");
                    }
                } catch (EvalError e) {
                    e.printStackTrace();
                    log.debug(e.toString());
                    throw new SQLBuilderException("bsh scriptError:" + e.toString());
                }
            }
        };
    }
}
