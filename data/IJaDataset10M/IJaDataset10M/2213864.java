package net.sourceforge.chimeralibrary.ui.ioc.factorybean;

import java.awt.Component;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sourceforge.chimeralibrary.oxm.annotation.Name;

/**
 * JMenu factory bean.
 * 
 * @author Christian Cruz
 * @version 1.0.000
 * @since 1.0.000
 */
@Name("jmenu")
public class JMenuFactoryBean extends AbstractMenuItemFactoryBean<JMenu> {

    private List<ComponentEntry> menus;

    public JMenuFactoryBean() {
        super(new JMenu());
    }

    @Override
    public void createObject() {
        if (menus != null) {
            for (final ComponentEntry entry : menus) {
                final Component component = entry.getComponent();
                if (component == null) {
                    continue;
                }
                if (component instanceof JMenuItem) {
                    object.add((JMenuItem) component);
                } else if (component instanceof Separator) {
                    object.addSeparator();
                }
            }
        }
        super.createObject();
    }

    /**
	 * Returns the menu components added in this menu.
	 * 
	 * @return the menu components added in this menu
	 */
    public List<ComponentEntry> getMenus() {
        return menus;
    }

    /**
	 * Sets the menu components added in this menu.
	 * 
	 * @param menus the menu components added in this menu
	 */
    public void setMenus(final List<ComponentEntry> menus) {
        this.menus = menus;
    }
}
