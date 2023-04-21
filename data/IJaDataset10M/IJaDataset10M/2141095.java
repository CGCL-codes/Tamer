package acide.gui.menuBar.configurationMenu.fileEditor.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import acide.factory.gui.AcideGUIFactory;

/**																
 * ACIDE - A Configurable IDE configure file editor display options menu item listener.											
 *					
 * @version 0.8
 * @see ActionListener																														
 */
public class AcideFileEditorDisplayOptionsMenuItemListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        AcideGUIFactory.getInstance().buildAcideFileEditorDisplayOptionsWindow();
    }
}
