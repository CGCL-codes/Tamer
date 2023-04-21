package org.dozer.functional_tests.support;

import org.dozer.BeanFactory;
import org.dozer.vo.interfacerecursion.User;
import org.dozer.vo.interfacerecursion.UserGroup;
import org.dozer.vo.interfacerecursion.UserGroupImpl;
import org.dozer.vo.interfacerecursion.UserGroupPrime;
import org.dozer.vo.interfacerecursion.UserGroupPrimeImpl;
import org.dozer.vo.interfacerecursion.UserImpl;
import org.dozer.vo.interfacerecursion.UserPrime;
import org.dozer.vo.interfacerecursion.UserPrimeImpl;

/**
 * @author Christoph Goldner 
 */
public class UserBeanFactory implements BeanFactory {

    public Object createBean(Object aSrcObj, Class<?> aSrcObjClass, String aTargetBeanId) throws RuntimeException {
        if (aSrcObj == null || aSrcObjClass == null || aTargetBeanId == null) {
            return null;
        }
        Class<?> targetClass;
        try {
            targetClass = Class.forName(aTargetBeanId);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (User.class.isAssignableFrom(targetClass)) {
            return new UserImpl();
        }
        if (UserGroup.class.isAssignableFrom(targetClass)) {
            return new UserGroupImpl();
        }
        if (UserPrime.class.isAssignableFrom(targetClass)) {
            return new UserPrimeImpl();
        }
        if (UserGroupPrime.class.isAssignableFrom(targetClass)) {
            return new UserGroupPrimeImpl();
        }
        try {
            return targetClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
