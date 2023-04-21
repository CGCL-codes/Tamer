package net.sf.rmoffice.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.sf.rmoffice.RMOffice;
import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.Characteristics;
import net.sf.rmoffice.core.Coins;
import net.sf.rmoffice.core.ExportImport;
import net.sf.rmoffice.core.RMLevelUp;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.RMSheet.State;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.core.items.MagicalItem;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.ui.actions.CharacterGeneratorAction;
import net.sf.rmoffice.ui.actions.CharacterNameAction;
import net.sf.rmoffice.ui.actions.CreatePDFAction;
import net.sf.rmoffice.ui.actions.DesktopAction;
import net.sf.rmoffice.ui.actions.SaveAction;
import net.sf.rmoffice.ui.actions.SaveAsAction;
import net.sf.rmoffice.ui.editor.NumberSpinnerTableCellEditor;
import net.sf.rmoffice.ui.models.BasicPresentationModel;
import net.sf.rmoffice.ui.models.CharacteristicsPresentationModel;
import net.sf.rmoffice.ui.models.LongRunningUIModel;
import net.sf.rmoffice.ui.models.SkillTreeNode;
import net.sf.rmoffice.ui.models.SkillcategoryTableModel;
import net.sf.rmoffice.ui.models.SkillsTableModel;
import net.sf.rmoffice.ui.models.StatValueModelAdapter;
import net.sf.rmoffice.ui.panels.BasicPanel;
import net.sf.rmoffice.ui.panels.CharacteristicsPanel;
import net.sf.rmoffice.ui.panels.CoinsPanel;
import net.sf.rmoffice.ui.panels.EquipmentPanel;
import net.sf.rmoffice.ui.panels.InfoPagePanel;
import net.sf.rmoffice.ui.panels.LevelUpStatusBar;
import net.sf.rmoffice.ui.panels.MagicalItemPanel;
import net.sf.rmoffice.ui.panels.ModifySkillDialog;
import net.sf.rmoffice.ui.panels.ProgressGlassPane;
import net.sf.rmoffice.ui.panels.StatPanel;
import net.sf.rmoffice.ui.panels.TodoPanel;
import net.sf.rmoffice.ui.renderer.ColoredBooleanRenderer;
import net.sf.rmoffice.ui.renderer.NumberSpinnerTableRenderer;
import net.sf.rmoffice.ui.renderer.SkillTreeRenderer;
import net.sf.rmoffice.util.RMOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.SearchableBar;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TreeSearchable;

/**
 * 
 */
