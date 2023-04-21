package pl.edu.mimuw.mas.agent.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import pl.edu.mimuw.mas.agent.AgentPossitionExtended;
import pl.edu.mimuw.mas.agent.gui.common.ColorEditor;
import pl.edu.mimuw.mas.agent.gui.common.ColorRenderer;

/**
 * Panel monitorujący wyświetlanie agentów na płutnie openGL. Umożliwia kontrolę
 * nad wyświetlaniem poszczególnych agentów. Zmianę ich opisów, koloru czy
 * wykluczenie z wizualizacji.
 */
public class AgentMonitorPanel extends JPanel {

    private static final long serialVersionUID = -6676785182164957695L;

    private static final int[] COLUMN_WIDTH = { 5, 150, 150, 5, 5, 50, 20, 20, 20, 20 };

    private static final int DISPLAY_COLUMN = 0;

    private static final int AID_COLUMN = 1;

    private static final int LABEL_COLUMN = 2;

    private static final int COLOR_COLUMN = 3;

    private static final int COLOR_OVERRIDE_COLUMN = 4;

    private static final String[] COLUMN_TOOL_TIPS = { "Wizualizuj agenta - gdy wyłączone agent nie jest wyświetlany", "AID - nazwa agenta z kontenera JADE", "Wyświetlana nazwa agenta", "Kolor agenta", "Nadpisuj kolor agneta - gdy włączone nadpisuje kolor agenta wybranym kolorem", "Data odebrania ostatniego komunikatu", "Liczba odebranych komunikatów", "X", "Y", "Z" };

    private final String FILTER_FIELD_DEFAULT_TEXT = "Filtr";

    private JScrollPane scrollPanel;

    private JTable agentTable;

    private JPanel bottomPanel;

    private JCheckBox agentShowDefault;

    private JTextField filterField;

    private TableRowSorter<AgentTableModel> agentTableSorter;

    private final IAgentMonitorBE myagent;

    private final HashMap<String, AgentPossitionExtended> agents;

    private final HashMap<String, Integer> rows;

    public AgentMonitorPanel(IAgentMonitorBE myagent) {
        this.myagent = myagent;
        this.agents = new HashMap<String, AgentPossitionExtended>();
        this.rows = new HashMap<String, Integer>();
        init();
    }

