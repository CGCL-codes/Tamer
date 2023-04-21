package org.tmjee.webworkanotation;

import com.opensymphony.xwork.ActionSupport;
import org.tmjee.webworkanotation.annotation.*;
import org.tmjee.webworkanotation.annotation.Package;

/**
 * @author tmjee
 * @date $Date$ $Id$
 */
@org.tmjee.webworkanotation.annotation.Package(value = "defaultPackage", namespace = "/defaultNamespace", extend = "default")
public class AnAction extends ActionSupport {

    @Package(value = "anotherPackage", namespace = "/anotherNamespace", extend = "default")
    @Action(value = "myAction", interceptorRefs = { @InterceptorRef(value = "mockInterceptor1"), @InterceptorRef(value = "mockInterceptorStack1", params = { @Param(name = "customInterceptor2.param1", value = "value1"), @Param(name = "customInterceptor2.param2", value = "value2"), @Param(name = "customInterceptor3.param1", value = "value3") }) }, results = { @Result(name = "input", type = "freemarker", value = "/error.ftl"), @Result(name = "success", type = "mockResult1", params = { @Param(name = "param1", value = "value1"), @Param(name = "param2", value = "value2") }) }, exceptionMappings = { @ExceptionMapping(name = "catchNPE", result = "input", exception = java.lang.NullPointerException.class), @ExceptionMapping(name = "catchIAE", result = "input", exception = java.lang.IllegalArgumentException.class) })
    public String doAction1() {
        return SUCCESS;
    }

    @Action(value = "yetAotherAction", interceptorRefs = { @InterceptorRef(value = "mockInterceptor1"), @InterceptorRef(value = "mockInterceptorStack1", params = { @Param(name = "customInterceptor2.param1", value = "value1"), @Param(name = "customInterceptor2.param2", value = "value2"), @Param(name = "customInterceptor3.param1", value = "value3") }) }, results = { @Result(name = "input", type = "freemarker", value = "/error.ftl"), @Result(name = "success", type = "mockResult1", params = { @Param(name = "param1", value = "value1"), @Param(name = "param2", value = "value2") }) }, exceptionMappings = { @ExceptionMapping(name = "catchNPE", result = "input", exception = java.lang.NullPointerException.class), @ExceptionMapping(name = "catchIAE", result = "input", exception = java.lang.IllegalArgumentException.class) })
    public String doAction2() {
        return SUCCESS;
    }
}
