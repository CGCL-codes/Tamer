package org.dozer.functional_tests.support;

import org.dozer.BeanFactory;
import org.dozer.vo.Car;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class SampleCustomBeanFactory3 implements BeanFactory {

    public Object createBean(Object srcObj, Class<?> srcObjClass, String id) {
        try {
            Object rvalue = Car.class.newInstance();
            return rvalue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
