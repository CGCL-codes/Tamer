package com.w20e.socrates.rendering;

import java.util.Map;
import com.w20e.socrates.expression.XObject;
import com.w20e.socrates.rendering.Control;

/**
 * @author D.A.Dokter A base control.
 */
public abstract class ControlImpl extends RenderableImpl implements Control {

    /**
	 * Hold help.
	 */
    private String help = "";

    /**
	 * Alert in case of errors.
	 */
    private String alert = "";

    /**
	 * Hold the control type.
	 */
    private String type;

    /**
	 * Provide the bind expression glue.
	 */
    private String bind = "";

    /**
	 * Constructor taking a unique id.
	 * @param newId
	 */
    public ControlImpl(String id) {
        super(id);
    }

    /**
	 * Get the bind string.
	 * 
	 * @return bind expression
	 */
    public final String getBind() {
        return this.bind;
    }

    /**
	 * Set the bind expression.
	 * 
	 * @param newBind
	 *            bind expression
	 */
    public final void setBind(final String newBind) {
        this.bind = newBind;
    }

    /**
	 * @return Returns the alert.
	 */
    public final String getAlert() {
        return this.alert;
    }

    /**
	 * @param newAlert
	 *            The alert to set.
	 */
    public final void setAlert(final String newAlert) {
        this.alert = newAlert;
    }

    /**
	 * Get the set help text.
	 * 
	 * @return Returns the help.
	 */
    public final String getHelp() {
        return this.help;
    }

    /**
	 * Set the help text for this item.
	 * 
	 * @param newHelp
	 *            The help to set.
	 */
    public final void setHelp(final String newHelp) {
        this.help = newHelp;
    }

    /**
	 * Set the control's type.
	 * 
	 * @param t
	 *            Type for this control.
	 */
    public final void setType(String t) {
        this.type = t;
    }

    /**
	 * Return the control's type.
	 */
    public final String getType() {
        return this.type;
    }

    /**
	 * All controls should implement their way of processing input.
	 */
    public abstract XObject processInput(Map<String, Object> data, Class datatype);
}
