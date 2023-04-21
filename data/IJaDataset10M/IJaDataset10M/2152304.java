package com.volantis.mcs.model.descriptor;

/**
 */
public interface ModelDescriptorBuilder {

    ModelDescriptor getModelDescriptor();

    BeanDescriptorBuilder getBeanBuilder(Class clazz, ModelObjectFactory modelObjectFactory);

    TypeDescriptor getTypeDescriptor(Class type);

    ClassDescriptor getClassDescriptor(Class type);

    ListClassDescriptor getStandardListDescriptor(Class listClass, ClassDescriptor itemClass);

    /**
     * Add a base class descriptor for the specified class.
     *
     * <p>A base class is essentially an abstract class, the reason it is not
     * called an abstract class is to avoid confusion with a major usage of the
     * "Abstract" which is used for partial implementations of interfaces.</p>
     *
     * @param baseClass The base class.
     * @return A base class descriptor.
     */
    BaseClassDescriptor addBaseClassDescriptor(Class baseClass);

    /**
     * Add a class descriptor for the opaque class.
     *
     * @param opaqueClass The opaque class.
     * @return An opaque class descriptor.
     */
    OpaqueClassDescriptor addOpaqueClassDescriptor(Class opaqueClass);
}
