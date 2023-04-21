package org.t2framework.commons.meta.impl;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.t2framework.commons.Constants;
import org.t2framework.commons.annotation.Lookup;
import org.t2framework.commons.annotation.composite.SingletonScope;
import org.t2framework.commons.annotation.composite.SingletonScope.SingletonType;
import org.t2framework.commons.meta.AnnotationConfig;
import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.commons.meta.Config;
import org.t2framework.commons.meta.ConfigContainer;
import org.t2framework.commons.meta.ConfigPrepareCallback;
import org.t2framework.commons.meta.ConfigType;
import org.t2framework.commons.meta.LookupContext;
import org.t2framework.commons.meta.spi.AnnotationConfigSupport;
import org.t2framework.commons.module.ModulesBootstrap;
import org.t2framework.commons.util.AnnotationUtil;
import org.t2framework.commons.util.ArrayUtil;
import org.t2framework.commons.util.Assertion;
import org.t2framework.commons.util.CollectionsUtil;
import org.t2framework.commons.util.StringUtil;
import org.t2framework.commons.util.Reflections.MethodUtil;

/**
 * <#if locale="en">
 * <p>
 * Concrete class of AnnotationConfig.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * @author c9katayama
 * @see org.t2framework.commons.meta.AnnotationConfig
 */
public class AnnotationConfigImpl implements AnnotationConfig {

    protected Annotation annotation;

    protected ElementType type;

    protected String annotationName;

    protected List<Annotation> metaAnnotationList;

    protected Class<? extends Annotation> annotationType;

    protected ElementType[] acceptableElementTypes;

    protected ConfigContainer container;

    protected List<String> methodNameList = CollectionsUtil.newArrayList();

    protected Map<Class<? extends Annotation>, LookupContext> lookupContextMap = CollectionsUtil.newHashMap();

    public AnnotationConfigImpl(Annotation annotation) {
        this(annotation, null);
    }

    public AnnotationConfigImpl(Annotation annotation, ElementType type) {
        this(annotation, type, null);
    }

    public AnnotationConfigImpl(Annotation annotation, ElementType type, ConfigContainer container) {
        this.annotation = Assertion.notNull(annotation);
        this.type = type;
        this.metaAnnotationList = CollectionsUtil.newArrayList();
        this.container = container;
        initAnnotationInfo(annotation);
        initMetaAnnotationList(annotationType);
        initLookupMethodMap(annotationType);
    }

    protected void initAnnotationInfo(Annotation annotation) {
        this.annotationName = annotation.annotationType().getSimpleName();
        this.annotationType = annotation.annotationType();
    }

    protected void initMetaAnnotationList(final Class<? extends Annotation> annotationType) {
        for (Annotation a : annotationType.getAnnotations()) {
            if (AnnotationUtil.isJavaLangAnnotation(a) == false) {
                metaAnnotationList.add(a);
            }
            if (AnnotationUtil.isTargetAnnotation(a)) {
                Target t = Target.class.cast(a);
                this.acceptableElementTypes = t.value();
                assertAcceptableElementType();
            }
        }
    }

    protected void initLookupMethodMap(Class<? extends Annotation> annotationType) {
        for (Method m : annotationType.getDeclaredMethods()) {
            for (Annotation a : m.getDeclaredAnnotations()) {
                if (a.annotationType() == Lookup.class) {
                    Lookup lookup = Lookup.class.cast(a);
                    final Class<? extends Annotation> key = lookup.value();
                    LookupContext context = null;
                    if (lookupContextMap.containsKey(key) == false) {
                        context = new LookupContext(key);
                    } else {
                        context = lookupContextMap.get(key);
                    }
                    String alias = lookup.alias();
                    if (StringUtil.isEmpty(alias)) {
                        alias = m.getName();
                    }
                    context.addMethodValue(alias, MethodUtil.invoke(m, this.annotation, Constants.EMPTY_ARRAY));
                    lookupContextMap.put(key, context);
                }
            }
            methodNameList.add(m.getName());
        }
    }

    protected void assertAcceptableElementType() {
        if (acceptableElementTypes == null) {
            return;
        }
        if (type != null && ArrayUtil.contains(acceptableElementTypes, type) == false) {
            throw new IllegalStateException("ElementType should be in @Target' element types.");
        }
    }

    @Override
    public ElementType getElementType() {
        return type;
    }

    @Override
    public List<Annotation> getMetaAnnotationList() {
        return metaAnnotationList;
    }

    @Override
    public void setElementType(ElementType type) {
        this.type = type;
        assertAcceptableElementType();
    }

    @Override
    public ElementType[] getAcceptableTypes() {
        return acceptableElementTypes;
    }

    @Override
    public boolean isAcceptableType(ElementType type) {
        Assertion.notNull(type);
        return ArrayUtil.contains(getAcceptableTypes(), type);
    }

    @Override
    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public Class<? extends Annotation> getType() {
        return annotationType;
    }

    @Override
    public ConfigType getConfigType() {
        return ConfigType.ANNOTATION;
    }

    @Override
    public boolean hasAnnotation() {
        return true;
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * Experimental, not stable.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 */
    @Override
    public void setValue(String methodName, Object value) {
        Assertion.notNull(methodName);
        Assertion.notNull(value);
        synchronized (this.annotation) {
            AnnotationConfigSupport annotationConfigSupport = ModulesBootstrap.getModules().getService(AnnotationConfigSupport.class);
            if (annotationConfigSupport != null) {
                this.annotation = annotationConfigSupport.createAnnotation(this.annotation, methodName, value);
                initAnnotationInfo(annotation);
            }
        }
    }

    @Override
    public boolean hasLookup(Class<? extends Annotation> annotationClass) {
        Assertion.notNull(annotationClass);
        return lookupContextMap.containsKey(annotationClass);
    }

    @Override
    public LookupContext getLookupContext(Class<? extends Annotation> annotationClass) {
        Assertion.notNull(annotationClass);
        return lookupContextMap.get(annotationClass);
    }

    @Override
    public void setValues(LookupContext lookupContext) {
        Assertion.notNull(lookupContext);
        for (String methodName : methodNameList) {
            if (lookupContext.contains(methodName) == false) {
                continue;
            }
            Object value = lookupContext.getValue(methodName);
            setValue(methodName, value);
        }
    }

    @Override
    public ConfigContainer getConfigContainer() {
        return container;
    }

    @Override
    public void prepare(ConfigPrepareCallback callback, BeanDesc<?> beanDesc) {
        if (annotationType != SingletonScope.class) {
            return;
        }
        if (beanDesc.hasConfig(SingletonScope.class)) {
            Config config = beanDesc.findConfig(SingletonScope.class);
            SingletonScope singletonScope = (SingletonScope) config.getAnnotation();
            if (singletonScope.type() != SingletonType.LAZY) {
                callback.callback(beanDesc);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("@").append(this.hashCode());
        builder.append("(").append(annotationType.getName()).append(")");
        builder.append("[");
        builder.append("annotationType=").append(this.annotationType.getName()).append(", ");
        builder.append("elementType=").append(this.type).append(", ");
        {
            builder.append("metaAnnotationList=[");
            for (Annotation a : metaAnnotationList) {
                builder.append(a);
                builder.append(", ");
            }
            if (this.metaAnnotationList.isEmpty() == false) {
                builder.setLength(builder.length() - 2);
            }
            builder.append("]");
        }
        builder.append("]");
        return new String(builder);
    }
}
