package br.gov.frameworkdemoiselle.internal.bootstrap;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import br.gov.frameworkdemoiselle.annotation.Startup;
import br.gov.frameworkdemoiselle.configuration.Constant;
import br.gov.frameworkdemoiselle.internal.context.ThreadLocalContext;
import br.gov.frameworkdemoiselle.internal.processor.StartupProcessor;

public class StartupBootstrap extends AbstractBootstrap {

    private static final Class<? extends Annotation> annotationClass = Startup.class;

    private final List<ThreadLocalContext> tempContexts = new ArrayList<ThreadLocalContext>();

    @SuppressWarnings("rawtypes")
    private static final List<StartupProcessor> actions = Collections.synchronizedList(new ArrayList<StartupProcessor>());

    /**
	 * Observes all methods annotated with @Startup and create an instance of StartupAction for them
	 * 
	 * @param <T>
	 * @param event
	 * @param beanManager
	 */
    public <T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> event, final BeanManager beanManager) {
        final AnnotatedType<T> annotatedType = event.getAnnotatedType();
        for (AnnotatedMethod<?> am : annotatedType.getMethods()) {
            if (am.isAnnotationPresent(annotationClass)) {
                @SuppressWarnings("unchecked") AnnotatedMethod<T> annotatedMethod = (AnnotatedMethod<T>) am;
                actions.add(new StartupProcessor<T>(annotatedMethod, beanManager));
            }
        }
    }

    public void loadTempContexts(@Observes final AfterBeanDiscovery event) {
        this.tempContexts.add(new ThreadLocalContext(SessionScoped.class));
        this.tempContexts.add(new ThreadLocalContext(RequestScoped.class));
        for (ThreadLocalContext tempContext : this.tempContexts) {
            addContext(tempContext, event);
        }
    }

    /**
	 * After the deployment validation it execute the methods annotateds with @Startup considering the priority order;
	 * 
	 * @param event
	 * @throws Exception
	 * @throws StartupException
	 */
    @SuppressWarnings("unchecked")
    public void startup(@Observes final AfterDeploymentValidation event) throws Throwable {
        getLogger().debug(getBundle(Constant.CORE_BUNDLE_BASE_NAME).getString("executing-all", annotationClass.getSimpleName()));
        Collections.sort(actions);
        for (StartupProcessor<?> action : actions) {
            action.process();
        }
        actions.clear();
        unloadTempContexts();
    }

    private void unloadTempContexts() {
        for (ThreadLocalContext tempContext : this.tempContexts) {
            disableContext(tempContext);
        }
    }
}
