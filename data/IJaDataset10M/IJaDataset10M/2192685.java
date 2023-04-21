package checkersGUI;

import graph.Axis;
import graph.DataSet;
import graph.Graph2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import checkersMain.CheckersGameListener;
import checkersMain.CheckersGameManager;
import checkersMain.CheckersPlayerInterface;
import checkersMain.CheckersPlayerLoader;
import checkersPlayer.Human;

/**
 * A graphical user interface designed to allow the user to have two groups of
 * {@link CheckersPlayerInterface}s play each other and keep track of the game
 * outcomes.
 * 
 * @author Amos Yuen
 * @version {@value #VERSION} - 16 August 2008
 */
@SuppressWarnings("serial")
public class CheckersTrainer extends JFrame implements WindowListener, ComponentListener, CheckersGameListener, ListSelectionListener {

    private class CheckersPlayerTable extends JPanel implements KeyListener, ListSelectionListener {

        private DefaultTableModel model;

        private JTable table;

        private AbstractAction add, insert, delete, up, down, clear, randomize;

        public CheckersPlayerTable() {
            super();
            add = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    insert(table.getRowCount());
                }
            };
            insert = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    insert(table.getSelectedRow());
                }
            };
            delete = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedRows = table.getSelectedRows();
                    for (int i = selectedRows.length - 1; i >= 0; i--) model.removeRow(selectedRows[i]);
                    table.clearSelection();
                    if (table.getRowCount() > 0) {
                        for (int i = 0; i < selectedRows.length; i++) if (selectedRows[i] < model.getRowCount()) table.addRowSelectionInterval(selectedRows[i], selectedRows[i]); else table.addRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
                    }
                }
            };
            up = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedRows = table.getSelectedRows();
                    for (int i = 0; i < selectedRows.length; i++) {
                        model.moveRow(selectedRows[i], selectedRows[i], --selectedRows[i]);
                    }
                    table.clearSelection();
                    for (int i = 0; i < selectedRows.length; i++) table.addRowSelectionInterval(selectedRows[i], selectedRows[i]);
                }
            };
            down = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedRows = table.getSelectedRows();
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        model.moveRow(selectedRows[i], selectedRows[i], ++selectedRows[i]);
                    }
                    table.clearSelection();
                    for (int i = 0; i < selectedRows.length; i++) table.addRowSelectionInterval(selectedRows[i], selectedRows[i]);
                }
            };
            randomize = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedRows = table.getSelectedRows();
                    Object[] selectedValues = new Object[selectedRows.length];
                    for (int i = 0; i < selectedValues.length; i++) selectedValues[i] = model.getValueAt(selectedRows[i], 0);
                    for (int i = 0; i < model.getRowCount(); i++) {
                        model.moveRow(i, i, (int) (Math.random() * model.getRowCount()));
                    }
                    int[] selectedIndices = new int[selectedValues.length];
                    for (int i = 0; i < selectedIndices.length; i++) {
                        for (int j = 0; j < model.getRowCount(); j++) {
                            if (selectedValues[i] == model.getValueAt(j, 0)) {
                                selectedIndices[i] = j;
                            }
                        }
                    }
                    table.clearSelection();
                    for (int i = 0; i < selectedIndices.length; i++) table.addRowSelectionInterval(selectedIndices[i], selectedIndices[i]);
                }
            };
            clear = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    model.setRowCount(0);
                    updateStatus();
                }
            };
            add.putValue(Action.NAME, "Add");
            insert.putValue(Action.NAME, "Insert");
            delete.putValue(Action.NAME, "Delete");
            up.putValue(Action.NAME, "Move Up");
            down.putValue(Action.NAME, "Move Down");
            clear.putValue(Action.NAME, "Clear");
            randomize.putValue(Action.NAME, "Randomize");
            add.putValue(Action.SHORT_DESCRIPTION, "Adds the selected Players from the Player List to this group table");
            insert.putValue(Action.SHORT_DESCRIPTION, "Inserts the selected Players from the Player List to this group table at the selected row");
            delete.putValue(Action.SHORT_DESCRIPTION, "Deletes the selected values in this table");
            up.putValue(Action.SHORT_DESCRIPTION, "Moves all selected values in this table one row up");
            down.putValue(Action.SHORT_DESCRIPTION, "Moves all selected values in this table one row down");
            clear.putValue(Action.SHORT_DESCRIPTION, "Clears all players from this table");
            randomize.putValue(Action.SHORT_DESCRIPTION, "Randomizes the order of the players in this table");
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            add(new JButton(add), c);
            c.gridy++;
            add(new JButton(insert), c);
            c.gridy++;
            add(new JButton(delete), c);
            c.gridy++;
            add(new JButton(up), c);
            c.gridy++;
            add(new JButton(down), c);
            c.gridy++;
            add(new JButton(randomize), c);
            c.gridy++;
            add(new JButton(clear), c);
            model = new DefaultTableModel(new Object[0][0], COLUMNS) {

                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            table = new JTable(model) {

                @Override
                protected JTableHeader createDefaultTableHeader() {
                    return new JTableHeader(columnModel) {

                        @Override
                        public String getToolTipText(MouseEvent e) {
                            int index = columnModel.getColumnIndexAtX(e.getPoint().x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            return COLUMN_TOOL_TIPS[realIndex];
                        }
                    };
                }

                @Override
                public String getToolTipText(MouseEvent e) {
                    if (getRowCount() == 0) return super.getToolTipText(e);
                    String tip = null;
                    Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);
                    int realColumnIndex = convertColumnIndexToModel(colIndex);
                    if (rowIndex < 0) return super.getToolTipText(e);
                    String name = (String) getValueAt(rowIndex, 0);
                    Object value = getValueAt(rowIndex, colIndex);
                    if (realColumnIndex == 0) tip = name; else if (realColumnIndex == 1) tip = name + " has won " + value + " game"; else if (realColumnIndex == 2) tip = name + " has drawn " + value + " game"; else if (realColumnIndex == 3) tip = name + " has lost " + value + " game"; else if (realColumnIndex == 4) tip = name + " has played " + value + " game"; else return super.getToolTipText(e);
                    if (value instanceof Integer && (Integer) value != 1) tip += "s";
                    return tip;
                }
            };
            table.setDragEnabled(true);
            table.setFillsViewportHeight(true);
            table.setTransferHandler(new TransferHandler() {

                @Override
                public boolean canImport(TransferHandler.TransferSupport info) {
                    if (!info.isDataFlavorSupported(PlayerData.playerDataFlavor)) return false;
                    return true;
                }

                @Override
                protected Transferable createTransferable(JComponent source) {
                    JTable table = (JTable) source;
                    return new PlayerData(source, table.getSelectedRows());
                }

                @Override
                public int getSourceActions(JComponent c) {
                    return MOVE;
                }

                @Override
                public boolean importData(TransferHandler.TransferSupport info) {
                    if (!info.isDrop()) return false;
                    if (!info.isDataFlavorSupported(PlayerData.playerDataFlavor)) return false;
                    if (info.getSourceDropActions() != COPY && info.getSourceDropActions() != MOVE) return false;
                    try {
                        Object data = (info.getTransferable()).getTransferData(PlayerData.playerDataFlavor);
                        Object source = (info.getTransferable()).getTransferData(PlayerData.sourceFlavor);
                        int index = table.rowAtPoint(info.getDropLocation().getDropPoint());
                        if (index == -1) index = table.getRowCount();
                        if (info.getSourceDropActions() == COPY) {
                            int initRowIndex = index;
                            Object[][] playerData = (Object[][]) data;
                            for (Object[] o : playerData) model.insertRow(index++, o);
                            table.setRowSelectionInterval(initRowIndex, index - 1);
                        } else if (source == table) {
                            int[] selectedRows = (int[]) data;
                            for (int i = 0; i < selectedRows.length; i++) {
                                int moveIndex = selectedRows[i] - i;
                                model.moveRow(moveIndex, moveIndex, model.getRowCount() - 1);
                            }
                            if (index < model.getRowCount() - selectedRows.length) {
                                model.moveRow(model.getRowCount() - selectedRows.length, model.getRowCount() - 1, index);
                                table.setRowSelectionInterval(index, index + selectedRows.length - 1);
                            } else table.setRowSelectionInterval(model.getRowCount() - selectedRows.length, model.getRowCount() - 1);
                        } else {
                            JTable sourceTable = (JTable) source;
                            int[] indices = (int[]) data;
                            int initRowIndex = index;
                            for (int i = 0; i < indices.length; i++) {
                                Object[] o = new Object[5];
                                for (int c = 0; c < o.length; c++) o[c] = sourceTable.getValueAt(indices[i], c);
                                model.insertRow(index++, o);
                            }
                            table.setRowSelectionInterval(initRowIndex, index - 1);
                            DefaultTableModel sourceModel = (DefaultTableModel) sourceTable.getModel();
                            for (int i = indices.length - 1; i >= 0; i--) sourceModel.removeRow(indices[i]);
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            });
            c.gridheight = 7;
            c.weightx = 1;
            c.gridx++;
            c.gridy = 0;
            add(new JScrollPane(table) {

                @Override
                public Dimension getMinimumSize() {
                    Dimension d1 = getViewport().getView().getMinimumSize();
                    d1.width += getVerticalScrollBar().getWidth() + 20;
                    return d1;
                }
            }, c);
            setOpaque(false);
            table.getSelectionModel().addListSelectionListener(this);
            table.addKeyListener(this);
            updateStatus();
        }

        public void deleteSelected() {
            int[] selectedRows = table.getSelectedRows();
            for (int i = selectedRows.length - 1; i >= 0; i--) model.removeRow(selectedRows[i]);
            updateStatus();
        }

        public void insert(int rowIndex) {
            rowIndex = Math.max(0, rowIndex);
            int initRowIndex = rowIndex;
            for (Object o : playersList.getSelectedValues()) model.insertRow(rowIndex++, new Object[] { o, 0, 0, 0, 0 });
            table.setRowSelectionInterval(initRowIndex, rowIndex - 1);
            updateStatus();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) deleteSelected();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            table.setEnabled(enabled);
            updateStatus();
        }

        public void updateStatus() {
            boolean enabled = table.isEnabled();
            boolean listHasSelection = playersList.getSelectedIndex() != -1;
            add.setEnabled(enabled && listHasSelection);
            insert.setEnabled(enabled && listHasSelection && table.getSelectedRowCount() == 1);
            enabled &= model.getRowCount() > 0;
            randomize.setEnabled(enabled);
            clear.setEnabled(enabled);
            if (enabled) enabled = table.getSelectedRowCount() > 0;
            delete.setEnabled(enabled);
            if (enabled) {
                int[] indices = table.getSelectedRows();
                up.setEnabled(indices[0] > 0);
                down.setEnabled(indices[indices.length - 1] < model.getRowCount() - 1);
            } else {
                up.setEnabled(false);
                down.setEnabled(false);
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == table.getSelectionModel()) {
                updateStatus();
                updateTrainerStatus();
            }
        }
    }

    private static class PlayerData implements Transferable {

        public static DataFlavor playerDataFlavor;

        public static DataFlavor sourceFlavor;

        static {
            try {
                playerDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
                sourceFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public final JComponent source;

        private final Object data;

        public PlayerData(JComponent source, Object data) {
            this.source = source;
            this.data = data;
        }

        @Override
        public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
            if (df == playerDataFlavor) return data;
            if (df == sourceFlavor) return source;
            throw new UnsupportedFlavorException(df);
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { playerDataFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor df) {
            return df.equals(playerDataFlavor);
        }
    }

    private static final String[] COLUMNS = new String[] { "Name", "Wins", "Draws", "Losses", "Total Games" };

    private static final String[] COLUMN_TOOL_TIPS = new String[] { "The name of the CheckersPlayer", "The number of games the CheckersPlayer has won", "The number of games the CheckersPlayer has drawn", "The number of games the CheckersPlayer has lost", "The number of games the CheckersPlayer has played" };

    public static final CompoundBorder BORDER = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());

    private static final int LEGEND_Y = 10;

    public static final String VERSION = "v1.12";

    private static final float WINS_X_RATIO = 0.2f, DRAWS_X_RATIO = 0.45f, LOSSES_X_RATIO = 0.7f;

    public static void main(String[] args) {
        new CheckersTrainer();
    }

    private AbstractAction checkersGUI, resetData, start;

    private Graph2D graph;

    private Axis axisX, axisY;

    private JTabbedPane tabbedPane;

    private JSplitPane splitPane, playerGroupsSplitPane, graphSplitPane;

    private DefaultListModel playersModel, graphPlayersModel;

    private JList playersList, graphPlayersList;

    private CheckersPlayerTable group1Table, group2Table;

    private JScrollPane playersSP, graphSP;

    private JToggleButton enableCheckersGUI, training;

    private CheckersGUI gui;

    private CheckersGameManager gameManager;

    private ArrayList<DataSet> wins, draws, losses;

    private CheckersPlayerInterface[] group1;

    private CheckersPlayerInterface[] group2;

    private int traineeStartIndex, opponentStartIndex, oldGraphPlayersIndex;

    private boolean group1IsPlayer1;

    public CheckersTrainer() {
        this(new CheckersGameManager());
        gameManager.setPlyTime(10000);
        gameManager.setMaxMoves(75);
    }

    public CheckersTrainer(CheckersGameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.addCheckersGameListener(this);
        initActions();
        setLayout(new GridBagLayout());
        tabbedPane = new JTabbedPane();
        playersModel = new DefaultListModel();
        playersList = new JList(playersModel);
        playersList.addListSelectionListener(this);
        playersList.setDragEnabled(true);
        playersList.setToolTipText("List of all CheckersPlayers");
        playersList.setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                try {
                    return info.getTransferable().getTransferData(PlayerData.sourceFlavor) instanceof JTable;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected Transferable createTransferable(JComponent source) {
                JList list = (JList) source;
                Object[] objects = list.getSelectedValues();
                Object[][] data = new Object[objects.length][5];
                for (int r = 0; r < data.length; r++) {
                    data[r][0] = objects[r];
                    for (int c = 1; c < 5; c++) data[r][c] = 0;
                }
                return new PlayerData(list, data);
            }

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                if (!info.isDrop()) return false;
                try {
                    int[] indices = (int[]) info.getTransferable().getTransferData(PlayerData.playerDataFlavor);
                    DefaultTableModel model = (DefaultTableModel) ((JTable) info.getTransferable().getTransferData(PlayerData.sourceFlavor)).getModel();
                    for (int i = indices.length - 1; i >= 0; i--) model.removeRow(indices[i]);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        playersSP = new JScrollPane(playersList) {

            @Override
            public Dimension getMinimumSize() {
                Dimension d1 = getViewport().getView().getMinimumSize();
                d1.width += getVerticalScrollBar().getWidth() + 20;
                return d1;
            }
        };
        TitledBorder border = BorderFactory.createTitledBorder(BORDER, "Players");
        playersSP.setBorder(border);
        playersSP.setOpaque(false);
        group1Table = new CheckersPlayerTable();
        group1Table.setToolTipText("A table of the players in Group 1 and their statistics");
        border = BorderFactory.createTitledBorder(BORDER, "Group 1");
        group1Table.setBorder(border);
        group2Table = new CheckersPlayerTable();
        group2Table.setToolTipText("A table of the players in Group 2 and their statistics");
        border = BorderFactory.createTitledBorder(BORDER, "Group 2");
        group2Table.setBorder(border);
        playerGroupsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, group1Table, group2Table);
        playerGroupsSplitPane.setOneTouchExpandable(true);
        playerGroupsSplitPane.setContinuousLayout(true);
        playerGroupsSplitPane.setOpaque(false);
        playerGroupsSplitPane.setBorder(null);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playersSP, playerGroupsSplitPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        graphPlayersModel = new DefaultListModel();
        graphPlayersList = new JList(graphPlayersModel);
        graphPlayersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        graphPlayersList.addListSelectionListener(this);
        graphSP = new JScrollPane(graphPlayersList);
        border = BorderFactory.createTitledBorder(BORDER, "Players");
        graphSP.setBorder(border);
        graphSP.setOpaque(false);
        wins = new ArrayList<DataSet>();
        draws = new ArrayList<DataSet>();
        losses = new ArrayList<DataSet>();
        DataSet playerWins = new DataSet();
        DataSet playerDraws = new DataSet();
        DataSet playerLosses = new DataSet();
        playerWins.linecolor = Color.GREEN;
        playerDraws.linecolor = Color.YELLOW;
        playerLosses.linecolor = Color.RED;
        playerWins.clipping = false;
        playerDraws.clipping = false;
        playerLosses.clipping = false;
        wins.add(playerWins);
        draws.add(playerDraws);
        losses.add(playerLosses);
        axisX = new Axis(Axis.BOTTOM);
        axisX.axiscolor = Color.CYAN;
        axisX.minimum = 1;
        axisX.maximum = 2;
        axisX.attachDataSet(playerWins);
        axisX.attachDataSet(playerDraws);
        axisX.attachDataSet(playerLosses);
        axisX.setTitleText("Game Count");
        axisY = new Axis(Axis.LEFT);
        axisY.axiscolor = Color.MAGENTA;
        axisY.minimum = 0;
        axisY.maximum = 100;
        axisY.minor_tic_count = 4;
        axisY.attachDataSet(playerWins);
        axisY.attachDataSet(playerDraws);
        axisY.attachDataSet(playerLosses);
        axisY.setTitleText("Game Outcome (%)");
        graph = new Graph2D();
        Dimension d = new Dimension(300, 300);
        graph.setMinimumSize(d);
        graph.setPreferredSize(d);
        graph.gridcolor = Color.WHITE;
        graph.setBackground(Color.DARK_GRAY);
        graph.setDataBackground(Color.BLACK);
        graph.attachAxis(axisX);
        graph.attachAxis(axisY);
        graph.attachDataSet(playerWins);
        graph.attachDataSet(playerDraws);
        graph.attachDataSet(playerLosses);
        graphSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphSP, graph);
        graphSplitPane.setOneTouchExpandable(true);
        graphSplitPane.setContinuousLayout(true);
        graphSplitPane.setOpaque(false);
        graphSplitPane.setBorder(null);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        tabbedPane.addTab("Players", splitPane);
        tabbedPane.addTab("Graph", graphSplitPane);
        add(tabbedPane, c);
        c.gridwidth = 1;
        c.weighty = 0;
        c.gridy++;
        enableCheckersGUI = new JToggleButton(checkersGUI);
        add(enableCheckersGUI, c);
        c.gridx++;
        add(new JButton(resetData), c);
        c.gridx++;
        training = new JToggleButton(start);
        add(training, c);
        for (int i = 0; i < CheckersPlayerLoader.getNumCheckersPlayers(); i++) {
            String player = CheckersPlayerLoader.getPlayerDisplayName(i);
            if (player.equals(Human.class.getSimpleName())) continue;
            playersModel.addElement(player);
        }
        graph.addComponentListener(this);
        int[] columnWidths = new int[] { 100, 45, 45, 45, 75 };
        TableColumnModel traineesColumnModel = group1Table.table.getColumnModel();
        TableColumnModel opponentsColumnModel = group2Table.table.getColumnModel();
        for (int i = 0; i < columnWidths.length; i++) {
            traineesColumnModel.getColumn(i).setMinWidth(columnWidths[i]);
            opponentsColumnModel.getColumn(i).setMinWidth(columnWidths[i]);
        }
        if (gui != null) {
            enableCheckersGUI.setSelected(true);
            gui.addWindowListener(this);
        }
        updatePlayersList();
        updateTrainerStatus();
        addWindowListener(this);
        setTitle("EBFCheckers Trainer " + VERSION);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500, 450));
        setSize(650, 450);
        setLocationRelativeTo(null);
        setVisible(true);
        playerGroupsSplitPane.setDividerLocation(0.5);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int width = graph.getWidth();
        for (int i = 0; i < wins.size(); i++) {
            wins.get(i).legend((int) (width * WINS_X_RATIO), LEGEND_Y, "Wins");
            draws.get(i).legend((int) (width * DRAWS_X_RATIO), LEGEND_Y, "Draws");
            losses.get(i).legend((int) (width * LOSSES_X_RATIO), LEGEND_Y, "Losses");
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void gameEnded(CheckersGameEvent ce) {
        int outcome = gameManager.getGameOutcome();
        if (outcome != CheckersGameManager.INTERRUPTED && outcome != CheckersGameManager.GAME_IN_PROGRESS) {
            int group1Column = 2;
            int group2Column = 2;
            if (outcome != CheckersGameManager.DRAW) {
                if (outcome == CheckersGameManager.PLAYER1_WINS ^ !group1IsPlayer1) {
                    group1Column = 1;
                    group2Column = 3;
                } else {
                    group1Column = 3;
                    group2Column = 1;
                }
            }
            int group1Row = group1Table.table.getSelectedRow();
            int group2Row = group2Table.table.getSelectedRow();
            group1Table.model.setValueAt((Integer) group1Table.model.getValueAt(group1Row, group1Column) + 1, group1Row, group1Column);
            int traineeGames = (Integer) group1Table.model.getValueAt(group1Row, 4) + 1;
            group1Table.model.setValueAt(traineeGames, group1Row, 4);
            group2Table.model.setValueAt((Integer) group2Table.model.getValueAt(group2Row, group2Column) + 1, group2Row, group2Column);
            group2Table.model.setValueAt((Integer) group2Table.model.getValueAt(group2Row, 4) + 1, group2Row, 4);
            try {
                int gameCount = (Integer) group1Table.model.getValueAt(group1Row, 4);
                wins.get(group1Row).append(new double[] { gameCount, 100.0 * ((Integer) group1Table.model.getValueAt(group1Row, 1)) / gameCount }, 1);
                draws.get(group1Row).append(new double[] { gameCount, 100.0 * ((Integer) group1Table.model.getValueAt(group1Row, 2)) / gameCount }, 1);
                losses.get(group1Row).append(new double[] { gameCount, 100.0 * ((Integer) group1Table.model.getValueAt(group1Row, 3)) / gameCount }, 1);
                axisY.minimum = 0;
                axisY.maximum = 100;
                if (gameCount <= 1) {
                    axisX.minimum = 1;
                    axisX.maximum = 2;
                    resetData.setEnabled(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            graph.repaint();
            int traineeRowCount = group1Table.model.getRowCount();
            int opponentRowCount = group2Table.model.getRowCount();
            group1Row = (group1Row + 1) % traineeRowCount;
            if (group1Row == traineeStartIndex) {
                group2Row = (group2Row + 1) % opponentRowCount;
                if (group2Row == opponentStartIndex) group1IsPlayer1 = !group1IsPlayer1;
            }
            if (group1IsPlayer1) {
                gameManager.setPlayer1(group1[group1Row]);
                gameManager.setPlayer2(group2[group2Row]);
            } else {
                gameManager.setPlayer1(group2[group2Row]);
                gameManager.setPlayer2(group1[group1Row]);
            }
            group1Table.table.setRowSelectionInterval(group1Row, group1Row);
            group2Table.table.setRowSelectionInterval(group2Row, group2Row);
        }
        if (training.isSelected()) gameManager.newGame();
    }

    @Override
    public void gameStarted(CheckersGameEvent ce) {
    }

    public void initActions() {
        checkersGUI = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (((JToggleButton) e.getSource()).isSelected()) {
                    setGUI(new CheckersGUI(gameManager));
                    gui.setTrainer(CheckersTrainer.this);
                } else {
                    gui.dispose();
                }
            }
        };
        resetData = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetData.setEnabled(false);
                for (int c = 1; c < group1Table.table.getColumnCount(); c++) group1Table.model.setValueAt(0, 0, c);
                for (int r = 0; r < group2Table.model.getRowCount(); r++) {
                    for (int c = 1; c < group2Table.model.getColumnCount(); c++) group2Table.model.setValueAt(0, r, c);
                }
                for (int i = 0; i < wins.size(); i++) {
                    wins.get(i).deleteData();
                    draws.get(i).deleteData();
                    losses.get(i).deleteData();
                }
                axisX.minimum = 1;
                axisX.maximum = 2;
            }
        };
        start = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (((JToggleButton) e.getSource()).isSelected()) {
                    start.putValue(Action.NAME, "Stop");
                    start.putValue(Action.SHORT_DESCRIPTION, "Stops the training");
                    playersList.setEnabled(false);
                    group1Table.setEnabled(false);
                    group2Table.setEnabled(false);
                    initTraining();
                } else {
                    gameManager.stop();
                    start.putValue(Action.NAME, "Start");
                    start.putValue(Action.SHORT_DESCRIPTION, "Starts the training");
                    playersList.setEnabled(true);
                    group1Table.setEnabled(true);
                    group2Table.setEnabled(true);
                }
            }
        };
        checkersGUI.putValue(Action.NAME, "Enable CheckersGUI");
        resetData.putValue(Action.NAME, "Reset Data");
        start.putValue(Action.NAME, "Start");
        checkersGUI.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
        resetData.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        start.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        checkersGUI.putValue(Action.SHORT_DESCRIPTION, "Disable the CheckersGUI");
        resetData.putValue(Action.SHORT_DESCRIPTION, "Resets the statistics for all players");
        start.putValue(Action.SHORT_DESCRIPTION, "Starts the training");
        resetData.setEnabled(false);
    }

    private void initTraining() {
        try {
            int numTrainees = group1Table.model.getRowCount();
            group1 = new CheckersPlayerInterface[numTrainees];
            HashMap<String, CheckersPlayerInterface> players = new HashMap<String, CheckersPlayerInterface>();
            DataSet data = wins.get(oldGraphPlayersIndex);
            axisX.detachDataSet(data);
            axisY.detachDataSet(data);
            graph.detachDataSet(data);
            while (wins.size() > numTrainees) {
                wins.remove(wins.size() - 1);
                draws.remove(draws.size() - 1);
                losses.remove(losses.size() - 1);
            }
            graphPlayersModel.clear();
            for (int i = 0; i < numTrainees; i++) {
                String name = (String) group1Table.model.getValueAt(i, 0);
                CheckersPlayerInterface player = players.get(name);
                if (player != null) {
                    group1[i] = player;
                    continue;
                }
                for (int j = 0; j < CheckersPlayerLoader.getNumCheckersPlayers(); j++) {
                    if (name.equals(CheckersPlayerLoader.getPlayerDisplayName(j))) {
                        player = CheckersPlayerLoader.getPlayerClass(j).newInstance();
                        players.put(name, player);
                        group1[i] = player;
                    }
                }
                graphPlayersModel.addElement(group1[i].getName());
                if (i < wins.size()) wins.get(i).deleteData(); else {
                    try {
                        DataSet playerWins = new DataSet();
                        DataSet playerDraws = new DataSet();
                        DataSet playerLosses = new DataSet();
                        playerWins.linecolor = Color.GREEN;
                        playerDraws.linecolor = Color.YELLOW;
                        playerLosses.linecolor = Color.RED;
                        playerWins.clipping = false;
                        playerDraws.clipping = false;
                        playerLosses.clipping = false;
                        int width = graph.getWidth();
                        playerWins.legend((int) (width * WINS_X_RATIO), LEGEND_Y, "Wins");
                        playerDraws.legend((int) (width * DRAWS_X_RATIO), LEGEND_Y, "Draws");
                        playerLosses.legend((int) (width * LOSSES_X_RATIO), LEGEND_Y, "Losses");
                        wins.add(playerWins);
                        draws.add(playerDraws);
                        losses.add(playerLosses);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            oldGraphPlayersIndex = 0;
            graphPlayersList.setSelectedIndex(0);
            int numOpponents = group2Table.table.getRowCount();
            group2 = new CheckersPlayerInterface[numOpponents];
            players.clear();
            for (int i = 0; i < numOpponents; i++) {
                String name = (String) group2Table.model.getValueAt(i, 0);
                CheckersPlayerInterface player = players.get(name);
                if (player != null) {
                    group2[i] = player;
                    continue;
                }
                for (int j = 0; j < CheckersPlayerLoader.getNumCheckersPlayers(); j++) {
                    if (name.equals(CheckersPlayerLoader.getPlayerDisplayName(j))) {
                        player = CheckersPlayerLoader.getPlayerClass(j).newInstance();
                        players.put(name, player);
                        group2[i] = player;
                    }
                }
            }
            group1IsPlayer1 = true;
            traineeStartIndex = Math.max(0, group1Table.table.getSelectedRow());
            opponentStartIndex = Math.max(0, group2Table.table.getSelectedRow());
            gameManager.setPlayer1(group1[traineeStartIndex]);
            gameManager.setPlayer2(group2[opponentStartIndex]);
            group1Table.table.setRowSelectionInterval(traineeStartIndex, traineeStartIndex);
            group2Table.table.setRowSelectionInterval(opponentStartIndex, opponentStartIndex);
            gameManager.newGame();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(CheckersTrainer.this, e.getMessage(), "Error Creating Players", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void pauseStateChanged(CheckersGameEvent ce) {
    }

    @Override
    public void player1Changed(CheckersGameEvent ce) {
    }

    @Override
    public void player2Changed(CheckersGameEvent ce) {
    }

    @Override
    public void plyTaken(CheckersGameEvent ce) {
    }

    @Override
    public void plyTimeChanged(CheckersGameEvent ce) {
    }

    public void setGUI(CheckersGUI gui) {
        if (this.gui != null || gui == null) return;
        this.gui = gui;
        enableCheckersGUI.setSelected(gui != null);
        checkersGUI.putValue(Action.NAME, "Disable CheckersGUI");
        checkersGUI.putValue(Action.SHORT_DESCRIPTION, "Disable the CheckersGUI");
        gui.addWindowListener(this);
    }

    private void updatePlayersList() {
        boolean enabled = playersList.getSelectedIndex() != -1;
        group1Table.add.setEnabled(group1Table.isEnabled() && enabled);
        group1Table.insert.setEnabled(group1Table.isEnabled() && enabled);
        group2Table.add.setEnabled(group2Table.isEnabled() && enabled);
        group2Table.insert.setEnabled(group2Table.isEnabled() && enabled);
    }

    private void updateTrainerStatus() {
        start.setEnabled(group1Table.model.getRowCount() > 0 && group2Table.model.getRowCount() > 0);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == playersList) updatePlayersList(); else if (e.getSource() == graphPlayersList) {
            if (graphPlayersModel.size() > 0) {
                int selected = graphPlayersList.getSelectedIndex();
                if (selected == -1) graphPlayersList.setSelectedIndex(oldGraphPlayersIndex); else {
                    DataSet data = wins.get(oldGraphPlayersIndex);
                    axisX.detachDataSet(data);
                    axisY.detachDataSet(data);
                    graph.detachDataSet(data);
                    data = draws.get(oldGraphPlayersIndex);
                    axisX.detachDataSet(data);
                    axisY.detachDataSet(data);
                    graph.detachDataSet(data);
                    data = losses.get(oldGraphPlayersIndex);
                    axisX.detachDataSet(data);
                    axisY.detachDataSet(data);
                    graph.detachDataSet(data);
                    data = wins.get(selected);
                    axisX.attachDataSet(data);
                    axisY.attachDataSet(data);
                    graph.attachDataSet(data);
                    data = draws.get(selected);
                    axisX.attachDataSet(data);
                    axisY.attachDataSet(data);
                    graph.attachDataSet(data);
                    data = losses.get(selected);
                    axisX.attachDataSet(data);
                    axisY.attachDataSet(data);
                    graph.attachDataSet(data);
                    oldGraphPlayersIndex = selected;
                    axisY.minimum = 0;
                    axisY.maximum = 100;
                    if (data.dataPoints() < 2) {
                        axisX.minimum = 1;
                        axisX.maximum = 2;
                    }
                    graph.repaint();
                }
            }
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (e.getSource() == gui) {
            gui = null;
            enableCheckersGUI.setSelected(false);
            checkersGUI.putValue(Action.NAME, "Enable CheckersGUI");
            checkersGUI.putValue(Action.SHORT_DESCRIPTION, "Enable the CheckersGUI");
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getSource() == this) {
            gameManager.removeCheckersGameListener(this);
            if (gui == null) {
                gameManager.stop();
                System.exit(0);
            }
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
