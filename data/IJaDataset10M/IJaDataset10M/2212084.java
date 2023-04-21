package de.jlab.ui.modules.runs;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComponent;
import de.jlab.boards.Board;
import de.jlab.lab.Lab;
import de.jlab.ui.modules.UILabModule;
import de.jlab.ui.valuewatch.ValueWatch;
import de.jlab.ui.valuewatch.ValueWatchManager;

public class RunDefinitionImportUIModule implements UILabModule {

    Lab theLab = null;

    public void close(Component comp) {
    }

    public JComponent createLabComponent() {
        RunDefinitionImportMainPanel curveMainPanel = new RunDefinitionImportMainPanel(theLab);
        return curveMainPanel;
    }

    public Board getBoard() {
        return null;
    }

    public String getId() {
        return "RunDefinition Import";
    }

    public String getMenuPath() {
        return "Runs";
    }

    public HashMap<String, String> getParametersForUIComponent(Component comp) {
        return null;
    }

    public List<ValueWatch> getValueWatches() {
        return null;
    }

    public void init(Lab lab, Board aBoard, ValueWatchManager vwManager) {
        this.theLab = lab;
    }

    public void setParametersForUIComponent(Component comp, HashMap<String, String> parameters) {
    }

    public void sleep(Component comp) {
    }

    public void wakeup(Component comp) {
    }
}
