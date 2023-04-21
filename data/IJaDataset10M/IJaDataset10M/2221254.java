package de.iritgo.openmetix.interfacing.lambrecht;

import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.core.tools.StringTools;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutputEditor;
import org.swixml.SwingEngine;
import javax.swing.JPanel;

/**
 * This gui pane is used to edit Lambrecht system outputs.
 *
 * @version $Id: LambrechtOutputEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class LambrechtOutputEditor extends GagingOutputEditor {

    /** The output code. */
    public ITextField code;

    /**
	 * Create a new LambrechtOutputEditor.
	 */
    public LambrechtOutputEditor() {
        super("LambrechtOutputEditor");
    }

    /**
	 * Create a panel for custom output parameter editing.
	 *
	 * @return The custom parameter panel.
	 */
    protected JPanel createCustomParameterPanel() {
        try {
            SwingEngine swingEngine = new SwingEngine(this);
            swingEngine.setClassLoader(InterfacingLambrechtPlugin.class.getClassLoader());
            return (JPanel) swingEngine.render(getClass().getResource("/swixml/LambrechtOutputEditor.xml"));
        } catch (Exception x) {
            Log.logError("client", "LambrechtOutputEditor.initGUI", x.toString());
        }
        return null;
    }

    /**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
    public GUIPane cloneGUIPane() {
        return new LambrechtOutputEditor();
    }

    /**
	 * Load the configuration properties into the gui.
	 */
    protected void loadFromProperties() {
        if (customConfig.get("code") != null) {
            code.setText(customConfig.get("code").toString());
        }
    }

    /**
	 * Store the gui values to the configuration properties.
	 */
    protected void storeToProperties() {
        if (!StringTools.isTrimEmpty(code.getText())) {
            int codeValue = NumberTools.toInt(code.getText(), 0);
            customConfig.put("code", String.valueOf(Math.min(codeValue, 255)));
        }
    }
}
