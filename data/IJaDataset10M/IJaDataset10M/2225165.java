package jmri.jmrit.beantable.oblock;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.beans.PropertyVetoException;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import jmri.util.com.sun.TransferActionListener;
import jmri.util.table.ButtonEditor;
import jmri.util.table.ButtonRenderer;
import jmri.InstanceManager;
import jmri.Path;
import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.OBlockManager;
import jmri.jmrit.logix.OPath;
import jmri.jmrit.logix.WarrantTableAction;

public class TableFrames extends jmri.util.JmriJFrame implements InternalFrameListener {

    static int ROW_HEIGHT;

    public static final int STRUT_SIZE = 10;

    public static final ResourceBundle rbo = ResourceBundle.getBundle("jmri.jmrit.beantable.OBlockTableBundle");

    OBlockTableModel _oBlockModel;

    PortalTableModel _portalModel;

    BlockPortalTableModel _blockPortalXRefModel;

    SignalTableModel _signalModel;

    JScrollPane _blockTablePane;

    JScrollPane _portalTablePane;

    JScrollPane _signalTablePane;

    JDesktopPane _desktop;

    JInternalFrame _blockTableFrame;

    JInternalFrame _portalTableFrame;

    JInternalFrame _blockPortalXRefFrame;

    JInternalFrame _signalTableFrame;

    boolean _showWarnings = true;

    JMenuItem _showWarnItem;

    JMenu _openMenu;

    HashMap<String, JInternalFrame> _blockPathMap = new HashMap<String, JInternalFrame>();

    HashMap<String, JInternalFrame> _PathTurnoutMap = new HashMap<String, JInternalFrame>();

    public TableFrames() {
        this("OBlock Table");
    }

    public TableFrames(String actionName) {
        super(actionName);
    }

