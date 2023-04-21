package sol.admin.systemmanagement.export;

import java.awt.Component;
import javax.swing.JMenuItem;

/**
 * all export filters must implement this interface
 */
public interface GuiExportFilter {

    public boolean editPropertiesGui(Component parent);

    public JMenuItem getMenuItem();
}
