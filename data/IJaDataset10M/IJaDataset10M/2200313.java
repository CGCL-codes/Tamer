package infinitewisdom.view.widgets;

import java.util.List;
import infinitewisdom.model.GameWorld;
import infinitewisdom.model.agent.PlayerAgentIF;
import infinitewisdom.view.GameWorldView;
import infinitewisdom.view.GameWorldView.GWVState;
import infinitewisdom.view.widgets.mapedit.UnitPalette;
import javax.swing.JPanel;

public abstract class TopPanel extends JPanel {

    public GameWorldView gameWorldView;

    public PropertiesPanel propertiesPanel;

    public UnitPalette unitPalette;

    public TimeControlPanel timeControlPanel;

    public GameWorld gameWorld;

    public TopPanel(GameWorld gw) {
    }

    public abstract GameWorldView getGameWorldView();

    public abstract void dispose();

    public abstract void startGameThread();

    public abstract void stopGameThread();

    public void setEditMode(boolean b) {
        unitPalette.setEnabled(b);
        if (b) {
            timeControlPanel.stop();
            gameWorldView.setState(GWVState.DELEGATED_MAPEDIT);
        }
    }

    public List<PlayerAgentIF> getAgents() {
        return timeControlPanel.getAgents();
    }
}
