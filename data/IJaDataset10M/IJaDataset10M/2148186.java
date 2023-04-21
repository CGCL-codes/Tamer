package net.sourceforge.eclipsetrader.ats.core.runnables;

import net.sourceforge.eclipsetrader.ats.core.IComponent;
import net.sourceforge.eclipsetrader.ats.core.IComponentContext;
import net.sourceforge.eclipsetrader.ats.core.internal.ComponentContext;
import net.sourceforge.eclipsetrader.core.db.Security;

public class ComponentRunnable {

    StrategyRunnable parent;

    IComponent component;

    Security security;

    IComponentContext context;

    public ComponentRunnable(StrategyRunnable parent, Security security, IComponent component) {
        this.parent = parent;
        this.security = security;
        this.component = component;
    }

    public void dispose() {
    }

    public synchronized void start() {
        context = new ComponentContext(getSecurity(), getParent().getMarketManager().getBars(getSecurity()), getParent().getMarketManager(), getParent().getParent().getExecutionManager());
        component.start(context);
    }

    public synchronized void stop() {
        component.stop(context);
    }

    public IComponent getComponent() {
        return component;
    }

    public IComponentContext getContext() {
        return context;
    }

    public StrategyRunnable getParent() {
        return parent;
    }

    public Security getSecurity() {
        return security;
    }
}
