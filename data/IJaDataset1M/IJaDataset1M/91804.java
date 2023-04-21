package com.aurorasoftworks.signal.examples.ui.mvc.lwuit;

import com.aurorasoftworks.signal.runtime.core.context.ContextException;
import com.aurorasoftworks.signal.runtime.core.context.IContext;
import com.aurorasoftworks.signal.runtime.core.context.IContextLoader;
import com.aurorasoftworks.signal.runtime.ui.mvc.lwuit.AbstractApplicationMIDlet;

public class AccountMIDlet extends AbstractApplicationMIDlet {

    public AccountMIDlet() throws Exception {
        super("com.aurorasoftworks.signal.examples.ui.mvc.lwuit." + "ApplicationContext");
        ((IAccountListRequestedEvent) getContext().getBean(IAccountListRequestedEvent.class)).onAccountListRequested();
    }

    protected IContext loadContext(String contextName, IContextLoader loader) throws ContextException {
        return super.loadContext(contextName, loader);
    }
}
