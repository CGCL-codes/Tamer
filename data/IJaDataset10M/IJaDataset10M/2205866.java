package org.opensourcephysics.tools;

import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;

/**
 * A FunctionEditor for initial values.
 *
 * @author Douglas Brown
 */
public class InitialValueEditor extends ParamEditor {

    /**
	 * Default constructor
	 */
    public InitialValueEditor(ParamEditor editor) {
        super();
        paramEditor = editor;
        functionPanel = editor.functionPanel;
    }

    /**
	 * Determines if an object's name is editable.
	 * 
	 * @param obj the object
	 * @return always false
	 */
    public boolean isNameEditable(Object obj) {
        return false;
    }

    /**
	 * Override getPreferredSize().
	 * 
	 * @return the table size plus button and instruction heights
	 */
    public Dimension getPreferredSize() {
        return table.getPreferredSize();
    }

    /**
	 * Evaluates all current objects.
	 */
    public void evaluateAll() {
        super.evaluateAll();
        if (paramValues.length != objects.size()) {
            paramValues = new double[objects.size()];
        }
        List params = paramEditor.getObjects();
        for (int i = 0; i < evaluate.size(); i++) {
            Parameter p = (Parameter) evaluate.get(i);
            p.evaluate(params);
        }
        for (int i = 0; i < objects.size(); i++) {
            Parameter p = (Parameter) objects.get(i);
            paramValues[i] = p.getValue();
        }
    }

    /**
   * Creates the GUI.
   */
    protected void createGUI() {
        super.createGUI();
        remove(buttonPanel);
    }

    /**
   * Refreshes the GUI.
   */
    protected void refreshGUI() {
        super.refreshGUI();
        setBorder(BorderFactory.createTitledBorder(ToolsRes.getString("InitialValueEditor.Border.Title")));
    }

    /**
   * Returns a String with the names of variables available for expressions.
   * Only parameter names are available to initial values.
   */
    protected String getVariablesString() {
        StringBuffer vars = new StringBuffer(" ");
        int init = vars.length();
        boolean firstItem = true;
        String[] paramNames = paramEditor.getNames();
        for (int i = 0; i < paramNames.length; i++) {
            if (!firstItem) vars.append(", ");
            vars.append(paramNames[i]);
            firstItem = false;
        }
        if (vars.length() == init) return ToolsRes.getString("FunctionPanel.Instructions.Help");
        return ToolsRes.getString("FunctionPanel.Instructions.ValueCell") + ":" + vars.toString();
    }
}
