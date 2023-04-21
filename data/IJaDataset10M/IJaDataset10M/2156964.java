package de.jlab.ui.modules.dcg;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComponent;
import de.jlab.boards.Board;
import de.jlab.boards.DCGBoard;
import de.jlab.config.CTModuleParameterConfig;
import de.jlab.lab.Lab;
import de.jlab.ui.modules.UILabModule;
import de.jlab.ui.modules.panels.GraphicalPanel;
import de.jlab.ui.modules.panels.dcg.DCGGraphicalPanel;
import de.jlab.ui.valuewatch.ValueWatch;
import de.jlab.ui.valuewatch.ValueWatchManager;

public class DcgGraphVoltage implements UILabModule {

    Lab theLab = null;

    Board theBoard = null;

    double maxVoltage = 20;

    public DcgGraphVoltage() {
        super();
    }

    public void close(Component comp) {
        if (comp instanceof DCGGraphicalPanel) theLab.deleteObserver((DCGGraphicalPanel) comp);
    }

    public JComponent createLabComponent() {
        GraphicalPanel dcgPanel = new GraphicalPanel(theLab, theBoard, 0, maxVoltage, "#0.000V", DCGBoard.CHANNEL_ACTUAL_VOLTAGE);
        theLab.addObserver(dcgPanel);
        return dcgPanel;
    }

    public Board getBoard() {
        return theBoard;
    }

    public String getMenuPath() {
        return theBoard.getBoardInstanceIdentifierForMenu() + "/Graphs";
    }

    public String getId() {
        return "Voltage";
    }

    public HashMap<String, String> getParametersForUIComponent(Component comp) {
        return ((GraphicalPanel) comp).getParameters();
    }

    public List<ValueWatch> getValueWatches() {
        List<ValueWatch> watches = new ArrayList<ValueWatch>();
        watches.add(new ValueWatch(DCGBoard.CHANNEL_ACTUAL_VOLTAGE, theBoard));
        return watches;
    }

    public void setParametersForUIComponent(Component comp, HashMap<String, String> parameters) {
        ((GraphicalPanel) comp).setParameters(parameters);
    }

    public void sleep(Component comp) {
    }

    public void wakeup(Component comp) {
    }

    public void init(Lab lab, Board aBoard, ValueWatchManager vwManager) {
        this.theLab = lab;
        this.theBoard = aBoard;
        CTModuleParameterConfig parameters = theLab.getConfig().getCTModuleConfigByTypeAndBoard(DCGBoard.BOARD_IDENTIFIER, theBoard);
        if (parameters != null) {
            String valueOfMaxVoltage = parameters.getParameterByName("maxVoltage");
            if (valueOfMaxVoltage != null) {
                maxVoltage = Double.parseDouble(valueOfMaxVoltage);
            }
        }
    }
}
