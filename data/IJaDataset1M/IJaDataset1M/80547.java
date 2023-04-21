package org.gvsig.gui.beans.panelGroup.exceptions;

import java.util.HashMap;
import java.util.Map;
import org.gvsig.exceptions.BaseException;
import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;

/**
 * <p>If an object of type {@link IPanelGroup IPanelGroup} tries to load a panel that its preferred sized
 *  hasn't been initialized, (not the default preferred size), then an exception of this kind will be
 *  launched.</p>
 * 
 * @version 28/11/2007
 * @author Pablo Piqueras Bartolom� (pablo.piqueras@iver.es) 
 */
public class PanelWithNoPreferredSizeDefinedException extends BaseException {

    private static final long serialVersionUID = 3953831724578904518L;

    protected HashMap<String, String> values;

    public static final String PANEL_LABEL = "PANEL_LABEL";

    /**
	 * <p>Creates an initializes a new instance of <code>PanelWithNoPreferredSizedDefinedException</code>.</p>
	 */
    public PanelWithNoPreferredSizeDefinedException() {
        super();
        initialize();
    }

    /**
	 * <p>Creates an initializes a new instance of <code>PanelWithNoPreferredSizedDefinedException</code>.</p>
	 * 
	 * @param panelLabel label of the panel which is the source of this exception
	 */
    public PanelWithNoPreferredSizeDefinedException(String panelLabel) {
        super();
        initialize();
        setPanelLabel(panelLabel);
    }

    /**
	 * <p>Initializes a <code>PanelBaseException</code> with the needed information.</p>
	 */
    protected void initialize() {
        this.code = serialVersionUID;
        this.formatString = "Panel with label \"%(" + PANEL_LABEL + ")\" without preferred size defined.";
        this.messageKey = "panel_without_preferred_size_defined_exception";
        values = new HashMap<String, String>();
        values.put(PANEL_LABEL, "");
        setTranslator(new Messages());
    }

    protected Map<String, String> values() {
        return values;
    }

    /**
	 * <p>Gets the label of the panel which is the source of this exception, or
	 *  <code>null</code> if hasn't been defined.</p>
	 * 
	 * @return label of the panel which is the source of this exception
	 */
    public String getPanelLabel() {
        return values.get(PANEL_LABEL);
    }

    /**
	 * <p>Sets the label of the panel which is the source of this exception.</p>
	 * 
	 * @param panelLabel label of the panel which is the source of this exception
	 */
    public void setPanelLabel(String panelLabel) {
        if (panelLabel == null) values.put(PANEL_LABEL, ""); else values.put(PANEL_LABEL, panelLabel);
    }
}
