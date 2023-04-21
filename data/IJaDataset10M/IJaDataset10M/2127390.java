package hoplugins;

import hoplugins.commons.utils.PluginProperty;
import hoplugins.experienceViewer.Spielereingabe;
import hoplugins.experienceViewer.Spielertabelle;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import plugins.IHOMiniModel;
import plugins.IOfficialPlugin;
import plugins.IPlugin;
import plugins.IRefreshable;

public class ExperienceViewer implements IPlugin, IOfficialPlugin, IRefreshable {

    private IHOMiniModel m_clModel;

    private JPanel m_jpPanel;

    private Spielertabelle spielertabelle;

    private static final String PLUGIN_PACKAGE = "experienceViewer";

    public ExperienceViewer() {
        m_clModel = null;
        m_jpPanel = null;
        spielertabelle = null;
    }

    public double getVersion() {
        return 0.511D;
    }

    public String getName() {
        return "ExperienceViewer";
    }

    public String getPluginName() {
        return getName();
    }

    public int getPluginID() {
        return 16;
    }

    public File[] getUnquenchableFiles() {
        return null;
    }

    public void start(IHOMiniModel arg0) {
        m_clModel = arg0;
        PluginProperty.loadPluginProperties(PLUGIN_PACKAGE);
        m_jpPanel = m_clModel.getGUI().createGrassPanel();
        m_jpPanel.setLayout(new BorderLayout());
        spielertabelle = new Spielertabelle(m_clModel);
        JPanel tabelle = new JPanel();
        tabelle.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(spielertabelle);
        scroll.setSize(1200, 600);
        tabelle.add(scroll, "Center");
        tabelle.setBorder(BorderFactory.createEtchedBorder());
        Spielereingabe eingabemaske = new Spielereingabe();
        JPanel eingabe = new JPanel();
        eingabe.add(eingabemaske);
        JSplitPane sp = new JSplitPane(0);
        sp.setLeftComponent(tabelle);
        sp.setRightComponent(eingabe);
        sp.setOneTouchExpandable(true);
        sp.setContinuousLayout(true);
        m_jpPanel.add(sp);
        String sTabName = getPluginName();
        m_clModel.getGUI().addTab(sTabName, m_jpPanel);
        m_clModel.getGUI().registerRefreshable(this);
        m_clModel.getGUI().addMainFrameListener(spielertabelle.gibWindowClosingAdapter());
    }

    public void refresh() {
        spielertabelle.aktualisieren();
    }
}