    @SuppressWarnings("serial")
    private void init() {
        agentTable = new JTable() {

            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {

                    public String getToolTipText(MouseEvent e) {
                        int index = columnModel.getColumnIndexAtX(e.getPoint().x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        return COLUMN_TOOL_TIPS[realIndex];
                    }
                };
            }
        };
        agentTable.setName("agentsTable");
        agentTable.setModel(new AgentTableModel());
        for (int i = 0; i < COLUMN_WIDTH.length; i++) {
            agentTable.getColumnModel().getColumn(i).setPreferredWidth(COLUMN_WIDTH[i]);
            if (COLUMN_WIDTH[i] == 5) {
                agentTable.getColumnModel().getColumn(i).setMaxWidth(COLUMN_WIDTH[i]);
            }
        }
        agentTable.setFillsViewportHeight(true);
        agentTable.setDefaultRenderer(Color.class, new ColorRenderer());
        agentTable.setDefaultEditor(Color.class, new ColorEditor());
        agentTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    if (e.getColumn() != TableModelEvent.ALL_COLUMNS) {
                        int row = agentTable.convertColumnIndexToModel(e.getFirstRow());
                        String aid = (String) agentTable.getModel().getValueAt(row, AID_COLUMN);
                        AgentPossitionExtended ap = agents.get(aid);
                        if (ap != null) {
                            switch(e.getColumn()) {
                                case DISPLAY_COLUMN:
                                    ap.setPrintable((Boolean) agentTable.getModel().getValueAt(row, DISPLAY_COLUMN));
                                    if (ap.isPrintable()) {
                                        myagent.registerAgentPossition(ap);
                                    } else {
                                        myagent.deregisterAgentPossition(ap);
                                    }
                                    break;
                                case LABEL_COLUMN:
                                    ap.setLabel((String) agentTable.getModel().getValueAt(row, LABEL_COLUMN));
                                    break;
                                case COLOR_COLUMN:
                                    Color col = (Color) agentTable.getModel().getValueAt(row, COLOR_COLUMN);
                                    if (ap.getR() != col.getRed() || ap.getG() != col.getGreen() || ap.getB() != col.getBlue()) {
                                        ((AgentTableModel) agentTable.getModel()).setValueAt(true, row, COLOR_OVERRIDE_COLUMN);
                                        ap.setColor(col.getRed(), col.getGreen(), col.getBlue());
                                    }
                                    break;
                                case COLOR_OVERRIDE_COLUMN:
                                    ap.setOverrideColor((Boolean) agentTable.getModel().getValueAt(row, COLOR_OVERRIDE_COLUMN));
                                    break;
                            }
                        }
                    }
                }
            }
        });
        agentTableSorter = new TableRowSorter<AgentTableModel>((AgentTableModel) agentTable.getModel());
        agentTable.setRowSorter(agentTableSorter);
        agentShowDefault = new JCheckBox();
        agentShowDefault.setText("domyślnie wyświetlaj agentów");
        agentShowDefault.setSelected(myagent.getDisplayStrategy());
        agentShowDefault.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                myagent.toggleDisplayStrategy();
            }
        });
        filterField = new JTextField();
        filterField.setMaximumSize(new Dimension(300, 20));
        filterField.setPreferredSize(new Dimension(300, 20));
        filterField.setText(FILTER_FIELD_DEFAULT_TEXT);
        filterField.setForeground(Color.GRAY);
        filterField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (filterField.getText().equals(FILTER_FIELD_DEFAULT_TEXT)) {
                    filterField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
            }
        });
        filterField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                createFilter();
            }

            public void insertUpdate(DocumentEvent e) {
                createFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                createFilter();
            }
        });
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.add(agentShowDefault);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(filterField);
        scrollPanel = new JScrollPane(agentTable);
        GroupLayout agentsPanelLayout = new GroupLayout(this);
        this.setLayout(agentsPanelLayout);
        agentsPanelLayout.setHorizontalGroup(agentsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(agentsPanelLayout.createSequentialGroup().addGroup(agentsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(bottomPanel).addComponent(scrollPanel))));
        agentsPanelLayout.setVerticalGroup(agentsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.CENTER, agentsPanelLayout.createSequentialGroup().addComponent(scrollPanel).addComponent(bottomPanel)));
    }

    /**
	 * Wyświetl w tabelce nowego agenta.
	 */
    public void putAgentPossition(AgentPossitionExtended ap) {
        if (agents.put(ap.getAid(), ap) != null) {
            refreshAgentPossition(ap);
        } else {
            AgentTableModel atm = ((AgentTableModel) agentTable.getModel());
            rows.put(ap.getAid(), atm.addRow(ap));
        }
    }

    /**
	 * Odświerz w tabeli wyświetlane informacje o agencie.
	 */
    public void refreshAgentPossition(String aid) {
        AgentPossitionExtended ap;
        if ((ap = agents.get(aid)) != null) {
            refreshAgentPossition(ap);
        }
    }

    /**
	 * Uaktualnij dane o agencie w tabeli używając informacji z <code>ap</code>.
	 */
    private void refreshAgentPossition(AgentPossitionExtended ap) {
        Integer row;
        if ((row = rows.get(ap.getAid())) != null) {
            ((AgentTableModel) agentTable.getModel()).setRow(row, ap);
        }
    }

    /**
	 * Załóż filtr na wyświetlane wiesze w tabeli. Filtruje kolumny "AID" i "nazwa".
	 */
    private void createFilter() {
        try {
            RowFilter<AgentTableModel, Object> rf;
            rf = RowFilter.regexFilter(filterField.getText(), AID_COLUMN, LABEL_COLUMN);
            agentTableSorter.setRowFilter(rf);
        } catch (java.util.regex.PatternSyntaxException e) {
        }
    }

    /**
	 * Model danych wyświetlanych w tabeli, domyślny model rozszerzony o
	 * definicje kolumn oraz dodatkowe metody importu danych.
	 * 
	 *
	 */
    private class AgentTableModel extends DefaultTableModel {

        private static final long serialVersionUID = 3412157070932550714L;

        private final SimpleDateFormat formatter;

        public AgentTableModel() {
            super(new Object[][] {}, new String[] { "", "AID", "Nazwa", "", "", "Data", "#Zdarzeń", "X", "Y", "Z" });
            this.formatter = new SimpleDateFormat("HH:mm:ss");
        }

        private Class<?>[] types = new Class[] { Boolean.class, String.class, String.class, Color.class, Boolean.class, String.class, Integer.class, Integer.class, Integer.class, Float.class };

        private boolean[] canEdit = new boolean[] { true, false, true, true, true, false, false, false, false, false };

        public Class<?> getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit[columnIndex];
        }

        public int addRow(AgentPossitionExtended ap) {
            addRow(agentPossitionToRow(ap));
            return getRowCount() - 1;
        }

        public void setRow(int row, AgentPossitionExtended ap) {
            setRow(row, agentPossitionToRow(ap));
        }

        @SuppressWarnings("unchecked")
        public void setRow(int row, Vector data) {
            dataVector.set(row, data);
            fireTableRowsUpdated(row, row);
        }

        @SuppressWarnings("unchecked")
        private Vector agentPossitionToRow(AgentPossitionExtended ap) {
            Vector<Object> vec = new Vector<Object>(10);
            vec.add(ap.isPrintable());
            vec.add(ap.getAid());
            vec.add(ap.getLabel());
            vec.add(new Color(ap.getR(), ap.getG(), ap.getB()));
            vec.add(ap.isOverrideColor());
            vec.add(formatter.format(ap.getLastEventDate()));
            vec.add(ap.getEventCount());
            vec.add(ap.getX());
            vec.add(ap.getY());
            vec.add(ap.getZ());
            return vec;
        }
    }
}
