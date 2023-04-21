package fi.mmmtike.tiira.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.GenericWebApplicationContext;
import fi.mmmtike.tiira.core.TiiraException;

/**
 * Service resolver that uses regular expression to resolve which beans
 * are services in an application context. Give regex in constructor, for example
 * ".*Service".
 * 
 * @author Tomi Tuomainen
 */
@SuppressWarnings("unchecked")
public class TiiraWebApplicationContextServiceResolver extends TiiraServiceResolver {

    private Pattern beanNamePattern;

    private GenericWebApplicationContext webApplicationContext;

    /**
     * 
     * @param context	application context that includes services as beans
     * @param beanNameRegex regular expression used to resolve service beans
     */
    public TiiraWebApplicationContextServiceResolver(GenericWebApplicationContext context, String beanNameRegex) {
        beanNamePattern = Pattern.compile(beanNameRegex);
        webApplicationContext = context;
    }

    /**
     * This is for convenience, it creates application context for given xml context file.
     * 
     * @param xmlContextFile	Spring xml context file in classpath
     * @param beanNameRegex regular expression used to resolve service beans
     */
    public TiiraWebApplicationContextServiceResolver(String xmlContextFile, String beanNameRegex) {
        webApplicationContext = new GenericWebApplicationContext();
        this.loadBeanDefinitions(xmlContextFile, webApplicationContext);
        beanNamePattern = Pattern.compile(beanNameRegex);
    }

    public List<TiiraService> resolveServices() {
        ArrayList<TiiraService> result = new ArrayList<TiiraService>();
        List<String> names = resolveServiceBeanNames();
        for (String beanName : names) {
            BeanDefinition def = webApplicationContext.getBeanDefinition(beanName);
            TiiraService service = createTiiraService(beanName, def);
            result.add(service);
        }
        return result;
    }

    /**
     * Convenience method for loading spring context file into application context.
     * 
     * @param springContextFile	spring xml context file (must be in classpath)
     * @param context			application context
     */
    public void loadBeanDefinitions(String springContextFile, GenericApplicationContext context) {
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
        ClassPathResource res = new ClassPathResource(springContextFile);
        xmlReader.loadBeanDefinitions(res);
    }

    /**
     * 
     * @return	bean names that are tiira services
     */
    private List<String> resolveServiceBeanNames() {
        List<String> result = new ArrayList<String>();
        String[] names = webApplicationContext.getBeanDefinitionNames();
        for (String beanName : names) {
            if (matches(beanName)) {
                result.add(beanName);
            }
        }
        return result;
    }

    /**
     * Create Tiira service of bean definition.
     * 
     * @param def	bean definition
     * @return		tiira service
     */
    private TiiraService createTiiraService(String beanName, BeanDefinition def) {
        String beanClass = def.getBeanClassName();
        Class implClass = createClass(beanClass);
        Class interfaceClass = resolveServiceInterface(implClass);
        String urlName = this.resolveUrlName(interfaceClass);
        return new TiiraService(interfaceClass, implClass, beanName, urlName);
    }

    /**
     * 
     * @param className	class name
     * @return			Class found (excpetion is thrown if not)
     */
    private Class createClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new TiiraException(e);
        }
    }

    /**
     * 
     * @param beanName	bean name
     * @return			if bean is a service
     */
    private boolean matches(String beanName) {
        return beanNamePattern.matcher(beanName).matches();
    }

    public Pattern getBeanNamePattern() {
        return beanNamePattern;
    }

    public void setBeanNamePattern(Pattern beanNamePattern) {
        this.beanNamePattern = beanNamePattern;
    }

    public GenericWebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    public void setWebApplicationContext(GenericWebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }
}
