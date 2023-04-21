package org.danann.cernunnos.script;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.danann.cernunnos.CacheHelper;
import org.danann.cernunnos.DynamicCacheHelper;
import org.danann.cernunnos.EntityConfig;
import org.danann.cernunnos.Formula;
import org.danann.cernunnos.Phrase;
import org.danann.cernunnos.Reagent;
import org.danann.cernunnos.ReagentType;
import org.danann.cernunnos.SimpleFormula;
import org.danann.cernunnos.SimpleReagent;
import org.danann.cernunnos.TaskRequest;
import org.danann.cernunnos.TaskResponse;
import org.danann.cernunnos.Tuple;

public class ScriptPhrase implements Phrase {

    private CacheHelper<Tuple<ScriptEngine, String>, ScriptEvaluator> scriptEvaluatorCache;

    private Phrase enginePhrase;

    private Phrase expression;

    public static final Reagent ENGINE = new SimpleReagent("ENGINE", "@engine", ReagentType.PHRASE, ScriptEngine.class, "Optional instance of ScriptEngine that will be used to invoke SCRIPT.  If one is not provided, " + "a new one will be created.  The default is the value of the 'ScriptAttributes.ENGINE.{ENGINE_NAME}' " + "request attribute, if present.");

    public static final Reagent EXPRESSION = new SimpleReagent("EXPRESSION", "descendant-or-self::text()", ReagentType.PHRASE, String.class, "Script expression to evaluate.");

    public Formula getFormula() {
        Reagent[] reagents = new Reagent[] { CacheHelper.CACHE, CacheHelper.CACHE_MODEL, ENGINE, EXPRESSION };
        final Formula rslt = new SimpleFormula(ScriptPhrase.class, reagents);
        return rslt;
    }

    public void init(EntityConfig config) {
        this.enginePhrase = (Phrase) config.getValue(ENGINE);
        this.expression = (Phrase) config.getValue(EXPRESSION);
        this.scriptEvaluatorCache = new DynamicCacheHelper<Tuple<ScriptEngine, String>, ScriptEvaluator>(config);
    }

    public Object evaluate(TaskRequest req, TaskResponse res) {
        final ScriptEngine engine = (ScriptEngine) this.enginePhrase.evaluate(req, res);
        final String script = (String) this.expression.evaluate(req, res);
        final Tuple<ScriptEngine, String> scriptEvaluatorKey = new Tuple<ScriptEngine, String>(engine, script);
        final ScriptEvaluator scriptEvaluator = this.scriptEvaluatorCache.getCachedObject(req, res, scriptEvaluatorKey, ScriptEvaluatorFactory.INSTANCE);
        final Bindings bindings = ScriptUtils.generateBindings(req, res);
        try {
            return scriptEvaluator.eval(bindings);
        } catch (ScriptException se) {
            throw new RuntimeException("Error while executing the specified script.  " + "\n\t\tENGINE_NAME:  " + engine.getFactory().getEngineName() + "\n\t\tSCRIPT (follows):\n" + script + "\n", se);
        }
    }
}
