package br.usp.iterador.logic;

import org.apache.log4j.Logger;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;
import br.usp.iterador.logic.ioc.Context;

/**
 * An object responsible for executing actions.
 */
public class DefaultActionHandler implements ActionHandler {

    private final Map<String, Class<? extends PulgaAction>> actions = new HashMap<String, Class<? extends PulgaAction>>();

    private static final Logger LOG = Logger.getLogger(DefaultActionHandler.class);

    private final Context context;

    public DefaultActionHandler(Context context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public void executeAction(String cmd) {
        if (!actions.containsKey(cmd)) {
            LOG.error("Action not found: " + cmd);
            return;
        }
        Class<? extends PulgaAction> type = actions.get(cmd);
        LOG.debug("Trying to execute action " + cmd);
        try {
            PulgaAction obj = context.newInstance(type);
            for (Method m : type.getMethods()) {
                if (m.getName().equals("execute")) {
                    Class<?>[] types = m.getParameterTypes();
                    Object[] values = new Object[types.length];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = context.get(types[i]);
                        if (values[i] == null) {
                            throw new RuntimeException("Unable to find value for type " + types[i].getName());
                        }
                    }
                    m.invoke(obj, values);
                    break;
                }
            }
        } catch (Exception e) {
            LOG.error("Unable to execute " + cmd, e);
        }
    }

    public void remove(String key) {
        actions.remove(key);
    }

    public void put(String key, Class<? extends PulgaAction> action) {
        actions.put(key, action);
    }
}
