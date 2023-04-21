package org.t2framework.t2.contexts.impl;

import java.lang.annotation.Annotation;
import java.util.Set;
import junit.framework.TestCase;
import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.commons.meta.BeanDescFactory;
import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.commons.util.CollectionsUtil;
import org.t2framework.t2.annotation.composite.DELETE;
import org.t2framework.t2.annotation.composite.GET;
import org.t2framework.t2.annotation.composite.POST;
import org.t2framework.t2.annotation.composite.PUT;
import org.t2framework.t2.annotation.core.ActionParam;
import org.t2framework.t2.annotation.core.ActionPath;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.contexts.ActionMethodDesc;
import org.t2framework.t2.contexts.WebContext;
import org.t2framework.t2.contexts.internal.ActionMethodUtil;
import org.t2framework.t2.spi.Navigation;

public class ActionMethodUtilTest extends TestCase {

    public static Set<Class<? extends Annotation>> set = CollectionsUtil.newHashSet();

    static {
        set.add(ActionPath.class);
        set.add(ActionParam.class);
        set.add(GET.class);
        set.add(POST.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateActionDesc_PropertyDesc() throws Exception {
        BeanDesc<BbbPage> beanDesc = BeanDescFactory.getBeanDesc(BbbPage.class);
        ActionMethodDesc amd = ActionMethodUtil.createActionMethodDesc(beanDesc, set);
        assertNotNull(amd);
        assertNotNull(amd.getMethodDesc("aaa"));
        assertNotNull(amd.getMethodDesc("getHoge"));
    }

    public void testResolve_simpleGET() throws Exception {
        BeanDesc<AaaPage> beanDesc = BeanDescFactory.getBeanDesc(AaaPage.class);
        ActionMethodDesc resolve = ActionMethodUtil.createActionMethodDesc(beanDesc, set);
        assertNotNull(resolve);
        MethodDesc methodDesc = resolve.getMethodDesc("execute1");
        assertNotNull(methodDesc);
        assertEquals("execute1", methodDesc.getMethodName());
    }

    public void testResolve_simple_valueExist() throws Exception {
        BeanDesc<AaaPage> beanDesc = BeanDescFactory.getBeanDesc(AaaPage.class);
        ActionMethodDesc resolve = ActionMethodUtil.createActionMethodDesc(beanDesc, set);
        assertNotNull(resolve);
        MethodDesc methodDesc = resolve.getMethodDesc("execute2");
        assertNotNull(methodDesc);
        assertEquals("execute2", methodDesc.getMethodName());
    }

    public void testResolve_noHttpMethodAnnotation() throws Exception {
        BeanDesc<AaaPage> beanDesc = BeanDescFactory.getBeanDesc(AaaPage.class);
        ActionMethodDesc resolve = ActionMethodUtil.createActionMethodDesc(beanDesc, set);
        assertNotNull(resolve);
        MethodDesc methodDesc = resolve.getMethodDesc("execute5");
        assertNotNull(methodDesc);
        assertEquals("execute5", methodDesc.getMethodName());
        resolve = ActionMethodUtil.createActionMethodDesc(beanDesc, set);
        assertNotNull(resolve);
        methodDesc = resolve.getMethodDesc("execute5");
        assertNotNull(methodDesc);
        assertEquals("execute5", methodDesc.getMethodName());
    }

    @Page("aaa")
    public static class AaaPage {

        @GET
        @ActionPath
        public Navigation execute1(WebContext context) {
            return null;
        }

        @POST
        @ActionPath("hogehoge")
        public Navigation execute2(WebContext context) {
            return null;
        }

        @DELETE
        @ActionPath
        public Navigation execute3(WebContext context) {
            return null;
        }

        @PUT
        @ActionPath
        public Navigation execute4(WebContext context) {
            return null;
        }

        @ActionPath
        public Navigation execute5(WebContext context) {
            return null;
        }
    }

    public static class BbbPage {

        @GET
        @ActionPath
        public Navigation aaa(WebContext context) {
            return null;
        }

        @GET
        @ActionPath
        public Navigation getHoge() {
            return null;
        }
    }
}
