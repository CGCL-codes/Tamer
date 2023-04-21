package org.jlense.uiworks.internal;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.eclipse.core.runtime.IConfigurationElement;
import org.jlense.uiworks.action.IMenuCreator;
import org.jlense.uiworks.workbench.IActionDelegate;
import org.jlense.uiworks.workbench.IWorkbenchWindow;
import org.jlense.uiworks.workbench.IWorkbenchWindowPulldownDelegate;

/**
 * A workbench window pulldown action.  
 */
public class WWinPluginPulldown extends WWinPluginAction {

    private IMenuCreator menuProxy;

    private class MenuProxy implements IMenuCreator {

        private JMenu menu;

        public JMenu getMenu() {
            IWorkbenchWindowPulldownDelegate delegate = getPulldownDelegate();
            if (delegate != null) {
                return delegate.getMenu();
            } else {
                return null;
            }
        }

        public JPopupMenu getPopupMenu() {
            return null;
        }

        public void dispose() {
        }
    }

    ;

    /**
 * WWinPluginPulldown constructor comment.
 * @param actionElement org.eclipse.core.runtime.IConfigurationElement
 * @param runAttribute java.lang.String
 * @param window org.jlense.uiworks.workbench.IWorkbenchWindow
 */
    public WWinPluginPulldown(IConfigurationElement actionElement, String runAttribute, IWorkbenchWindow window) {
        super(actionElement, runAttribute, window);
        menuProxy = new MenuProxy();
        setMenuCreator(menuProxy);
    }

    /**
 * Creates an instance of the delegate class.
 */
    protected IActionDelegate createDelegate() {
        IActionDelegate delegate = super.createDelegate();
        if (delegate instanceof IWorkbenchWindowPulldownDelegate) {
            return delegate;
        } else {
            WorkbenchPlugin.log("Action should implement IWorkbenchWindowPluginDelegate: " + getText());
            return null;
        }
    }

    /**
 * Returns the pulldown delegate.  If it does not exist it is created.
 */
    protected IWorkbenchWindowPulldownDelegate getPulldownDelegate() {
        IActionDelegate delegate = getDelegate();
        if (delegate == null) {
            delegate = createDelegate();
            setDelegate(delegate);
        }
        return (IWorkbenchWindowPulldownDelegate) delegate;
    }
}
