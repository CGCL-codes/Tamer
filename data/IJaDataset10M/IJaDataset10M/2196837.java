package org.mcisb.ui.util;

import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public abstract class ParameterPanel extends BorderedPanel implements Disposable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public static final String VALID = "VALID";

    /**
	 * 
	 */
    public static final int DEFAULT_COLUMNS = 20;

    /**
	 * 
	 */
    private boolean validated = false;

    /**
	 * 
	 * @param title
	 */
    public ParameterPanel(String title) {
        super(title);
    }

    /**
	 * 
	 * @param validated
	 */
    public void setValid(boolean validated) {
        final boolean oldValidated = this.validated;
        this.validated = validated;
        firePropertyChange(VALID, oldValidated, validated);
    }

    /**
	 * 
	 * @return boolean
	 */
    public boolean isValidated() {
        return validated;
    }
}
