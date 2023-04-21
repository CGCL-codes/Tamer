package net.sourceforge.sbr.plugin.services.internal;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.sbr.common.model.Bean;
import net.sourceforge.sbr.plugin.services.SpringService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SpringServiceImpl implements SpringService {

    @Override
    public List<Bean> parseXmlContext(String xmlFileContextLocation, ClassLoader beanClassLoader) throws Exception {
        List<Bean> listOfBean = new ArrayList<Bean>();
        FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext();
        applicationContext.setClassLoader(beanClassLoader);
        applicationContext.setConfigLocation(xmlFileContextLocation);
        applicationContext.refresh();
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        String[] arrayOfBeanName = beanFactory.getBeanDefinitionNames();
        for (String beanName : arrayOfBeanName) {
            Bean bean = new Bean();
            bean.setName(beanName);
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            bean.setType(beanDefinition.getBeanClassName());
            listOfBean.add(bean);
        }
        String[] tmp = beanFactory.getDependenciesForBean("bookManager");
        String[] tmp2 = beanFactory.getDependentBeans("bookDao");
        System.out.println(tmp2);
        return listOfBean;
    }
}
