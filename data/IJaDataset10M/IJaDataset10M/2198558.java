package org.oxbow.form;

import java.io.Serializable;
import javax.swing.JComponent;
import org.oxbow.form.decorator.IFormDecorator;
import org.oxbow.form.validation.IFormValidationListener;

public interface IFormController<T extends Serializable> {

    /**
	 * Controller's Edit mode
	 */
    public enum Mode {

        INSERT, UPDATE
    }

    public enum InitializationMode {

        SINGLE, MULTIPLE
    }

    /**
	 * Returns component used as a view
	 * @return
	 */
    JComponent getView();

    /**
	 * Returns decorated view with ability show validation and progress visuals
	 * @return
	 */
    JComponent getDecoratedView();

    IFormDecorator getFormDecorator();

    InitializationMode getInitializationMode();

    void setInitializationMode(InitializationMode mode);

    /**
	 * Initialization step #1. Executed on the EDT
	 */
    void preInit();

    /**
	 * Initialization step #2. Executed in separate background thread
	 * immediately after step #1. This method should be used for
	 * time consuming initialization such loading data. Progress animation is usually shown
	 * during execution of this method.
	 */
    void init();

    /**
	 * Initialization step #3. Executed on the EDT immediately after step #2 is done.
	 * Usually used to perform additional initialization after long running background process
	 */
    void postInit();

    void addValidationListener(IFormValidationListener<T> listener);

    void removeValidationListener(IFormValidationListener<T> listener);

    T getModel();

    void setModel(T model);
}
