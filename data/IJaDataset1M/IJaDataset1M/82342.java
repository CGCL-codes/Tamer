package org.danann.cernunnos.spring;

import java.net.URL;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.danann.cernunnos.AbstractContainerTask;
import org.danann.cernunnos.EntityConfig;
import org.danann.cernunnos.Formula;
import org.danann.cernunnos.LiteralPhrase;
import org.danann.cernunnos.Phrase;
import org.danann.cernunnos.Reagent;
import org.danann.cernunnos.ReagentType;
import org.danann.cernunnos.ResourceHelper;
import org.danann.cernunnos.SimpleFormula;
import org.danann.cernunnos.SimpleReagent;
import org.danann.cernunnos.TaskRequest;
import org.danann.cernunnos.TaskResponse;

public final class ApplicationContextTask extends AbstractContainerTask {

    private final ResourceHelper resource = new ResourceHelper();

    private Phrase cache;

    private URL prevUrl = null;

    private ApplicationContext prevBeans = null;

    public static final Reagent CACHE = new SimpleReagent("CACHE", "@cache", ReagentType.PHRASE, Boolean.class, "If true (the default), this task will retain and reuse an existing ApplicationContext " + "unless/until either:  (1) a different XML file is specified;  or (2) this reagent " + "becomes false.", new LiteralPhrase(Boolean.TRUE));

    public Formula getFormula() {
        Reagent[] reagents = new Reagent[] { ResourceHelper.CONTEXT_SOURCE, ResourceHelper.LOCATION_TASK, CACHE, AbstractContainerTask.SUBTASKS };
        final Formula rslt = new SimpleFormula(ApplicationContextTask.class, reagents);
        return rslt;
    }

    public void init(EntityConfig config) {
        super.init(config);
        this.resource.init(config);
        this.cache = (Phrase) config.getValue(CACHE);
    }

    public void perform(TaskRequest req, TaskResponse res) {
        URL loc = resource.evaluate(req, res);
        ApplicationContext beans = getApplicationContext(loc, (Boolean) cache.evaluate(req, res));
        for (String name : beans.getBeanDefinitionNames()) {
            try {
                res.setAttribute(name, beans.getBean(name));
            } catch (BeanIsAbstractException biae) {
                log.debug("Not adding bean '" + name + "' to TaskResponse because it is abstract.");
            }
        }
        super.performSubtasks(req, res);
    }

    private synchronized ApplicationContext getApplicationContext(URL config, boolean useCache) {
        if (!useCache || !config.equals(prevUrl)) {
            prevBeans = new FileSystemXmlApplicationContext(config.toExternalForm());
            prevUrl = config;
        }
        return prevBeans;
    }
}
