package graphlab.gui.plugins.manageplugins;

import graphlab.gui.plugins.manageplugins.xml.GlupdateHandlerImpl;
import graphlab.gui.plugins.manageplugins.xml.GlupdateParser;
import graphlab.main.GraphLab;
import graphlab.main.plugin.Plugger;
import graphlab.main.core.Event;
import graphlab.main.core.BlackBoard.BlackBoard;
import graphlab.main.core.action.AbstractAction;
import graphlab.ui.UI;
import java.net.URL;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class Wizard extends AbstractAction {

    public static final Event event = UI.getUIEvent("wizard");

    public Wizard(BlackBoard bb) {
        super(bb);
        this.setEvent(event);
    }

    public void performJob(String name) {
        String s = JOptionPane.showInputDialog("Please Enter the URL :", "http://graphlab.sf.net/update.xml");
        if (s == null) return;
        URL url;
        try {
            url = new URL(s);
            GlupdateHandlerImpl handler = new GlupdateHandlerImpl();
            GlupdateParser.parse(url, handler);
            String preferedMirror = handler.mirrors.values().iterator().next();
            WhichPlugins whichPlugins = new WhichPlugins(blackboard, preferedMirror);
            DefaultListModel list = new DefaultListModel();
            Plugger plugger = ((GraphLab) blackboard.get(GraphLab.name)).plugger;
            for (Plugin plugin : handler.plugins.values()) {
                plugin.oldVersion = plugger.versions.get(plugin.name);
                if ((plugin.oldVersion == null) || (plugin.oldVersion.longValue() < plugin.version)) {
                    list.addElement(plugin);
                }
            }
            whichPlugins.jList1.setModel(list);
            whichPlugins.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
