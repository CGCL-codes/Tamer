package acide.gui.menuBar.configurationMenu.menuMenu.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import acide.factory.gui.AcideGUIFactory;

/**																
 * ACIDE - A Configurable IDE menu menu modify menu menu item listener.											
 *					
 * @version 0.8
 * @see ActionListener																														
 */
public class AcideModifyMenuMenuItemListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        AcideGUIFactory.getInstance().buildAcideMenuConfigurationWindow(true);
    }
}
