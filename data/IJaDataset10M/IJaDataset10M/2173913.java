package org.tmjee.webworkanotation;

import org.tmjee.webworkanotation.annotation.Action;
import org.tmjee.webworkanotation.annotation.ExceptionMapping;
import org.tmjee.webworkanotation.annotation.InterceptorRef;
import org.tmjee.webworkanotation.annotation.Package;
import org.tmjee.webworkanotation.annotation.Param;
import org.tmjee.webworkanotation.annotation.Result;
import com.opensymphony.xwork.ActionSupport;

/**
 * @author tmjee
 * @version $Date$ $Id$
 */
@Package(value = "package", namespace = "/namespace", extend = "actionAnnotationTest-default")
public class Action1 extends ActionSupport {

    private static final long serialVersionUID = 1275014080413828702L;

    @Package(value = "anotherPackage", namespace = "/anotherNamespace", extend = "actionAnnotationTest-default")
    @Action(value = "myAction", interceptorRefs = { @InterceptorRef(value = "mockInterceptor1"), @InterceptorRef(value = "mockInterceptorStack", params = { @Param(name = "mockInterceptor2.param1", value = "value1"), @Param(name = "mockInterceptor2.param2", value = "value2"), @Param(name = "mockInterceptor3.param1", value = "value1"), @Param(name = "mockInterceptor3.param2", value = "value2") }) }, results = { @Result(name = "input", type = "freemarker", value = "/error2.ftl"), @Result(name = "success", type = "mockResult1", params = { @Param(name = "param1", value = "somevalue1"), @Param(name = "param2", value = "somevalue2") }) }, exceptionMappings = { @ExceptionMapping(name = "catchNPE", result = "input", exception = java.lang.NullPointerException.class), @ExceptionMapping(name = "catchIAE", result = "input", exception = java.lang.IllegalArgumentException.class) })
    public String anotherExecute() throws Exception {
        return SUCCESS;
    }

    @Action(value = "yetAotherAction", interceptorRefs = { @InterceptorRef(value = "mockInterceptor1", params = { @Param(name = "param1", value = "value1"), @Param(name = "param3", value = "value3") }), @InterceptorRef(value = "mockInterceptorStack", params = { @Param(name = "mockInterceptor3.param1", value = "value1"), @Param(name = "mockInterceptor3.param2", value = "value2") }) }, results = { @Result(name = "input", type = "freemarker", value = "/error.ftl"), @Result(name = "success", type = "mockResult2", params = { @Param(name = "param1", value = "value1"), @Param(name = "param2", value = "value2") }) }, exceptionMappings = { @ExceptionMapping(name = "catchNPE", result = "input", exception = java.lang.NullPointerException.class), @ExceptionMapping(name = "catchIAE", result = "input", exception = java.lang.IllegalArgumentException.class) })
    public String executeIt() throws Exception {
        return SUCCESS;
    }
}
