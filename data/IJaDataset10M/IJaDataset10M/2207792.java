package de.schwarzrot.ui.control.model;

import javax.swing.DefaultBoundedRangeModel;
import de.schwarzrot.app.support.ProgressTracker;

/**
 * advances the progressbar slider at each message
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public class ProgressBarModel extends DefaultBoundedRangeModel implements ProgressTracker {

    private static final long serialVersionUID = 713L;

    public ProgressBarModel(int value, int extent, int min, int max) {
        super(value, extent, min, max);
    }

    public final String getMessage() {
        return message;
    }

    @Override
    public final void setMessage(String message) {
        this.message = message;
        setValue(getValue() + 1);
    }

    private String message;
}
