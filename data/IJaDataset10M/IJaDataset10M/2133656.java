package hoplugins.trainingExperience.ui;

import hoplugins.Commons;
import hoplugins.TrainingExperience;
import hoplugins.commons.utils.PluginProperty;
import hoplugins.trainingExperience.ui.model.OutputTableModel;
import hoplugins.trainingExperience.ui.renderer.OutputTableRenderer;
import plugins.IHOMiniModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

/**
 * The Panel where the main training table is shown ("Training").
 *
 * <p>
 * TODO Costomize to show only players that received training?
 * </p>
 *
 * <p>
 * TODO Maybe i want to test for players that haven't received trainings to preview effect of
 * change of training.
 * </p>
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class OutputPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7955126207696897546L;

    private JPanel m_jpPanel;

    private JTable m_jtOutputTable;

    private OutputTableSorter sorter;

    /**
     * Creates a new OutputPanel object.
     */
    public OutputPanel() {
        super();
        jbInit();
    }

    /**
     * update the panel with the new value
     */
    public void reload() {
        OutputTableSorter otm = (OutputTableSorter) m_jtOutputTable.getModel();
        otm.fillWithData();
    }

    /**
     * Import a match from Hattrick
     */
    private void import_matches() {
        IHOMiniModel model = Commons.getModel();
        String input = JOptionPane.showInputDialog(PluginProperty.getString("GameID"));
        try {
            if (input != null) input = input.trim();
            Integer matchID = new Integer(input);
            if (model.getHelper().isUserMatch(input)) {
                if (model.getHelper().downloadMatchData(matchID.intValue())) {
                    model.getHelper().showMessage(null, PluginProperty.getString("MatchImported"), PluginProperty.getString("ImportOK"), 1);
                    model.getGUI().doRefresh();
                }
            } else {
                model.getHelper().showMessage(null, PluginProperty.getString("NotUserMatch"), PluginProperty.getString("ImportError"), 1);
            }
        } catch (Exception e) {
            model.getHelper().showMessage(null, PluginProperty.getString("MatchNotImported"), PluginProperty.getString("ImportError"), 1);
        }
    }

    /**
     * Initialize the object layout
     */
    private void jbInit() {
        IHOMiniModel model = Commons.getModel();
        m_jpPanel = model.getGUI().createImagePanel();
        m_jpPanel.setLayout(new BorderLayout());
        OutputTableModel outputTableModel = new OutputTableModel(model);
        sorter = new OutputTableSorter(outputTableModel);
        m_jtOutputTable = new OutputTable(sorter);
        m_jtOutputTable.getTableHeader().setReorderingAllowed(false);
        sorter.setTableHeader(m_jtOutputTable.getTableHeader());
        m_jtOutputTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_jtOutputTable.setDefaultRenderer(Object.class, new OutputTableRenderer());
        ListSelectionModel rowSM = m_jtOutputTable.getSelectionModel();
        rowSM.addListSelectionListener(new PlayerSelectionListener(m_jtOutputTable, 11));
        for (int i = 0; i < m_jtOutputTable.getColumnCount(); i++) {
            TableColumn column = m_jtOutputTable.getColumnModel().getColumn(i);
            switch(i) {
                case 0:
                    column.setPreferredWidth(150);
                    break;
                case 1:
                    column.setPreferredWidth(43);
                    break;
                case 2:
                    column.setPreferredWidth(100);
                    break;
                default:
                    column.setPreferredWidth(70);
            }
        }
        m_jtOutputTable.getTableHeader().getColumnModel().getColumn(11).setPreferredWidth(0);
        m_jtOutputTable.getTableHeader().getColumnModel().getColumn(11).setMinWidth(0);
        m_jtOutputTable.getTableHeader().getColumnModel().getColumn(11).setMaxWidth(0);
        m_jtOutputTable.setAutoResizeMode(0);
        m_jtOutputTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        JScrollPane scrollPane = new JScrollPane(m_jtOutputTable);
        m_jpPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel m_jpPanel2 = new JPanel();
        m_jpPanel2.setLayout(new BorderLayout());
        JButton p_JB_berechne = new JButton(PluginProperty.getString("Berechnen"));
        p_JB_berechne.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Commons.getModel().getTrainingsManager().recalcSubskills(true);
                reload();
                TrainingExperience.getTabbedPanel().getRecap().reload();
            }
        });
        m_jpPanel2.add(p_JB_berechne, BorderLayout.CENTER);
        JButton p_JB_import = new JButton(PluginProperty.getString("ImportMatch"));
        p_JB_import.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                import_matches();
            }
        });
        m_jpPanel2.add(p_JB_import, BorderLayout.WEST);
        m_jpPanel.add(m_jpPanel2, BorderLayout.NORTH);
        setLayout(new BorderLayout());
        add(m_jpPanel, BorderLayout.CENTER);
    }
}
