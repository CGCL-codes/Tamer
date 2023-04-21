package jung.ext.registry;

import jung.ext.actions.ActionFactory;

/**
 * The <code>ActionFactoryDecorator</code> decorates classes know how to
 * access an action factory. These classes are even more advanced than 
 * classes that have access to a mutation factory. The objects that they
 * trade are not mere <code>GraphMutations</code>, but graph mutations in
 * <code>AbstractActions</code> objects. These action objects can for 
 * example be added to Swing <code>JMenu</code>s.  
 * <p>In the case that a mutation factory is replaced by an action factory,
 * replace the existing <code>MutationFactoryDecorator</code> from its own
 * class. The <code>ActionFactory</code> already contains a mutation 
 * factory and one mutation factory should be sufficient. :-)
 * 
 * @author A.C. van Rossum
 *
 */
public interface ActionFactoryDecorator {

    public void setActionFactory(ActionFactory actionFactory);

    public ActionFactory getActionFactory();
}
