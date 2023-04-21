package net.forsuber;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * User: lzap
 * Date: 25.7.2007
 * Time: 23:15:58
 */
public class FormSubmiterMain {

    static {
        System.getProperties().setProperty("java.util.logging.config.file", "logging.properties");
    }

    private static final Log LOG = LogFactory.getLog(FormSubmiterMain.class);

    public static void main(String[] params) {
        Resource resource = new FileSystemResource("forsuber.xml");
        BeanFactory factory = new XmlBeanFactory(resource);
        FormSubmiter submiter = (FormSubmiter) factory.getBean("submiter");
        try {
            submiter.run();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
