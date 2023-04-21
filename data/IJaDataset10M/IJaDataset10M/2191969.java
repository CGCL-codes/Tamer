package com.nhncorp.usf.core.service.supplement;

import java.util.List;
import java.util.Map;
import javax.script.ScriptException;
import com.nhncorp.usf.core.config.runtime.Method;
import com.nhncorp.usf.core.config.runtime.ScriptInfo;
import com.nhncorp.usf.core.service.ExecutionException;
import com.nhncorp.usf.core.service.ServiceExecutionContext;
import com.nhncorp.usf.core.servlet.ServletContextHolder;
import com.nhncorp.usf.core.util.ScriptUtil;

/**
 * @author Web Platform Development Team.
 */
public class PreScriptExecutor implements PreSupplement {

    /**
     * @param context ServiceExecutionContext
     * @return boolean
     * @throws ExecutionException the ExecutionException
     * @see com.nhncorp.usf.core.service.supplement.PreSupplement#beforeExecute(com.nhncorp.usf.core.service.ServiceExecutionContext)
     */
    @SuppressWarnings("unchecked")
    public boolean beforeExecute(ServiceExecutionContext context) throws ExecutionException {
        Method method = context.getMethod();
        List<ScriptInfo> preScripts = method.getPreScriptList();
        if (!preScripts.isEmpty()) {
            Map<String, Object> scriptReturn;
            try {
                scriptReturn = (Map<String, Object>) ScriptUtil.invokeScript(ServletContextHolder.getContextRealPath(), context.getMethod().getPreScriptList(), context.getMethodParameter());
            } catch (ScriptException e) {
                throw new ExecutionException(e);
            } catch (NoSuchMethodException e) {
                throw new ExecutionException(e);
            }
            context.setMethodParameter(scriptReturn);
        }
        return true;
    }
}
