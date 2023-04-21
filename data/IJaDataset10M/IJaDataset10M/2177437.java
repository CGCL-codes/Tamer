package gumbo.app.widget.impl;

import gumbo.app.widget.TangibleWidget;
import gumbo.core.proxy.GenericProxyManager;

/**
 * Default base class for TangibleWidget.
 * @author jonb
 * @param <WW> The native widget base type.
 * @param <W> The native widget peer type.
 */
public abstract class TangibleWidgetImpl<WW, W extends WW> extends WidgetImpl<WW, W> implements TangibleWidget<WW, W> {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates an instance, with no peer (i.e. none or late bind).
	 * @param manager Shared exposed proxy manager. Never null.
	 */
    public TangibleWidgetImpl(GenericProxyManager<?, WW> manager) {
        super(manager);
    }

    /**
	 * Creates an instance, with the specified peer.
	 * @param manager Shared exposed proxy manager. Never null.
	 * @param peer Shared exposed peer object. Never null.
	 */
    public TangibleWidgetImpl(GenericProxyManager<?, WW> manager, W peer) {
        super(manager, peer);
    }

    /**
	 * Called by subclasses to set the "updated" status when a data state update
	 * occurs. Does not affect the control state of descendant widgets. Does
	 * nothing if isEnabled() returns false.
	 */
    protected final void setUpdated() {
        if (isEnabled()) {
            _isUpdatedIn = true;
        }
    }

    /**
	 * Called by subclasses to set the "touched" and "updated" status when a
	 * data state touch occurs. Does not affect the control state of descendant
	 * widgets. Does nothing if isEnabledAndTouchable() returns false.
	 */
    protected final void setTouched() {
        if (isEnabledAndTouchable()) {
            _isUpdatedIn = true;
            _isTouchedIn = true;
        }
    }

    @Override
    public final boolean isUpdated() {
        return _isUpdatedOut;
    }

    @Override
    public final boolean isTouched() {
        return _isTouchedOut;
    }

    @Override
    public synchronized void updateOutput() {
        _isUpdatedOut = _isUpdatedIn;
        _isUpdatedIn = false;
        _isTouchedOut = _isTouchedIn;
        _isTouchedIn = false;
    }

    private boolean _isUpdatedIn = false;

    private boolean _isTouchedIn = false;

    private boolean _isUpdatedOut = false;

    private boolean _isTouchedOut = false;
}
