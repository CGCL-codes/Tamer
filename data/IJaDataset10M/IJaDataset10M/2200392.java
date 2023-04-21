package net.paoding.rose.web.paramresolver;

import java.lang.reflect.Method;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.DefValue;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.impl.validation.ParameterBindingResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;

/**
 * @author 王志亮 [qieqie.wang@gmail.com]
 */
public final class MethodParameterResolver {

    private static Log logger = LogFactory.getLog(MethodParameterResolver.class);

    private final Method method;

    private final String[] parameterNames;

    private final ParamResolver[] resolvers;

    private final ParamMetaData[] paramMetaDatas;

    public MethodParameterResolver(Class<?> controllerClazz, Method method, ParameterNameDiscovererImpl parameterNameDiscoverer, ResolverFactory resolverFactory) {
        this.method = method;
        Class<?>[] parameterTypes = method.getParameterTypes();
        parameterNames = parameterNameDiscoverer.getParameterNames(method);
        resolvers = new ParamResolver[parameterTypes.length];
        paramMetaDatas = new ParamMetaData[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            ParamMetaDataImpl paramMetaData = new ParamMetaDataImpl(controllerClazz, method, parameterTypes[i], parameterNames[i], i);
            paramMetaDatas[i] = paramMetaData;
            resolvers[i] = resolverFactory.supports(paramMetaData);
        }
    }

    public ParamMetaData[] getParamMetaDatas() {
        return paramMetaDatas;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public Method getMethod() {
        return method;
    }

    public Param getParamAnnotationAt(int index) {
        return this.paramMetaDatas[index].getAnnotation(Param.class);
    }

    public Object[] resolve(final Invocation inv, final ParameterBindingResult parameterBindingResult) throws Exception {
        Object[] parameters = new Object[paramMetaDatas.length];
        for (int i = 0; i < resolvers.length; i++) {
            if (resolvers[i] == null) {
                continue;
            }
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Resolves parameter " + paramMetaDatas[i].getParamType().getSimpleName() + " using " + resolvers[i].getClass().getName());
                }
                parameters[i] = resolvers[i].resolve(inv, paramMetaDatas[i]);
                if (parameters[i] instanceof InitializingBean) {
                    ((InitializingBean) parameters[i]).afterPropertiesSet();
                }
            } catch (TypeMismatchException e) {
                logger.debug("", e);
                if (paramMetaDatas[i].getParamType().isPrimitive()) {
                    DefValue defValudeAnnotation = paramMetaDatas[i].getAnnotation(DefValue.class);
                    if (defValudeAnnotation == null || DefValue.NATIVE_DEFAULT.equals(defValudeAnnotation.value())) {
                        if (paramMetaDatas[i].getParamType() == int.class) {
                            parameters[i] = Integer.valueOf(0);
                        } else if (paramMetaDatas[i].getParamType() == long.class) {
                            parameters[i] = Long.valueOf(0);
                        } else if (paramMetaDatas[i].getParamType() == boolean.class) {
                            parameters[i] = Boolean.FALSE;
                        } else if (paramMetaDatas[i].getParamType() == double.class) {
                            parameters[i] = Double.valueOf(0);
                        } else if (paramMetaDatas[i].getParamType() == float.class) {
                            parameters[i] = Float.valueOf(0);
                        } else {
                            TypeConverter typeConverter = SafedTypeConverterFactory.getCurrentConverter();
                            parameters[i] = typeConverter.convertIfNecessary("0", paramMetaDatas[i].getParamType());
                        }
                    } else {
                        TypeConverter typeConverter = SafedTypeConverterFactory.getCurrentConverter();
                        parameters[i] = typeConverter.convertIfNecessary(defValudeAnnotation.value(), paramMetaDatas[i].getParamType());
                    }
                }
                String paramName = parameterNames[i];
                if (paramName == null) {
                    for (String name : paramMetaDatas[i].getParamNames()) {
                        if ((paramName = name) != null) {
                            break;
                        }
                    }
                }
                Assert.isTrue(paramName != null);
                FieldError fieldError = new FieldError("method", paramName, inv.getParameter(paramName), true, new String[] { e.getErrorCode() }, new String[] { inv.getParameter(paramName) }, null);
                parameterBindingResult.addError(fieldError);
            } catch (Exception e) {
                logger.error("", e);
                throw e;
            }
        }
        return parameters;
    }
}
