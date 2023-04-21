package org.exist.eclipse.xquery.ui.context;

/**
 * With this interface, you can registered a {@link IContextSwitcher} on the
 * singleton instance {@link ContextSwitcherRegistration}. After that, if you
 * want to switch the context in the xquery editor, then this registered context
 * will show as choice.
 * 
 * @see ContextSwitcherRegistration
 * @author Pascal Schmidiger
 * 
 */
public interface IContextSwitcher {

    /**
	 * @return the name of the context, which is shown as choice in the context
	 *         switch.
	 */
    public String getName();

    /**
	 * @return the default {@link IConnectionContext}.
	 */
    public IConnectionContext getDefault();
}
