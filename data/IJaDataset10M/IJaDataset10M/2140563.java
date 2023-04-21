package gov.sns.apps.score;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import javax.swing.DefaultListModel;
import java.awt.Color;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Timestamp;
import gov.sns.application.*;
import gov.sns.tools.database.*;

/**
 * ScoreWindow contains the gui components for the score app.
 *
 * @author  jdg
 */
public class ScoreWindow extends XalWindow {

    protected JTabbedPane theTabbedPane;

    protected JPanel sysSelectPanel, typeSelectPanel, selectorPanel;

    protected JSplitPane selectorSplitPane;

    private ScoreDocument theDoc;

    private JButton typeSetButton, sysSetButton, selectAllButton;

    private JLabel snapTimeLabel;

    protected JTextField nameMatchField;

    private JScrollPane typeSelectScrollPane, systemSelectScrollPane;

    private DefaultListModel typeListModel, systemListModel;

    protected JList typeJList, systemJList;

    protected JTextArea textArea;

    protected javax.swing.Timer timer;

    protected JTextField errorText;

    /** menu for displaying the list of available pvlogger groups */
    protected JComboBox _groupMenu;

    protected ScoreDataModel _model;

    /** Creates a new instance of MainWindow */
    public ScoreWindow(XalDocument aDocument, ScoreDataModel model) {
        super(aDocument);
        setSize(1000, 700);
        theDoc = (ScoreDocument) aDocument;
        _model = model;
        typeListModel = new DefaultListModel();
        systemListModel = new DefaultListModel();
        typeJList = new JList(typeListModel);
        systemJList = new JList(systemListModel);
        snapTimeLabel = new JLabel("No snapshot data");
        snapTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorText = new JTextField();
        makeContent();
        handleWindowEvents();
    }

