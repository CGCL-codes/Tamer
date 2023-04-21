package org.t2framework.t2.spi.impl;

import java.lang.annotation.Annotation;
import java.util.Map;
import junit.framework.TestCase;
import org.t2framework.t2.action.method.ActionMethodResolver;
import org.t2framework.t2.action.method.impl.ActionParamResolver;
import org.t2framework.t2.action.method.impl.ActionPathResolver;
import org.t2framework.t2.action.method.impl.AjaxResolver;
import org.t2framework.t2.action.method.impl.HttpMethodActionMethodResolver;
import org.t2framework.t2.annotation.composite.DELETE;
import org.t2framework.t2.annotation.composite.GET;
import org.t2framework.t2.annotation.composite.HEAD;
import org.t2framework.t2.annotation.composite.OPTIONS;
import org.t2framework.t2.annotation.composite.POST;
import org.t2framework.t2.annotation.composite.PUT;
import org.t2framework.t2.annotation.composite.TRACE;
import org.t2framework.t2.annotation.core.ActionPath;
import org.t2framework.t2.annotation.core.ActionParam;
import org.t2framework.t2.annotation.core.Ajax;
import org.t2framework.t2.annotation.core.Amf;
import org.t2framework.t2.contexts.HttpMethod;
import org.t2framework.t2.format.amf.action.method.impl.AmfResolver;

public class DefaultAnnotationResolverCreatorImplTest extends TestCase {

    public void test1() throws Exception {
        DefaultAnnotationResolverCreatorImpl resolver = new DefaultAnnotationResolverCreatorImpl();
        Map<Class<? extends Annotation>, ActionMethodResolver> resolvers = resolver.createActionMethodResolvers(null);
        assertNotNull(resolvers);
        assertTrue(resolvers.get(GET.class).equals(new HttpMethodActionMethodResolver(HttpMethod.GET)));
        assertTrue(resolvers.get(POST.class).equals(new HttpMethodActionMethodResolver(HttpMethod.POST)));
        assertTrue(resolvers.get(PUT.class).equals(new HttpMethodActionMethodResolver(HttpMethod.PUT)));
        assertTrue(resolvers.get(DELETE.class).equals(new HttpMethodActionMethodResolver(HttpMethod.DELETE)));
        assertTrue(resolvers.get(HEAD.class).equals(new HttpMethodActionMethodResolver(HttpMethod.HEAD)));
        assertTrue(resolvers.get(OPTIONS.class).equals(new HttpMethodActionMethodResolver(HttpMethod.OPTIONS)));
        assertTrue(resolvers.get(TRACE.class).equals(new HttpMethodActionMethodResolver(HttpMethod.TRACE)));
        assertTrue(resolvers.get(ActionPath.class).getClass() == ActionPathResolver.class);
        assertTrue(resolvers.get(ActionParam.class).getClass() == ActionParamResolver.class);
        assertTrue(resolvers.get(Ajax.class).getClass() == AjaxResolver.class);
        assertTrue(resolvers.get(Amf.class).getClass() == AmfResolver.class);
    }
}
