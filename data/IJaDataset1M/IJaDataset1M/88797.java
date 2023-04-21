package net.sf.mvc.prototype.view;

import net.sf.mvc.prototype.controller.InputController;
import net.sf.mvc.prototype.controller.OutputController;
import net.sf.mvc.prototype.model.Model;

/**
 * 
 * An {@code InputOutputView} is a view that combines both
 * {@link InputOutputView} and {@link OutputView}. A typical Swing- or AWT-frame would
 * be an implementation of {@code InputOutputView}.
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-11-01
 * 
 * @param <D>
 *            type of {@link net.sf.mvc.prototype.controller.InputController
 *            InputController}
 */
public interface InputOutputView<D extends InputController<? extends Model<? extends OutputController<? extends OutputView>>>> extends InputView<D>, OutputView {
}
