package com.abra.j2xb.beans.exceptions;

/**
 * @author Yoav Abrahami
 * @version 1.0, May 1, 2008
 * @since   JDK1.5
 */
public class MOBeanClassNotMappedException extends MOBeansException {

    public MOBeanClassNotMappedException(Class aClass) {
        super("the class [%s] is not mapped by the MOBeansFactory", aClass.getName());
    }
}
