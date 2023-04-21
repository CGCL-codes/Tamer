package net.sourceforge.ondex.workflow2;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.sourceforge.ondex.workflow2.support.AbstractWorkerElement;
import net.sourceforge.ondex.workflow2.support.ResourcePool;

/**
 * 
 * @author lysenkoa
 *
 */
public class StandardWorkerElement extends AbstractWorkerElement {

    private static final Object[] NOARGS = new Object[0];

    private Method m;

    public StandardWorkerElement(Method targetFunction, String uniqueTypeId) {
        this.uniqueTypeId = uniqueTypeId;
        this.m = targetFunction;
    }

    public StandardWorkerElement(Class<?> c, String methodName, String uniqueTypeId) {
        this.uniqueTypeId = uniqueTypeId;
        for (Method m : c.getMethods()) {
            if (m.getName().equals(methodName)) {
                this.m = m;
                return;
            }
        }
        throw new IllegalArgumentException("No method with name '" + methodName + "' found in class " + c.getCanonicalName());
    }

    @Override
    public UUID[] execute(ResourcePool rp) throws Exception {
        UUID[] result = super.execute(rp);
        if (result.length != 0) {
            if (m.getParameterTypes().length == 0) {
                rp.addResource(result[0], m.invoke(null, NOARGS));
            } else {
                Object[] res = getResources(rp);
                System.err.println(m);
                rp.addResource(result[0], m.invoke(null, res));
            }
        } else {
            if (m.getParameterTypes().length == 0) {
                m.invoke(null, NOARGS);
            } else {
                m.invoke(null, getResources(rp));
            }
        }
        return result;
    }

    public String getType() {
        return "function";
    }

    public Map<String, String> getDefinition() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("class", m.getDeclaringClass().getCanonicalName());
        result.put("method", m.getName());
        Class<?>[] args = m.getParameterTypes();
        String strArgs = "";
        for (Class<?> arg : args) {
            if (!strArgs.equals("")) {
                strArgs = strArgs + ", " + arg.getCanonicalName();
            } else {
                strArgs = arg.getCanonicalName();
            }
        }
        result.put("methodArgs", strArgs);
        return result;
    }
}
