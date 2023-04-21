package net.bioteam.perl5;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * Wrap Perl in a ScriptEngine.
 * Not Implemented.
 * <ui>
 * <li>You can not call back java from perl script.</li>
 * <li>You can only pass primitive value, list of primitive value, map of primitive type as parameter 
 * into Context. If EXCEPITON_FOR_UNKNOW_TYPE is true, exception is thrown if unsupported type is 
 * detected in Context. Otherwise, unsupported type is coerced into String (toString()).
 * </li>
 * <li>return type is always string. If you expect boolean value, empty string and 
 * string "0" represetns false. Anything else is true.
 * </ui>
 * set SCRIPT_DEBUG=true to have generated script dumped to standard output.
 * Please see PerlScriptTest for how this class is used.
 * @author jason
 *
 */
public class Perl5ScriptEngine extends AbstractScriptEngine {

    protected ScriptEngineFactory fac;

    public static boolean SCRIPT_DEBUG = false;

    public static boolean EXCEPITON_FOR_UNKNOW_TYPE = false;

    public Bindings createBindings() {
        return new SimpleBindings();
    }

    public Object eval(String script, ScriptContext context) throws ScriptException {
        String finalScript = createScript(script, context);
        if (SCRIPT_DEBUG) System.out.println(finalScript);
        try {
            return Perl.eval(finalScript);
        } catch (Exception e) {
            throw new ScriptException(e.getMessage());
        }
    }

    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        String script;
        try {
            StringWriter sw = new StringWriter();
            char[] buffer = new char[1024];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                sw.write(buffer, 0, n);
            }
            script = sw.toString();
        } catch (IOException e) {
            throw new ScriptException(e.getMessage());
        }
        return eval(script, context);
    }

    public ScriptEngineFactory getFactory() {
        return fac;
    }

    /**
	 * This is internal class. Here I make it public so that it can be overridden.
	 * @param script
	 * @param context
	 * @return the real script handled to Perl.
	 * @throws ScriptException
	 */
    public String createScript(String script, ScriptContext context) throws ScriptException {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        for (Map.Entry<String, Object> entry : context.getBindings(ScriptContext.ENGINE_SCOPE).entrySet()) {
            if (entry.getValue() == null) {
                sb.append("\tmy $" + entry.getKey() + "=undef;\n");
                continue;
            }
            if (entry.getValue() instanceof Number || entry.getValue() instanceof String) {
                sb.append("\tmy $" + entry.getKey() + "='" + safeValue(entry.getValue()) + "';\n");
            } else if (entry.getValue() instanceof List) {
                List list = (List) entry.getValue();
                Iterator iter = list.iterator();
                boolean first = true;
                sb.append("\tmy @" + entry.getKey() + "=(");
                while (iter.hasNext()) {
                    Object value = iter.next();
                    if (!first) {
                        sb.append(",");
                    } else {
                        first = false;
                    }
                    if (value == null) {
                        sb.append("undef");
                    } else {
                        if (value instanceof Number || value instanceof String) {
                            sb.append("'" + safeValue(value) + "'");
                        } else if (EXCEPITON_FOR_UNKNOW_TYPE) {
                            throw new ScriptException(value.getClass().getName() + " is not supported as list element type");
                        } else {
                            sb.append("'" + safeValue(value) + "'");
                        }
                    }
                }
                sb.append(");\n");
            } else if (entry.getValue() instanceof Map) {
                Map map = (Map) entry.getValue();
                Iterator iter = map.entrySet().iterator();
                boolean first = true;
                sb.append("\tmy %" + entry.getKey() + "=(");
                while (iter.hasNext()) {
                    Map.Entry value = (Map.Entry) iter.next();
                    if (!first) {
                        sb.append(",");
                    } else {
                        first = false;
                    }
                    if (value.getValue() == null) {
                        sb.append("'" + safeValue(value.getKey()) + "'=>undef");
                    } else {
                        if (value.getValue() instanceof Number || value.getValue() instanceof String) {
                            sb.append("'" + safeValue(value.getKey()) + "'=>'" + safeValue(value.getValue()) + "'");
                        } else if (EXCEPITON_FOR_UNKNOW_TYPE) {
                            throw new ScriptException(value.getValue().getClass().getName() + " is not supported as Map value type");
                        } else {
                            sb.append("'" + safeValue(value.getKey()) + "'=>'" + safeValue(value.getValue()) + "'");
                        }
                    }
                }
                sb.append(");\n");
            } else {
                if (EXCEPITON_FOR_UNKNOW_TYPE) {
                    throw new ScriptException(entry.getValue().getClass().getName() + " is not supported as Map value type");
                } else {
                    sb.append("\tmy $" + entry.getKey() + "='" + safeValue(entry.getValue()) + "';\n");
                }
            }
        }
        sb.append("\t#-------script from end user\n");
        sb.append("\t" + script);
        sb.append("\n}\n");
        return sb.toString();
    }

    /**
	 * @param value
	 * @return value with ' character escaped. 
	 */
    protected String safeValue(Object value) {
        return value.toString().replaceAll("'", "\\'");
    }
}
