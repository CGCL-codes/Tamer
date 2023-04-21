package de.jlab.ui.modules.div;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComponent;
import de.jlab.boards.Board;
import de.jlab.boards.DIVBoard;
import de.jlab.lab.Lab;
import de.jlab.ui.modules.UILabModule;
import de.jlab.ui.modules.panels.GraphicalPanel;
import de.jlab.ui.modules.panels.div.DIVGraphicalPanel;
import de.jlab.ui.valuewatch.ValueWatch;
import de.jlab.ui.valuewatch.ValueWatchManager;

public class DivGraph implements UILabModule {

    Lab theLab = null;

    Board theBoard = null;

    double maxCurr = 1000;

    public DivGraph() {
        super();
    }

    public void close(Component comp) {
        if (comp instanceof DIVGraphicalPanel) theLab.deleteObserver((DIVGraphicalPanel) comp);
    }

    public JComponent createLabComponent() {
        DIVGraphicalPanel divPanel = new DIVGraphicalPanel(theLab, theBoard, "#0.00");
        theLab.addObserver(divPanel);
        return divPanel;
    }

    public Board getBoard() {
        return theBoard;
    }

    public String getMenuPath() {
        return theBoard.getBoardInstanceIdentifierForMenu();
    }

    public String getId() {
        return "Graph";
    }

    public HashMap<String, String> getParametersForUIComponent(Component comp) {
        return ((DIVGraphicalPanel) comp).getParameters();
    }

    public List<ValueWatch> getValueWatches() {
        List<ValueWatch> watches = new ArrayList<ValueWatch>();
        watches.add(new ValueWatch(DIVBoard.CHANNEL_INTEGRATED_MEASUREMENT, theBoard));
        watches.add(new ValueWatch(DIVBoard.CHANNEL_RANGE, theBoard));
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
    }
}
