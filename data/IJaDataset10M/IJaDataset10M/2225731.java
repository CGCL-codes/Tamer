package cn.common.mybatis;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import cn.common.log.GeneralManager;
import cn.common.log.SqlLog;
import cn.common.util.PropertyUtils;
import com.google.gson.Gson;

/**
 * mybatis执行sql语句记录执行的sql及参数，及用户权限执行
 * 
 * @author 孙树林
 * 
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class SqlQueryLogInterceptor implements Interceptor {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        GeneralManager generalManager = GeneralManager.getCurrentManager();
        if (generalManager == null) {
            return invocation.proceed();
        }
        Object[] objs = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) objs[0];
        Object obj = objs[1];
        if (obj != null) {
            BoundSql boundSql = mappedStatement.getBoundSql(obj);
            String sql = boundSql.getSql().replaceAll("\\s+|\n|\t|\b", " ");
            int size = boundSql.getParameterMappings() != null ? boundSql.getParameterMappings().size() : 0;
            Map maps = new HashMap();
            for (int i = 0; i < size; i++) {
                ParameterMapping pm = boundSql.getParameterMappings().get(i);
                String parameterName = pm.getProperty();
                String parameterValue = PropertyUtils.getPropertyValue(obj, pm.getProperty()) != null ? String.valueOf(PropertyUtils.getPropertyValue(obj, pm.getProperty())) : "";
                maps.put(parameterName, parameterValue);
            }
            Gson json = new Gson();
            generalManager.addSql(new SqlLog(sql, json.toJson(maps)));
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
