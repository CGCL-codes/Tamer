package org.springframework.web.servlet.mvc.annotation;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter;
import com.googlecode.restfulspringmvc.web.annotation.RestfulAnnotationMethodHandlerAdapter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.agitar.mock.servlet.MockHttpServletRequest;
import org.agitar.mock.servlet.MockHttpServletResponse;
import org.agitar.mock.servlet.MockServletContext;
import org.apache.commons.logging.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ModelMap;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class AnnotationMethodHandlerAdapterAgitarTest extends AgitarTestCase {

    public Class getTargetClass() {
        return AnnotationMethodHandlerAdapter.class;
    }

    public void testArgumentsResolverConstructor() throws Throwable {
        Object obj = callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "<init>", new Class[] { AnnotationMethodHandlerAdapter.class, Set.class }, null, new Object[] { new AnnotationMethodHandlerAdapter(), new HashSet(100, 100.0F) });
        boolean actual = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "isProcessingComplete", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertFalse("(Object) obj.isProcessingComplete()", actual);
    }

    public void testConstructor() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        assertTrue("annotationMethodHandlerAdapter.isUseExpiresHeader()", annotationMethodHandlerAdapter.isUseExpiresHeader());
        assertEquals("annotationMethodHandlerAdapter.getCacheSeconds()", -1, annotationMethodHandlerAdapter.getCacheSeconds());
        assertFalse("annotationMethodHandlerAdapter.isRequireSession()", annotationMethodHandlerAdapter.isRequireSession());
        assertNotNull("annotationMethodHandlerAdapter.methodNameResolver", getPrivateField(annotationMethodHandlerAdapter, "methodNameResolver"));
        assertTrue("annotationMethodHandlerAdapter.isUseCacheControlHeader()", annotationMethodHandlerAdapter.isUseCacheControlHeader());
        int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
        assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 0, actual);
        int actual2 = ((Map) getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeNames")).size();
        assertEquals("annotationMethodHandlerAdapter.sessionAttributeNames.size()", 0, actual2);
        boolean actual3 = ((Log) getPrivateField(annotationMethodHandlerAdapter, "logger")).isDebugEnabled();
        assertFalse("annotationMethodHandlerAdapter.logger.isDebugEnabled()", actual3);
        assertEquals("annotationMethodHandlerAdapter.getSupportedMethods().length", 3, annotationMethodHandlerAdapter.getSupportedMethods().length);
        assertNotNull("annotationMethodHandlerAdapter.sessionAttributeStore", getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeStore"));
        assertNotNull("annotationMethodHandlerAdapter.pathMatcher", getPrivateField(annotationMethodHandlerAdapter, "pathMatcher"));
        UrlPathHelper privateField = (UrlPathHelper) getPrivateField(annotationMethodHandlerAdapter, "urlPathHelper");
        Object actual4 = callPrivateMethod("org.springframework.web.util.UrlPathHelper", "getDefaultEncoding", new Class[] {}, privateField, new Object[] {});
        assertEquals("annotationMethodHandlerAdapter.urlPathHelper.getDefaultEncoding()", "ISO-8859-1", actual4);
    }

    public void testHandlerMethodResolverConstructor() throws Throwable {
        Object obj = callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "<init>", new Class[] { AnnotationMethodHandlerAdapter.class, Class.class }, null, new Object[] { new AnnotationMethodHandlerAdapter(), Object.class });
        boolean actual = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "hasHandlerMethods", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertFalse("(Object) obj.hasHandlerMethods()", actual);
    }

    public void testRequestMappingInfoConstructor() throws Throwable {
        Object obj = callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "<init>", new Class[] {}, null, new Object[] {});
        boolean actual = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "isEmpty", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertTrue("(Object) obj.isEmpty()", actual);
    }

    public void testArgumentsResolverGetModelAndViewWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        setPrivateField(obj, "responseArgumentUsed", Boolean.TRUE);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        ModelAndView result = (ModelAndView) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "getModelAndView", new Class[] { Method.class, Object.class, ModelMap.class }, obj, new Object[] { null, null, null });
        assertNull("result", result);
    }

    public void testArgumentsResolverGetModelAndViewWithAggressiveMocks1() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        ModelAndView modelAndView = new ModelAndView();
        ModelMap modelMap = (ModelMap) Mockingbird.getProxyObject(ModelMap.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(modelAndView.getModelMap(), modelMap);
        Mockingbird.setReturnValue(modelMap.mergeAttributes(null), null);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        ModelAndView result = (ModelAndView) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "getModelAndView", new Class[] { Method.class, Object.class, ModelMap.class }, obj, new Object[] { null, modelAndView, null });
        assertNotNull("result", result);
    }

    public void testArgumentsResolverGetModelAndViewWithAggressiveMocks2() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        Map map = (Map) Mockingbird.getProxyObject(Map.class);
        Mockingbird.enterRecordingMode();
        ModelAndView modelAndView = new ModelAndView();
        Mockingbird.replaceObjectForRecording(ModelAndView.class, "<init>()", modelAndView);
        ModelAndView modelAndView2 = new ModelAndView();
        Mockingbird.setReturnValue(modelAndView.addAllObjects(null), modelAndView2);
        Mockingbird.setReturnValue(modelAndView2.addAllObjects(map), null);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        ModelAndView result = (ModelAndView) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "getModelAndView", new Class[] { Method.class, Object.class, ModelMap.class }, obj, new Object[] { null, map, null });
        assertNull("result", result);
    }

    public void testArgumentsResolverGetModelAndViewWithAggressiveMocks3() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        Mockingbird.enterRecordingMode();
        ModelAndView modelAndView = new ModelAndView();
        Mockingbird.replaceObjectForRecording(ModelAndView.class, "<init>(java.lang.String)", modelAndView);
        Mockingbird.setReturnValue(modelAndView.addAllObjects(null), null);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        ModelAndView result = (ModelAndView) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "getModelAndView", new Class[] { Method.class, Object.class, ModelMap.class }, obj, new Object[] { null, "", null });
        assertNull("result", result);
    }

    public void testArgumentsResolverGetModelAndViewWithAggressiveMocks4() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        Mockingbird.enterRecordingMode();
        ModelAndView modelAndView = new ModelAndView();
        Mockingbird.replaceObjectForRecording(ModelAndView.class, "<init>()", modelAndView);
        setPrivateField(obj, "responseArgumentUsed", Boolean.FALSE);
        Mockingbird.setReturnValue(modelAndView.addAllObjects(null), null);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        ModelAndView result = (ModelAndView) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "getModelAndView", new Class[] { Method.class, Object.class, ModelMap.class }, obj, new Object[] { null, null, null });
        assertNull("result", result);
    }

    public void testArgumentsResolverIsProcessingCompleteWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        SimpleSessionStatus simpleSessionStatus = (SimpleSessionStatus) Mockingbird.getProxyObject(SimpleSessionStatus.class);
        setPrivateField(obj, "sessionStatus", simpleSessionStatus);
        setPrivateField(simpleSessionStatus, "complete", Boolean.FALSE);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "isProcessingComplete", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertFalse("result", result);
    }

    public void testArgumentsResolverResolveArgumentsWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = (AnnotationMethodHandlerAdapter) Mockingbird.getProxyObject(AnnotationMethodHandlerAdapter.class);
        Object obj2 = Mockingbird.getProxyObject(Object.class);
        Method method = (Method) Mockingbird.getProxyObject(Method.class);
        Mockingbird.enterRecordingMode();
        SimpleTypeConverter simpleTypeConverter = (SimpleTypeConverter) Mockingbird.getProxyObject(SimpleTypeConverter.class);
        Mockingbird.replaceObjectForRecording(SimpleTypeConverter.class, "<init>()", simpleTypeConverter);
        Class class2 = (Class) Mockingbird.getProxyObject(Class.class);
        Class class3 = (Class) Mockingbird.getProxyObject(Class.class);
        Annotation[] annotations = new Annotation[1];
        Annotation annotation = (Annotation) Mockingbird.getProxyObject(Annotation.class);
        MultipartFile multipartFile = (MultipartFile) Mockingbird.getProxyObject(MultipartFile.class);
        Object obj3 = Mockingbird.getProxyObject(Object.class);
        Class class4 = (Class) Mockingbird.getProxyObject(Class.class);
        Class class5 = (Class) Mockingbird.getProxyObject(Class.class);
        Class class6 = (Class) Mockingbird.getProxyObject(Class.class);
        Annotation[] annotations2 = new Annotation[2];
        Annotation annotation2 = (Annotation) Mockingbird.getProxyObject(Annotation.class);
        Object obj4 = Mockingbird.getProxyObject(Object.class);
        WebBindingInitializer webBindingInitializer = (WebBindingInitializer) Mockingbird.getProxyObject(WebBindingInitializer.class);
        Iterator iterator = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        Method method2 = (Method) Mockingbird.getProxyObject(Method.class);
        String[] strings = new String[0];
        Method method3 = (Method) Mockingbird.getProxyObject(Method.class);
        String[] strings2 = new String[0];
        Method method4 = (Method) Mockingbird.getProxyObject(Method.class);
        String[] strings3 = new String[1];
        List list = (List) Mockingbird.getProxyObject(List.class);
        Method method5 = (Method) Mockingbird.getProxyObject(Method.class);
        String[] strings4 = new String[1];
        List list2 = (List) Mockingbird.getProxyObject(List.class);
        BindingResult bindingResult = (BindingResult) Mockingbird.getProxyObject(BindingResult.class);
        setPrivateField(obj, "initBinderMethods", set);
        setPrivateField(obj, "this$0", annotationMethodHandlerAdapter);
        setPrivateField(obj, "sessionStatus", null);
        MethodParameter methodParameter = (MethodParameter) Mockingbird.getProxyObject(MethodParameter.class);
        Mockingbird.replaceObjectForRecording(MethodParameter.class, "<init>(java.lang.reflect.Method,int)", methodParameter);
        annotations[0] = annotation;
        annotations2[0] = annotation2;
        annotations2[1] = (Annotation) Mockingbird.getProxyObject(Annotation.class);
        Mockingbird.enterNormalMode();
        annotationMethodHandlerAdapter.setWebBindingInitializer(webBindingInitializer);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(methodParameter.getParameterType(), class2);
        Mockingbird.setReturnValue(methodParameter.getParameterType(), class3);
        Mockingbird.setReturnValue(methodParameter.getParameterType(), null);
        Mockingbird.setReturnValue(ClassUtils.getShortNameAsProperty(null), "");
        Mockingbird.setReturnValue(methodParameter.getParameterAnnotations(), annotations);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(RequestParam.class), "value", "()java.lang.String", "X", 1);
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(RequestParam.class), "required", "()boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(MultipartHttpServletRequest.class), "getFile", "(java.lang.String)org.springframework.web.multipart.MultipartFile", multipartFile, 1);
        Mockingbird.setReturnValue(methodParameter.getParameterType(), null);
        Mockingbird.setReturnValue(simpleTypeConverter.convertIfNecessary(multipartFile, null), obj3);
        MethodParameter methodParameter2 = (MethodParameter) Mockingbird.getProxyObject(MethodParameter.class);
        Mockingbird.replaceObjectForRecording(MethodParameter.class, "<init>(java.lang.reflect.Method,int)", methodParameter2);
        Mockingbird.setReturnValue(methodParameter2.getParameterType(), class4);
        Mockingbird.setReturnValue(methodParameter2.getParameterType(), class5);
        Mockingbird.setReturnValue(methodParameter2.getParameterType(), class6);
        Mockingbird.setReturnValue(methodParameter2.getParameterType(), null);
        Mockingbird.setReturnValue(ClassUtils.getShortNameAsProperty(null), "");
        Mockingbird.setReturnValue(methodParameter2.getParameterAnnotations(), annotations2);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(ModelAttribute.class), "value", "()java.lang.String", "X", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(ModelAttribute.class), "value", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(ModelAttribute.class), "value", "()java.lang.String", "X", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(ModelAttribute.class), "value", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(methodParameter2.getParameterType(), null);
        Mockingbird.setReturnValue(BeanUtils.isSimpleProperty(null), false);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(LinkedHashMap.class), "get", "(java.lang.Object)java.lang.Object", null, 1);
        Mockingbird.setReturnValue(methodParameter2.getParameterType(), null);
        Mockingbird.setReturnValue(BeanUtils.instantiateClass(null), obj4);
        Mockingbird.replaceObjectForRecording(ServletRequestDataBinder.class, "<init>(java.lang.Object,java.lang.String)", Mockingbird.getProxyObject(ServletRequestDataBinder.class));
        Mockingbird.setReturnValue(false, webBindingInitializer, "initBinder", "(org.springframework.web.bind.WebDataBinder,org.springframework.web.context.request.WebRequest)void", null, 1);
        Mockingbird.setReturnValue(false, set, "iterator", "()java.util.Iterator", iterator, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), method2);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(InitBinder.class), "value", "()java.lang.String[]", strings, 1);
        Mockingbird.setReturnValue(true, ReflectionUtils.class, "makeAccessible", "(java.lang.reflect.Method)void", null, 1);
        Mockingbird.setReturnValue(true, ReflectionUtils.class, "invokeMethod", "(java.lang.reflect.Method,java.lang.Object,java.lang.Object[])java.lang.Object", null, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), method3);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(InitBinder.class), "value", "()java.lang.String[]", strings2, 1);
        Mockingbird.setReturnValue(true, ReflectionUtils.class, "makeAccessible", "(java.lang.reflect.Method)void", null, 1);
        Mockingbird.setReturnValue(true, ReflectionUtils.class, "invokeMethod", "(java.lang.reflect.Method,java.lang.Object,java.lang.Object[])java.lang.Object", null, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), method4);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(InitBinder.class), "value", "()java.lang.String[]", strings3, 1);
        Mockingbird.setReturnValue(true, Arrays.class, "asList", "(java.lang.Object[])java.util.List", new Object[] { strings3 }, list, 1);
        Mockingbird.setReturnValue(false, list, "contains", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), method5);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(InitBinder.class), "value", "()java.lang.String[]", strings4, 1);
        Mockingbird.setReturnValue(true, Arrays.class, "asList", "(java.lang.Object[])java.util.List", new Object[] { strings4 }, list2, 1);
        Mockingbird.setReturnValue(false, list2, "contains", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), false);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(ServletRequestDataBinder.class), "bind", "(javax.servlet.ServletRequest)void", null, 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(DataBinder.class), "getBindingResult", "()org.springframework.validation.BindingResult", bindingResult, 1);
        Mockingbird.setReturnValue(bindingResult.getModel(), null);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(HashMap.class), "putAll", "(java.util.Map)void", null, 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(ServletRequestDataBinder.class), "closeNoCatch", "()void", null, 1);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        Object[] result = (Object[]) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "resolveArguments", new Class[] { Object.class, Method.class, HttpServletRequest.class, HttpServletResponse.class, WebRequest.class, ModelMap.class, Set.class }, obj, new Object[] { obj2, method, null, null, null, null, null });
        assertEquals("result.length", 0, result.length);
    }

    public void testGetLastModified() throws Throwable {
        HttpServletRequest request = new MockServletContext().createHttpServletRequest("testAnnotationMethodHandlerAdapterParam1");
        long result = new AnnotationMethodHandlerAdapter().getLastModified(request, "testString");
        assertEquals("result", -1L, result);
    }

    public void testGetMethodResolver() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        Object result = callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter", "getMethodResolver", new Class[] { Object.class }, annotationMethodHandlerAdapter, new Object[] { new Integer(-100) });
        int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
        assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 1, actual);
        boolean actual2 = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "hasHandlerMethods", new Class[] {}, result, new Object[] {})).booleanValue();
        assertFalse("result.hasHandlerMethods()", actual2);
    }

    public void testGetMethodResolver1() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        annotationMethodHandlerAdapter.supports("");
        Object result = callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter", "getMethodResolver", new Class[] { Object.class }, annotationMethodHandlerAdapter, new Object[] { "testString" });
        boolean actual = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "hasHandlerMethods", new Class[] {}, result, new Object[] {})).booleanValue();
        assertFalse("result.hasHandlerMethods()", actual);
        int actual2 = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
        assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 1, actual2);
    }

    public void testHandlerMethodResolverCheckParametersWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"), true);
        HttpServletRequest httpServletRequest = (HttpServletRequest) Mockingbird.getProxyObject(HttpServletRequest.class);
        Object obj2 = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        RequestMethod[] requestMethods = new RequestMethod[2];
        RequestMethod requestMethod = (RequestMethod) Mockingbird.getProxyObject(RequestMethod.class);
        RequestMethod requestMethod2 = (RequestMethod) Mockingbird.getProxyObject(RequestMethod.class);
        setPrivateField(obj2, "methods", requestMethods);
        requestMethods[0] = requestMethod;
        requestMethods[1] = requestMethod2;
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, requestMethod, "toString", "()java.lang.String", "X", 1);
        Mockingbird.setReturnValue(httpServletRequest.getMethod(), "");
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(String.class), "toUpperCase", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, requestMethod2, "toString", "()java.lang.String", "X", 1);
        Mockingbird.setReturnValue(httpServletRequest.getMethod(), "");
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(String.class), "toUpperCase", "()java.lang.String", "", 1);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "checkParameters", new Class[] { HttpServletRequest.class, Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo") }, obj, new Object[] { httpServletRequest, obj2 })).booleanValue();
        assertFalse("result", result);
    }

    public void testHandlerMethodResolverCheckParametersWithAggressiveMocks1() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"), true);
        HttpServletRequest httpServletRequest = (HttpServletRequest) Mockingbird.getProxyObject(HttpServletRequest.class);
        Object obj2 = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        RequestMethod[] requestMethods = new RequestMethod[0];
        String[] strings = new String[3];
        setPrivateField(obj2, "methods", requestMethods);
        setPrivateField(obj2, "params", strings);
        strings[0] = "=";
        strings[1] = "";
        strings[2] = "";
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(String.class), "substring", "(int,int)java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(String.class), "substring", "(int)java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, httpServletRequest, "getParameter", "(java.lang.String)java.lang.String", "", 1);
        Mockingbird.setReturnValue(true, WebUtils.class, "hasSubmitParameter", "(javax.servlet.ServletRequest,java.lang.String)boolean", Boolean.TRUE, 1);
        Mockingbird.setReturnValue(true, WebUtils.class, "hasSubmitParameter", "(javax.servlet.ServletRequest,java.lang.String)boolean", Boolean.FALSE, 1);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "checkParameters", new Class[] { HttpServletRequest.class, Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo") }, obj, new Object[] { httpServletRequest, obj2 })).booleanValue();
        assertFalse("result", result);
    }

    public void testHandlerMethodResolverCheckParametersWithAggressiveMocks2() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"), true);
        HttpServletRequest httpServletRequest = (HttpServletRequest) Mockingbird.getProxyObject(HttpServletRequest.class);
        Object obj2 = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        RequestMethod[] requestMethods = new RequestMethod[0];
        String[] strings = new String[3];
        setPrivateField(obj2, "methods", requestMethods);
        setPrivateField(obj2, "params", strings);
        strings[0] = "";
        strings[1] = "";
        strings[2] = "=";
        Mockingbird.enterRecordingMode();
        Boolean boolean2 = Boolean.TRUE;
        Mockingbird.setReturnValue(true, WebUtils.class, "hasSubmitParameter", "(javax.servlet.ServletRequest,java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(true, WebUtils.class, "hasSubmitParameter", "(javax.servlet.ServletRequest,java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(String.class), "substring", "(int,int)java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(String.class), "substring", "(int)java.lang.String", "X", 1);
        Mockingbird.setReturnValue(false, httpServletRequest, "getParameter", "(java.lang.String)java.lang.String", "", 1);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "checkParameters", new Class[] { HttpServletRequest.class, Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo") }, obj, new Object[] { httpServletRequest, obj2 })).booleanValue();
        assertFalse("result", result);
    }

    public void testHandlerMethodResolverGetInitBinderMethodsWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"), true);
        setPrivateField(obj, "initBinderMethods", null);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"));
        Set result = (Set) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "getInitBinderMethods", new Class[] {}, obj, new Object[] {});
        assertNull("result", result);
    }

    public void testHandlerMethodResolverGetModelAttributeMethodsWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"), true);
        setPrivateField(obj, "modelAttributeMethods", null);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"));
        Set result = (Set) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "getModelAttributeMethods", new Class[] {}, obj, new Object[] {});
        assertNull("result", result);
    }

    public void testHandlerMethodResolverHasHandlerMethods() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        Object methodResolver = callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter", "getMethodResolver", new Class[] { Object.class }, annotationMethodHandlerAdapter, new Object[] { new Object() });
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "hasHandlerMethods", new Class[] {}, methodResolver, new Object[] {})).booleanValue();
        assertFalse("result", result);
    }

    public void testHandlerMethodResolverHasHandlerMethodsWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"), true);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        setPrivateField(obj, "handlerMethods", set);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(set.isEmpty(), false);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "hasHandlerMethods", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertTrue("result", result);
    }

    public void testRequestMappingInfoEqualsWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"), true);
        String[] strings = new String[0];
        Object obj2 = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"), true);
        String[] strings2 = new String[0];
        setPrivateField(obj, "paths", strings);
        setPrivateField(obj2, "paths", strings2);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, strings, "equals", "(java.lang.Object)boolean", new Object[] { strings2 }, Boolean.FALSE, 1);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "equals", new Class[] { Object.class }, obj, new Object[] { obj2 })).booleanValue();
        assertFalse("result", result);
    }

    public void testRequestMappingInfoHashCodeWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"), true);
        String[] strings = new String[0];
        RequestMethod[] requestMethods = new RequestMethod[0];
        String[] strings2 = new String[0];
        setPrivateField(obj, "paths", strings);
        setPrivateField(obj, "methods", requestMethods);
        setPrivateField(obj, "params", strings2);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(true, Arrays.class, "hashCode", "(java.lang.Object[])int", new Object[] { strings }, new Integer(0), 1);
        Mockingbird.setReturnValue(true, Arrays.class, "hashCode", "(java.lang.Object[])int", new Object[] { requestMethods }, new Integer(1), 1);
        Mockingbird.setReturnValue(true, Arrays.class, "hashCode", "(java.lang.Object[])int", new Object[] { strings2 }, new Integer(-5), 1);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        int result = ((Number) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "hashCode", new Class[] {}, obj, new Object[] {})).intValue();
        assertEquals("result", 26, result);
    }

    public void testRequestMappingInfoIsEmptyWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"), true);
        String[] strings = new String[0];
        RequestMethod[] requestMethods = new RequestMethod[0];
        String[] strings2 = new String[0];
        setPrivateField(obj, "paths", strings);
        setPrivateField(obj, "methods", requestMethods);
        setPrivateField(obj, "params", strings2);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "isEmpty", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertTrue("result", result);
    }

    public void testRequestMappingInfoIsEmptyWithAggressiveMocks1() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"), true);
        String[] strings = new String[1];
        setPrivateField(obj, "paths", strings);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "isEmpty", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertFalse("result", result);
    }

    public void testRequestMappingInfoIsEmptyWithAggressiveMocks2() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"), true);
        String[] strings = new String[0];
        RequestMethod[] requestMethods = new RequestMethod[1];
        setPrivateField(obj, "paths", strings);
        setPrivateField(obj, "methods", requestMethods);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "isEmpty", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertFalse("result", result);
    }

    public void testRequestMappingInfoIsEmptyWithAggressiveMocks3() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"), true);
        String[] strings = new String[0];
        RequestMethod[] requestMethods = new RequestMethod[0];
        String[] strings2 = new String[1];
        setPrivateField(obj, "paths", strings);
        setPrivateField(obj, "methods", requestMethods);
        setPrivateField(obj, "params", strings2);
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo"));
        boolean result = ((Boolean) callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$RequestMappingInfo", "isEmpty", new Class[] {}, obj, new Object[] {})).booleanValue();
        assertFalse("result", result);
    }

    public void testSetAlwaysUseFullPath() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        annotationMethodHandlerAdapter.setAlwaysUseFullPath(true);
        boolean actual = ((Boolean) getPrivateField(getPrivateField(annotationMethodHandlerAdapter, "urlPathHelper"), "alwaysUseFullPath")).booleanValue();
        assertTrue("annotationMethodHandlerAdapter.urlPathHelper.alwaysUseFullPath", actual);
        UrlPathHelper privateField = (UrlPathHelper) getPrivateField(annotationMethodHandlerAdapter, "urlPathHelper");
        Object actual2 = callPrivateMethod("org.springframework.web.util.UrlPathHelper", "getDefaultEncoding", new Class[] {}, privateField, new Object[] {});
        assertEquals("annotationMethodHandlerAdapter.urlPathHelper.getDefaultEncoding()", "ISO-8859-1", actual2);
    }

    public void testSetMethodNameResolver() throws Throwable {
        new RestfulAnnotationMethodHandlerAdapter().setMethodNameResolver(new PropertiesMethodNameResolver());
        assertTrue("Test call resulted in expected outcome", true);
    }

    public void testSetPathMatcher() throws Throwable {
        new RestfulAnnotationMethodHandlerAdapter().setPathMatcher(new AntPathMatcher());
        assertTrue("Test call resulted in expected outcome", true);
    }

    public void testSetSessionAttributeStore() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();
        annotationMethodHandlerAdapter.setSessionAttributeStore(sessionAttributeStore);
        assertSame("annotationMethodHandlerAdapter.sessionAttributeStore", sessionAttributeStore, getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeStore"));
    }

    public void testSetUrlDecode() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        annotationMethodHandlerAdapter.setUrlDecode(true);
        UrlPathHelper privateField = (UrlPathHelper) getPrivateField(annotationMethodHandlerAdapter, "urlPathHelper");
        Object actual = callPrivateMethod("org.springframework.web.util.UrlPathHelper", "getDefaultEncoding", new Class[] {}, privateField, new Object[] {});
        assertEquals("annotationMethodHandlerAdapter.urlPathHelper.getDefaultEncoding()", "ISO-8859-1", actual);
    }

    public void testSetUrlPathHelper() throws Throwable {
        new RestfulAnnotationMethodHandlerAdapter().setUrlPathHelper(new UrlPathHelper());
        assertTrue("Test call resulted in expected outcome", true);
    }

    public void testSetWebBindingInitializer() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        WebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
        annotationMethodHandlerAdapter.setWebBindingInitializer(webBindingInitializer);
        assertSame("annotationMethodHandlerAdapter.webBindingInitializer", webBindingInitializer, getPrivateField(annotationMethodHandlerAdapter, "webBindingInitializer"));
    }

    public void testSupports() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        boolean result = annotationMethodHandlerAdapter.supports("testString");
        int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
        assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 1, actual);
        assertFalse("result", result);
    }

    public void testSupports1() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        boolean supports = annotationMethodHandlerAdapter.supports("testString");
        boolean result = annotationMethodHandlerAdapter.supports("testString");
        assertFalse("result", result);
        int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
        assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 1, actual);
    }

    public void testHandlerMethodResolverConstructorThrowsNullPointerException() throws Throwable {
        try {
            callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "<init>", new Class[] { AnnotationMethodHandlerAdapter.class, Class.class }, null, new Object[] { new AnnotationMethodHandlerAdapter(), null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ReflectionUtils.class, ex);
        }
    }

    public void testArgumentsResolverGetModelAndViewThrowsIllegalArgumentExceptionWithAggressiveMocks() throws Throwable {
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"), true);
        Object obj2 = Mockingbird.getProxyObject(Object.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(true, BeanUtils.class, "isSimpleProperty", "(java.lang.Class)boolean", Boolean.TRUE, 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.replaceObjectForRecording(IllegalArgumentException.class, "<init>(java.lang.String)", Mockingbird.getProxyObject(IllegalArgumentException.class));
        Mockingbird.enterTestMode(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver"));
        try {
            callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$ArgumentsResolver", "getModelAndView", new Class[] { Method.class, Object.class, ModelMap.class }, obj, new Object[] { null, obj2, null });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertTrue("Test call resulted in expected outcome", true);
        }
    }

    public void testGetMethodResolverThrowsNullPointerException() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        try {
            callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter", "getMethodResolver", new Class[] { Object.class }, annotationMethodHandlerAdapter, new Object[] { null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AnnotationMethodHandlerAdapter.class, ex);
            int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
            assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 0, actual);
        }
    }

    public void testHandleThrowsHttpRequestMethodNotSupportedException() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        String[] methods = new String[1];
        annotationMethodHandlerAdapter.setSupportedMethods(methods);
        MockServletContext mockServletContext = new MockServletContext();
        HttpServletResponse response = mockServletContext.createHttpServletResponse();
        HttpServletRequest request = mockServletContext.createHttpServletRequest("testAnnotationMethodHandlerAdapterParam1");
        try {
            annotationMethodHandlerAdapter.handle(request, response, "");
            fail("Expected HttpRequestMethodNotSupportedException to be thrown");
        } catch (HttpRequestMethodNotSupportedException ex) {
            assertEquals("(MockHttpServletResponse) response", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
            assertEquals("(MockHttpServletResponse) response", "UTF-8", ((MockHttpServletResponse) response).getCharacterEncoding());
            assertEquals("ex.getMessage()", "Request method 'GET' not supported", ex.getMessage());
            assertThrownBy(WebContentGenerator.class, ex);
            assertEquals("ex.getMethod()", "GET", ex.getMethod());
            assertEquals("annotationMethodHandlerAdapter.getSupportedMethods().length", 1, annotationMethodHandlerAdapter.getSupportedMethods().length);
            assertNull("annotationMethodHandlerAdapter.webBindingInitializer", getPrivateField(annotationMethodHandlerAdapter, "webBindingInitializer"));
            int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
            assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 0, actual);
            int actual2 = ((Map) getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeNames")).size();
            assertEquals("annotationMethodHandlerAdapter.sessionAttributeNames.size()", 0, actual2);
            assertNotNull("annotationMethodHandlerAdapter.sessionAttributeStore", getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeStore"));
            assertEquals("(MockHttpServletRequest) request.getServerName()", "localhost", ((MockHttpServletRequest) request).getServerName());
            assertEquals("(MockHttpServletResponse) response.getContentType()", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
        }
    }

    public void testHandleThrowsHttpSessionRequiredException() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        annotationMethodHandlerAdapter.setRequireSession(true);
        MockServletContext mockServletContext = new MockServletContext();
        HttpServletResponse response = mockServletContext.createHttpServletResponse();
        HttpServletRequest request = mockServletContext.createHttpServletRequest("testAnnotationMethodHandlerAdapterParam1");
        try {
            annotationMethodHandlerAdapter.handle(request, response, "");
            fail("Expected HttpSessionRequiredException to be thrown");
        } catch (HttpSessionRequiredException ex) {
            assertEquals("(MockHttpServletResponse) response", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
            assertEquals("(MockHttpServletResponse) response", "UTF-8", ((MockHttpServletResponse) response).getCharacterEncoding());
            assertEquals("ex.getMessage()", "Pre-existing session required but none found", ex.getMessage());
            assertThrownBy(WebContentGenerator.class, ex);
            assertEquals("annotationMethodHandlerAdapter.getSupportedMethods().length", 3, annotationMethodHandlerAdapter.getSupportedMethods().length);
            assertNull("annotationMethodHandlerAdapter.webBindingInitializer", getPrivateField(annotationMethodHandlerAdapter, "webBindingInitializer"));
            int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
            assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 0, actual);
            int actual2 = ((Map) getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeNames")).size();
            assertEquals("annotationMethodHandlerAdapter.sessionAttributeNames.size()", 0, actual2);
            assertNotNull("annotationMethodHandlerAdapter.sessionAttributeStore", getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeStore"));
            assertEquals("(MockHttpServletRequest) request.getServerName()", "localhost", ((MockHttpServletRequest) request).getServerName());
            assertEquals("(MockHttpServletResponse) response.getContentType()", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
        }
    }

    public void testHandleThrowsIllegalArgumentException() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        annotationMethodHandlerAdapter.setRequireSession(true);
        HttpServletRequest request = (HttpServletRequest) Mockingbird.getProxyObject(HttpServletRequest.class);
        HttpServletResponse response = new MockServletContext().createHttpServletResponse();
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(request.getMethod(), "GET");
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(HashSet.class), "contains", "(java.lang.Object)boolean", Boolean.TRUE, 1);
        Mockingbird.setReturnValue(request.getSession(true), Mockingbird.getProxyObject(HttpSession.class));
        Mockingbird.replaceObjectForRecording(ServletWebRequest.class, "<init>(javax.servlet.http.HttpServletRequest)", Mockingbird.getProxyObject(ServletWebRequest.class));
        Object obj = Mockingbird.getProxyObject(Class.forName("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver"));
        Object handlerMethod = callPrivateMethod("com.googlecode.restfulspringmvc.web.annotation.AnnotationMethodHandlerAdapter$HandlerMethodResolver", "resolveHandlerMethod", new Class[] { HttpServletRequest.class }, obj, new Object[] { request });
        Mockingbird.setException(true, handlerMethod, (Throwable) Mockingbird.getProxyObject(IllegalArgumentException.class));
        Mockingbird.enterTestMode(AnnotationMethodHandlerAdapter.class);
        try {
            annotationMethodHandlerAdapter.handle(request, response, new Integer(32));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
            assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 1, actual);
            assertEquals("(MockHttpServletResponse) response", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
            assertEquals("(MockHttpServletResponse) response", "UTF-8", ((MockHttpServletResponse) response).getCharacterEncoding());
            assertEquals("annotationMethodHandlerAdapter.getSupportedMethods().length", 3, annotationMethodHandlerAdapter.getSupportedMethods().length);
            assertNull("annotationMethodHandlerAdapter.webBindingInitializer", getPrivateField(annotationMethodHandlerAdapter, "webBindingInitializer"));
            int actual2 = ((Map) getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeNames")).size();
            assertEquals("annotationMethodHandlerAdapter.sessionAttributeNames.size()", 0, actual2);
            assertNotNull("annotationMethodHandlerAdapter.sessionAttributeStore", getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeStore"));
            assertEquals("(MockHttpServletResponse) response.getContentType()", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
        }
    }

    public void testHandleThrowsNullPointerException() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        MockServletContext mockServletContext = new MockServletContext();
        HttpServletResponse response = mockServletContext.createHttpServletResponse();
        HttpServletRequest request = mockServletContext.createHttpServletRequest("testAnnotationMethodHandlerAdapterParam1");
        try {
            annotationMethodHandlerAdapter.handle(request, response, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("(MockHttpServletResponse) response", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
            assertEquals("(MockHttpServletResponse) response", "UTF-8", ((MockHttpServletResponse) response).getCharacterEncoding());
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AnnotationMethodHandlerAdapter.class, ex);
            assertNull("annotationMethodHandlerAdapter.webBindingInitializer", getPrivateField(annotationMethodHandlerAdapter, "webBindingInitializer"));
            int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
            assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 0, actual);
            int actual2 = ((Map) getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeNames")).size();
            assertEquals("annotationMethodHandlerAdapter.sessionAttributeNames.size()", 0, actual2);
            assertEquals("annotationMethodHandlerAdapter.getSupportedMethods().length", 3, annotationMethodHandlerAdapter.getSupportedMethods().length);
            assertNotNull("annotationMethodHandlerAdapter.sessionAttributeStore", getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeStore"));
            assertEquals("(MockHttpServletRequest) request.getServerName()", "localhost", ((MockHttpServletRequest) request).getServerName());
            assertEquals("(MockHttpServletResponse) response.getContentType()", "text/html; charset=UTF-8", ((MockHttpServletResponse) response).getContentType());
        }
    }

    public void testSetPathMatcherThrowsIllegalArgumentException() throws Throwable {
        AnnotationMethodHandlerAdapter restfulAnnotationMethodHandlerAdapter = new RestfulAnnotationMethodHandlerAdapter();
        try {
            restfulAnnotationMethodHandlerAdapter.setPathMatcher(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "PathMatcher must not be null", ex.getMessage());
            assertThrownBy(Assert.class, ex);
            assertNotNull("(RestfulAnnotationMethodHandlerAdapter) restfulAnnotationMethodHandlerAdapter.pathMatcher", getPrivateField(restfulAnnotationMethodHandlerAdapter, "pathMatcher"));
        }
    }

    public void testSetSessionAttributeStoreThrowsIllegalArgumentException() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        try {
            annotationMethodHandlerAdapter.setSessionAttributeStore(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "SessionAttributeStore must not be null", ex.getMessage());
            assertThrownBy(Assert.class, ex);
            assertNotNull("annotationMethodHandlerAdapter.sessionAttributeStore", getPrivateField(annotationMethodHandlerAdapter, "sessionAttributeStore"));
        }
    }

    public void testSetUrlPathHelperThrowsIllegalArgumentException() throws Throwable {
        AnnotationMethodHandlerAdapter restfulAnnotationMethodHandlerAdapter = new RestfulAnnotationMethodHandlerAdapter();
        try {
            restfulAnnotationMethodHandlerAdapter.setUrlPathHelper(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "UrlPathHelper must not be null", ex.getMessage());
            assertThrownBy(Assert.class, ex);
            UrlPathHelper privateField = (UrlPathHelper) getPrivateField(restfulAnnotationMethodHandlerAdapter, "urlPathHelper");
            Object actual = callPrivateMethod("org.springframework.web.util.UrlPathHelper", "getDefaultEncoding", new Class[] {}, privateField, new Object[] {});
            assertEquals("(RestfulAnnotationMethodHandlerAdapter) restfulAnnotationMethodHandlerAdapter.urlPathHelper.getDefaultEncoding()", "ISO-8859-1", actual);
        }
    }

    public void testSupportsThrowsNullPointerException() throws Throwable {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        try {
            annotationMethodHandlerAdapter.supports(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AnnotationMethodHandlerAdapter.class, ex);
            int actual = ((Map) getPrivateField(annotationMethodHandlerAdapter, "methodResolverCache")).size();
            assertEquals("annotationMethodHandlerAdapter.methodResolverCache.size()", 0, actual);
        }
    }
}
