package br.com.linkcom.neo.hibernate;

import java.io.IOException;
import javax.persistence.Entity;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import br.com.linkcom.neo.classmanager.ClassManager;
import br.com.linkcom.neo.classmanager.ClassRegister;
import br.com.linkcom.neo.classmanager.WebClassRegister;

/**
 * @author rogelgarcia
 * @since 18/10/2005
 * @version 1.0
 */
public class DefaultEntityFinder implements EntityFinder, ApplicationContextAware {

    private static final String NEO_PACKAGE = "br.com.linkcom.neo";

    private ApplicationContext applicationContext;

    private ClassManager classManager;

    public void setClassManager(ClassManager classManager) {
        this.classManager = classManager;
    }

    public Class<?>[] findEntities() throws IOException {
        if (classManager == null) {
            if (applicationContext instanceof WebApplicationContext) {
                classManager = WebClassRegister.getClassManager(((WebApplicationContext) applicationContext).getServletContext(), NEO_PACKAGE);
            } else {
                classManager = ClassRegister.getClassManager();
            }
        }
        return classManager.getClassesWithAnnotation(Entity.class);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
