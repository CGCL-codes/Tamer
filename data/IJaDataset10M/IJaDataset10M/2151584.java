package org.eclipse.ui.help;

import java.util.ArrayList;
import org.eclipse.core.runtime.Assert;
import org.eclipse.help.IContext;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.widgets.Control;

/**
 * For determining the help context for controls in a dialog page.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * @deprecated nested contexts are no longer supported by the help support system
 */
public class DialogPageContextComputer implements IContextComputer {

    private IDialogPage page;

    private ArrayList contextList;

    private Object context;

    /**
     * Creates a new context computer for the given dialog page and help context.
     *
     * @param page the dialog page
     * @param helpContext a single help context id (type <code>String</code>) or
     *  help context object (type <code>IContext</code>)
     */
    public DialogPageContextComputer(IDialogPage page, Object helpContext) {
        Assert.isTrue(helpContext instanceof String || helpContext instanceof IContext);
        this.page = page;
        context = helpContext;
    }

    /**
     * Add the contexts to the context list.
     *
     * @param object the contexts (<code>Object[]</code> or <code>IContextComputer</code>)
     * @param event the help event 
     */
    private void addContexts(Object object, HelpEvent event) {
        Assert.isTrue(object instanceof Object[] || object instanceof IContextComputer || object instanceof String);
        if (object instanceof String) {
            contextList.add(object);
            return;
        }
        Object[] contexts;
        if (object instanceof IContextComputer) {
            contexts = ((IContextComputer) object).getLocalContexts(event);
        } else {
            contexts = (Object[]) object;
        }
        for (int i = 0; i < contexts.length; i++) {
            contextList.add(contexts[i]);
        }
    }

    /**
     * Add the contexts for the given control to the context list.
     *
     * @param control the control from which to obtain the contexts
     * @param event the help event 
     */
    private void addContextsForControl(Control control, HelpEvent event) {
        Object object = WorkbenchHelp.getHelp(control);
        if (object == null || object == this) {
            return;
        }
        addContexts(object, event);
    }

    public Object[] computeContexts(HelpEvent event) {
        contextList = new ArrayList();
        contextList.add(context);
        addContextsForControl(page.getControl(), event);
        addContextsForControl(page.getControl().getShell(), event);
        return contextList.toArray();
    }

    public Object[] getLocalContexts(HelpEvent event) {
        return new Object[] { context };
    }
}
