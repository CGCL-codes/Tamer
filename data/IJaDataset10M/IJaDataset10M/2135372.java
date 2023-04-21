package org.nexopenframework.xml.binding.xservices.registry;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.xml.binding.xservices.XService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.Assert;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

/** 
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Aquesta clase implementa un pseudo Singleton i �s 
 *    l� encarregada de fer el auto-discovery dels XService's via Spring
 *    {@link org.springframework.beans.factory.BeanFactoryAware}, 
 *    a m�s a m�s de busqueda de XServices tamb� els enregistra i desregistra.
 * </p> 
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0.1 
 * @since 1.0 
 */
public class XServiceRegistry implements BeanFactoryAware, DisposableBean {

    /**Nom d'aquest registre en el contexte de Spring*/
    public static final String BEAN_NAME = "openfrwk.xservice.registry";

    /**an instance of this class*/
    private static XServiceRegistry me;

    /**registry of XServices. It is thread-safe*/
    private Map xservices = new ConcurrentHashMap();

    /** {@link org.apache.commons.logging} logging facility  */
    private Log logger = LogFactory.getLog(this.getClass());

    /**
	 * <p>Constructor del registre de XServices</p>
	 * 
	 * @throws IllegalStateException si ja ha estat instanciat
	 */
    public XServiceRegistry() {
        Assert.state(me == null, "Only one instance per JVM");
        me = this;
    }

    /**
	 * <p>Retorna la �nica inst�ncia d'aquesta classe</p>
	 * 
	 * @return
	 * @throws IllegalStateException si no ha estat instanciada aquesta classe
	 */
    public static XServiceRegistry getInstance() {
        Assert.state(me != null, "must be properly instantiated");
        return me;
    }

    /**
	 * <p>We load references of XServices from Spring ApplicationContext. 
	 *    Auto discovery of XServices in the application bootstrapping</p>
	 * 
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
    public void setBeanFactory(BeanFactory beanFactory) {
        Assert.isTrue(beanFactory instanceof ListableBeanFactory);
        if (logger.isDebugEnabled()) {
            logger.debug("\n\n setBeanFactory invoked");
        }
        Map _services = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory) beanFactory, XService.class, false, false);
        if (_services.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Not registered XServices in the application context.");
            }
            return;
        }
        this.xservices.putAll(_services);
        if (logger.isInfoEnabled()) {
            logger.info("XService auto-discovery success!.");
        }
    }

    /**
	 * @return
	 */
    public int getXServicesCount() {
        return (this.xservices != null) ? this.xservices.size() : 0;
    }

    /**
	 * @param xservices
	 */
    public void setXServices(Map xservices) {
        this.xservices.putAll(xservices);
    }

    /**
	 * <p>Esborra el XService del registre</p>
	 * 
	 * @param endpoint  endpoint name service
	 */
    public void unregisterXService(String endpoint) {
        if (xservices.containsKey(endpoint)) {
            xservices.remove(endpoint);
            if (logger.isDebugEnabled()) {
                logger.debug("XService <" + endpoint + "> has been removed.");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("XService <" + endpoint + "> not registered.");
        }
        throw new IllegalArgumentException("XService <" + endpoint + "> not registered.");
    }

    /**
	 * <p>XService register</p>
	 * 
	 * @param endpoint URN's service
	 * @throws IllegalArgumentException if the service has been registered previoulsy
	 * @param xs  Implementing class of the service
	 */
    public void registerXService(String endpoint, XService xs) {
        if (this.xservices.containsKey(endpoint)) {
            if (logger.isDebugEnabled()) {
                logger.debug("XService <" + endpoint + "> already registered.");
            }
            throw new IllegalArgumentException("XService <" + endpoint + "> already registered.");
        }
        this.xservices.put(endpoint, xs);
        if (logger.isDebugEnabled()) {
            logger.debug("XService <" + endpoint + "> successfully registered.");
        }
    }

    /**
	 * <p>Busca un servei associat a una urn. En cas que no existeixi aquest servei retorna null</p>
	 * 
	 * @param endpoint  endpoint name related with the given {@link XService}
	 * @return XService
	 */
    public XService findXService(String endpoint) {
        if (xservices.containsKey(endpoint)) {
            if (logger.isDebugEnabled()) {
                logger.debug("XService found <" + endpoint + ">");
            }
            return (XService) xservices.get(endpoint);
        }
        if (logger.isInfoEnabled()) {
            logger.info("Servei no trobat associat a " + endpoint);
        }
        return null;
    }

    public void destroy() {
        this.xservices.clear();
        this.xservices = null;
        me = null;
    }
}
