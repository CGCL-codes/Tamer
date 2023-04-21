package gestionservicio.view.bean;

import gestionservicio.controller.PersonaController;
import gestionservicio.model.dao.interfaces.PersonaDao;
import gestionservicio.util.FacesUtils;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author principal
 */
public class ServiceLocatorBean implements ServiceLocator {

    private Log logger = LogFactory.getLog(this.getClass());

    private PersonaController personaController;

    private PersonaDao personaDao;

    private ApplicationContext appContext;

    public ServiceLocatorBean() {
        ServletContext context = FacesUtils.getServletContext();
        this.appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        this.personaDao = (PersonaDao) this.lookupService("personaDao");
        this.personaController = new PersonaController(this.personaDao);
    }

    private Object lookupService(String serviceBeanName) {
        return appContext.getBean(serviceBeanName);
    }

    public PersonaController getPersonaController() {
        return this.personaController;
    }
}
