package de.ibis.permoto.gui.loganalyzer.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import de.ibis.permoto.gui.loganalyzer.LogAnalyzer;
import de.ibis.permoto.loganalyzer.db.JdbcDataSourceFactory;
import de.ibis.permoto.model.basic.types.DistributionNameType;
import de.ibis.permoto.model.basic.types.DropRuleType;
import de.ibis.permoto.model.basic.types.QueueingDisciplineType;
import de.ibis.permoto.model.basic.types.RoutingAlgorithmType;
import de.ibis.permoto.util.PermotoPreferences;

/**
 * Preferences Dialog for PerMoTo LogAnalyzer
 * @author Andreas Schamberger
 *
 */
public class PreferencesDialog extends JDialog {

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(PreferencesDialog.class);

    /** serial version uid */
    private static final long serialVersionUID = 1L;

    private static LogAnalyzer logAnalyzer;

    private static PreferencesDialog dialog;

    private Preferences prefs;

    private JPanel prefsPanel;

    private JPanel buttonPanel;

    private int fieldSize = 15;

    private JComboBox applicationModelList;

    private JComboBox namespaceBox;

    private HashMap<String, JComponent> prefsComponent = new HashMap<String, JComponent>();

    private GridBagConstraints c;

    private int row = 0;

    private Insets topInset = new Insets(10, 0, 0, 0);

    private Insets topRightInset = new Insets(10, 0, 0, 10);

    public static void showDialog(JFrame owner) {
        logAnalyzer = (LogAnalyzer) owner;
        dialog = new PreferencesDialog(owner);
        dialog.setVisible(true);
    }

    /**
     * get all available routing algorithms
     * @return
     */
    private String[] getRoutingAlgorithms() {
        HashSet<String> tmp = new HashSet<String>();
        for (RoutingAlgorithmType c : RoutingAlgorithmType.values()) {
            tmp.add(c.value());
        }
        return tmp.toArray(new String[0]);
    }

    /**
     * get all available queueing disciplines
     * @return
     */
    private String[] getQueueingDisciplines() {
        HashSet<String> tmp = new HashSet<String>();
        for (QueueingDisciplineType c : QueueingDisciplineType.values()) {
            tmp.add(c.value());
        }
        return tmp.toArray(new String[0]);
    }

    /**
     * get all available drop rules
     * @return
     */
    private String[] getDropRules() {
        HashSet<String> tmp = new HashSet<String>();
        for (DropRuleType c : DropRuleType.values()) {
            tmp.add(c.value());
        }
        return tmp.toArray(new String[0]);
    }

    /**
     * get all available distributions
     * @return
     */
    private String[] getDistributions() {
        HashSet<String> tmp = new HashSet<String>();
        for (DistributionNameType c : DistributionNameType.values()) {
            tmp.add(c.value());
        }
        return tmp.toArray(new String[0]);
    }

