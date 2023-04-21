package org.jowidgets.impl.layout;

import java.util.HashMap;
import java.util.Map;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.layout.ILayouter;

abstract class AbstractCachingLayout implements ILayouter {

    private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

    private static final Dimension EMPTY_SIZE = new Dimension(0, 0);

    private final Map<IControl, Dimension> prefSizes;

    private final Map<IControl, Dimension> minSizes;

    private final ISizeProvider minimum;

    private final ISizeProvider preferred;

    private Dimension preferredSize;

    private Dimension minSize;

    AbstractCachingLayout() {
        this.prefSizes = new HashMap<IControl, Dimension>();
        this.minSizes = new HashMap<IControl, Dimension>();
        this.minimum = new ISizeProvider() {

            @Override
            public Dimension getSize(final IControl control) {
                return getControlMinSize(control);
            }
        };
        this.preferred = new ISizeProvider() {

            @Override
            public Dimension getSize(final IControl control) {
                return getControlPrefSize(control);
            }
        };
    }

    protected ISizeProvider minimumPolicy() {
        return minimum;
    }

    protected ISizeProvider preferredPolicy() {
        return preferred;
    }

    protected abstract Dimension calculateMinSize();

    protected abstract Dimension calculatePreferredSize();

    protected Dimension getControlMinSize(final IControl control) {
        if (control == null) {
            return EMPTY_SIZE;
        }
        Dimension result = minSizes.get(control);
        if (result == null) {
            result = control.getMinSize();
            minSizes.put(control, result);
        }
        return result;
    }

    protected Dimension getControlPrefSize(final IControl control) {
        if (control == null) {
            return EMPTY_SIZE;
        }
        Dimension result = prefSizes.get(control);
        if (result == null) {
            result = control.getPreferredSize();
            prefSizes.put(control, result);
        }
        return result;
    }

    @Override
    public void invalidate() {
        prefSizes.clear();
        minSizes.clear();
        minSize = null;
        preferredSize = null;
    }

    @Override
    public Dimension getMinSize() {
        if (minSize == null) {
            this.minSize = calculateMinSize();
        }
        return minSize;
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            this.preferredSize = calculatePreferredSize();
        }
        return preferredSize;
    }

    @Override
    public Dimension getMaxSize() {
        return MAX_SIZE;
    }

    interface ISizeProvider {

        Dimension getSize(IControl control);
    }
}
