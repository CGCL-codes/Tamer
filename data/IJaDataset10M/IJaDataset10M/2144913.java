package com.cwxstat.dev;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class MySpringMapTest0 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("MySpringMap.xml"));
        Books book = (Books) beanFactory.getBean("book0");
        MySpringMap myBean = (MySpringMap) beanFactory.getBean("MySpringMapBean");
        book.pr();
        myBean.sayHello();
    }
}