    /**
     * listener for add DB button
     */
    private class AddDbListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            AddDbConnectionDialog.showDialog(dialog);
        }
    }

    ;

    private class DeleteDbListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Preferences jdbcDataSourceDatabasesPrefs = prefs.node("db/JdbcDataSource");
            String prefsPath = jdbcDataSourceDatabasesPrefs.absolutePath() + "/logs";
            JComboBox dbs = (JComboBox) prefsComponent.get(prefsPath);
            String selectedDb = (String) dbs.getSelectedItem();
            int answer = JOptionPane.showConfirmDialog(null, "Deleting only works with added databases. Default databases cannot be deleted. Do you really want to delete database: " + selectedDb);
            if (answer == JOptionPane.OK_OPTION) {
                prefs = PermotoPreferences.getUserPreferences().node("/de/ibis/permoto/loganalyzer/db/JdbcDataSource/databases");
                prefs.remove(selectedDb);
                PermotoPreferences.saveUserPreferences();
                logger.info("deleted Database: " + selectedDb);
                logger.info("Saved changed Preferences!");
            } else {
                logger.info("Database is not being deleted!");
            }
        }
    }

    /**
     * listener for edit DB button
     */
    private class EditDbListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            EditDbConnectionDialog.showDialog(dialog);
        }
    }

    /**
     * listener for save button
     */
    private class SaveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            save();
        }
    }

    ;

    /**
     * listener for cancel button
     */
    private class CancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    }

    ;

    /**
     * constructor
     * @param owner
     */
    private PreferencesDialog(JFrame owner) {
        super(owner, "Preferences", true);
        prefs = PermotoPreferences.getUserPreferences().node("/de/ibis/permoto/loganalyzer");
        JButton addDbButton = new JButton("Add new Database");
        addDbButton.addActionListener(new AddDbListener());
        JButton editDbButton = new JButton("Edit Database");
        editDbButton.addActionListener(new EditDbListener());
        JButton deleteDbButton = new JButton("Delete Database");
        deleteDbButton.addActionListener(new DeleteDbListener());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelListener());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveListener());
        getRootPane().setDefaultButton(saveButton);
        prefsPanel = new JPanel();
        prefsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        prefsPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.insets = topInset;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        Preferences pamPrefs = prefs.node("PAM");
        newHeadline("PAM");
        String[] names = new String[2];
        names[0] = "Normal";
        names[1] = "Safe";
        addComboBoxNamespace(pamPrefs, "Namespace", names);
        Preferences preference;
        preference = Preferences.userRoot().node("/de/ibis/permoto/loganalyzer/PAM");
        String namespace = preference.get("Namespace", "Normal");
        String[] appList = null;
        if (namespace.equals("Normal")) {
            appList = logAnalyzer.wf.getNormalApplicationModelNames();
        } else {
            appList = logAnalyzer.wf.getSaveApplicationModelNames();
        }
        addComboBoxPAMList(pamPrefs, "defaultApplicationModel", appList);
        Preferences analyzerPrefs = prefs.node("Analyzer");
        newHeadline("Analyzer");
        addTextPreference(analyzerPrefs, "arrivalRateDiagramInterval", "60000");
        addTextPreference(analyzerPrefs, "averageDiagramInterval", "60000");
        addTextPreference(analyzerPrefs, "movingAverageDiagramInterval", "100");
        Preferences parameterCalculatorPrefs = prefs.node("ParameterCalculator");
        newHeadline("ParameterCalculator");
        addTextPreference(parameterCalculatorPrefs, "DefaultServiceTime", "0.01");
        addTextPreference(parameterCalculatorPrefs, "DefaultQueueLength", "-1");
        addTextPreference(parameterCalculatorPrefs, "DefaultNrParallelWorkers", "1");
        addComboBoxPreference(parameterCalculatorPrefs, "DefaultRoutingAlgorithm", getRoutingAlgorithms());
        addComboBoxPreference(parameterCalculatorPrefs, "DefaultQueueingDiscipline", getQueueingDisciplines());
        addComboBoxPreference(parameterCalculatorPrefs, "DefaultDropRule", getDropRules());
        String[] distributions = getDistributions();
        addComboBoxPreference(parameterCalculatorPrefs, "DefaultArrivalRateDistribution", distributions);
        addComboBoxPreference(parameterCalculatorPrefs, "DefaultServiceTimeDistribution", distributions);
        Preferences jdbcDataSourcePrefs = prefs.node("db/JdbcDataSource");
        newHeadline("JdbcDataSource");
        Preferences jdbcDataSourceDatabasesPrefs = prefs.node("db/JdbcDataSource/databases");
        String[] databases;
        try {
            databases = jdbcDataSourceDatabasesPrefs.keys();
            addComboBoxPreference(jdbcDataSourcePrefs, "logs", databases);
        } catch (BackingStoreException e) {
        }
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        buttonPanel.add(addDbButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(editDbButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(deleteDbButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(saveButton);
        Container contentPane = getContentPane();
        contentPane.add(prefsPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);
        pack();
        setLocationRelativeTo(null);
    }

    private void newHeadline(String header) {
        JLabel jl = new JLabel("<html><font size=3><b>" + header + "</b></color></html>");
        jl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        c.insets = topInset;
        prefsPanel.add(jl, c);
        ++row;
    }

    private void addTextPreference(Preferences prefs, String key, String def) {
        String prefsPath = prefs.absolutePath() + "/" + key;
        JTextField prefsField = new JTextField(fieldSize);
        prefsField.setName(prefsPath);
        prefsField.setText(prefs.get(key, def));
        JLabel prefsLabel = new JLabel(key + ":");
        prefsLabel.setLabelFor(prefsField);
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.insets = topRightInset;
        prefsPanel.add(prefsLabel, c);
        c.gridx = 1;
        c.gridy = row;
        c.insets = topInset;
        prefsPanel.add(prefsField, c);
        this.prefsComponent.put(prefsPath, prefsField);
        ++row;
    }

    private void addComboBoxPreference(Preferences prefs, String key, String[] options) {
        String prefsPath = prefs.absolutePath() + "/" + key;
        JComboBox prefsField = new JComboBox(options);
        prefsField.setName(prefsPath);
        prefsField.setSelectedItem(prefs.get(key, ""));
        JLabel prefsLabel = new JLabel(key + ":");
        prefsLabel.setLabelFor(prefsField);
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.insets = topRightInset;
        prefsPanel.add(prefsLabel, c);
        c.gridx = 1;
        c.gridy = row;
        c.insets = topInset;
        prefsPanel.add(prefsField, c);
        this.prefsComponent.put(prefsPath, prefsField);
        ++row;
    }

    private void addComboBoxPAMList(Preferences prefs, String key, String[] options) {
        String prefsPath = prefs.absolutePath() + "/" + key;
        applicationModelList = new JComboBox(options);
        applicationModelList.setName(prefsPath);
        applicationModelList.setSelectedItem(prefs.get(key, ""));
        JLabel prefsLabel = new JLabel(key + ":");
        prefsLabel.setLabelFor(applicationModelList);
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.insets = topRightInset;
        prefsPanel.add(prefsLabel, c);
        c.gridx = 1;
        c.gridy = row;
        c.insets = topInset;
        prefsPanel.add(applicationModelList, c);
        this.prefsComponent.put(prefsPath, applicationModelList);
        ++row;
    }

    private void addComboBoxNamespace(Preferences prefs, String key, String[] options) {
        String prefsPath = prefs.absolutePath() + "/" + key;
        namespaceBox = new JComboBox(options);
        namespaceBox.setName(prefsPath);
        namespaceBox.setSelectedItem(prefs.get(key, ""));
        JLabel prefsLabel = new JLabel(key + ":");
        prefsLabel.setLabelFor(namespaceBox);
        Action action = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                reloadPAMBox(namespaceBox.getSelectedItem().toString());
            }
        };
        namespaceBox.setAction(action);
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.insets = topRightInset;
        prefsPanel.add(prefsLabel, c);
        c.gridx = 1;
        c.gridy = row;
        c.insets = topInset;
        prefsPanel.add(namespaceBox, c);
        this.prefsComponent.put(prefsPath, namespaceBox);
        ++row;
    }

    private void reloadPAMBox(String name) {
        ComboBoxModel model = null;
        if (name.equals("Normal")) {
            model = new DefaultComboBoxModel(logAnalyzer.wf.getNormalApplicationModelNames());
        } else {
            model = new DefaultComboBoxModel(logAnalyzer.wf.getSaveApplicationModelNames());
        }
        applicationModelList.setModel(model);
        applicationModelList.repaint();
    }

    private void save() {
        dialog.setVisible(false);
        logger.info("Applying the changed Preferences ...");
        Preferences tmpPrefs;
        for (String key : prefsComponent.keySet()) {
            String value = null;
            try {
                JTextComponent jc = (JTextComponent) prefsComponent.get(key);
                value = jc.getText();
            } catch (ClassCastException e) {
                try {
                    JComboBox jc = (JComboBox) prefsComponent.get(key);
                    value = (String) jc.getSelectedItem();
                } catch (ClassCastException ee) {
                }
            }
            if (null != value) {
                int lastSlash = key.lastIndexOf("/");
                String pathName = key.substring(0, lastSlash);
                String prefsKey = key.substring(lastSlash + 1);
                tmpPrefs = prefs.node(pathName);
                tmpPrefs.put(prefsKey, value);
            }
        }
        PermotoPreferences.saveUserPreferences();
        logger.info("Finished saving the changed Preferences!");
        JdbcDataSourceFactory.resetJdbcDataSource();
        logAnalyzer.wf.setActiveApplicationModel(applicationModelList.getSelectedItem().toString());
        logAnalyzer.loadGuiData();
        logAnalyzer.reloadPAMBox();
        logAnalyzer.reloadTabTrees();
        logAnalyzer.wf.generateTableNameTable();
    }

    private void cancel() {
        dialog.setVisible(false);
    }
}
