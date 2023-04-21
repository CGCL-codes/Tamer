package org.springframework.beans.factory.yaml.test;

import junit.framework.TestCase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.yaml.YamlBeanFactoryLite;
import org.springframework.core.io.ClassPathResource;

public class YamlBeanFactoryLiteTests extends TestCase {

    /**
	 * @param args
	 */
    public void testPlay() {
        BeanFactory factory = new YamlBeanFactoryLite(new ClassPathResource("org/springframework/beans/factory/yaml/test/lite.yml"));
        System.out.println(factory.getBean("play"));
    }

    public void testKids() {
        BeanFactory factory = new YamlBeanFactoryLite(new ClassPathResource("org/springframework/beans/factory/yaml/test/kids-lite.yml"));
        System.out.println(factory.getBean("danny"));
    }
}
