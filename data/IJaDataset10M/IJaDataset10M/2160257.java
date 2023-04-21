package graphlab.gui.plugins.developeplugin;

import graphlab.gui.plugins.main.help.Utils;
import graphlab.main.plugin.PluginInterface;
import graphlab.main.core.BlackBoard.BlackBoard;
import graphlab.ui.UI;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * 
 * @author reza
 */
public class Init implements PluginInterface {

    public static final String filter = "graphlab/gui/plugins/developeplugin/content";

    public void init(BlackBoard blackboard) {
        Utils.registerHelpPlugin(blackboard, "developeplugin", "GraphLab's Developer Guide", filter);
        UI ui = (UI) blackboard.get(UI.name);
        try {
            ui.addXML("/graphlab/gui/plugins/developeplugin/config.xml", getClass());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("xml file was not found , or IO error");
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
