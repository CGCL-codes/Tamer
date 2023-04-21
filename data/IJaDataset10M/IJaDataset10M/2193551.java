package net.sf.mvc.prototype.model;

import java.util.ArrayList;
import java.util.Collection;
import net.sf.mvc.prototype.controller.InputController;
import net.sf.mvc.prototype.controller.OutputController;
import net.sf.mvc.prototype.view.OutputView;

/**
 * 
 * 
 * A prototype implementation for {@link Model}.
 * 
 * <p>
 * <b>Note:</b>{@code AbstractModel} is thread save.
 * </p>
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-11-01
 * 
 * @param <D>
 *            type of {@link InputController}
 * @param <C>
 *            type of {@link OutputController}
 */
public abstract class AbstractModel<D extends InputController<? extends Model<C>>, C extends OutputController<? extends OutputView>> implements Model<C> {

    /**
	 * {@link InputController} that controls this {@code AbstractModel}.
	 */
    protected final D cIn;

    /**
	 * {@link OutputController}'s that are registered to this
	 * {@link AbstractModel}
	 */
    protected final Collection<C> cOut = new ArrayList<C>();

    /**
	 * 
	 * 
	 * Create a new {@link AbstractModel}.
	 * 
	 * @param controller
	 *            {@link InputController} that controls this
	 *            {@code AbstractModel}
	 */
    public AbstractModel(D controller) {
        synchronized (cOut.getClass()) {
            this.cIn = controller;
        }
    }

    /**
	 * 
	 */
    public void addControllerOut(C control) {
        synchronized (cOut) {
            this.cOut.add(control);
        }
    }

    /**
	 * 
	 */
    public void removeControllerOut(C control) {
        synchronized (cOut) {
            this.cOut.remove(control);
        }
    }
}
