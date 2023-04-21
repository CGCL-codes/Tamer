package org.danann.cernunnos.flow;

import java.net.URL;
import org.danann.cernunnos.Attributes;
import org.danann.cernunnos.CacheHelper;
import org.danann.cernunnos.DynamicCacheHelper;
import org.danann.cernunnos.EntityConfig;
import org.danann.cernunnos.Formula;
import org.danann.cernunnos.ManagedException;
import org.danann.cernunnos.Phrase;
import org.danann.cernunnos.Reagent;
import org.danann.cernunnos.ReagentType;
import org.danann.cernunnos.ResourceHelper;
import org.danann.cernunnos.ReturnValueImpl;
import org.danann.cernunnos.SimpleFormula;
import org.danann.cernunnos.SimpleReagent;
import org.danann.cernunnos.Task;
import org.danann.cernunnos.TaskRequest;
import org.danann.cernunnos.TaskResponse;
import org.danann.cernunnos.CacheHelper.Factory;
import org.danann.cernunnos.runtime.ScriptRunner;

public final class CernunnosPhrase implements Phrase {

    private Phrase task;

    private final ResourceHelper resource = new ResourceHelper();

    private ScriptRunner runner;

    private Factory<String, Task> taskFactory;

    private CacheHelper<String, Task> taskCache;

    private EntityConfig config;

    public static final Reagent TASK = new SimpleReagent("TASK", "descendant-or-self::text()", ReagentType.PHRASE, Object.class, "Cernunnos Task to invoke.  Specify either LOCATION or TASK, but not both.", null);

    public Formula getFormula() {
        Reagent[] reagents = new Reagent[] { TASK, CacheHelper.CACHE, CacheHelper.CACHE_MODEL, ResourceHelper.CONTEXT_SOURCE, ResourceHelper.LOCATION_PHRASE };
        final Formula rslt = new SimpleFormula(getClass(), reagents);
        return rslt;
    }

    public void init(EntityConfig config) {
        this.runner = new ScriptRunner(config.getGrammar());
        this.task = (Phrase) config.getValue(TASK);
        resource.init(config);
        this.taskFactory = new CachedTaskFactory(this.runner);
        this.taskCache = new DynamicCacheHelper<String, Task>(config);
        this.config = config;
    }

    public Object evaluate(TaskRequest req, TaskResponse res) {
        ReturnValueImpl rslt = new ReturnValueImpl();
        res.setAttribute(Attributes.RETURN_VALUE, rslt);
        Task k = null;
        Object taskReagentValue = task.evaluate(req, res);
        if (taskReagentValue == null) {
            String msg = "Reagent TASK cannot be null";
            throw new RuntimeException(msg);
        }
        if (taskReagentValue instanceof Task) {
            k = (Task) taskReagentValue;
        } else if (taskReagentValue instanceof String) {
            final URL crn = resource.evaluate(req, res);
            final String taskPath = crn.toExternalForm();
            k = this.taskCache.getCachedObject(req, res, taskPath, this.taskFactory);
        }
        try {
            runner.run(k, req, res);
        } catch (Throwable t) {
            throw new ManagedException(this.config, req, t);
        }
        return rslt.getValue();
    }
}
