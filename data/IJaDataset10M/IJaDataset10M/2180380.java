package net.sf.balm.common.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author dz
 */
public class AnnotationIntrospector {

    private static Map<Class<? extends Annotation>, AnnotationInfo> beanInfoCache = Collections.synchronizedMap(new WeakHashMap<Class<? extends Annotation>, AnnotationInfo>());

    /**
     * 
     * @param annotationClass
     * @return
     */
    public static AnnotationInfo getAnnotationInfo(Class<? extends Annotation> annotationClass) {
        AnnotationInfo ai = (AnnotationInfo) beanInfoCache.get(annotationClass);
        if (null == ai) {
            ai = (new AnnotationIntrospector(annotationClass)).getAnnotationInfo();
            beanInfoCache.put(annotationClass, ai);
        }
        return ai;
    }

    private Class<? extends Annotation> annotationClass;

    private Method[] annotationAttributeMethods;

    /**
     * 
     * @param annotationClass
     */
    private AnnotationIntrospector(Class<? extends Annotation> annotationClass) {
        super();
        this.annotationClass = annotationClass;
        this.annotationAttributeMethods = annotationClass.getMethods();
    }

    /**
     * 
     * @return
     */
    private AnnotationInfo getAnnotationInfo() {
        return new GenericAnnotationInfo(annotationClass, annotationAttributeMethods);
    }
}

class GenericAnnotationInfo extends SimpleAnnotationInfo {

    private AnnotationDescriptor annotationDescriptor;

    private AttributeDescriptor[] attributeDescriptors;

    public GenericAnnotationInfo(Class<? extends Annotation> clazz, Method[] methods) {
        this.annotationDescriptor = new AnnotationDescriptor(clazz);
        this.attributeDescriptors = new AttributeDescriptor[methods.length];
        for (int i = 0; i < methods.length; i++) {
            this.attributeDescriptors[i] = new AttributeDescriptor(methods[i]);
        }
    }

    public GenericAnnotationInfo(AnnotationDescriptor annotationDescriptor, AttributeDescriptor[] attributeDescriptors) {
        super();
        this.annotationDescriptor = annotationDescriptor;
        this.attributeDescriptors = attributeDescriptors;
    }

    @Override
    public AnnotationDescriptor getAnnotationDescriptor() {
        return annotationDescriptor;
    }

    @Override
    public AttributeDescriptor[] getAttributeDescriptor() {
        return this.attributeDescriptors;
    }
}
