package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import data.AbstractGroup;
import data.Group;
import data.battle.BattleGroup;
import data.simulation.*;
import gui.dialogs.SimulatorGroupDialog;
import gui.tables.DataTable;
import gui.tables.TableCellStyle;
import gui.tables.TableCellStylizer;
import ogv.OGV;
import util.ConfigNode;
import util.DoubleLabel;
import util.FilePrompter;
import util.SwingUtils;
import static gui.CommandsLocal.*;
import static ogv.OGVPreferences.SIMULATOR_GROUP_TABLE;
import static ogv.OGVPreferences.SIMULATOR_RESULT_TABLE;

public class Simulator extends ViewList<StatGroup> implements TableCellStylizer {

    private DataTable<StatRace> resultTable;

    private final Simulation sim;

    private volatile boolean simulate;

    private DoubleLabel count;

    private DoubleLabel speed;

    private DoubleLabel avgRound;

    private long start;

    private JButton button;

    private Action startAction;

    private Action stopAction;

    private final Random seedGenerator = new Random();

    private volatile int battleNumber;

    private volatile long totalRounds;

    public Simulator() {
        sim = new Simulation();
        initResultTable(OGV.getConfig().subnode(SIMULATOR_RESULT_TABLE));
        init(StatGroup.class, OGV.getConfig().subnode(SIMULATOR_GROUP_TABLE));
        table.setStylizer(this);
        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE_GROUPS);
        table.getActionMap().put(DELETE_GROUPS, actions.getAction(DELETE_GROUPS));
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateResults();
            }
        });
        update();
    }

    @Override
    public String getTitle() {
        return "Simulator";
    }

    @Override
    public String getIconName() {
        return OGV.getConfig().getString("Actions." + CommandsGlobal.SIMULATE_BATTLE + ".Icon", null);
    }

    @Override
    public void defaultActionPerformed(StatGroup g) {
        SimulatorGroupDialog.showDialog(g, sim);
    }

    @Override
    protected JPopupMenu getPopupMenu() {
        return SwingUtils.createPopup(actions.getAction(EDIT_GROUP), actions.getAction(NEW_GROUP), actions.getAction(COPY_GROUPS), actions.getAction(DELETE_GROUPS), null, actions.getAction(IN_BATTLE), actions.getAction(OUT_BATTLE), null, actions.getAction(LOAD_BATTLE), actions.getAction(SAVE_BATTLE), actions.getAction(CLEAR));
    }

    @Override
    protected void updatePopupMenu() {
        List<StatGroup> groups = table.getSelectedItems();
        actions.getAction(EDIT_GROUP).setEnabled(groups.size() == 1);
        actions.getAction(COPY_GROUPS).setEnabled(!groups.isEmpty());
        actions.getAction(NEW_GROUP).setEnabled(true);
        actions.getAction(DELETE_GROUPS).setEnabled(!groups.isEmpty());
        actions.getAction(SAVE_BATTLE).setEnabled(!sim.getGroups().isEmpty());
        actions.getAction(CLEAR).setEnabled(!sim.getGroups().isEmpty());
        boolean hasInBattle = false;
        boolean hasOutBattle = false;
        for (StatGroup g : groups) {
            if (g.isInBattle()) hasInBattle = true; else hasOutBattle = true;
            if (hasInBattle && hasOutBattle) break;
        }
        actions.getAction(IN_BATTLE).setEnabled(hasOutBattle);
        actions.getAction(OUT_BATTLE).setEnabled(hasInBattle);
    }

    @Override
    protected void initActions() {
        CommandsLocal[] commands = { EDIT_GROUP, NEW_GROUP, COPY_GROUPS, DELETE_GROUPS, IN_BATTLE, OUT_BATTLE, LOAD_BATTLE, SAVE_BATTLE, CLEAR };
        actions.initActions(commands);
    }

    @Override
    public void actionPerformed(ActionEvent e, CommandsLocal c) {
        switch(c) {
            case EDIT_GROUP:
                {
                    StatGroup g = table.getSelectedItem();
                    if (g != null) SimulatorGroupDialog.showDialog(g, sim);
                    break;
                }
            case COPY_GROUPS:
                {
                    stopSimulation();
                    for (StatGroup g : table.getSelectedItems()) sim.addGroup(new StatGroup(g));
                    update();
                    break;
                }
            case NEW_GROUP:
                {
                    SimulatorGroupDialog.showDialog(null, sim);
                    update();
                    break;
                }
            case DELETE_GROUPS:
                {
                    deleteSelectedGroups();
                    break;
                }
            case IN_BATTLE:
                {
                    stopSimulation();
                    for (StatGroup g : table.getSelectedItems()) g.setInBattle(true);
                    update();
                    break;
                }
            case OUT_BATTLE:
                {
                    stopSimulation();
                    for (StatGroup g : table.getSelectedItems()) g.setInBattle(false);
                    update();
                    break;
                }
            case SAVE_BATTLE:
                {
                    saveBattle();
                    break;
                }
            case LOAD_BATTLE:
                {
                    loadBattle();
                    break;
                }
            case CLEAR:
                {
                    clear();
                    break;
                }
        }
    }

    @Override
    public TableCellStyle getStyle(int row, int col) {
        StatGroup g = table.getModel().getData(table.convertRowIndexToModel(row));
        TableCellStyle style = OGV.getStyle(game.getRace(g.getRace().getName()));
        if (!g.isInBattle()) style.update(OGV.getStyle(OGV.TableRowType.UPGRADE));
        return style;
    }

    public void addGroup(Group g) {
        addGroup(g, true);
    }

    public void addGroup(BattleGroup bg) {
        addGroup(bg, bg.isInBattle());
    }

    public void addGroup(AbstractGroup g, boolean inBattle) {
        stopSimulation();
        sim.addGroup(g, inBattle);
    }

    @Override
    public void update() {
        update(sim.getGroups());
        resultTable.update(sim.getRaces());
    }

    private void initResultTable(ConfigNode config) {
        resultTable = new StatRaceTable(config);
    }

    @Override
    protected void initGUI() {
        setLayout(new BorderLayout());
        JPanel info = new JPanel(new GridBagLayout());
        JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, info, new JScrollPane(resultTable));
        SwingUtils.setupSplitPane(hSplitPane);
        hSplitPane.setDividerLocation(150);
        JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), hSplitPane);
        SwingUtils.setupSplitPane(vSplitPane);
        vSplitPane.setDividerLocation(-200);
        vSplitPane.setResizeWeight(1.0);
        add(vSplitPane, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.NORTH);
        startAction = new AbstractAction("Start") {

            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        };
        stopAction = new AbstractAction("Stop") {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopSimulation();
            }
        };
        button = new JButton(startAction);
        count = new DoubleLabel(0);
        count.setValue(0);
        speed = new DoubleLabel(2);
        speed.setValue(0);
        avgRound = new DoubleLabel(2);
        avgRound.setValue(0);
        SwingUtils.addRow(info, button);
        SwingUtils.addRow(info, new JLabel("Simulations:"), count);
        SwingUtils.addRow(info, new JLabel("Speed:"), speed);
        SwingUtils.addRow(info, new JLabel("Rounds:"), avgRound);
    }

    public synchronized void startSimulation() {
        if (simulate || table.getModel().getRowCount() == 0) return;
        simulate = true;
        button.setAction(stopAction);
        start = System.nanoTime();
        resetStat();
        int t = Runtime.getRuntime().availableProcessors();
        while (t-- > 0) {
            final Random random = new Random(seedGenerator.nextLong());
            new Thread(new Runnable() {

                @Override
                public void run() {
                    List<SimRace> races = sim.initSimulation();
                    if (races == null) {
                        stopSimulation();
                        return;
                    }
                    while (simulate) addSimulation(races, Simulation.simulate(races, random));
                }
            }).start();
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (simulate) {
                        Thread.sleep(1000);
                        firePropertyChange("cycles", false, true);
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    public synchronized void stopSimulation() {
        if (!simulate) return;
        simulate = false;
        button.setAction(startAction);
        updateResults();
    }

    private synchronized void resetStat() {
        battleNumber = 0;
        totalRounds = 0;
        for (StatRace r : sim.getRaces()) r.resetStat();
    }

    private synchronized void addSimulation(List<SimRace> races, int rounds) {
        battleNumber++;
        totalRounds += rounds;
        for (SimRace br : races) br.getRace().addSimulation(br, rounds);
    }

    private synchronized void updateResults() {
        count.setValue(battleNumber);
        long t = System.nanoTime() - start;
        if (t > 0) speed.setValue(battleNumber * 1.0e9 / t);
        avgRound.setValue(battleNumber > 0 ? (double) totalRounds / battleNumber : 0);
        repaint();
        resultTable.repaint();
    }

    private void deleteSelectedGroups() {
        stopSimulation();
        for (StatGroup g : table.getSelectedItems()) sim.removeGroup(g);
        update();
    }

    private void clear() {
        for (StatGroup g : new ArrayList<StatGroup>(sim.getGroups())) sim.removeGroup(g);
        update();
    }

    private void saveBattle() {
        File file = FilePrompter.SaveFileDialog(null, "btl", game.getGameDir());
        if (file == null) return;
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            IO.saveBattle(sim, writer);
            writer.close();
        } catch (IOException ignore) {
        }
    }

    private void loadBattle() {
        File file = FilePrompter.OpenFileDialog(null, "btl", game.getGameDir());
        if (file == null) return;
        stopSimulation();
        try {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            try {
                IO.readBattle(sim, reader);
            } catch (Exception err) {
                err.printStackTrace();
                JOptionPane.showMessageDialog(this, new String[] { "Error at line " + reader.getLineNumber() + ":", err.getLocalizedMessage() }, "Battle read error", JOptionPane.ERROR_MESSAGE);
            }
            reader.close();
        } catch (IOException ignore) {
        }
        update();
    }

    private class StatRaceTable extends DataTable<StatRace> implements TableCellStylizer, DataSelectionListener {

        StatRaceTable(ConfigNode config) {
            super(StatRace.class, config);
            setDataSelectionListener(this);
            setStylizer(this);
        }

        @Override
        public void selectionChanged() {
            List<StatRace> selected = getSelectedItems();
            table.clearSelection();
            if (!selected.isEmpty()) {
                Set<StatRace> races = new HashSet<StatRace>(selected);
                for (int i = 0; i < table.getRowCount(); ++i) if (races.contains(list.get(table.convertRowIndexToModel(i)).getRace())) table.getSelectionModel().addSelectionInterval(i, i);
            }
        }

        @Override
        public TableCellStyle getStyle(int row, int col) {
            StatRace r = resultTable.getModel().getData(resultTable.convertRowIndexToModel(row));
            return OGV.getStyle(OGV.getGame().getRace(r.getName()));
        }
    }
}
