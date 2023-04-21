package sippoint;

import sippoint.data.message.model.IMessage;
import sippoint.framework.module.ModuleContext;

/**
 * @author Martin Hynar
 * 
 */
public abstract class SipStack {

    private ModuleContext context;

    protected void setContext(ModuleContext context) {
        this.context = context;
    }

    public void send(IMessage message) {
        this.context.getTransactionService().handleInTransaction(message);
    }

    public abstract void received(IMessage message);
}
