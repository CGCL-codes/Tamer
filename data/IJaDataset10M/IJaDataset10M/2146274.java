package cspom.predicate;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import cspom.AbstractRelation;

public class Predicate extends AbstractRelation {

    private final Map<String, Type> types;

    private final List<String> parameters;

    private final String expression;

    private static final Compilable engine;

    private static final Bindings bindings;

    private final CompiledScript script;

    private static final Logger logger = Logger.getLogger(Predicate.class.getSimpleName());

    static {
        final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        if (scriptEngine == null) {
            logger.warning("Could not find JavaScript engine");
            bindings = null;
            engine = null;
        } else {
            try {
                URL url = Predicate.class.getResource("predefinedFunctions.js");
                if (url == null) {
                    url = new URL("file:predefinedFunctions.js");
                }
                scriptEngine.eval(new InputStreamReader(url.openStream()));
            } catch (Exception e) {
                logger.throwing(Predicate.class.getSimpleName(), "static", e);
            }
            bindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
            engine = (Compilable) scriptEngine;
        }
    }

    public Predicate(final String name, final String parameters, final String expression) throws ScriptException {
        this(name, new ArrayList<String>(), new HashMap<String, Type>(), expression.trim());
        final String[] args = parameters.trim().split(" +");
        for (int i = 0; i < args.length; i += 2) {
            types.put(args[i + 1], Type.parse(args[i]));
            this.parameters.add(args[i + 1]);
        }
    }

    public Predicate(final String name, final List<String> parameters, final Map<String, Type> types, final String expression) throws ScriptException {
        super(name);
        this.parameters = parameters;
        this.types = types;
        this.expression = expression;
        if (engine == null) {
            script = null;
        } else {
            script = engine.compile(transform(expression));
        }
    }

    public String toString() {
        return super.toString() + " (" + types + "): " + expression;
    }

    public boolean evaluate(final Map<String, ? extends Number> values) throws ScriptException {
        bindings.putAll(values);
        return (Boolean) script.eval(bindings);
    }

    public boolean evaluate(final Number[] values) throws ScriptException {
        for (int i = values.length; --i >= 0; ) {
            bindings.put(parameters.get(i), values[i]);
        }
        return (Boolean) script.eval(bindings);
    }

    private static final String transform(final String expr) {
        return expr.replace("if(", "ite(");
    }

    public final List<String> getParameters() {
        return parameters;
    }

    public String getExpression() {
        return expression;
    }

    public final Map<String, Type> getTypes() {
        return types;
    }

    public boolean equals(final Object object) {
        if (!(object instanceof Predicate)) {
            return false;
        }
        final Predicate predicate = (Predicate) object;
        return predicate.expression.equals(expression);
    }

    public int hashCode() {
        return expression.hashCode();
    }

    public enum Type {

        INTEGER, UNKNOWN;

        public static Type parse(final String type) {
            if ("int".equals(type)) {
                return INTEGER;
            }
            return UNKNOWN;
        }
    }
}
