package ar.com.khronos.web.flex;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ar.com.khronos.core.annotations.Remote;
import flex.messaging.FactoryInstance;
import flex.messaging.FlexContext;
import flex.messaging.FlexFactory;
import flex.messaging.config.ConfigMap;

/**
 * {@link FactoryInstance} para la creacion de servicios a partir de
 * beans de Spring.
 * 
 * @author <a href="mailto:tezequiel@gmail.com">Ezequiel Turovetzky</a>
 */
public class SpringFactoryInstance extends FactoryInstance {

    /** Logger */
    private static final Log logger = LogFactory.getLog(SpringFactoryInstance.class);

    /**
	 * Crea una nueva instancia de esta clase
	 * @param factory
	 * @param id
	 * @param properties
	 */
    public SpringFactoryInstance(FlexFactory factory, String id, ConfigMap properties) {
        super(factory, id, properties);
    }

    @Override
    public Object lookup() {
        try {
            String beanName = getServiceBean();
            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(FlexContext.getServletContext());
            Object bean = ctx.getBean(beanName);
            if (isRemoteService(bean)) {
                return bean;
            } else throw new FlexServiceCreationException("Cannot access service from Flex. Service is not @Remote: " + beanName);
        } catch (FlexServiceCreationException e) {
            logger.fatal(e);
            throw e;
        } catch (Exception e) {
            logger.fatal(e.getMessage(), e);
            throw new FlexServiceCreationException(e.getMessage(), e);
        }
    }

    /**
     * Informa si el bean buscado por el cliente Flex es en realidad
     * un servicio remoto.
     * <p>
     * Los servicios remotos son los unicos que pueden ser invocados
     * desde Flex, y se anotan con la annotation @{@link Remote}
     * <p>
     * Este metodo busca a nivel de clase e interfaz la anotation @{@link Remote}.
     * 
     * @param bean Bean buscado por Flex, a saber si es un servicio remoto
     * @return <code>true</code> si es un servicio remoto, 
     * 		   <code>false</code> en caso opuesto
     */
    private boolean isRemoteService(Object bean) {
        if (bean.getClass().isAnnotationPresent(Remote.class)) {
            return true;
        }
        for (Class<?> c : bean.getClass().getInterfaces()) {
            if (c.isAnnotationPresent(Remote.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve el nombre del bean del servicio
     * que el cliente Flex desea invocar.
     * <br>
     * Este metodo es un workaround a cuando el nombre
     * del servicio viene con el "jsessionid".
     * 
     * @return El nombre del servicio
     */
    private String getServiceBean() {
        String bean = FlexContext.getHttpRequest().getParameter("service");
        if (bean.contains("jsessionid")) {
            bean = bean.substring(0, bean.indexOf(";"));
        }
        return bean;
    }
}
