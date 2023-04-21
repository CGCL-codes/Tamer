package org.vikamine.gui.correlationTable;

import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.vikamine.app.BasicComponentManager;
import org.vikamine.app.VIKAMINE;
import org.vikamine.app.plugin.ShownPluginComponentAdapter;
import org.vikamine.app.plugin.SimplePlugin;
import de.d3web.ka.gui.ResourceBundleManager;

/**
 * 
 * @author kaempgen, atzmueller
 * 
 */
public class CorrelationTablePlugin extends JPanel implements SimplePlugin {

    private static final long serialVersionUID = -4843944257623866632L;

    private CorrelationTableController controller;

    private static final String SD_SIMPLE_CORRELATION_TABLE_PLUGIN_TITLE = VIKAMINE.I18N.getString("vikamine.subgroup.simpleCorrelation.tablePanel.title");

    private static final Icon SD_SIMPLE_CORRELATION_TABLE_PLUGIN_ICON = ResourceBundleManager.getInstance().getIcon(BasicComponentManager.class, "resources/images/correlTable.gif");

    private static final String SD_SIMPLE_CORRELATION_TABLE_PLUGIN_MENUBAR_PATH = "views.SimpleCorrelationTable";

    public CorrelationTablePlugin() {
        super();
        initPlugin();
    }

    public void install() {
    }

    public ShownPluginComponentAdapter createShownPluginComponentAdapter() {
        ShownPluginComponentAdapter correlationTablePlugin = new ShownPluginComponentAdapter(this, SD_SIMPLE_CORRELATION_TABLE_PLUGIN_TITLE, SD_SIMPLE_CORRELATION_TABLE_PLUGIN_TITLE, SD_SIMPLE_CORRELATION_TABLE_PLUGIN_ICON, SD_SIMPLE_CORRELATION_TABLE_PLUGIN_MENUBAR_PATH);
        return correlationTablePlugin;
    }

    public void initPlugin() {
        controller = new CorrelationTableController();
        setLayout(new BorderLayout());
        add(controller.getComponent(), BorderLayout.CENTER);
    }

    public CorrelationTableController getController() {
        return controller;
    }
}