    public void initComponents() {
        setTitle(rbo.getString("TitleOBlocks"));
        JMenuBar menuBar = new JMenuBar();
        ResourceBundle rb = ResourceBundle.getBundle("apps.AppsBundle");
        JMenu fileMenu = new JMenu(rb.getString("MenuFile"));
        fileMenu.add(new jmri.configurexml.SaveMenu());
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu(rb.getString("MenuEdit"));
        editMenu.setMnemonic(KeyEvent.VK_E);
        TransferActionListener actionListener = new TransferActionListener();
        JMenuItem menuItem = new JMenuItem(rb.getString("MenuItemCut"));
        menuItem.setActionCommand((String) TransferHandler.getCutAction().getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_T);
        editMenu.add(menuItem);
        menuItem = new JMenuItem(rb.getString("MenuItemCopy"));
        menuItem.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_C);
        editMenu.add(menuItem);
        menuItem = new JMenuItem(rb.getString("MenuItemPaste"));
        menuItem.setActionCommand((String) TransferHandler.getPasteAction().getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_P);
        editMenu.add(menuItem);
        menuBar.add(editMenu);
        JMenu optionMenu = new JMenu(rbo.getString("MenuOptions"));
        _showWarnItem = new JMenuItem(rbo.getString("SuppressWarning"));
        _showWarnItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String cmd = event.getActionCommand();
                setShowWarnings(cmd);
            }
        });
        optionMenu.add(_showWarnItem);
        setShowWarnings("ShowWarning");
        menuBar.add(optionMenu);
        _openMenu = new JMenu(rbo.getString("OpenMenu"));
        updateOpenMenu();
        menuBar.add(_openMenu);
        setJMenuBar(menuBar);
        addHelpMenu("package.jmri.jmrit.logix.OBlockTable", true);
        _desktop = new JDesktopPane();
        _desktop.putClientProperty("JDesktopPane.dragMode", "outline");
        _desktop.setPreferredSize(new Dimension(1100, 550));
        setContentPane(_desktop);
        _blockTableFrame = makeBlockFrame();
        _blockTableFrame.setVisible(true);
        _desktop.add(_blockTableFrame);
        _portalTableFrame = makePortalFrame();
        _portalTableFrame.setVisible(true);
        _desktop.add(_portalTableFrame);
        _signalTableFrame = makeSignalFrame();
        _signalTableFrame.setVisible(true);
        _desktop.add(_signalTableFrame);
        _blockPortalXRefFrame = makeBlockPortalFrame();
        _blockPortalXRefFrame.setVisible(false);
        _desktop.add(_blockPortalXRefFrame);
        setLocation(10, 30);
        setVisible(true);
        pack();
        errorCheck();
    }

    protected final JScrollPane getBlockTablePane() {
        return _blockTablePane;
    }

    protected final JScrollPane getPortalTablePane() {
        return _portalTablePane;
    }

    protected final JScrollPane getSignalTablePane() {
        return _signalTablePane;
    }

    protected final OBlockTableModel getBlockModel() {
        return _oBlockModel;
    }

    protected final PortalTableModel getPortalModel() {
        return _portalModel;
    }

    protected final SignalTableModel getSignalModel() {
        return _signalModel;
    }

    protected final BlockPortalTableModel getXRefModel() {
        return _blockPortalXRefModel;
    }

    private void setShowWarnings(String cmd) {
        if (cmd.equals("ShowWarning")) {
            _showWarnings = true;
            _showWarnItem.setActionCommand("SuppressWarning");
            _showWarnItem.setText(rbo.getString("SuppressWarning"));
        } else {
            _showWarnings = false;
            _showWarnItem.setActionCommand("ShowWarning");
            _showWarnItem.setText(rbo.getString("ShowWarning"));
        }
        if (log.isDebugEnabled()) log.debug("setShowWarnings: _showWarnings= " + _showWarnings);
    }

    /**
    * Add the cut/copy/paste actions to the action map.
    */
    private void setActionMappings(JTable table) {
        ActionMap map = table.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
    }

    public void windowClosing(java.awt.event.WindowEvent e) {
        errorCheck();
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        if (log.isDebugEnabled()) log.debug("windowClosing: " + toString());
    }

    private void errorCheck() {
        WarrantTableAction.initPathPortalCheck();
        OBlockManager manager = InstanceManager.oBlockManagerInstance();
        String[] sysNames = manager.getSystemNameArray();
        for (int i = 0; i < sysNames.length; i++) {
            WarrantTableAction.checkPathPortals(manager.getBySystemName(sysNames[i]));
        }
        if (_showWarnings) WarrantTableAction.showPathPortalErrors();
    }

    protected void updateOpenMenu() {
        _openMenu.removeAll();
        JMenuItem openBlock = new JMenuItem(rbo.getString("OpenBlockMenu"));
        _openMenu.add(openBlock);
        openBlock.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                _blockTableFrame.setVisible(true);
                try {
                    _blockTableFrame.setIcon(false);
                } catch (PropertyVetoException pve) {
                    log.warn("Block Table Frame vetoed setIcon " + pve.toString());
                }
                _blockTableFrame.moveToFront();
            }
        });
        JMenuItem openPortal = new JMenuItem(rbo.getString("OpenPortalMenu"));
        _openMenu.add(openPortal);
        openPortal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                _portalTableFrame.setVisible(true);
                try {
                    _portalTableFrame.setIcon(false);
                } catch (PropertyVetoException pve) {
                    log.warn("Portal Table Frame vetoed setIcon " + pve.toString());
                }
                _portalTableFrame.moveToFront();
            }
        });
        JMenuItem openXRef = new JMenuItem(rbo.getString("OpenXRefMenu"));
        _openMenu.add(openXRef);
        openXRef.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                _blockPortalXRefFrame.setVisible(true);
                try {
                    _blockPortalXRefFrame.setIcon(false);
                } catch (PropertyVetoException pve) {
                    log.warn("XRef Table Frame vetoed setIcon " + pve.toString());
                }
                _blockPortalXRefFrame.moveToFront();
            }
        });
        JMenuItem openSignal = new JMenuItem(rbo.getString("OpenSignalMenu"));
        _openMenu.add(openSignal);
        openSignal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                _signalTableFrame.setVisible(true);
                try {
                    _signalTableFrame.setIcon(false);
                } catch (PropertyVetoException pve) {
                    log.warn("Signal Table Frame vetoed setIcon " + pve.toString());
                }
                _signalTableFrame.moveToFront();
            }
        });
        JMenu openBlockPath = new JMenu(rbo.getString("OpenBlockPathMenu"));
        ActionListener openFrameAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String sysName = e.getActionCommand();
                openBlockPathFrame(sysName);
            }
        };
        OBlockManager manager = InstanceManager.oBlockManagerInstance();
        String[] sysNames = manager.getSystemNameArray();
        for (int i = 0; i < sysNames.length; i++) {
            OBlock block = manager.getBySystemName(sysNames[i]);
            JMenuItem mi = new JMenuItem(java.text.MessageFormat.format(rbo.getString("OpenPathMenu"), block.getDisplayName()));
            mi.setActionCommand(sysNames[i]);
            mi.addActionListener(openFrameAction);
            openBlockPath.add(mi);
        }
        _openMenu.add(openBlockPath);
        JMenu openTurnoutPath = new JMenu(rbo.getString("OpenBlockPathTurnoutMenu"));
        sysNames = manager.getSystemNameArray();
        for (int i = 0; i < sysNames.length; i++) {
            OBlock block = manager.getBySystemName(sysNames[i]);
            JMenu openTurnoutMenu = new JMenu(java.text.MessageFormat.format(rbo.getString("OpenTurnoutMenu"), block.getDisplayName()));
            openTurnoutPath.add(openTurnoutMenu);
            openFrameAction = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String pathTurnoutName = e.getActionCommand();
                    openPathTurnoutFrame(pathTurnoutName);
                }
            };
            Iterator<Path> iter = block.getPaths().iterator();
            while (iter.hasNext()) {
                OPath path = (OPath) iter.next();
                JMenuItem mi = new JMenuItem(java.text.MessageFormat.format(rbo.getString("OpenPathTurnoutMenu"), path.getName()));
                mi.setActionCommand(makePathTurnoutName(sysNames[i], path.getName()));
                mi.addActionListener(openFrameAction);
                openTurnoutMenu.add(mi);
            }
        }
        _openMenu.add(openTurnoutPath);
    }

    /***********************  BlockFrame ******************************/
    protected JInternalFrame makeBlockFrame() {
        JInternalFrame frame = new JInternalFrame(rbo.getString("TitleBlockTable"), true, false, false, true);
        _oBlockModel = new OBlockTableModel(this);
        _oBlockModel.init();
        JTable blockTable = new DnDJTable(_oBlockModel, new int[] { OBlockTableModel.EDIT_COL, OBlockTableModel.DELETE_COL, OBlockTableModel.UNITSCOL });
        blockTable.setDefaultEditor(JComboBox.class, new jmri.jmrit.symbolicprog.ValueEditor());
        blockTable.getColumnModel().getColumn(OBlockTableModel.EDIT_COL).setCellEditor(new ButtonEditor(new JButton()));
        blockTable.getColumnModel().getColumn(OBlockTableModel.EDIT_COL).setCellRenderer(new ButtonRenderer());
        blockTable.getColumnModel().getColumn(OBlockTableModel.DELETE_COL).setCellEditor(new ButtonEditor(new JButton()));
        blockTable.getColumnModel().getColumn(OBlockTableModel.DELETE_COL).setCellRenderer(new ButtonRenderer());
        blockTable.getColumnModel().getColumn(OBlockTableModel.UNITSCOL).setCellRenderer(new MyBooleanRenderer());
        JComboBox box = new JComboBox(OBlockTableModel.curveOptions);
        blockTable.getColumnModel().getColumn(OBlockTableModel.CURVECOL).setCellEditor(new DefaultCellEditor(box));
        for (int i = 0; i < _oBlockModel.getColumnCount(); i++) {
            int width = _oBlockModel.getPreferredWidth(i);
            blockTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        blockTable.sizeColumnsToFit(-1);
        blockTable.setDragEnabled(true);
        setActionMappings(blockTable);
        ROW_HEIGHT = blockTable.getRowHeight();
        int tableWidth = blockTable.getPreferredSize().width;
        blockTable.setPreferredScrollableViewportSize(new java.awt.Dimension(tableWidth, ROW_HEIGHT * 10));
        _blockTablePane = new JScrollPane(blockTable);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        JLabel prompt = new JLabel(rbo.getString("AddBlockPrompt"));
        contentPane.add(prompt, BorderLayout.NORTH);
        contentPane.add(_blockTablePane, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createVerticalStrut(STRUT_SIZE));
        contentPane.add(panel, BorderLayout.SOUTH);
        frame.setContentPane(contentPane);
        frame.pack();
        return frame;
    }

    /***********************  PortalFrame ******************************/
    protected JInternalFrame makePortalFrame() {
        JInternalFrame frame = new JInternalFrame(rbo.getString("TitlePortalTable"), true, false, false, true);
        _portalModel = new PortalTableModel(this);
        _portalModel.init();
        JTable portalTable = new DnDJTable(_portalModel, new int[] { PortalTableModel.DELETE_COL });
        portalTable.getColumnModel().getColumn(PortalTableModel.DELETE_COL).setCellEditor(new ButtonEditor(new JButton()));
        portalTable.getColumnModel().getColumn(PortalTableModel.DELETE_COL).setCellRenderer(new ButtonRenderer());
        for (int i = 0; i < _portalModel.getColumnCount(); i++) {
            int width = _portalModel.getPreferredWidth(i);
            portalTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        portalTable.sizeColumnsToFit(-1);
        portalTable.setDragEnabled(true);
        setActionMappings(portalTable);
        int tableWidth = portalTable.getPreferredSize().width;
        portalTable.setPreferredScrollableViewportSize(new java.awt.Dimension(tableWidth, ROW_HEIGHT * 10));
        _portalTablePane = new JScrollPane(portalTable);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        JLabel prompt = new JLabel(rbo.getString("AddPortalPrompt"));
        contentPane.add(prompt, BorderLayout.NORTH);
        contentPane.add(_portalTablePane, BorderLayout.CENTER);
        frame.setContentPane(contentPane);
        frame.setLocation(0, 200);
        frame.pack();
        return frame;
    }

    /***********************  BlockPortalFrame ******************************/
    protected JInternalFrame makeBlockPortalFrame() {
        JInternalFrame frame = new JInternalFrame(rbo.getString("TitleBlockPortalXRef"), true, false, false, true);
        _blockPortalXRefModel = new BlockPortalTableModel(_oBlockModel);
        JTable blockPortalTable = new DnDJTable(_blockPortalXRefModel, new int[] { BlockPortalTableModel.BLOCK_NAME_COLUMN, BlockPortalTableModel.PORTAL_NAME_COLUMN });
        blockPortalTable.setDefaultRenderer(String.class, new jmri.jmrit.symbolicprog.ValueRenderer());
        blockPortalTable.setDefaultEditor(String.class, new jmri.jmrit.symbolicprog.ValueEditor());
        for (int i = 0; i < _blockPortalXRefModel.getColumnCount(); i++) {
            int width = _blockPortalXRefModel.getPreferredWidth(i);
            blockPortalTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        blockPortalTable.sizeColumnsToFit(-1);
        blockPortalTable.setDragEnabled(true);
        setActionMappings(blockPortalTable);
        int tableWidth = blockPortalTable.getPreferredSize().width;
        blockPortalTable.setPreferredScrollableViewportSize(new java.awt.Dimension(tableWidth, ROW_HEIGHT * 25));
        JScrollPane tablePane = new JScrollPane(blockPortalTable);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.add(tablePane, BorderLayout.CENTER);
        frame.addInternalFrameListener(this);
        frame.setContentPane(contentPane);
        frame.setLocation(700, 30);
        frame.pack();
        return frame;
    }

    /***********************  SignalFrame ******************************/
    protected JInternalFrame makeSignalFrame() {
        JInternalFrame frame = new JInternalFrame(rbo.getString("TitleSignalTable"), true, false, false, true);
        _signalModel = new SignalTableModel(this);
        _signalModel.init();
        JTable signalTable = new DnDJTable(_signalModel, new int[] { SignalTableModel.DELETE_COL });
        signalTable.getColumnModel().getColumn(SignalTableModel.DELETE_COL).setCellEditor(new ButtonEditor(new JButton()));
        signalTable.getColumnModel().getColumn(SignalTableModel.DELETE_COL).setCellRenderer(new ButtonRenderer());
        for (int i = 0; i < _signalModel.getColumnCount(); i++) {
            int width = _signalModel.getPreferredWidth(i);
            signalTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        signalTable.sizeColumnsToFit(-1);
        signalTable.setDragEnabled(true);
        setActionMappings(signalTable);
        int tableWidth = signalTable.getPreferredSize().width;
        signalTable.setPreferredScrollableViewportSize(new java.awt.Dimension(tableWidth, ROW_HEIGHT * 8));
        _signalTablePane = new JScrollPane(signalTable);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        JLabel prompt = new JLabel(rbo.getString("AddSignalPrompt"));
        contentPane.add(prompt, BorderLayout.NORTH);
        contentPane.add(_signalTablePane, BorderLayout.CENTER);
        frame.setContentPane(contentPane);
        frame.setLocation(200, 350);
        frame.pack();
        return frame;
    }

    /***********************  BlockPathFrame ******************************/
    static class BlockPathFrame extends JInternalFrame {

        BlockPathTableModel blockPathModel;

        public BlockPathFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
            super(title, resizable, closable, maximizable, iconifiable);
        }

        public void init(OBlock block, TableFrames parent) {
            blockPathModel = new BlockPathTableModel(block, parent);
        }

        public BlockPathTableModel getModel() {
            return blockPathModel;
        }
    }

    /***********************  BlockPathFrame ******************************/
    protected BlockPathFrame makeBlockPathFrame(OBlock block) {
        String title = java.text.MessageFormat.format(rbo.getString("TitleBlockPathTable"), block.getDisplayName());
        BlockPathFrame frame = new BlockPathFrame(title, true, true, false, true);
        if (log.isDebugEnabled()) log.debug("makeBlockPathFrame for Block " + block.getDisplayName());
        frame.setName(block.getSystemName());
        frame.init(block, this);
        BlockPathTableModel blockPathModel = frame.getModel();
        blockPathModel.init();
        JTable blockPathTable = new DnDJTable(blockPathModel, new int[] { BlockPathTableModel.EDIT_COL, BlockPathTableModel.DELETE_COL });
        blockPathTable.getColumnModel().getColumn(BlockPathTableModel.EDIT_COL).setCellEditor(new ButtonEditor(new JButton()));
        blockPathTable.getColumnModel().getColumn(BlockPathTableModel.EDIT_COL).setCellRenderer(new ButtonRenderer());
        blockPathTable.getColumnModel().getColumn(BlockPathTableModel.DELETE_COL).setCellEditor(new ButtonEditor(new JButton()));
        blockPathTable.getColumnModel().getColumn(BlockPathTableModel.DELETE_COL).setCellRenderer(new ButtonRenderer());
        blockPathTable.setDragEnabled(true);
        setActionMappings(blockPathTable);
        for (int i = 0; i < blockPathModel.getColumnCount(); i++) {
            int width = blockPathModel.getPreferredWidth(i);
            blockPathTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        blockPathTable.sizeColumnsToFit(-1);
        int tableWidth = blockPathTable.getPreferredSize().width;
        blockPathTable.setPreferredScrollableViewportSize(new java.awt.Dimension(tableWidth, ROW_HEIGHT * 10));
        JScrollPane tablePane = new JScrollPane(blockPathTable);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        JLabel prompt = new JLabel(rbo.getString("AddPathPrompt"));
        contentPane.add(prompt, BorderLayout.NORTH);
        contentPane.add(tablePane, BorderLayout.CENTER);
        frame.addInternalFrameListener(this);
        frame.setContentPane(contentPane);
        frame.setLocation(50, 30);
        frame.pack();
        return frame;
    }

    /***********************  PathTurnoutFrame ******************************/
    protected JInternalFrame makePathTurnoutFrame(OBlock block, String pathName) {
        String title = java.text.MessageFormat.format(rbo.getString("TitlePathTurnoutTable"), block.getDisplayName(), pathName);
        JInternalFrame frame = new JInternalFrame(title, true, true, false, true);
        if (log.isDebugEnabled()) log.debug("makePathTurnoutFrame for Block " + block.getDisplayName() + " and Path " + pathName);
        frame.setName(makePathTurnoutName(block.getSystemName(), pathName));
        OPath path = block.getPathByName(pathName);
        if (path == null) {
            return null;
        }
        PathTurnoutTableModel PathTurnoutModel = new PathTurnoutTableModel(path);
        PathTurnoutModel.init();
        JTable PathTurnoutTable = new DnDJTable(PathTurnoutModel, new int[] { PathTurnoutTableModel.SETTINGCOLUMN, PathTurnoutTableModel.DELETE_COL });
        JComboBox box = new JComboBox(PathTurnoutTableModel.turnoutStates);
        PathTurnoutTable.getColumnModel().getColumn(PathTurnoutTableModel.SETTINGCOLUMN).setCellEditor(new DefaultCellEditor(box));
        PathTurnoutTable.getColumnModel().getColumn(PathTurnoutTableModel.DELETE_COL).setCellEditor(new ButtonEditor(new JButton()));
        PathTurnoutTable.getColumnModel().getColumn(PathTurnoutTableModel.DELETE_COL).setCellRenderer(new ButtonRenderer());
        for (int i = 0; i < PathTurnoutModel.getColumnCount(); i++) {
            int width = PathTurnoutModel.getPreferredWidth(i);
            PathTurnoutTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        PathTurnoutTable.sizeColumnsToFit(-1);
        PathTurnoutTable.setDragEnabled(true);
        setActionMappings(PathTurnoutTable);
        int tableWidth = PathTurnoutTable.getPreferredSize().width;
        PathTurnoutTable.setPreferredScrollableViewportSize(new java.awt.Dimension(tableWidth, ROW_HEIGHT * 5));
        JScrollPane tablePane = new JScrollPane(PathTurnoutTable);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        JLabel prompt = new JLabel(rbo.getString("AddTurnoutPrompt"));
        contentPane.add(prompt, BorderLayout.NORTH);
        contentPane.add(tablePane, BorderLayout.CENTER);
        frame.addInternalFrameListener(this);
        frame.setContentPane(contentPane);
        frame.setLocation(10, 270);
        frame.pack();
        return frame;
    }

    protected void openBlockPathFrame(String sysName) {
        JInternalFrame frame = _blockPathMap.get(sysName);
        if (frame == null) {
            OBlock block = InstanceManager.oBlockManagerInstance().getBySystemName(sysName);
            if (block == null) {
                return;
            }
            frame = makeBlockPathFrame(block);
            _blockPathMap.put(sysName, frame);
            frame.setVisible(true);
            _desktop.add(frame);
            frame.moveToFront();
        } else {
            frame.setVisible(true);
            try {
                frame.setIcon(false);
            } catch (PropertyVetoException pve) {
                log.warn("BlockPath Table Frame for \"" + sysName + "\" vetoed setIcon " + pve.toString());
            }
            frame.moveToFront();
        }
    }

    protected String makePathTurnoutName(String blockSysName, String pathName) {
        return "%" + pathName + "&" + blockSysName;
    }

    protected void openPathTurnoutFrame(String pathTurnoutName) {
        JInternalFrame frame = _PathTurnoutMap.get(pathTurnoutName);
        log.debug("openPathTurnoutFrame for " + pathTurnoutName);
        if (frame == null) {
            int index = pathTurnoutName.indexOf('&');
            String pathName = pathTurnoutName.substring(1, index);
            String sysName = pathTurnoutName.substring(index + 1);
            OBlock block = InstanceManager.oBlockManagerInstance().getBySystemName(sysName);
            if (block == null) {
                return;
            }
            frame = makePathTurnoutFrame(block, pathName);
            if (frame == null) {
                return;
            }
            _PathTurnoutMap.put(pathTurnoutName, frame);
            frame.setVisible(true);
            _desktop.add(frame);
            frame.moveToFront();
        } else {
            frame.setVisible(true);
            try {
                frame.setIcon(false);
            } catch (PropertyVetoException pve) {
                log.warn("PathTurnout Table Frame for \"" + pathTurnoutName + "\" vetoed setIcon " + pve.toString());
            }
            frame.moveToFront();
        }
    }

    static class MyBooleanRenderer extends javax.swing.table.DefaultTableCellRenderer {

        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel val;
            if (value instanceof Boolean) {
                if (((Boolean) value).booleanValue()) {
                    val = new JLabel("cm");
                } else {
                    val = new JLabel("in");
                }
            } else {
                val = new JLabel("");
            }
            val.setFont(table.getFont().deriveFont(java.awt.Font.PLAIN));
            return val;
        }
    }

    /*********************** InternalFrameListener implementatiom ******************/
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    public void internalFrameClosed(InternalFrameEvent e) {
        JInternalFrame frame = (JInternalFrame) e.getSource();
        String name = frame.getName();
        if (log.isDebugEnabled()) log.debug("Internal frame closed: " + frame.getTitle() + ", name= " + name + " size (" + frame.getSize().getWidth() + ", " + frame.getSize().getHeight() + ")");
        if (name != null && name.startsWith("OB")) {
            _blockPathMap.remove(name);
            WarrantTableAction.initPathPortalCheck();
            WarrantTableAction.checkPathPortals(((BlockPathFrame) frame).getModel().getBlock());
            ((BlockPathFrame) frame).getModel().removeListener();
            if (_showWarnings) WarrantTableAction.showPathPortalErrors();
        } else {
            _PathTurnoutMap.remove(name);
        }
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public void internalFrameIconified(InternalFrameEvent e) {
        JInternalFrame frame = (JInternalFrame) e.getSource();
        String name = frame.getName();
        if (log.isDebugEnabled()) log.debug("Internal frame Iconified: " + frame.getTitle() + ", name= " + name + " size (" + frame.getSize().getWidth() + ", " + frame.getSize().getHeight() + ")");
        if (name != null && name.startsWith("OB")) {
            WarrantTableAction.initPathPortalCheck();
            WarrantTableAction.checkPathPortals(((BlockPathFrame) frame).getModel().getBlock());
            if (_showWarnings) WarrantTableAction.showPathPortalErrors();
        }
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameActivated(InternalFrameEvent e) {
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TableFrames.class.getName());
}
