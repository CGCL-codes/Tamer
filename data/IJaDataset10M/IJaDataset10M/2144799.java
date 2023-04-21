package net.jforerunning.gui.mainwnd.calendar.dlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import net.jforerunning.DataInterface;
import net.jforerunning.StaticFunctions;
import net.jforerunning.XDistance;
import net.jforerunning.XDuration;
import net.jforerunning.gui.components.DefaultComponent;
import net.jforerunning.gui.components.DefaultLabel;
import net.jforerunning.gui.components.DistanceEditor;
import net.jforerunning.gui.components.XButton;
import net.miginfocom.swing.MigLayout;

public class SplitEditor extends DefaultComponent implements ActionListener {

    public static final int INSERT_LAST = -1;

    private DataInterface dataInterface;

    private ArrayList<SplitLine> currentLines = new ArrayList<SplitLine>();

    public SplitEditor(DataInterface dataInterface) {
        this.dataInterface = dataInterface;
        addLine(INSERT_LAST);
        StaticFunctions.deepDisable(this);
    }

    /**
	 * Eine neue (leere) Zeile an der übergebenen Position anzeigen.
	 * Übergabe der Konstante INSERT_LAST führt dazu, dass die Zeile am Ende eingefügt wird.
	 * @param pos Position (mit 1 beginnend) oder INSERT_LAST
	 */
    private void addLine(int pos) {
        if (pos == INSERT_LAST) {
            currentLines.add(new SplitLine(dataInterface, this, new SplitData(currentLines.size() + 1)));
        } else {
            currentLines.add(pos - 1, new SplitLine(dataInterface, this, new SplitData(currentLines.size() + 1)));
        }
        for (int i = 1; i <= currentLines.size(); i++) {
            currentLines.get(i - 1).renumberTo(i);
        }
        assembleGUI();
    }

    /**
	 * Die Zeile der übergebenen Nummer aus dem Container entfernen.
	 * @param lineNr Die Zeilennummer, wie sie auch in der GUI steht, also beginnend mit 1
	 */
    private void removeLine(int lineNr) {
        currentLines.remove(currentLines.get(lineNr - 1));
        for (int i = 1; i <= currentLines.size(); i++) {
            currentLines.get(i - 1).renumberTo(i);
        }
        if (currentLines.size() < 1) addLine(1);
        assembleGUI();
    }

    private void assembleGUI() {
        removeAll();
        for (SplitLine line : currentLines) {
            add(line, "wrap");
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            int command = Integer.parseInt(e.getActionCommand());
            if (command < 0) removeLine(Math.abs(command)); else addLine(command);
        } catch (NumberFormatException x) {
            dataInterface.log("Illegal action command '" + e.getActionCommand() + "'", this);
        }
    }

    private class SplitLine extends JComponent {

        private DataInterface dataInterface;

        private ActionListener btnListener;

        private SplitData data;

        private DefaultLabel lbNr = new DefaultLabel();

        private DistanceEditor distanceEditor = null;

        private XButton addBtn = null;

        private XButton removeBtn = null;

        public SplitLine(DataInterface dataInterface, ActionListener btnListener, SplitData data) {
            this.dataInterface = dataInterface;
            this.btnListener = btnListener;
            this.data = data;
            setLayout(new MigLayout());
            assembleGUI();
            fillInData();
        }

        public void renumberTo(int nr) {
            data.nr = nr;
            lbNr.setText("(" + nr + ")");
            addBtn.setActionCommand("" + nr);
            removeBtn.setActionCommand("-" + nr);
        }

        private void assembleGUI() {
            removeAll();
            add(lbNr);
            if (distanceEditor == null) distanceEditor = new DistanceEditor(dataInterface, this);
            add(distanceEditor);
            if (addBtn == null) addBtn = new XButton(XButton.ADD_BUTTON, btnListener, "");
            add(addBtn);
            if (removeBtn == null) removeBtn = new XButton(XButton.REMOVE_BUTTON, btnListener, "");
            add(removeBtn);
        }

        private void fillInData() {
            renumberTo(data.nr);
        }
    }

    private class SplitData {

        public int nr;

        public XDistance distance;

        public XDuration duration;

        public SplitData(int nr, XDistance distance, XDuration duration) {
            this.nr = nr;
            this.distance = distance;
            this.duration = duration;
        }

        public SplitData(int nr) {
            this(nr, new XDistance(0), new XDuration(0));
        }
    }
}