public class RMFrame extends JFrame implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(RMFrame.class);

    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale");

    private static final String TITLE = "RoleMaster Office";

    private List<JComponent> allComponents = new ArrayList<JComponent>();

    private JTabbedPane tabbedPane;

    private Map<SkillCategory, JTextField> skillcostFields = new HashMap<SkillCategory, JTextField>();

    private List<SkillCategory> modifiableSkillgroups = new ArrayList<SkillCategory>();

    private RMSheet sheet;

    private MetaData data;

    private boolean refreshing = false;

    private JTable skillgroupTable;

    private JTable skillsTable;

    private SkillsTableModel skillModel;

    private File currentFile;

    private SkillTreeNode skillSelectionRootNode;

    private JTree skillsSelectionTree;

    private JTextArea taSkillDescription;

    private TodoPanel toDoPanel;

    private BeanAdapter<Characteristics> characteristicsAdapter;

    private BeanAdapter<RMSheet> rmsheetAdapter;

    private BeanAdapter<Coins> coins;

    private ValueHolder enableValueHolder = new ValueHolder(false);

    private ValueHolder enableMenuSaveValueHolder = new ValueHolder(false);

    private ValueHolder enableMenuExportPDFValueHolder = new ValueHolder(false);

    private BeanAdapter<StatValueModelAdapter> statValueModelAdapter;

    private SkillcategoryTableModel skillcatModel;

    private JMenu newLatestVersionMenu;

    private LongRunningUIModel longRunAdapter;

    private ProgressGlassPane glassPane;

    /**
	 * 
	 */
    public RMFrame() {
        super(TITLE);
        setPreferredSize(new Dimension(1000, 800));
        setIconImage(UIConstants.FRAME_ICON.getImage());
        glassPane = new ProgressGlassPane();
        setGlassPane(glassPane);
        longRunAdapter = new LongRunningUIModel(glassPane);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLookAndFeel();
    }

    private static void setLookAndFeel() {
        try {
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac")) {
            } else {
                UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
            }
        } catch (ClassNotFoundException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        } catch (InstantiationException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        } catch (IllegalAccessException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Can't initialize Look and Feel. Using Java-LaF.", e);
        }
    }

    public void init(MetaData data) {
        this.data = data;
        createCleanRMSheet();
        initMenu();
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        toDoPanel = new TodoPanel();
        toDoPanel.setSheet(sheet);
        toDoPanel.init();
        panel.add(toDoPanel, BorderLayout.NORTH);
        tabbedPane = new JideTabbedPane();
        BasicPresentationModel basicModel = new BasicPresentationModel(this, getRMSheetAdapter(), data);
        BasicPanel basicPanel = new BasicPanel(basicModel, enableValueHolder);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.basic"), new JScrollPane(basicPanel));
        CharacteristicsPresentationModel characteristicsModel = new CharacteristicsPresentationModel(getCharacteristicsAdapter().getBeanChannel());
        CharacteristicsPanel characteristicsPanel = new CharacteristicsPanel(characteristicsModel, getRMSheetAdapter(), enableValueHolder);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.characteristics"), null, createNorthPanel(characteristicsPanel));
        StatPanel statPanel = new StatPanel(getStatValueModelAdapter(), getRMSheetAdapter(), enableValueHolder);
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(statPanel, BorderLayout.WEST);
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(panel1, BorderLayout.NORTH);
        JScrollPane pane = new JScrollPane(panel2);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.stats"), null, pane);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.skillgroups"), null, initSkillcategoryPanel());
        tabbedPane.addTab(RESOURCE.getString("ui.tab.weaponcost"), null, createNorthPanel(initWeaponCategories()));
        tabbedPane.addTab(RESOURCE.getString("ui.tab.skills"), null, initSkillsPanel());
        JTabbedPane equipmentTabs = new JideTabbedPane();
        EquipmentPanel equipmentPanel = new EquipmentPanel(getRMSheetAdapter());
        equipmentTabs.addTab(RESOURCE.getString("ui.tab.equipment"), equipmentPanel);
        CoinsPanel coinsPanel = new CoinsPanel(getCoinsAdapter());
        equipmentTabs.addTab(RESOURCE.getString("rolemaster.money.header"), createNorthPanel(coinsPanel));
        MagicalItemPanel itemPanel = registerComp(new MagicalItemPanel(getRMSheetAdapter(), enableValueHolder));
        equipmentTabs.addTab(RESOURCE.getString("ui.tab.items"), null, createNorthPanel(itemPanel));
        tabbedPane.addTab(RESOURCE.getString("ui.tab.equipment"), null, equipmentTabs);
        InfoPagePanel infoPanel = new InfoPagePanel(getRMSheetAdapter());
        Bindings.bind(infoPanel, "enabled", enableValueHolder);
        tabbedPane.addTab(RESOURCE.getString("ui.tab.info"), null, infoPanel);
        panel.add(tabbedPane);
        add(panel, BorderLayout.CENTER);
        LevelUpStatusBar statusBar = new LevelUpStatusBar();
        add(statusBar, BorderLayout.SOUTH);
        initPropertyChangeListener();
        sheet.setMetaData(data);
        sheet.init();
        Bindings.bind(statusBar, "text", getRMSheetAdapter().getValueModel(RMLevelUp.PROPERTY_LVLUP_STATUSTEXT, "getLvlUpStatusText", "setLvlUpStatusText"));
        Bindings.bind(statusBar, "devPoints", getRMSheetAdapter().getValueModel(RMLevelUp.PROPERTY_LVLUP_DEVPOINTS, "getLvlUpDevPoints", "setLvlUpDevPoints"));
        Bindings.bind(statusBar, "spellRanks", getRMSheetAdapter().getValueModel(RMLevelUp.PROPERTY_LVLUP_SPELLRANKS, "getLvlUpSpellRanks", "setLvlUpSpellRanks"));
    }

    /**
	 * 
	 */
    private void createCleanRMSheet() {
        sheet = new RMSheet();
        updateAdapters();
    }

    private JComponent createNorthPanel(JComponent comp) {
        JPanel bgPanel = new JPanel(new BorderLayout());
        bgPanel.add(comp, BorderLayout.NORTH);
        JScrollPane pane = new JScrollPane(bgPanel);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        return pane;
    }

    private JComponent initWeaponCategories() {
        modifiableSkillgroups = new ArrayList<SkillCategory>();
        for (SkillCategory group : data.getSkillCategories()) {
            if (group.getRankType().isCostSwitchable()) {
                modifiableSkillgroups.add(group);
            }
        }
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints col1 = new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        GridBagConstraints col2 = new GridBagConstraints(1, 0, 1, 1, 0.3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        GridBagConstraints col3 = new GridBagConstraints(2, 0, 1, 1, 0.1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        GridBagConstraints col4 = new GridBagConstraints(3, 0, 1, 1, 0.1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 1, 2);
        ActionListener listenerUp = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = Integer.parseInt(e.getActionCommand());
                SkillCategory sg1 = modifiableSkillgroups.get(idx);
                SkillCategory sg2 = modifiableSkillgroups.get(idx - 1);
                sheet.switchSkillCategoryCosts(sg1, sg2);
                refreshing = true;
                sheet2ui();
                refreshing = false;
            }
        };
        ActionListener listenerDown = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = Integer.parseInt(e.getActionCommand());
                SkillCategory sg1 = modifiableSkillgroups.get(idx);
                SkillCategory sg2 = modifiableSkillgroups.get(idx + 1);
                sheet.switchSkillCategoryCosts(sg1, sg2);
                refreshing = true;
                sheet2ui();
                refreshing = false;
            }
        };
        for (int i = 0; i < modifiableSkillgroups.size(); i++) {
            panel.add(new JLabel(modifiableSkillgroups.get(i).getName()), col1);
            JTextField tfCost = new JTextField("? / ?");
            tfCost.setEditable(false);
            tfCost.setFocusable(false);
            tfCost.setPreferredSize(new Dimension(60, UIConstants.TABLE_ROW_HEIGHT));
            skillcostFields.put(modifiableSkillgroups.get(i), tfCost);
            panel.add(tfCost, col2);
            if (i > 0) {
                JButton btUp = new JButton(UIConstants.ICON_ARRUP);
                Bindings.bind(btUp, "enabled", enableValueHolder);
                btUp.addActionListener(listenerUp);
                btUp.setActionCommand("" + i);
                panel.add(btUp, col3);
            }
            if (i < (modifiableSkillgroups.size() - 1)) {
                JButton btDown = new JButton(UIConstants.ICON_ARRDOWN);
                Bindings.bind(btDown, "enabled", enableValueHolder);
                btDown.addActionListener(listenerDown);
                btDown.setActionCommand("" + i);
                panel.add(btDown, col4);
            }
            col1.gridy++;
            col2.gridy++;
            col3.gridy++;
            col4.gridy++;
        }
        JPanel positionPanel = new JPanel(new BorderLayout());
        positionPanel.add(panel, BorderLayout.WEST);
        return positionPanel;
    }

    private Component initSkillsPanel() {
        skillSelectionRootNode = new SkillTreeNode();
        skillsSelectionTree = new JTree(skillSelectionRootNode);
        skillsSelectionTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        skillsSelectionTree.setEditable(false);
        skillsSelectionTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (e.getNewLeadSelectionPath() != null) {
                    Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();
                    if (lastPathComponent != null && lastPathComponent instanceof SkillTreeNode) {
                        SkillTreeNode node = (SkillTreeNode) lastPathComponent;
                        if (node.getUserObject() instanceof ISkill) {
                            taSkillDescription.setText(((ISkill) node.getUserObject()).getDescription());
                        }
                    }
                }
            }
        });
        skillsSelectionTree.setCellRenderer(new SkillTreeRenderer(getRMSheetAdapter()));
        skillsSelectionTree.addMouseListener(new MouseAdapter() {

            /** {@inheritDoc} */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addCurrentSelectedISkill(false);
                }
            }
        });
        TreeSearchable searchable = SearchableUtils.installSearchable(skillsSelectionTree);
        searchable.setRepeats(true);
        searchable.setRecursive(true);
        skillModel = new SkillsTableModel();
        skillModel.setSheet(sheet);
        skillsTable = new JTable(skillModel);
        skillsTable.setSelectionBackground(UIConstants.COLOR_SELECTION_BG);
        skillsTable.setSelectionForeground(UIConstants.COLOR_SELECTION_FG);
        skillsTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        TableColumn skillCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_SKILL);
        skillCol.setPreferredWidth(200);
        TableColumn favCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_FAVORITE);
        favCol.setPreferredWidth(30);
        favCol.setCellRenderer(new ColoredBooleanRenderer());
        TableColumn rankCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_RANK);
        rankCol.setPreferredWidth(50);
        NumberSpinnerTableCellEditor spinner = new NumberSpinnerTableCellEditor(0.5);
        rankCol.setCellEditor(spinner);
        NumberSpinnerTableRenderer spinnRenderer = new NumberSpinnerTableRenderer();
        rankCol.setCellRenderer(spinnRenderer);
        skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_COST).setPreferredWidth(50);
        TableColumn specialCol = skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_SPECIAL_BONUS);
        specialCol.setPreferredWidth(50);
        NumberSpinnerTableCellEditor spinner2 = new NumberSpinnerTableCellEditor(1, true);
        specialCol.setCellEditor(spinner2);
        NumberSpinnerTableRenderer spinnRenderer2 = new NumberSpinnerTableRenderer();
        specialCol.setCellRenderer(spinnRenderer2);
        skillsTable.getColumnModel().getColumn(SkillsTableModel.COL_TOTAL_BONUS).setPreferredWidth(50);
        skillsTable.setAutoCreateRowSorter(true);
        JPanel buttonsPanel = new JPanel();
        JButton btAdd = new JButton(RESOURCE.getString("ui.skills.btAdd"), UIConstants.ICON_NEWLINE);
        Bindings.bind(btAdd, "enabled", enableValueHolder);
        btAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addCurrentSelectedISkill(false);
            }
        });
        buttonsPanel.add(btAdd);
        JButton btEditBeforeAdd = new JButton(RESOURCE.getString("ui.skills.btEditBeforeAdd"), UIConstants.ICON_NEWEDIT);
        Bindings.bind(btEditBeforeAdd, "enabled", enableValueHolder);
        btEditBeforeAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addCurrentSelectedISkill(true);
            }
        });
        buttonsPanel.add(btEditBeforeAdd);
        buttonsPanel.add(new JLabel(" | "));
        final ValueHolder skillSelectedValuseHolder = new ValueHolder(false);
        ListSelectionModel skillSelectionModel = skillsTable.getSelectionModel();
        skillSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        skillSelectionModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    skillSelectedValuseHolder.setValue(skillsTable.getSelectionModel().getMinSelectionIndex() > -1);
                }
            }
        });
        JButton btEdit = new JButton(RESOURCE.getString("ui.skills.btEdit"), UIConstants.ICON_EDIT);
        Bindings.bind(btEdit, "enabled", skillSelectedValuseHolder);
        btEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                modifyExistingISkill();
            }
        });
        buttonsPanel.add(btEdit);
        JButton btRemove = new JButton(RESOURCE.getString("ui.skills.btRemove"), UIConstants.ICON_DELETE);
        Bindings.bind(btRemove, "enabled", skillSelectedValuseHolder);
        btRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (skillsTable.getSelectedRow() > -1) {
                    skillModel.removeRow(skillsTable.getSelectedRow());
                }
            }
        });
        buttonsPanel.add(btRemove);
        JScrollPane treechoserPane = new JScrollPane(skillsSelectionTree);
        final JPanel leftPanel = new JPanel(new BorderLayout());
        taSkillDescription = new JTextArea();
        taSkillDescription.setEditable(false);
        taSkillDescription.setWrapStyleWord(true);
        taSkillDescription.setLineWrap(true);
        taSkillDescription.setRows(2);
        leftPanel.add(new JScrollPane(taSkillDescription), BorderLayout.NORTH);
        leftPanel.add(treechoserPane, BorderLayout.CENTER);
        final SearchableBar searchableBar = new SearchableBar(searchable, true);
        int visibleBt = SearchableBar.SHOW_NAVIGATION | SearchableBar.SHOW_STATUS;
        searchableBar.setVisibleButtons(visibleBt);
        leftPanel.add(searchableBar, BorderLayout.SOUTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0);
        splitPane.add(leftPanel);
        splitPane.add(new JScrollPane(skillsTable));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonsPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private void createSkillSelectionData(SkillTreeNode rootNode) {
        Map<SkillCategory, SkillTreeNode> folders = new HashMap<SkillCategory, SkillTreeNode>();
        for (SkillCategory group : data.getSkillCategories()) {
            SkillTreeNode node = new SkillTreeNode();
            node.setUserObject(group);
            rootNode.add(node);
            folders.put(group, node);
        }
        if (sheet.getRace() != null) {
            for (ISkill skill : data.getSkills()) {
                if ((skill.getScope() == null || skill.getScope() != null && skill.getScope().equals(sheet.getRace().getScope())) && (!RMPreferences.getInstance().isExcluded(skill.getSource()))) {
                    SkillTreeNode skillnode = new SkillTreeNode();
                    skillnode.setUserObject(skill);
                    SkillTreeNode parentNode = folders.get(sheet.getSkillcategory(skill));
                    if (parentNode != null) {
                        parentNode.add(skillnode);
                    }
                }
            }
        }
    }

    private Component initSkillcategoryPanel() {
        skillcatModel = new SkillcategoryTableModel(data);
        skillcatModel.setSheet(sheet);
        skillgroupTable = new JTable(skillcatModel);
        skillgroupTable.setSelectionBackground(UIConstants.COLOR_SELECTION_BG);
        skillgroupTable.setSelectionForeground(UIConstants.COLOR_SELECTION_FG);
        skillgroupTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        skillgroupTable.setTableHeader(new JTableHeader(skillgroupTable.getColumnModel()));
        skillgroupTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_NAME).setPreferredWidth(200);
        skillgroupTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_STAT).setPreferredWidth(40);
        skillgroupTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_AP_COST).setPreferredWidth(40);
        TableColumn rankCol = skillgroupTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_RANKS);
        rankCol.setPreferredWidth(20);
        NumberSpinnerTableCellEditor spinner = new NumberSpinnerTableCellEditor(1, false);
        rankCol.setCellEditor(spinner);
        NumberSpinnerTableRenderer spinnRenderer = new NumberSpinnerTableRenderer();
        rankCol.setCellRenderer(spinnRenderer);
        TableColumn spec2Col = skillgroupTable.getColumnModel().getColumn(SkillcategoryTableModel.SKILLCAT_TABLE_COL_SPEC2_BONUS);
        NumberSpinnerTableCellEditor spinner2 = new NumberSpinnerTableCellEditor(1, true);
        spec2Col.setCellEditor(spinner2);
        NumberSpinnerTableRenderer spinnRenderer2 = new NumberSpinnerTableRenderer(true);
        spec2Col.setCellRenderer(spinnRenderer2);
        for (int i = 4; i < 10; i++) {
            skillgroupTable.getColumnModel().getColumn(i).setPreferredWidth(15);
        }
        JScrollPane pane = new JScrollPane(skillgroupTable);
        return pane;
    }

    private void initMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu(RESOURCE.getString("ui.menu.file"));
        bar.add(file);
        JMenuItem menuNew = new JMenuItem(RESOURCE.getString("ui.menu.new"), UIConstants.ICON_NEW_SHEET);
        menuNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (sheet != null) {
                    sheet.dispose();
                }
                setCurrentFile(null);
                createCleanRMSheet();
                initPropertyChangeListener();
                toDoPanel.setSheet(sheet);
                sheet.setMetaData(data);
                sheet.init();
                skillModel.setSheet(sheet);
                skillcatModel.setSheet(sheet);
                tabbedPane.setSelectedIndex(0);
                RMFrame.this.setTitle(TITLE);
                refreshing = true;
                clearUi();
                refreshing = false;
            }
        });
        file.add(menuNew);
        JMenuItem menuOpen = new JMenuItem(RESOURCE.getString("ui.menu.openfile"), UIConstants.ICON_OPEN);
        menuOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Reader freader = null;
                InputStream in = null;
                try {
                    JFileChooser fch = new JFileChooser(RMPreferences.getInstance().getLastDir());
                    fch.setAcceptAllFileFilterUsed(false);
                    fch.setFileFilter(new RMOFileFilter());
                    int result = fch.showOpenDialog(RMFrame.this);
                    if (JFileChooser.APPROVE_OPTION == result) {
                        if (sheet != null) {
                            removePropertyChangeListener();
                        }
                        File selectedFile = fch.getSelectedFile();
                        RMPreferences.getInstance().setLastDir(selectedFile.getParentFile());
                        in = new FileInputStream(selectedFile);
                        freader = new BufferedReader(new InputStreamReader(in, ExportImport.ENCODING));
                        sheet = ExportImport.importXml(freader);
                        sheet.setMetaData(data);
                        updateAdapters();
                        setCurrentFile(selectedFile);
                        RMFrame.this.setTitle(selectedFile.getName());
                        initPropertyChangeListener();
                        toDoPanel.setSheet(sheet);
                        sheet.init();
                        if (refreshing) return;
                        refreshing = true;
                        skillModel.setSheet(sheet);
                        skillcatModel.setSheet(sheet);
                        if (RMSheet.State.NORMAL.equals(sheet.getState())) {
                            clearUi();
                            sheet2ui();
                        }
                    }
                } catch (Exception ex) {
                    if (log.isDebugEnabled()) log.debug("Exception while opening file: ", ex);
                    JOptionPane.showMessageDialog(RMFrame.this, RESOURCE.getString("error.file.open") + ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                } finally {
                    refreshing = false;
                    if (in != null) {
                        try {
                            if (log.isDebugEnabled()) log.debug("closing opened file");
                            in.close();
                        } catch (IOException ex) {
                            JOptionPane.showConfirmDialog(RMFrame.this, ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                            log.error("ignoring Exception while closing the file stream " + ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                        }
                    }
                    try {
                        if (freader != null) {
                            if (log.isDebugEnabled()) log.debug("closing input reader");
                            freader.close();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showConfirmDialog(RMFrame.this, ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                        if (log.isDebugEnabled()) log.debug("ignoring Exception while closing file reader " + ex.getClass().getName() + ":" + ex.getLocalizedMessage());
                    }
                }
            }
        });
        file.add(menuOpen);
        JMenuItem menuCreatePDF = new JMenuItem(RESOURCE.getString("ui.menu.exportpdf"), UIConstants.ICON_PDF);
        CreatePDFAction pdfCreateListener = new CreatePDFAction(this, data, getRMSheetAdapter(), true, longRunAdapter);
        menuCreatePDF.addActionListener(pdfCreateListener);
        file.add(menuCreatePDF);
        Bindings.bind(menuCreatePDF, "enabled", enableMenuExportPDFValueHolder);
        JMenuItem menuCreatePDFShort = new JMenuItem(RESOURCE.getString("ui.menu.exportpdf.short"), UIConstants.ICON_PDF);
        CreatePDFAction pdfCreateListenerNpc = new CreatePDFAction(this, data, getRMSheetAdapter(), false, longRunAdapter);
        menuCreatePDFShort.addActionListener(pdfCreateListenerNpc);
        file.add(menuCreatePDFShort);
        Bindings.bind(menuCreatePDFShort, "enabled", enableMenuExportPDFValueHolder);
        JMenuItem menuSave = new JMenuItem(RESOURCE.getString("ui.menu.save"), UIConstants.ICON_SAVE);
        menuSave.setEnabled(false);
        SaveAction saveAction = new SaveAction(this, getRMSheetAdapter());
        menuSave.addActionListener(saveAction);
        file.add(menuSave);
        Bindings.bind(menuSave, "enabled", enableMenuSaveValueHolder);
        JMenuItem menuSaveAs = new JMenuItem(RESOURCE.getString("ui.menu.saveas"), UIConstants.ICON_SAVE_AS);
        SaveAsAction saveAsAction = new SaveAsAction(this, getRMSheetAdapter());
        menuSaveAs.addActionListener(saveAsAction);
        file.add(menuSaveAs);
        JMenu generatorMenu = new JMenu(RESOURCE.getString("ui.menu.generator"));
        bar.add(generatorMenu);
        JMenuItem menuGenName = new JMenuItem(RESOURCE.getString("ui.menu.generatename"), UIConstants.ICON_GEN_NAME);
        generatorMenu.add(menuGenName);
        menuGenName.addActionListener(new CharacterNameAction(data, getCharacteristicsAdapter(), getRMSheetAdapter()));
        JMenuItem menuGenNPC = new JMenuItem(RESOURCE.getString("ui.menu.generatecharacter"), UIConstants.ICON_GEN_ALL);
        generatorMenu.add(menuGenNPC);
        menuGenNPC.addActionListener(new CharacterGeneratorAction(this, data, getRMSheetAdapter(), getCharacteristicsAdapter(), longRunAdapter));
        Bindings.bind(menuGenNPC, "enabled", enableValueHolder);
        JMenu helpMenu = new JMenu(RESOURCE.getString("ui.meni.help"));
        bar.add(helpMenu);
        JMenuItem aboutMenu = new JMenuItem(RESOURCE.getString("ui.menu.about"), UIConstants.ICON_HELP);
        helpMenu.add(aboutMenu);
        aboutMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String about = RMOffice.getProgramString() + "\n\n" + RESOURCE.getString("ui.about.configfilepath") + "\n" + RMPreferences.getPropertiesFilePath();
                JideOptionPane.showMessageDialog(RMFrame.this, about, RESOURCE.getString("ui.menu.about"), JideOptionPane.INFORMATION_MESSAGE, UIConstants.RMO_LOGO125);
            }
        });
        newLatestVersionMenu = new JMenu(RESOURCE.getString("ui.menu.newversion"));
        newLatestVersionMenu.setVisible(false);
        bar.add(newLatestVersionMenu);
        JMenuItem getNewVersionMenu = new JMenuItem(RESOURCE.getString("ui.menu.getnewversion"), UIConstants.ICON_ARRDOWN);
        newLatestVersionMenu.add(getNewVersionMenu);
        getNewVersionMenu.addActionListener(new DesktopAction(URI.create("http://sourceforge.net/projects/rmoffice/")));
        setJMenuBar(bar);
    }

    private void clearUi() {
    }

    private void sheet2ui() {
        skillModel.updateTable();
        skillcatModel.updateTable();
        for (SkillCategory gr : modifiableSkillgroups) {
            skillcostFields.get(gr).setText(sheet.getSkillcost(gr).toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshing = true;
        try {
            if (RMSheet.PROPERTY_SKILL_CATEGORIES.equals(evt.getPropertyName())) {
                skillSelectionRootNode.removeAllChildren();
                createSkillSelectionData(skillSelectionRootNode);
                DefaultTreeModel model = (DefaultTreeModel) skillsSelectionTree.getModel();
                model.setRoot(skillSelectionRootNode);
            } else if (RMSheet.PROPERTY_SHEET_ENABLE_ALL.equals(evt.getPropertyName())) {
                boolean enabled = ((Boolean) evt.getNewValue()).booleanValue();
                setAllEnabled(enabled);
            } else if (RMSheet.PROPERTY_SKILL_STRUCTURE_CHANGED.equals(evt.getPropertyName())) {
                skillModel.updateTable();
                for (SkillCategory sg : modifiableSkillgroups) {
                    skillcostFields.get(sg).setText(sheet.getSkillcost(sg).toString());
                }
            } else if (RMSheet.PROPERTY_SKILLCATEGORY_CHANGED.equals(evt.getPropertyName())) {
                skillcatModel.updateTable();
            } else if (RMSheet.PROPERTY_SKILLS_CHANGED.equals(evt.getPropertyName())) {
                skillModel.skillValuesChanged();
            } else if (RMSheet.PROPERTY_STATE.equals(evt.getPropertyName())) {
                enableMenuExportPDFValueHolder.setValue(State.NORMAL.equals(evt.getNewValue()));
            }
        } finally {
            refreshing = false;
        }
    }

    private void setAllEnabled(boolean enabled) {
        for (JComponent comp : allComponents) {
            comp.setEnabled(enabled);
        }
        enableValueHolder.setValue(enabled);
        if (enabled) {
            refreshing = true;
            sheet2ui();
            refreshing = false;
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        enableMenuSaveValueHolder.setValue(currentFile != null);
    }

    private <T extends JComponent> T registerComp(T comp) {
        allComponents.add(comp);
        return comp;
    }

    /** Bonus format 
	 * @param number the number
	 * @return a formatted string */
    public static String format(int number) {
        if (number > 0) {
            return "+" + number;
        } else if (number == 0) {
            return "0";
        }
        return "" + number;
    }

    /**
	 * 
	 */
    private void modifyExistingISkill() {
        if (skillsTable.getSelectedRow() < 0) {
            return;
        }
        int actualRow = skillsTable.convertRowIndexToModel(skillsTable.getSelectedRow());
        ISkill skill = skillModel.getSkillAtRow(actualRow);
        if (skill != null) {
            ModifySkillDialog dialog = new ModifySkillDialog(skill, sheet.getSkillTypeCustom(skill));
            JOptionPane pane = new JOptionPane(dialog, JideOptionPane.QUESTION_MESSAGE, JideOptionPane.OK_CANCEL_OPTION);
            JDialog d = pane.createDialog(RMFrame.this, RESOURCE.getString("ui.skills.btEdit.title"));
            pane.selectInitialValue();
            d.setVisible(true);
            d.dispose();
            Object result = pane.getValue();
            if (result != null && result instanceof Integer && ((Integer) result).intValue() == JOptionPane.OK_OPTION) {
                sheet.modifySkill(skill, dialog.getSkillName(), dialog.getSkillType());
            }
        }
    }

    /**
	 * 
	 */
    private void addCurrentSelectedISkill(boolean withModifiedName) {
        TreePath selectionPath = skillsSelectionTree.getSelectionPath();
        if (selectionPath != null) {
            Object lastPathComponent = selectionPath.getLastPathComponent();
            if (lastPathComponent != null && lastPathComponent instanceof SkillTreeNode) {
                SkillTreeNode node = (SkillTreeNode) lastPathComponent;
                if (node.getUserObject() instanceof ISkill) {
                    ISkill skill = (ISkill) node.getUserObject();
                    if (withModifiedName) {
                        ModifySkillDialog dialog = new ModifySkillDialog(skill, null);
                        JOptionPane pane = new JOptionPane(dialog, JideOptionPane.QUESTION_MESSAGE, JideOptionPane.OK_CANCEL_OPTION);
                        JDialog d = pane.createDialog(RMFrame.this, RESOURCE.getString("ui.skills.btEditBeforeAdd.title"));
                        pane.selectInitialValue();
                        d.setVisible(true);
                        d.dispose();
                        Object result = pane.getValue();
                        if (result != null && result instanceof Integer && ((Integer) result).intValue() == JOptionPane.OK_OPTION) {
                            skillModel.addSkill(skill, dialog.getSkillName(), dialog.getSkillType());
                        }
                    } else if (!sheet.hasSkillRank(skill)) {
                        skillModel.addSkill(skill);
                    }
                }
            }
        }
    }

    private void initPropertyChangeListener() {
        sheet.addPropertyChangeListener(RMFrame.this);
        if (sheet.getMagicalitems() != null) {
            for (MagicalItem mi : sheet.getMagicalitems()) {
                mi.init(getRMSheetAdapter());
                for (MagicalFeature mf : mi.getFeatures()) {
                    mf.init(getRMSheetAdapter());
                }
            }
        }
    }

    private void removePropertyChangeListener() {
        sheet.removePropertyChangeListener(RMFrame.this);
    }

    private BeanAdapter<Characteristics> getCharacteristicsAdapter() {
        if (characteristicsAdapter == null) {
            characteristicsAdapter = new BeanAdapter<Characteristics>(sheet.getCharacteristics(), true);
        }
        return characteristicsAdapter;
    }

    private BeanAdapter<Coins> getCoinsAdapter() {
        if (coins == null) {
            coins = new BeanAdapter<Coins>(sheet.getCoins(), true);
        }
        return coins;
    }

    private BeanAdapter<RMSheet> getRMSheetAdapter() {
        if (rmsheetAdapter == null) {
            rmsheetAdapter = new BeanAdapter<RMSheet>(sheet, true);
        }
        return rmsheetAdapter;
    }

    private BeanAdapter<StatValueModelAdapter> getStatValueModelAdapter() {
        if (statValueModelAdapter == null) {
            statValueModelAdapter = new BeanAdapter<StatValueModelAdapter>(new StatValueModelAdapter(getRMSheetAdapter()), true);
        }
        return statValueModelAdapter;
    }

    private void updateAdapters() {
        getRMSheetAdapter().setBean(sheet);
        getCharacteristicsAdapter().setBean(sheet.getCharacteristics());
        getCoinsAdapter().setBean(sheet.getCoins());
    }

    /**
	 * 
	 * @param latestVersion
	 */
    public void newLatestVersion(String latestVersion) {
        String menuLabel = MessageFormat.format(RESOURCE.getString("ui.menu.newversion"), latestVersion);
        newLatestVersionMenu.setText(menuLabel);
        newLatestVersionMenu.setVisible(true);
    }
}
