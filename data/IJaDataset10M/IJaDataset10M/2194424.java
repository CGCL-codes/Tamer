package org.columba.core.context.semantic.api;

import java.util.EventListener;

public interface IContextListener extends EventListener {

    public void contextChanged(IContextEvent event);
}
