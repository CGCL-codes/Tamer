package civquest.swing;

import civquest.core.GameDataAccessor;
import civquest.parser.ruleset.Registry;
import civquest.swing.game.AbortToMainWindowAction;
import civquest.swing.panes.managers.ScenarioEditorInfoPaneManager;
import civquest.swing.quadmap.QuadMap;
import civquest.swing.scenario.AddNationAction;
import civquest.swing.scenario.ComputeRandomMapAction;
import java.awt.Point;
import swifu.main.FunctionAction;
import swifu.main.FunctionComponent;
import swifu.main.PositionInfo;

/**
 */
public class ScenarioEditorFunctionComponent implements FunctionComponent {

    private QuadMap quadMap;

    private Registry registry;

    private CivQuest civQuest;

    private ScenarioEditorInfoPaneManager editor;

    public ScenarioEditorFunctionComponent(QuadMap quadMap, CivQuest civQuest, Registry registry, ScenarioEditorInfoPaneManager editor) {
        this.quadMap = quadMap;
        this.registry = registry;
        this.civQuest = civQuest;
        this.editor = editor;
    }

    public void newData(GameDataAccessor gameData) {
        quadMap.newData(gameData);
    }

    public FunctionComponent getSubComponent(String name) {
        if (name.equals("quadmap")) {
            return quadMap;
        }
        return null;
    }

    public FunctionAction getFunctionAction(String name) {
        if (name.equals("abort-to-main-window")) {
            return new AbortToMainWindowAction(civQuest);
        } else if (name.equals("compute-random-map")) {
            return new ComputeRandomMapAction(civQuest);
        } else if (name.equals("add-nation")) {
            return new AddNationAction(this.registry, this.editor);
        } else {
            return null;
        }
    }

    public void beforeEvent(Point point) {
    }

    public void afterEvent(Point point) {
    }

    public PositionInfo getPositionInfo() {
        return null;
    }
}
