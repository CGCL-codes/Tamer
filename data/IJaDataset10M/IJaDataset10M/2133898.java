package net.dadajax.gui.swing;

import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import net.dadajax.xml.XmlSwingMenuBuilder;

/**
 * @author dadajax
 *
 */
public class SwingJMenuBarBuilder implements SwingMenuBuilder {

    private JMenuBar menuBar;

    private static final String FILENAME = "./res/menu.xml";

    public SwingJMenuBarBuilder(ActionListener listener) {
        XmlSwingMenuBuilder xml = new XmlSwingMenuBuilder(FILENAME, listener);
        menuBar = xml.getMenuBar();
    }

    @Override
    public JMenuBar getJMenuBar() {
        return menuBar;
    }
}
