package org.t2framework.t2.contexts.impl;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.commons.meta.BeanDescFactory;
import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.commons.util.CollectionsUtil;
import org.t2framework.t2.action.impl.PageDescFinderImplTest.TestPage;
import org.t2framework.t2.annotation.composite.GET;
import org.t2framework.t2.annotation.composite.POST;
import org.t2framework.t2.annotation.core.ActionParam;
import org.t2framework.t2.annotation.core.ActionPath;
import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.contexts.ActionMethodDesc;
import org.t2framework.t2.contexts.Request;
import org.t2framework.t2.contexts.WebContext;
import org.t2framework.t2.navigation.Forward;
import org.t2framework.t2.spi.Navigation;
import org.t2framework.t2.util.impl.UrlTemplateImpl;

/**
 * @author shot
 * 
 */
public class PageDescImplTest extends TestCase {

    public static Set<Class<? extends Annotation>> set = CollectionsUtil.newHashSet();

    static {
        set.add(ActionPath.class);
        set.add(ActionParam.class);
        set.add(GET.class);
        set.add(POST.class);
    }

    public void testCreate1() throws Exception {
        PageDescImpl pageDesc = new PageDescImpl("hoge", "testPage", BeanDescFactory.getBeanDesc(TestPage.class), new UrlTemplateImpl("/test"), set);
        assertNotNull(pageDesc);
        assertNotNull(pageDesc.getDefaultMethodDesc());
        ActionMethodDesc amd = pageDesc.getActionMethodDesc();
        assertNotNull(amd);
        assertTrue(!amd.isEmpty());
        amd = pageDesc.getActionMethodDesc();
        assertNotNull(amd);
        assertTrue(!amd.isEmpty());
    }

    public void testCreate2() throws Exception {
        PageDescImpl pageDesc = new PageDescImpl("hoge", "testPage", BeanDescFactory.getBeanDesc(Test2Page.class), new UrlTemplateImpl("/test"), set);
        assertNotNull(pageDesc);
        assertNotNull(pageDesc.getDefaultMethodDesc());
        ActionMethodDesc amd = pageDesc.getActionMethodDesc();
        assertNotNull(amd);
        assertTrue(amd.getMethodDescSize() == 3);
        MethodDesc methodDesc = amd.getMethodDesc("link");
        assertEquals("link", methodDesc.getMethodName());
        methodDesc = amd.getMethodDesc("submit");
        assertEquals("submit", methodDesc.getMethodName());
        ActionMethodDesc amd2 = pageDesc.getActionMethodDesc();
        assertNotNull(amd2);
        assertTrue(amd2.getMethodDescSize() == 3);
        MethodDesc methodDesc2 = amd2.getMethodDesc("submit");
        assertEquals("submit", methodDesc2.getMethodName());
    }

    public void testMatch_el() throws Exception {
        PageDescImpl pageDesc = new PageDescImpl("hoge", "test3Page", BeanDescFactory.getBeanDesc(Test3Page.class), new UrlTemplateImpl("/test/{hoge}"), set);
        Map<String, String> parseUrl = pageDesc.getUrlTemplate().parseUrl("/test/0!_-");
        assertEquals("0!_-", parseUrl.get("{hoge}"));
    }

    public void test_toString() throws Exception {
        PageDescImpl pageDesc = new PageDescImpl("hoge", "test4Page", BeanDescFactory.getBeanDesc(Test4Page.class), new UrlTemplateImpl("*"), set);
        System.out.println(pageDesc);
    }

    public void test_overload() throws Exception {
        BeanDesc<Test5Page> beanDesc = BeanDescFactory.getBeanDesc(Test5Page.class);
        UrlTemplateImpl template = new UrlTemplateImpl("*");
        PageDescImpl pageDesc = new PageDescImpl("hoge", "test4Page", beanDesc, template, set);
        ActionMethodDesc amd = pageDesc.getActionMethodDesc();
        MethodDesc md = amd.getMethodDesc("hoge");
        System.out.println("md:" + md);
        System.out.println(amd.getMethodDescSize());
    }

    @Page("/test2")
    public static class Test2Page {

        @Default
        public Navigation getAaa(WebContext context) {
            return Forward.to("aaa.jsp");
        }

        @GET
        @POST
        @ActionParam("button1")
        public Navigation submit(WebContext context) {
            return null;
        }

        @GET
        @ActionParam("link2")
        public Navigation link(WebContext context) {
            return null;
        }
    }

    @Page("/test/{hoge}")
    public static class Test3Page {
    }

    @Page
    public static class Test4Page {
    }

    @Page
    public static class Test5Page {

        @Default
        public Navigation hoge(WebContext context) {
            return Forward.to("aaa.jsp");
        }

        @ActionParam("hogehoge")
        public Navigation hoge(WebContext context, Request request) {
            return null;
        }
    }
}
