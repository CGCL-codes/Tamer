package gumbo.app.awt.widget.impl;

import gumbo.app.awt.widget.AwtContainerWidget;
import gumbo.app.awt.widget.AwtWidget;
import gumbo.app.awt.widget.AwtWidgetManager;
import gumbo.app.widget.impl.WidgetImpl;
import gumbo.core.proxy.GenericProxyManager;
import java.awt.Component;

/**
 * Base class for all AwtWidget. Assures that instances use the
 * AwtWidgetManager.
 * @author jonb
 * @param <W> The native widget peer type.
 */
public class AwtWidgetImpl<W extends Component> extends WidgetImpl<Component, W> implements AwtWidget<W> {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates an instance, with no peer (i.e. none or late bind).
	 */
    public AwtWidgetImpl() {
        super(AwtWidgetManager.getInstance());
    }

    /**
	 * Creates an instance, with the specified peer.
	 * @param peer Shared exposed peer object. Never null.
	 */
    public AwtWidgetImpl(W peer) {
        super(AwtWidgetManager.getInstance(), peer);
    }

    @Override
    protected final void addProxy(GenericProxyManager<?, ?> manager) {
        ((AwtWidgetManager) manager).addProxy(this);
    }

    @Override
    protected final void removeProxy(GenericProxyManager<?, ?> manager) {
        ((AwtWidgetManager) manager).removeProxy(this);
    }

    @Override
    public AwtContainerWidget<?> getParent() {
        return (AwtContainerWidget<?>) super.getParent();
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        getPeer().setEnabled(isEnabled());
    }
}
