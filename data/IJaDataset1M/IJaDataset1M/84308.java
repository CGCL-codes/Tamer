package com.abra.j2xb.beans.xmlModel.exceptions;

/**
 * @author Yoav Abrahami
 * @version 1.0, May 1, 2008
 * @since   JDK1.5
 */
public class MoXmlSchemaException extends MoXmlBeanException {

    public MoXmlSchemaException(Class<?> beanClass, String namespace) {
        super("Bean %s is not of the expected namespace %s", beanClass.getName(), namespace);
    }
}
