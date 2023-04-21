package org.t2framework.t2.action.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import junit.framework.TestCase;
import org.t2framework.commons.Constants;
import org.t2framework.commons.bootstrap.CommonsInitializer;
import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.commons.util.Reflections.MethodUtil;
import org.t2framework.t2.action.ActionContext;
import org.t2framework.t2.action.ActionInvokingContext;
import org.t2framework.t2.adapter.SimpleContainerAdapter;
import org.t2framework.t2.annotation.composite.GET;
import org.t2framework.t2.annotation.core.ActionParam;
import org.t2framework.t2.annotation.core.ActionPath;
import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.contexts.PageDesc;
import org.t2framework.t2.exception.NoDefaultActionMethodFoundRuntimeException;
import org.t2framework.t2.mock.MockPageDescImpl;
import org.t2framework.t2.mock.MockWebContext;
import org.t2framework.t2.spi.AnnotationResolverCreator;
import org.t2framework.t2.spi.Navigation;

public class ActionContextBuilderImplTest extends TestCase {

    private AnnotationResolverCreator creator = CommonsInitializer.get(AnnotationResolverCreator.class);

    private SimpleContainerAdapter containerAdapter = new SimpleContainerAdapter();

    public void testBuild1_defaultOnly() throws Exception {
        MockWebContext mock = MockWebContext.createMock("/hoge");
        ActionInvokingContext invokingContext = new ActionInvokingContextImpl(mock, creator, containerAdapter, null);
        PageDesc pageDesc = new MockPageDescImpl(Hoge.class, "/moge");
        ActionContext actionContext = new ActionContextImpl(mock, pageDesc);
        mock.setActionContext(actionContext);
        actionContext = invokingContext.buildActionContext();
        Method m = MethodUtil.getDeclaredMethod(Hoge.class, "default_", Constants.EMPTY_CLASS_ARRAY);
        MethodDesc md = actionContext.getTargetMethodDesc();
        assertEquals(m, md.getMethod());
    }

    public void testBuild2_defaultAndActionPath() throws Exception {
        ActionContextBuilderImpl builder = new ActionContextBuilderImpl(creator.createActionMethodResolvers(containerAdapter), creator.createDefaultActionMethodResolver(containerAdapter));
        MockWebContext mock = MockWebContext.createMock("/hoge", "foo/list");
        mock.getMockHttpServletRequest().setMethod("GET");
        PageDesc pageDesc = new MockPageDescImpl(Foo.class, "foo");
        ActionContext actionContext = new ActionContextImpl(mock);
        actionContext.setPageDescCandidates(Arrays.asList(pageDesc));
        mock.setActionContext(actionContext);
        ActionInvokingContext invokingContext = new ActionInvokingContextImpl(mock, creator, containerAdapter, null);
        builder.build(invokingContext);
        Method m = MethodUtil.getDeclaredMethod(Foo.class, "list", Constants.EMPTY_CLASS_ARRAY);
        MethodDesc md = actionContext.getTargetMethodDesc();
        assertEquals(m, md.getMethod());
    }

    public void testBuild3_otherAnnotationShouldBeIgnored() throws Exception {
        ActionContextBuilderImpl builder = new ActionContextBuilderImpl(creator.createActionMethodResolvers(containerAdapter), creator.createDefaultActionMethodResolver(containerAdapter));
        MockWebContext mock = MockWebContext.createMock("/hoge", "bar/exec");
        mock.getMockHttpServletRequest().setMethod("GET");
        mock.getMockHttpServletRequest().setParameter("exec", "hogehoge");
        PageDesc pageDesc = new MockPageDescImpl(Bar.class, "bar");
        mock.setActionContext(new ActionContextImpl(mock, pageDesc));
        ActionInvokingContext invokingContext = new ActionInvokingContextImpl(mock, creator, containerAdapter, null);
        ActionContext actionContext = builder.build(invokingContext);
        Method m = MethodUtil.getMethod(Bar.class, "exec", Constants.EMPTY_CLASS_ARRAY);
        MethodDesc md = actionContext.getTargetMethodDesc();
        assertEquals(m, md.getMethod());
    }

    public void testBuild4_noActionMethod() throws Exception {
        ActionContextBuilderImpl builder = new ActionContextBuilderImpl(creator.createActionMethodResolvers(containerAdapter), creator.createDefaultActionMethodResolver(containerAdapter));
        MockWebContext mock = MockWebContext.createMock("/hoge", "baz/no_such_actionmethod");
        mock.getMockHttpServletRequest().setMethod("GET");
        PageDesc pageDesc = new MockPageDescImpl(Baz.class, "baz");
        mock.setActionContext(new ActionContextImpl(mock, pageDesc));
        ActionInvokingContext invokingContext = new ActionInvokingContextImpl(mock, creator, containerAdapter, null);
        try {
            @SuppressWarnings("unused") ActionContext actionContext = builder.build(invokingContext);
            fail();
        } catch (NoDefaultActionMethodFoundRuntimeException expected) {
        }
    }

    @Page
    public static class Hoge {

        @Default
        public Navigation default_() {
            return null;
        }
    }

    @Page("foo")
    public static class Foo {

        @Default
        public Navigation default_() {
            return null;
        }

        @GET
        @ActionPath
        public Navigation list() {
            return null;
        }
    }

    @Page("bar")
    public static class Bar {

        @Aaa
        @GET
        @Bbb
        @ActionParam
        @ActionPath
        public Navigation exec() {
            return null;
        }
    }

    @Page("baz")
    public static class Baz {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Aaa {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Bbb {
    }
}
