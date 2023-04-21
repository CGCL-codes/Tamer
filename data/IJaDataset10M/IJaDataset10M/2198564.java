package org.netxilia.spi.impl.formula.function;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.beanutils.ConvertUtils;
import org.netxilia.api.formula.IFormulaContext;
import org.netxilia.api.reference.AreaReference;
import org.netxilia.api.value.ErrorValue;
import org.netxilia.api.value.ErrorValueType;
import org.netxilia.api.value.GenericValueUtils;
import org.netxilia.api.value.IDelegateValue;
import org.netxilia.api.value.IGenericValue;
import org.netxilia.api.value.ReferenceValue;
import org.netxilia.api.value.RichValue;
import org.netxilia.spi.formula.Function;
import org.netxilia.spi.impl.formula.parser.ASTBaseNode;

/**
 * This implements the interface {@link IFunction} using a given method from an object containing a list of different
 * functions. This wrapper does the proper type conversions between the ASTNodes and basic types.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class MethodWrapper implements IFunction {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MethodWrapper.class);

    private final Object instance;

    private final Method method;

    private final IParamConverter[] paramConverters;

    private final boolean cacheable;

    public MethodWrapper(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        Class<?>[] paramTypes = method.getParameterTypes();
        paramConverters = new IParamConverter[paramTypes.length];
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (int i = 0; i < genericParameterTypes.length; ++i) {
            Type genericParameterType = genericParameterTypes[i];
            if (genericParameterType instanceof ParameterizedType) {
                ParameterizedType aType = (ParameterizedType) genericParameterType;
                if (Iterator.class.isAssignableFrom((Class<?>) aType.getRawType())) {
                    Class<?> componentType = (Class<?>) aType.getActualTypeArguments()[0];
                    paramConverters[i] = new IteratorConverter(componentType);
                }
            } else {
                Class<?> paramType = (Class<?>) genericParameterType;
                if (IGenericValue.class == paramType) {
                    paramConverters[i] = new LazyParamConverter();
                } else if (RichValue.class == paramType) {
                    paramConverters[i] = new RichValueParamConverter();
                } else {
                    paramConverters[i] = new BasicTypeConverter(paramType);
                }
            }
        }
        Function annotation = this.method.getAnnotation(Function.class);
        cacheable = annotation == null || annotation.cacheable();
    }

    @Override
    public IGenericValue eval(IFormulaContext context, ASTBaseNode... paramNodes) {
        Object[] params = new Object[paramConverters.length];
        for (int i = 0; i < paramConverters.length; ++i) {
            params[i] = paramConverters[i].convert(context, paramNodes, i);
        }
        Object returnValue;
        try {
            returnValue = method.invoke(instance, params);
        } catch (Exception e) {
            log.error("Exception executing method:" + method + " with params:" + Arrays.toString(params) + ":" + e, e);
            return new ErrorValue(ErrorValueType.VALUE);
        }
        if (returnValue == null) {
            return null;
        }
        if (returnValue instanceof IGenericValue) {
            return (IGenericValue) returnValue;
        }
        if (returnValue instanceof AreaReference) {
            return new ReferenceValue((AreaReference) returnValue, context);
        }
        return GenericValueUtils.objectAsValue(returnValue);
    }

    @Override
    public String getDocumentation() {
        return null;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public String getParamNames() {
        return null;
    }

    private interface IParamConverter {

        public Object convert(IFormulaContext context, ASTBaseNode[] nodes, int pos);
    }

    private class LazyParamConverter implements IParamConverter {

        @Override
        public Object convert(IFormulaContext context, ASTBaseNode[] nodes, int pos) {
            return new LazyGenericValue(nodes[pos], context);
        }
    }

    private class BasicTypeConverter implements IParamConverter {

        private Class<?> paramType;

        public BasicTypeConverter(Class<?> paramType) {
            this.paramType = paramType;
        }

        @Override
        public Object convert(IFormulaContext context, ASTBaseNode[] nodes, int pos) {
            if (pos >= nodes.length) {
                return ConvertUtils.convert((String) null, paramType);
            }
            IGenericValue value = nodes[pos].eval(context);
            return GenericValueUtils.convert(value, paramType);
        }
    }

    private class RichValueParamConverter implements IParamConverter {

        public RichValueParamConverter() {
        }

        @Override
        public Object convert(IFormulaContext context, ASTBaseNode[] nodes, int pos) {
            IGenericValue value = nodes[pos].eval(context);
            if (value == null) {
                return null;
            }
            while (value instanceof IDelegateValue) {
                if (value instanceof RichValue) {
                    return value;
                }
                value = ((IDelegateValue) value).getGenericValue();
            }
            return new RichValue(value);
        }
    }

    private class IteratorConverter implements IParamConverter {

        private final Class<?> elementType;

        public IteratorConverter(Class<?> elementType) {
            this.elementType = elementType;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object convert(IFormulaContext context, ASTBaseNode[] nodes, int pos) {
            return new ParameterIterator(elementType, context, nodes, pos);
        }
    }

    @Override
    public boolean isCacheble() {
        return cacheable;
    }
}
