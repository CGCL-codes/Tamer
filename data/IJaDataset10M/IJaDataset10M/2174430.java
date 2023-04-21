package PolicyAlgebra.Plugin;

import PolicyAlgebra.Type.Policy;

/** 
 * Ths inteface defines the actions that any user inteface
 * to the algebra must provide.
 */
public interface Plugin {

    public Policy importPolicy(String resource) throws PluginException;

    public void exportPolicy(String resource);
}
