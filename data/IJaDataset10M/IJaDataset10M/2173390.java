package net.sf.sasl.aop.common.grammar.placeholder.resolver;

import java.lang.reflect.Method;
import java.util.List;
import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;

/**
 * Simple delegator class to paranamer implementations which tries to resolve
 * parameter names over different paranamer resolve strategies until one
 * strategie succeeds. The class does basically the same as
 * {@link com.thoughtworks.paranamer.AdaptiveParanamer AdaptiveParanamer}. If
 * all resolvers fail than the default parameter table will be of the form
 * "arg0", "arg1", ...
 * 
 * @author Philipp Förmer
 * @since 0.0.1
 * @see {@link http://paranamer.codehaus.org/}
 * 
 */
public class ParanamerChainDelegator implements IParameterNameLoader {

    /**
	 * List of paranamer implementations to be used for resolution.
	 * 
	 * @since 0.0.1
	 */
    private List<Paranamer> chainList;

    /**
	 * Returns the list of paranamer implementations which will be used for
	 * resolution of parameter names.
	 * 
	 * @return null or non null
	 * @since 0.0.1
	 */
    public List<Paranamer> getChainList() {
        return chainList;
    }

    /**
	 * Sets a list of paranamer implementations which will be used for
	 * resolution of parameter names. For resolution list element n has a higher
	 * priority as list element n+1. That means if list element n resolves
	 * parameter names for a method successfully, than list element n+1 will be
	 * never called for parameter name resolution for that method.
	 * 
	 * @param chainList
	 *            null or non null
	 * @since 0.0.1
	 */
    public void setChainList(List<Paranamer> chainList) {
        this.chainList = chainList;
    }

    /**
	 * @see {@link philipp.aop.logging.parameter.IParameterNameLoader}
	 * @since 0.0.1
	 */
    public String[] loadParameterTable(Method method) throws IllegalArgumentException {
        if (method == null) {
            throw new IllegalArgumentException("Parameter method must be non null!");
        }
        if (chainList != null) {
            for (Paranamer resolver : chainList) {
                try {
                    String[] table = resolver.lookupParameterNames(method);
                    if (table != null) {
                        return table;
                    }
                } catch (ParameterNamesNotFoundException exception) {
                }
            }
        }
        int parameterCount = method.getParameterTypes().length;
        String[] table = new String[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            table[i] = "arg" + i;
        }
        return table;
    }
}
