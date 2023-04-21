package net.sf.balm.spring.beans;

import org.springframework.beans.FatalBeanException;

/**
 * 当Spring中找到相同类型(多个bean的class的值相同)的多个bean的时候，无法决定选择哪一个实例，可抛出本异常
 * 
 * @author dz
 */
public class MultiBeanDefinitionFoundException extends FatalBeanException {

    public MultiBeanDefinitionFoundException(String msg) {
        super(msg);
    }

    public MultiBeanDefinitionFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MultiBeanDefinitionFoundException(Class clazz, String msg) {
        super("Multi instance of " + clazz.getName() + " found!" + msg);
    }

    public MultiBeanDefinitionFoundException(Class clazz, String msg, Throwable cause) {
        super("Multi instance of " + clazz.getName() + " found!" + msg, cause);
    }
}
