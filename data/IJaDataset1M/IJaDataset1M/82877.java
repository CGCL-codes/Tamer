package net.sf.doolin.gui.action.path.statusbar;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import net.sf.doolin.bus.Bus;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.action.path.item.AbstractActionPath;
import net.sf.doolin.gui.action.swing.ActionFactory;
import net.sf.doolin.gui.action.swing.MenuBuilder;

/**
 * Defines a status bar, a component that just lists some components which
 * listen to the application state (usually the {@link Bus}). This component
 * provides a common layout for all the components in the bar.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class GUIStatusBar extends AbstractActionPath {

    /**
	 * Creates a border for the components that are put in the status bar.
	 * 
	 * @return Border to set
	 */
    public static Border createDefaultBorder() {
        return BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    private Border border = createDefaultBorder();

    private List<StatusBarComponent> components = new ArrayList<StatusBarComponent>();

    /**
	 * Gets the border to apply to the components in the status bar.
	 * 
	 * @return Border to set
	 */
    public Border getBorder() {
        return this.border;
    }

    /**
	 * Gets the list of component definitions for this status bar.
	 * 
	 * @return List of component definitions
	 */
    public List<StatusBarComponent> getComponents() {
        return this.components;
    }

    @Override
    public void install(MenuBuilder menuBuilder, ActionFactory actionFactory, ActionContext actionContext) {
        for (StatusBarComponent component : this.components) {
            JComponent itsComponent = component.createComponent(actionContext);
            itsComponent.setBorder(this.border);
            menuBuilder.add(itsComponent);
        }
    }

    /**
	 * Sets the border to apply to the components in the status bar. It is set
	 * by default to {@link #createDefaultBorder()}.
	 * 
	 * @param border
	 *            Border to set
	 */
    public void setBorder(Border border) {
        this.border = border;
    }

    /**
	 * Sets the list of component definitions for this status bar. It is set by
	 * default to an empty list.
	 * 
	 * @param components
	 *            List of component definitions
	 */
    public void setComponents(List<StatusBarComponent> components) {
        this.components = components;
    }
}
