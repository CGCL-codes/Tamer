package com.ibm.celldt.environment.wizard;

import java.util.Map;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Ricardo M. Matinata
 * @since 1.1
 */
public abstract class AbstractEnvironmentDialogPage extends WizardPage {

    public AbstractEnvironmentDialogPage(String pageName) {
        super(pageName);
    }

    public abstract void createControl(Composite parent);

    public final boolean canFinish() {
        String name = getName();
        if (name != null) {
            if (!name.equals("")) {
                return isValid();
            }
        }
        return false;
    }

    /**
	 * Should return the configuration's map derived from populating this own
	 * page. null is not allowed.
	 * 
	 * @return the configuration attributes map
	 */
    public abstract Map getAttributes();

    /**
	 * Provides this configuration instance key name.
	 * 
	 * @returns the configuration's name
	 */
    public abstract String getName();

    /**
	 * Returns weather this current page information represents a valid state of
	 * configuration.
	 * 
	 * @return true if valid, false otherwise.
	 */
    public abstract boolean isValid();
}
