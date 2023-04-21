package org.t2framework.confeito.parameter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.t2framework.confeito.action.ActionContext;
import org.t2framework.confeito.annotation.Var;
import org.t2framework.confeito.contexts.Request;
import org.t2framework.confeito.exception.VarParameterNotFoundRuntimeException;
import org.t2framework.confeito.parameter.AbstractParameterResolver;
import org.t2framework.confeito.util.ConverterUtil;
import org.t2framework.confeito.util.Logger;
import org.t2framework.confeito.util.URLUtil;

/**
 * <#if locale="en">
 * <p>
 * VarParameterResolver is a resolver class to inject a part of url which is
 * described by annotation value. For example, consider the page class like
 * below:
 * 
 * <pre>
 * 	&#064;Page(&quot;{s}/vartest&quot;)
 * 	public static class VarPage {
 * 		public Navigation aaa(@Var(&quot;s&quot;) String s) {
 * 			......
 * </pre>
 * 
 * The point is that {s} annotated with &#064;Page takes any value like
 * aaa/vartest or bbb/vartest.VarParameterResolver is enbale to get that value,
 * aaa or bbb.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 */
public class VarParameterResolver extends AbstractParameterResolver {

    protected static Logger logger = Logger.getLogger(VarParameterResolver.class);

    public VarParameterResolver() {
        setTargetAnnotationClass(Var.class);
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * Find and resolve templated value(with {@literal aaa -> aaa} ) which is
	 * specified by {@link Var#value()}, then return.The returned value is
	 * decoded by {@link URLUtil#decode(String, String)} with character encoding
	 * specified by web.xml.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return string : templated value.
	 * @throws VarParameterNotFoundRuntimeException
	 *             value is not found and {@link Var#nullable()} is false
	 */
    @Override
    public Object resolve(final ActionContext actionContext, final Method md, final int paramIndex, final Annotation[] paramAnnotations, final Class<?> paramClass) {
        final Var var = findTargetAnnotation(paramAnnotations, getTargetAnnotationClass());
        if (var == null) {
            throw new IllegalStateException("must have @Var annotation.");
        }
        final String templateKey = var.value();
        final boolean nullable = var.nullable();
        final String value = actionContext.getVariables(templateKey);
        if (value != null) {
            final Request request = actionContext.getRequest();
            final String encoding = request.getCharacterEncoding();
            String ret = URLUtil.decode(value, encoding);
            return ConverterUtil.convert(ret, paramClass);
        } else if (nullable) {
            return null;
        } else {
            throw new VarParameterNotFoundRuntimeException(templateKey);
        }
    }
}
