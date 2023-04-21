package com.nhncorp.usf.core.result;

import java.util.HashMap;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.opensymphony.xwork.ActionContext;
import com.nhncorp.lucy.web.helper.ServletHelper;
import com.nhncorp.usf.core.config.runtime.ResultPageInfo;
import com.nhncorp.usf.core.config.runtime.ResultPageInfo.DISPATCHER_TYPE;
import com.nhncorp.usf.core.config.ReplaceAllInfo;
import com.nhncorp.usf.core.util.StringUtil;
import junit.framework.Assert;

/**
 * {@link ForwardResult} TestCase.
 *
 * @author Web Platform Development Team
 */
public class ForwardResultTestCase {

    AbstractResult result = new ForwardResult();

    ResultPageInfo resultPageInfo;

    @BeforeClass
    public static void initialize() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ActionContext.getContext().put(ServletHelper.HTTP_REQUEST, request);
        ActionContext.getContext().put(ServletHelper.HTTP_RESPONSE, response);
    }

    @Test
    public void noQueryStringForward() throws Exception {
        resultPageInfo = new ResultPageInfo(DISPATCHER_TYPE.forward, "/hello.jsp");
        try {
            result.execute(resultPageInfo, new HashMap<String, Object>());
            String forwardedUrl = ((MockHttpServletResponse) ServletHelper.getResponse()).getForwardedUrl();
            Assert.assertEquals(forwardedUrl, "/hello.jsp");
        } catch (Exception except) {
            Assert.fail();
        }
    }

    @Test
    public void queryStringForward() throws Exception {
        resultPageInfo = new ResultPageInfo(DISPATCHER_TYPE.forward, "/hello.jsp?name=${name}&age=${age}");
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("name", "Gildong,Hong");
        parameterMap.put("age", "100");
        try {
            result.execute(resultPageInfo, parameterMap);
            String forwardedUrl = ((MockHttpServletResponse) ServletHelper.getResponse()).getForwardedUrl();
            Assert.assertEquals(forwardedUrl, "/hello.jsp?name=Gildong,Hong&age=100");
        } catch (Exception except) {
            Assert.fail();
        }
    }

    @Test
    public void queryStringForwardWithReplaceAll1() throws Exception {
        ReplaceAllInfo replaceAllInfo = ReplaceAllInfo.getInstance();
        replaceAllInfo.clear();
        replaceAllInfo.putReplaceInfo("name", "choco-1.0");
        replaceAllInfo.putReplaceInfo("age", "200");
        resultPageInfo = new ResultPageInfo(DISPATCHER_TYPE.forward, "/hello.jsp?name=${name}&age=${age}");
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("name", "Gildong,Hong");
        parameterMap.put("age", "100");
        try {
            result.execute(resultPageInfo, parameterMap);
            String forwardedUrl = ((MockHttpServletResponse) ServletHelper.getResponse()).getForwardedUrl();
            Assert.assertEquals(forwardedUrl, "/hello.jsp?name=Gildong,Hong&age=100");
        } catch (Exception except) {
            Assert.fail();
        }
    }

    @Test
    public void queryStringForwardWithReplaceAll2() throws Exception {
        ReplaceAllInfo replaceAllInfo = ReplaceAllInfo.getInstance();
        replaceAllInfo.clear();
        replaceAllInfo.putReplaceInfo("name", "choco-1.0");
        replaceAllInfo.putReplaceInfo("age", "200");
        String src = "/hello.jsp?name=@{name}&age=@{age}";
        resultPageInfo = new ResultPageInfo(DISPATCHER_TYPE.forward, StringUtil.replacePageSrc(src));
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("name", "GildongHong");
        parameterMap.put("age", "100");
        try {
            result.execute(resultPageInfo, parameterMap);
            String forwardedUrl = ((MockHttpServletResponse) ServletHelper.getResponse()).getForwardedUrl();
            Assert.assertEquals(forwardedUrl, "/hello.jsp?name=choco-1.0&age=200");
        } catch (Exception except) {
            Assert.fail();
        }
    }

    @Test
    public void queryStringForwardWithReplaceAll3() throws Exception {
        ReplaceAllInfo replaceAllInfo = ReplaceAllInfo.getInstance();
        replaceAllInfo.clear();
        replaceAllInfo.putReplaceInfo("name", "choco-1.0");
        replaceAllInfo.putReplaceInfo("age", "200");
        String src = "/hello.jsp?name=${name}&age=@{age}";
        resultPageInfo = new ResultPageInfo(DISPATCHER_TYPE.forward, StringUtil.replacePageSrc(src));
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("name", "Gildong,Hong");
        parameterMap.put("age", "100");
        try {
            result.execute(resultPageInfo, parameterMap);
            String forwardedUrl = ((MockHttpServletResponse) ServletHelper.getResponse()).getForwardedUrl();
            Assert.assertEquals(forwardedUrl, "/hello.jsp?name=Gildong,Hong&age=200");
        } catch (Exception except) {
            Assert.fail();
        }
    }
}
