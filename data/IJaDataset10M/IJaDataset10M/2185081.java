package com.googlecode.proxymatic.apps.data.handlers;

import com.googlecode.proxymatic.core.BuildtimeContext;
import com.googlecode.proxymatic.core.RuntimeContext;
import com.googlecode.proxymatic.core.handlers.method.ToStringMethodHandler;
import com.googlecode.proxymatic.core.util.StringUtil;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeSet;

public class DataToStringMethodHandler extends ToStringMethodHandler {

    public Object invoke(Method method, Object[] parameters, RuntimeContext context) throws Throwable {
        BuildtimeContext buildtimeContext = context.getBuildtimeContext();
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtil.shortName(buildtimeContext.getTargetInterface()));
        sb.append('{');
        Map map = (Map) context.getImplementationObject(0);
        boolean first = true;
        for (Object key : new TreeSet(map.keySet())) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(key).append("='").append(map.get(key)).append('\'');
            first = false;
        }
        sb.append('}');
        return sb.toString();
    }
}
