package org.seqtagutils.util;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.Properties;
import org.seqtagutils.util.dao.CDatabaseHelper;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public final class CSpringHelper {

    private CSpringHelper() {
    }

    public static void loadXmlBeanDefinitions(BeanDefinitionRegistry context, String... paths) {
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
        for (String path : paths) {
            System.out.println("loading classpath XML bean definition: " + path);
            xmlReader.loadBeanDefinitions(new ClassPathResource(path));
        }
    }

    public static void loadPropertiesBeanDefinitions(BeanDefinitionRegistry context, String... paths) {
        PropertiesBeanDefinitionReader propReader = new PropertiesBeanDefinitionReader(context);
        try {
            for (String path : paths) {
                System.out.println("loading property bean definition: " + path);
                propReader.loadBeanDefinitions(new UrlResource("file:///" + path));
            }
        } catch (MalformedURLException e) {
            throw new CException(e);
        }
    }

    public static void loadPropertiesBeanDefinitions(BeanDefinitionRegistry context, Properties properties) {
        PropertiesBeanDefinitionReader rdr = new PropertiesBeanDefinitionReader(context);
        rdr.registerBeanDefinitions(properties);
    }

    public static void registerDataSource(GenericApplicationContext context, String name, CDatabaseHelper.Params params) {
        CSpringHelper.registerDataSource(context, name, params.getDriver(), params.getUrl(), params.getUsername(), params.getPassword());
    }

    public static void registerDataSource(GenericApplicationContext context, String name, CDatabaseHelper.Params params, String dbname) {
        CSpringHelper.registerDataSource(context, name, params.getDriver(), params.getUrl(dbname), params.getUsername(), params.getPassword());
    }

    public static void registerDataSource(GenericApplicationContext context, String name, String driver, String url, String username, String password) {
        registerBean(context, name, DriverManagerDataSource.class, "driverClassName", driver, "url", url, "username", username, "password", password);
    }

    public static void registerBean(GenericApplicationContext context, String name, Class<?> cls, Map<String, Object> properties) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(cls);
        for (String property : properties.keySet()) {
            Object value = properties.get(property);
            System.out.println("adding property: " + property + "=" + value);
            builder.addPropertyValue(property, value);
        }
        context.registerBeanDefinition(name, builder.getBeanDefinition());
    }

    public static void registerBean(GenericApplicationContext context, String name, Class<?> cls, Object... args) {
        Map<String, Object> properties = CStringHelper.createMap(args);
        registerBean(context, name, cls, properties);
    }

    public static void registerValue(GenericApplicationContext context, String name, Object value) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(String.class);
        builder.addConstructorArgValue(value);
        context.registerBeanDefinition(name, builder.getBeanDefinition());
    }

    public static void registerPropertyPlaceholderConfigurer(GenericApplicationContext context, String... locations) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(PropertyPlaceholderConfigurer.class);
        builder.addPropertyValue("fileEncoding", CFileHelper.ENCODING.toString());
        builder.addPropertyValue("locations", locations);
        context.registerBeanDefinition("propertyPlaceholderConfigurer", builder.getBeanDefinition());
    }

    public static String checkResolvedProperty(String property, String value) {
        if (!CStringHelper.hasContent(value)) throw new CException("property " + property + " is not set: " + value);
        if (value.indexOf("${") != -1) throw new CException("property " + property + " has unresolved placeholder: " + value);
        return value;
    }
}