    /**
	* Create the main window subviews     */
    protected void makeContent() {
        Container container = getContentPane();
        container.add(errorText, BorderLayout.SOUTH);
        sysSelectPanel = new JPanel(new BorderLayout());
        sysSelectPanel.setPreferredSize(new Dimension(125, 150));
        JLabel sysSelectLabel = new JLabel("Select Systems:");
        sysSelectPanel.add(sysSelectLabel, BorderLayout.NORTH);
        systemSelectScrollPane = new JScrollPane(systemJList);
        sysSelectPanel.add(systemSelectScrollPane, BorderLayout.CENTER);
        typeSelectPanel = new JPanel(new BorderLayout());
        typeSelectPanel.setPreferredSize(new Dimension(125, 150));
        JLabel typeSelectLabel = new JLabel("Select Subsys:");
        typeSelectPanel.add(typeSelectLabel, BorderLayout.NORTH);
        typeSelectScrollPane = new JScrollPane(typeJList);
        typeSelectPanel.add(typeSelectScrollPane, BorderLayout.CENTER);
        selectorSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sysSelectPanel, typeSelectPanel);
        selectorSplitPane.setVisible(true);
        typeSetButton = new JButton("Set Selections");
        typeSetButton.setBackground(Color.cyan);
        typeSetButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTypesSystems(evt);
            }
        });
        selectAllButton = new JButton("Select All");
        selectAllButton.setBackground(Color.cyan);
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAll(evt);
            }
        });
        JLabel matchFieldLabel = new JLabel("PV name filter");
        nameMatchField = new JTextField(15);
        nameMatchField.setMaximumSize(new Dimension(150, 10));
        selectorPanel = new JPanel();
        selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
        selectorPanel.add(selectorSplitPane);
        selectorPanel.add(Box.createVerticalStrut(5));
        selectorPanel.add(matchFieldLabel);
        selectorPanel.add(nameMatchField);
        selectorPanel.add(Box.createVerticalStrut(5));
        selectorPanel.add(typeSetButton);
        selectorPanel.add(Box.createVerticalStrut(5));
        selectorPanel.add(selectAllButton);
        selectorPanel.add(Box.createVerticalStrut(5));
        container.add(selectorPanel, BorderLayout.WEST);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(snapTimeLabel, BorderLayout.SOUTH);
        theTabbedPane = new JTabbedPane();
        theTabbedPane.setVisible(true);
        Box openView = new Box(javax.swing.SwingConstants.VERTICAL);
        openView.add(buildQueryView());
        theTabbedPane.add("Open", openView);
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        theTabbedPane.add("Comment", textArea);
        if (theDoc.theTables.size() > 0) addTables();
        tablePanel.add(theTabbedPane, BorderLayout.CENTER);
        container.add(tablePanel, BorderLayout.CENTER);
        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableTask();
            }
        };
        timer = new javax.swing.Timer(2000, taskPerformer);
        timer.start();
    }

    /** the action to perform when the tables need updating */
    protected void tableTask() {
        String name;
        JTable table;
        JTabbedPane ttp;
        ScoreTableModel tableModel;
        ttp = theTabbedPane;
        int nTabs = ttp.getTabCount();
        for (int i = 0; i < nTabs; i++) {
            if (ttp.isEnabledAt(i)) {
                name = ttp.getTitleAt(i);
                table = (JTable) theDoc.theTables.get(name);
                if (table != null) {
                    tableModel = (ScoreTableModel) table.getModel();
                    tableModel.fireTableRowsUpdated(0, tableModel.getRowCount());
                }
            }
        }
    }

    /** add tables from the document to a seperate scollable pane in the big tabbed pane */
    protected void addTables() {
        while (theTabbedPane.getTabCount() > 2) theTabbedPane.remove(theTabbedPane.getTabCount() - 1);
        TreeSet set = new TreeSet(theDoc.theTables.keySet());
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            String name = (String) itr.next();
            JScrollPane tableScrollPane = new JScrollPane((JTable) theDoc.theTables.get(name));
            theTabbedPane.add(name, tableScrollPane);
        }
        if (theTabbedPane.getTabCount() > 1) theTabbedPane.setSelectedIndex(1);
        theTabbedPane.setVisible(true);
    }

    /** set  all  types + systems as selected and start the makeTable process */
    private void selectAll(java.awt.event.ActionEvent event) {
        if (theDoc.theSnapshot != null) {
            updateSelectionLists(true);
            theDoc.setSelectedTypes(typeJList.getSelectedValues());
            theDoc.setSelectedSystems(systemJList.getSelectedValues());
            nameMatchField.setText("");
            theDoc.makeTables();
        } else {
            theDoc.dumpErr("Whoa dude - Load up a score snapshot first!!");
        }
    }

    /** set  the selected types + systems start the makeTable process */
    private void setTypesSystems(java.awt.event.ActionEvent event) {
        if (theDoc.theSnapshot != null) {
            setTypes(event);
            setSystems(event);
            theDoc.makeTables();
        } else {
            theDoc.dumpErr("Whoa dude - Load up a score snapshot first!!");
        }
    }

    /** select a subset of available types */
    private void setTypes(java.awt.event.ActionEvent event) {
        Object[] types = typeJList.getSelectedValues();
        theDoc.setSelectedTypes(types);
        System.out.println("Types set");
    }

    /** select a subset of available systems */
    private void setSystems(java.awt.event.ActionEvent event) {
        Object[] systems = systemJList.getSelectedValues();
        theDoc.setSelectedSystems(systems);
        System.out.println("Systems set");
    }

    /** update the lists for the system and type choices 
	* @param tf - if true, default status is selected, otherwise it is not selected.
	*/
    protected void updateSelectionLists(boolean tf) {
        typeListModel.clear();
        systemListModel.clear();
        Iterator itr = theDoc.typeList.iterator();
        while (itr.hasNext()) {
            typeListModel.addElement(itr.next());
        }
        itr = theDoc.systemList.iterator();
        while (itr.hasNext()) {
            systemListModel.addElement(itr.next());
        }
        int listLength;
        if (tf) {
            listLength = typeListModel.size();
            typeJList.setSelectionInterval(0, listLength - 1);
            listLength = systemListModel.size();
            systemJList.setSelectionInterval(0, listLength - 1);
        }
    }

    /** update the snapshot time label */
    protected void updateSnapLabel(Date time) {
        if (time != null) snapTimeLabel.setText("Machine data saved at " + time.toString()); else snapTimeLabel.setText("No snapshot time info");
    }

    /**
	 * Build the view for querying the database for the machine snapshots.
	 * @return the query view
	 */
    protected Container buildQueryView() {
        final SnapshotTableModel snapshotTableModel = new SnapshotTableModel(_model);
        final JTable snapshotTable = new JTable(snapshotTableModel);
        Box queryPanel = new Box(javax.swing.SwingConstants.VERTICAL);
        queryPanel.add(new JLabel("This panel is in testing mode"));
        Box queryView2 = new Box(javax.swing.SwingConstants.HORIZONTAL);
        queryView2.setBorder(BorderFactory.createEtchedBorder());
        final int BUTTON_GAP = 20;
        JButton connectButton = new JButton("Connect");
        queryView2.add(connectButton);
        connectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                requestUserConnection();
            }
        });
        queryView2.add(Box.createHorizontalStrut(BUTTON_GAP));
        _groupMenu = new JComboBox();
        _groupMenu.setMaximumSize(new Dimension(200, 25));
        queryView2.add(_groupMenu);
        _groupMenu.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                _model.setType((String) _groupMenu.getSelectedItem());
            }
        });
        queryView2.add(Box.createHorizontalStrut(BUTTON_GAP));
        queryView2.add(new JLabel("From:"));
        final SpinnerDateModel fromDateModel = new SpinnerDateModel();
        JSpinner fromSpinner = new JSpinner(fromDateModel);
        fromSpinner.setEditor(new JSpinner.DateEditor(fromSpinner, "MMM dd, yyyy HH:mm:ss"));
        fromSpinner.setMaximumSize(new Dimension(200, 25));
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.MONTH, calFrom.get(Calendar.MONTH) - 1);
        fromSpinner.setValue(calFrom.getTime());
        queryView2.add(fromSpinner);
        queryView2.add(Box.createHorizontalStrut(10));
        queryView2.add(new JLabel("To:"));
        final SpinnerDateModel toDateModel = new SpinnerDateModel();
        JSpinner toSpinner = new JSpinner(toDateModel);
        toSpinner.setEditor(new JSpinner.DateEditor(toSpinner, "MMM dd, yyyy HH:mm:ss"));
        toSpinner.setMaximumSize(new Dimension(200, 25));
        Calendar calTo = Calendar.getInstance();
        calTo.set(Calendar.DAY_OF_MONTH, calTo.get(Calendar.DAY_OF_MONTH) + 1);
        toSpinner.setValue(calTo.getTime());
        queryView2.add(toSpinner);
        queryView2.add(Box.createHorizontalStrut(BUTTON_GAP));
        JButton fetchButton = new JButton("Fetch Snapshots in Range");
        queryView2.add(fetchButton);
        fetchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String groupType = (String) _groupMenu.getSelectedItem();
                Date startDate = fromDateModel.getDate();
                Date endDate = toDateModel.getDate();
                _model.fetchScoreSnapshots(_model.getType(), startDate, endDate);
                snapshotTableModel.fireTableDataChanged();
            }
        });
        queryView2.add(Box.createHorizontalGlue());
        queryPanel.add(queryView2);
        snapshotTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        snapshotTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        JScrollPane tableScrollPane = new JScrollPane(snapshotTable);
        queryPanel.add(tableScrollPane);
        Box queryView3 = new Box(javax.swing.SwingConstants.HORIZONTAL);
        JButton selectSnapshotButton = new JButton("Fetch selected snapshot");
        selectSnapshotButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (snapshotTable.getSelectedRow() == -1) {
                    _model.fetchScoreGroup(_model.getType());
                    theDoc.setSnapshot(new ScoreSnapshot(_model.getFetchedScoreGroup()));
                    theDoc.setupFromSnapshot();
                } else {
                    Timestamp date = snapshotTableModel.getSelectedDate(snapshotTable.getSelectedRow());
                    _model.fetchScoreSnapshot(date, _model.getType());
                    if (_model.getFetchedSnapshot().getRowCount() < 1) {
                        theDoc.dumpErr("No data is in the selected snapshot");
                    } else {
                        theDoc.setSnapshot(_model.getFetchedSnapshot());
                        theDoc.setupFromSnapshot();
                    }
                }
            }
        });
        queryView3.add(selectSnapshotButton);
        JButton selectGoldenButton = new JButton("Fetch golden snapshot");
        selectGoldenButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                _model.fetchGoldenSnapshot(_model.getType());
                if (_model.getFetchedSnapshot() != null) {
                    theDoc.setSnapshot(_model.getFetchedSnapshot());
                    theDoc.setupFromSnapshot();
                } else {
                    theDoc.dumpErr("No Golden Set was Found");
                }
            }
        });
        queryView3.add(selectGoldenButton);
        queryPanel.add(queryView3);
        return queryPanel;
    }

    /**
	 * Display a connection dialog to the user and connect to the database using the resulting connection
	 * dictionary.
	 */
    protected void requestUserConnection() {
        ConnectionDictionary dictionary = ConnectionDictionary.defaultDictionary();
        ConnectionDialog dialog = ConnectionDialog.getInstance(this, dictionary);
        Connection connection = dialog.showConnectionDialog();
        if (connection != null) {
            _model.setDatabaseConnection(connection, dialog.getConnectionDictionary());
        }
        updateGroupMenu();
    }

    protected void updateGroupMenu() {
        _groupMenu.removeAllItems();
        String[] types = _model.getScoreTypes();
        if (types == null) {
            _model.setType(null);
            return;
        }
        for (int index = 0; index < types.length; index++) {
            _groupMenu.addItem(types[index]);
        }
        if (types.length > 0) {
            _model.setType(types[0]);
        }
    }

    /**
	 * Handle window events.  When the window opens, request a connection.
	 */
    protected void handleWindowEvents() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent event) {
                try {
                    ConnectionDictionary dictionary = ConnectionDictionary.defaultDictionary();
                    if (dictionary != null) {
                        _model.connect();
                        updateGroupMenu();
                    } else {
                        requestUserConnection();
                    }
                } catch (Exception exception) {
                    requestUserConnection();
                }
            }
        });
    }
}
