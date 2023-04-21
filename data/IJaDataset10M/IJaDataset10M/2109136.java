package launcher;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;

/**
 *  A base class for option panes that makes some common tasks easier.
 *
 *  @author		Marcelo Vanzin, Francois Rey
 */
public abstract class LauncherTypeOptionPane extends AbstractOptionPane {

    private static final String OPT_PREFIX = LauncherPlugin.OPT_PREFIX;

    public static final String LABEL_SUFFIX = ".label";

    public static final String TOOLTIP_SUFFIX = ".tooltip";

    public static final String OPT_ADD = OPT_PREFIX + ".add";

    public static final String OPT_DELETE = OPT_PREFIX + ".delete";

    /**
	 * Creates a new option pane.
	 *
	 * @param	name		Name of the pane.
	 */
    public LauncherTypeOptionPane(String name) {
        super(name);
    }

    /**
	 * Adds a component adding an optional label, and setting its tooltip
	 * if one is available. Component is added with the fill property set to
	 * GridBagConstraints.HORIZONTAL.
	 *
	 * @param	comp		Component to add.
	 * @param	prop		Option property name. Label and tooltip derived by
	 *						adding ".label" and ".toolip".
	 */
    protected void addComponent(JComponent comp, String prop) {
        addComponent(comp, prop, GridBagConstraints.HORIZONTAL);
    }

    /**
	 * Adds a component adding an optional label, and setting its tooltip
	 * if one is available, using the given fill property.
	 *
	 * @param	comp		Component to add.
	 * @param	prop		Option property name. Label and tooltip derived by
	 *						adding ".label" and ".toolip".
	 * @param	fill		GridBagConstraints fill property.
	 */
    protected void addComponent(JComponent comp, String prop, int fill) {
        JLabel label = null;
        if (prop != null) {
            String lText = prop(prop + LABEL_SUFFIX);
            if (lText != null) {
                label = new JLabel(lText);
            }
            lText = prop(prop + TOOLTIP_SUFFIX);
            if (lText != null) {
                comp.setToolTipText(lText);
            }
        }
        if (label == null) {
            addComponent(comp, fill);
        } else {
            addComponent(label, comp, fill);
        }
    }

    /**
	 * Shortcut for adding a checkbox with an optional tooltip.
	 *
	 * @param	label		Property name for the option value. Label
	 * 						and tooltip will be derived by adding ".label"
	 * 						and ".tooltip".
	 *
	 * @return The checkbox (already added to the UI and initialized).
	 */
    protected JCheckBox addCheckBox(String prop) {
        JCheckBox cb = new JCheckBox(prop(prop + LABEL_SUFFIX));
        String tooltip = prop(prop + TOOLTIP_SUFFIX);
        if (tooltip != null) {
            cb.setToolTipText(tooltip);
        }
        cb.setSelected(bprop(prop));
        addComponent(cb);
        return cb;
    }

    /**
	 * Creates a button based on the given property name.
	 *
	 * @param	prop	Property name for the text / tooltip.
	 *
	 * @return A new JButton.
	 */
    protected JButton createButton(String prop) {
        JButton button = new JButton(prop(prop + LABEL_SUFFIX));
        String tooltip = prop(prop + TOOLTIP_SUFFIX);
        if (tooltip != null) {
            button.setToolTipText(tooltip);
        }
        return button;
    }

    /**
	 * Creates a label based on the given property name.
	 *
	 * @param	prop	Property name for the text / tooltip.
	 *
	 * @return A new JLabel.
	 */
    protected JLabel createLabel(String prop) {
        JLabel label = new JLabel(prop(prop + LABEL_SUFFIX));
        String tooltip = prop(prop + TOOLTIP_SUFFIX);
        if (tooltip != null) {
            label.setToolTipText(tooltip);
        }
        return label;
    }

    /**
	 * Shortcut for retrieving jEdit properties, avoiding ridiculously long
	 * lines.
	 *
	 * @param	property	Property name.
	 *
	 * @return Property value (may be null).
	 */
    protected static String prop(String property) {
        return jEdit.getProperty(property);
    }

    /**
	 * Shortcut for retrieving jEdit properties, avoiding ridiculously long
	 * lines.
	 *
	 * @param	property	Property name.
	 *
	 * @return Property value.
	 */
    protected static boolean bprop(String property) {
        return jEdit.getBooleanProperty(property);
    }

    protected JComboBox addDefaultLauncherComboBox(LauncherType launcherType) {
        JComboBox launcherChoice = new JComboBox(launcherType.getDefaultLaunchersChoice());
        addComponent(launcherChoice, launcherType.getDefaultLauncherPropertyName());
        launcherChoice.setSelectedItem(launcherType.getDefaultLauncher());
        return launcherChoice;
    }
}
