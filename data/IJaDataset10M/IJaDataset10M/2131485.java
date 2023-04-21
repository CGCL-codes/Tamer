package example.chapter3;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        System.out.println(">>postProcessBeforeInitialization: " + name);
        if (bean instanceof BasicBean) System.out.println(">> Becareful! This guy will be born!");
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        System.out.println(">>postProcessAfterInitialization: " + name);
        if (bean instanceof BasicBean) {
            System.out.println(">> Check BasicBean...");
            BasicBean bb = (BasicBean) bean;
            bb.setTitle(bb.getTitle().toLowerCase());
        }
        return bean;
    }
}
