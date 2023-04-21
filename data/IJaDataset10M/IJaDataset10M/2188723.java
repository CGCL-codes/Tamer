package ho.module.teamAnalyzer;

import java.awt.event.KeyEvent;
import ho.core.model.HOVerwaltung;
import ho.core.module.DefaultModule;
import ho.module.teamAnalyzer.ui.TeamAnalyzerPanel;
import ho.module.teamAnalyzer.ui.component.SettingPanel;
import ho.module.teamAnalyzer.ui.component.TAMenu;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public final class TeamAnalyzerModule extends DefaultModule {

    public TeamAnalyzerModule() {
        super(true);
    }

    @Override
    public int getModuleId() {
        return TEAMANALYZER;
    }

    @Override
    public String getDescription() {
        return HOVerwaltung.instance().getLanguageString("TeamAnalyzer");
    }

    @Override
    public JPanel createTabPanel() {
        return new TeamAnalyzerPanel();
    }

    @Override
    public boolean hasConfigPanel() {
        return true;
    }

    @Override
    public JPanel createConfigPanel() {
        return new SettingPanel();
    }

    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
    }

    @Override
    public boolean hasMenu() {
        return true;
    }

    @Override
    public JMenu getMenu() {
        return new TAMenu();
    }
}
