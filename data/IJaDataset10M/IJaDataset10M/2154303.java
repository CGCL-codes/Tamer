package com.nhncorp.usf.core.interceptor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;
import com.nhncorp.lucy.common.config.model.ApplicationInfo;
import com.nhncorp.usf.core.config.InterceptorInfo;
import com.nhncorp.usf.core.config.runtime.Action;
import com.nhncorp.usf.core.config.runtime.PageInfo;
import com.nhncorp.usf.core.config.runtime.ResultPageInfo;
import com.nhncorp.usf.core.config.runtime.ResultPageInfo.DISPATCHER_TYPE;
import com.nhncorp.usf.core.config.runtime.ResultPageInfos;
import com.nhncorp.usf.core.config.runtime.RuntimeInterceptorConfig;
import com.nhncorp.usf.core.servlet.ServletContextHolder;
import com.nhncorp.usf.core.test.MockActionInvocation;

/**
 * {@link com.nhncorp.usf.core.interceptor.impl.Json2ObjectInterceptor}
 * TestCase.
 * 
 * @author Web Platform Development Team
 */
public class Json2ObjectInterceptorTest {

    private static final String JSON_PARAMETER_NAME_PREFIX = "json_";

    private static MockActionInvocation invocation;

    private static PageInfo pageInfo;

    private static Action actionInfo;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ApplicationInfo.setNativeEncoding("utf-8");
        pageInfo = new PageInfo();
        ResultPageInfo defaultResultPageInfo = new ResultPageInfo(DISPATCHER_TYPE.mock, "mock script source");
        ResultPageInfos resultPageInfos = new ResultPageInfos();
        resultPageInfos.setDefaultResultPageInfo(defaultResultPageInfo);
        pageInfo.setGlobalSuccessResultInfo(resultPageInfos);
        pageInfo.setGlobalFailureResultInfo(resultPageInfos);
        actionInfo = new Action("testAction");
        pageInfo.addActionInfo("testAction", actionInfo);
        InterceptorInfo interceptor = InterceptorInfo.getInstance();
        interceptor.addInterceptorInfo("json2ObjectInterceptor", "com.nhncorp.usf.core.interceptor.impl.Json2ObjectInterceptor");
        RuntimeInterceptorConfig runtimeInterceptor = new RuntimeInterceptorConfig("json2ObjectInterceptor");
        Map<String, String> params = new HashMap<String, String>();
        params.put("prefixOfParamter", "\"" + JSON_PARAMETER_NAME_PREFIX + "\"");
        runtimeInterceptor.setParams(params);
        actionInfo.addRuntimeInterceptor(runtimeInterceptor);
        invocation = new MockActionInvocation(pageInfo, actionInfo);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void intercept() throws Exception {
        invocation = new MockActionInvocation(pageInfo, actionInfo);
        MockServletContext servletContext = new MockServletContext() {

            @Override
            public String getRealPath(String arg) {
                return super.getRealPath(arg);
            }
        };
        ServletContextHolder.set(servletContext);
        Map map = new HashMap();
        map.put("authServices", new String[] {});
        Map subMap1 = new HashMap();
        subMap1.put("id", "all");
        subMap1.put("newCnt", 2);
        Map subMap2 = new HashMap();
        subMap2.put("id", "blog");
        subMap2.put("isNew", "Y");
        subMap2.put("newCnt", 0);
        Map subMap3 = new HashMap();
        subMap3.put("id", "cafe");
        subMap3.put("isNew", "Y");
        subMap3.put("newCnt", 0);
        Map subMap4 = new HashMap();
        subMap4.put("id", "kin");
        subMap4.put("isNew", "Y");
        subMap4.put("newCnt", 0);
        Map subMap5 = new HashMap();
        subMap5.put("id", "happybean");
        subMap5.put("isNew", "Y");
        subMap5.put("newCnt", 0);
        Map subMap6 = new HashMap();
        subMap6.put("id", "calendar");
        subMap6.put("isNew", "N");
        subMap6.put("newCnt", 0);
        Map subMap7 = new HashMap();
        subMap7.put("id", "me2day");
        subMap7.put("isNew", "N");
        subMap7.put("newCnt", 0);
        ArrayList valueList = new ArrayList();
        valueList.add(subMap1);
        valueList.add(subMap2);
        valueList.add(subMap3);
        valueList.add(subMap4);
        valueList.add(subMap5);
        valueList.add(subMap6);
        valueList.add(subMap7);
        map.put("onServices", valueList.toArray());
        invocation.getDataMap().put(JSON_PARAMETER_NAME_PREFIX + "sample", map);
        invocation.invoke();
        Object mapObject = invocation.getDataMap().get("sample");
        Map mapObj = (Map) mapObject;
        List<Object> objs = (List<Object>) mapObj.get("onServices");
        Assert.assertEquals(7, objs.size());
    }
}
