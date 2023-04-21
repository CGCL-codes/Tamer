package qwicket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

/**
 * Created Jul 6, 2006
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class QwicketSessionFactory extends AnnotationSessionFactoryBean implements ApplicationContextAware {

    private ApplicationContext context;

    private static Log log = LogFactory.getLog(QwicketSessionFactory.class);

    /**
     * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
