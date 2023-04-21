package orbe.gui.main;

import net.sf.doolin.bus.Bus;
import net.sf.doolin.context.Application;
import net.sf.doolin.gui.service.ConfirmResult;
import orbe.gui.IActions;
import orbe.gui.IStrings;
import orbe.gui.context.OrbeContext;
import orbe.gui.message.OrbeMessageContextChanged;

/**
 * Action that interacts with the context.
 * 
 * @author Damien Coraboeuf
 */
public abstract class AbstractActionContext extends AbstractActionMain {

    protected boolean closeCurrent() {
        OrbeContext current = getContext();
        if (current != null) {
            return tryToCloseCurrent();
        } else {
            return true;
        }
    }

    protected boolean doClose() {
        setContext(null);
        return true;
    }

    protected void openContext(OrbeContext ctx) {
        if (getContext() != null) {
            throw new IllegalStateException("The previous context has not been closed.");
        } else {
            setContext(ctx);
        }
    }

    protected void setContext(OrbeContext ctx) {
        getView().setViewData(ctx);
        Bus.get().publish(new OrbeMessageContextChanged(ctx));
    }

    protected boolean tryToCloseCurrent() {
        OrbeContext context = getContext();
        if (context.isDirty()) {
            ConfirmResult result = GUIUtils.getAlertManager().confirmSave(getView(), IStrings.ACTION_CLOSE_PROMPT);
            if (result == ConfirmResult.SAVE_AND_CLOSE) {
                ActionSave action = (ActionSave) Application.getApplication().getBean(IActions.ACTION_SAVE);
                getView().execute(action);
                if (context.isDirty()) {
                    return false;
                } else {
                    return doClose();
                }
            } else if (result == ConfirmResult.CLOSE) {
                return doClose();
            } else {
                return false;
            }
        } else {
            return doClose();
        }
    }
}
