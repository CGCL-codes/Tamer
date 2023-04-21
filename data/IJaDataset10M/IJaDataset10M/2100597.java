package net.sf.compositor;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import net.sf.compositor.util.Config;

class RadioButtonGenerator extends Generator {

    RadioButtonGenerator(final App app) {
        super(app, "javax.swing.JRadioButton", false);
    }

    /**
	 * Selects this buttton if requested.
	 */
    @Override
    protected void setAttributes(final App app, final JComponent component, final String windowName, final String componentName, final Config infoMap, final int indent) {
        if ("true".equals(infoMap.getProperty("selected", "false"))) {
            ((JRadioButton) component).setSelected(true);
        }
        if (infoMap.containsKey("action")) {
            final String actionName = infoMap.getProperty("action");
            final JRadioButton button = (JRadioButton) component;
            final Action action = app.getAction(actionName);
            if (null == action) {
                throw new RuntimeException("No action " + actionName + " found for radio button " + windowName + '.' + componentName);
            }
            button.setAction(action);
        }
    }
}
