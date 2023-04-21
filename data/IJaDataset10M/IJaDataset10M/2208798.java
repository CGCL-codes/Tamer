package pi.eclipse.cle.util;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public abstract class AbstractWidget extends Composite {

    private final Collection<WidgetListener> listeners = new ArrayList();

    /**
	 * @param container
	 * @param parent
	 */
    public AbstractWidget(Composite parent, int style) {
        super(parent, style);
    }

    /**
	 * @param l
	 */
    public final void addWidgetListener(WidgetListener l) {
        if (!this.listeners.contains(l)) {
            this.listeners.add(l);
        }
    }

    /**
	 * @param status
	 */
    public final void fireWidgetModified(String status) {
        for (final WidgetListener l : this.listeners) {
            l.updateContainer(status);
        }
    }

    /**
	 * @param l
	 */
    public final void removeStatusListener(WidgetListener l) {
        this.listeners.remove(l);
    }

    /**
	 * 
	 */
    public abstract void updateWidgetContainer();
}
