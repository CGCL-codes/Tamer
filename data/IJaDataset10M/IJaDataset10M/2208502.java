package com.javathis.mapeditor;

import com.javathis.utilities.*;
import com.javathis.utilities.ui.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * The primary window for the Map Editor.
 */
public class EditFrame extends JFrame implements MouseListener, MouseMotionListener {

    protected static final ResourceBundle MAIN_RESOURCE_BUNDLE = ResourceBundle.getBundle("com/javathis/mapeditor/properties/EditFrame");

    protected static final String TILE_PREFIX = MAIN_RESOURCE_BUNDLE.getString("frameTitle.Prefex");

    protected static final String LNF_METAL = JTUtilities.LNF_METAL;

    protected static final String LNF_MOTIF = JTUtilities.LNF_MOTIF;

    protected static final String LNF_WINDOWS = JTUtilities.LNF_WINDOWS;

    protected static final String LNF_MAC = JTUtilities.LNF_MAC;

    private static final String FILENAME_TILESET = "tileset.bin";

    private static final String FILENAME_TILESET_IMAGE = "tileset.img";

    private static final String FILENAME_MAP = "map.bin";

    private static final int DEFAULT_TILE_SET_PANEL_WRAP_AT = 4;

    private static final int DEFAULT_MAP_WIDTH = 35;

    private static final int DEFAULT_MAP_HEIGHT = 35;

    private static final Cursor SELECTION_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);

    private static final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);

    private static final Cursor NORMAL_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

    private static final Dimension INITITIAL_WINDOW_SIZE = new Dimension(800, 600);

    private static String currentLookAndFeel = MapEditor.CURRENT_LOOK_AND_FEEL;

    private volatile TileMapTable map;

    private TileSetPanel tileSetPanel;

    private TileSet tileSet;

    private File tileSetImageFile;

    private File mapEditorIcon32File = new File(JTUtilities.fixURLFileString(ClassLoader.getSystemResource("com/javathis/mapeditor/icons/MapEditorIcon32.gif").getFile()));

    private ImageIcon mapEditorIcon32 = JTUtilities.getImageIcon(mapEditorIcon32File.getAbsolutePath());

    private JTStatusBar statusBar = new JTStatusBar(MAIN_RESOURCE_BUNDLE.getString("initialStatusBar.Status"), null, "(col, row)", "(cols, rows)");

    private JScrollPane tileSetScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    private ButtonGroup tileButtonGroup = new ButtonGroup();

    private JTToolBar mainToolBar = new JTToolBar(MAIN_RESOURCE_BUNDLE.getString("mainToolBar.Title"), JTToolBar.APPEARANCE_FLAT);

    private JButton newMapButton = new JButton();

    private JButton openMapButton = new JButton();

    private JButton saveMapButton = new JButton();

    private JButton preferencesButton = new JButton();

    private JButton showGridButton = new JButton();

    private JButton showTraverseRatingButton = new JButton();

    private JButton pointerButton = new JButton();

    private JButton selectionButton = new JButton();

    private JButton fillButton = new JButton();

    private JButton moveButton = new JButton();

    private JMenuBar menuBar = new JMenuBar();

    private JMenu fileMenu = new JMenu(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("file.Text"));

    private JMenu editMenu = new JMenu(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("edit.Text"));

    private JMenu viewMenu = new JMenu(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("view.Text"));

    private JMenu toolsMenu = new JMenu(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("tools.Text"));

    private JMenu helpMenu = new JMenu(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("help.Text"));

    private JMenuItem fileNewMapMenuItem = new JMenuItem(MAIN_RESOURCE_BUNDLE.getString("newMap.Text") + JTUtilities.ELLIPSIS, MAIN_RESOURCE_BUNDLE.getString("newMap.Mnemonic").charAt(0));

    private JMenuItem fileOpenMapMenuItem = new JMenuItem(MAIN_RESOURCE_BUNDLE.getString("openMap.Text") + JTUtilities.ELLIPSIS, MAIN_RESOURCE_BUNDLE.getString("openMap.Mnemonic").charAt(0));

    private JMenuItem fileSaveMapMenuItem = new JMenuItem(MAIN_RESOURCE_BUNDLE.getString("saveMap.Text") + JTUtilities.ELLIPSIS, MAIN_RESOURCE_BUNDLE.getString("saveMap.Mnemonic").charAt(0));

    private JMenuItem fileExitMenuItem = new JMenuItem(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("exit.Text"), JTUtilities.COMMON_RESOURCE_BUNDLE.getString("exit.Mnemonic").charAt(0));

    private JMenuItem editPreferencesMenuItem = new JMenuItem(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("preferences.Text") + JTUtilities.ELLIPSIS, JTUtilities.COMMON_RESOURCE_BUNDLE.getString("preferences.Mnemonic").charAt(0));

    private JCheckBoxMenuItem viewShowGridMenuItem = new JCheckBoxMenuItem(MAIN_RESOURCE_BUNDLE.getString("showGrid.Text"), false);

    private JCheckBoxMenuItem viewShowTraverseRatingMenuItem = new JCheckBoxMenuItem(MAIN_RESOURCE_BUNDLE.getString("showTraverseRating.Text"), false);

    private JRadioButtonMenuItem pointerRadioButtonMenuItem = new JRadioButtonMenuItem(MAIN_RESOURCE_BUNDLE.getString("pointerButton.Text"));

    private JRadioButtonMenuItem selectionRadioButtonMenuItem = new JRadioButtonMenuItem(MAIN_RESOURCE_BUNDLE.getString("selectionButton.Text"));

    private JRadioButtonMenuItem moveRadioButtonMenuItem = new JRadioButtonMenuItem(MAIN_RESOURCE_BUNDLE.getString("moveButton.Text"));

    private JMenuItem fillMenuItem = new JMenuItem(MAIN_RESOURCE_BUNDLE.getString("fillButton.Text"));

    private ButtonGroup mapToolButtonGroup = new ButtonGroup();

    private JMenuItem helpHelpTopics = new JMenuItem(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("helpTopics.Text") + JTUtilities.ELLIPSIS, JTUtilities.COMMON_RESOURCE_BUNDLE.getString("helpTopics.Mnemonic").charAt(0));

    private JMenuItem helpAbout = new JMenuItem(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("about.Text") + JTUtilities.ELLIPSIS, JTUtilities.COMMON_RESOURCE_BUNDLE.getString("about.Mnemonic").charAt(0));

    private JPopupMenu mapPopupMenu = new JPopupMenu();

    private JCheckBoxMenuItem mapPopupShowGridMenuItem = new JCheckBoxMenuItem(MAIN_RESOURCE_BUNDLE.getString("showGrid.Text"), false);

    private JCheckBoxMenuItem mapPopupShowTraverseRatingMenuItem = new JCheckBoxMenuItem(MAIN_RESOURCE_BUNDLE.getString("showTraverseRating.Text"), false);

    private JMenuItem mapPopupChangeTraverseRatingMenuItem = new JMenuItem(MAIN_RESOURCE_BUNDLE.getString("changeTraverseRating.Text") + JTUtilities.ELLIPSIS, MAIN_RESOURCE_BUNDLE.getString("changeTraverseRating.Mnemonic").charAt(0));

    private JTOrnateAboutBox aboutBox = new JTOrnateAboutBox(this, MAIN_RESOURCE_BUNDLE.getString("aboutBox.Title"));

    private boolean isBusy;

    private boolean isSelecting = false;

    private boolean isMovingMap = false;

    private Dimension tileSize;

    private String tempDirectory = System.getProperty("java.io.tmpdir");

    private static byte[] mutex = new byte[0];

    public EditFrame(TileSet tileSet, File tileSetImageFile) {
        this.tileSet = tileSet;
        this.tileSetImageFile = tileSetImageFile;
        mapInit(DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT);
        guiInit();
        mapGuiInit();
        registerMapEvents();
        registerEvents();
    }

    private void mapInit(int columns, int rows) {
        map = null;
        tileSize = null;
        System.gc();
        map = new TileMapTable(columns, rows, tileSet);
        tileSize = new Dimension(tileSet.getTileWidth(), tileSet.getTileHeight());
        updateStatusBarSize(columns, rows);
    }

    private void mapInit(File mapFile) {
        map = null;
        tileSize = null;
        System.gc();
        map = new TileMapTable(mapFile, tileSet);
        tileSize = new Dimension(tileSet.getTileWidth(), tileSet.getTileHeight());
        updateStatusBarSize(map.getColumnCount(), map.getRowCount());
    }

    private void mapGuiInit() {
        this.getContentPane().add(tileSetScrollPane, BorderLayout.EAST);
        this.getContentPane().add(map, BorderLayout.CENTER);
        if (tileSet == MapEditor.DEFAULT_TILE_SET) tileSetPanel = new TileSetPanel(tileSet, DEFAULT_TILE_SET_PANEL_WRAP_AT); else {
            int wrapAt = 0;
            if (tileSize.width > 100) wrapAt = 1; else wrapAt = (int) 200 / tileSize.width;
            tileSetPanel = new TileSetPanel(tileSet, wrapAt);
        }
        tileSetScrollPane.setViewportView(tileSetPanel);
        tileSetScrollPane.getVerticalScrollBar().setBlockIncrement(tileSize.height + 15);
        tileSetScrollPane.getVerticalScrollBar().setUnitIncrement(tileSize.height + 15);
    }

    private void mapGuiUnInit() {
        tileSetScrollPane.getViewport().remove(tileSetPanel);
        getContentPane().remove(tileSetScrollPane);
        getContentPane().remove(map);
    }

    private void guiInit() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        this.setTitle(TILE_PREFIX + " " + MAIN_RESOURCE_BUNDLE.getString("newMap.Text"));
        this.setIconImage(mapEditorIcon32.getImage());
        this.setSize(INITITIAL_WINDOW_SIZE);
        this.getContentPane().add(mainToolBar, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        newMapButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/new16.gif"));
        openMapButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/open16.gif"));
        saveMapButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/save16.gif"));
        preferencesButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/preferences16.gif"));
        showGridButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/grids16.gif"));
        showTraverseRatingButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/showTraverseRating16.gif"));
        newMapButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("newMap.ToolTip"));
        openMapButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("openMap.ToolTip"));
        saveMapButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("saveMap.ToolTip"));
        preferencesButton.setToolTipText(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("preferences.ToolTip"));
        showGridButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("showGrid.ToolTip"));
        showTraverseRatingButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("showTraverseRating.ToolTip"));
        pointerButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/pointer16.gif"));
        selectionButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/selection16.gif"));
        moveButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/move16.gif"));
        fillButton.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/fill16.gif"));
        pointerButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("pointerButton.ToolTip"));
        selectionButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("selectionButton.ToolTip"));
        moveButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("moveButton.ToolTip"));
        fillButton.setToolTipText(MAIN_RESOURCE_BUNDLE.getString("fillButton.ToolTip"));
        pointerRadioButtonMenuItem.setSelected(true);
        handlePointerTool();
        fillButton.setEnabled(false);
        fillMenuItem.setEnabled(false);
        mapToolButtonGroup.add(pointerRadioButtonMenuItem);
        mapToolButtonGroup.add(selectionRadioButtonMenuItem);
        mapToolButtonGroup.add(moveRadioButtonMenuItem);
        mainToolBar.setFloatable(false);
        mainToolBar.add(newMapButton);
        mainToolBar.add(openMapButton);
        mainToolBar.add(saveMapButton);
        mainToolBar.addSeparator();
        mainToolBar.add(preferencesButton);
        mainToolBar.addSeparator();
        mainToolBar.add(showGridButton);
        mainToolBar.add(showTraverseRatingButton);
        mainToolBar.addSeparator();
        mainToolBar.add(pointerButton);
        mainToolBar.add(selectionButton);
        mainToolBar.add(moveButton);
        mainToolBar.addSeparator();
        mainToolBar.add(fillButton);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        fileMenu.setMnemonic(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("file.Mnemonic").charAt(0));
        editMenu.setMnemonic(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("edit.Mnemonic").charAt(0));
        viewMenu.setMnemonic(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("view.Mnemonic").charAt(0));
        toolsMenu.setMnemonic(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("tools.Mnemonic").charAt(0));
        helpMenu.setMnemonic(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("help.Mnemonic").charAt(0));
        fileNewMapMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/new16.gif"));
        fileOpenMapMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/open16.gif"));
        fileSaveMapMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/save16.gif"));
        fileExitMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/empty16.gif"));
        fileMenu.add(fileNewMapMenuItem);
        fileMenu.add(fileOpenMapMenuItem);
        fileMenu.add(fileSaveMapMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(fileExitMenuItem);
        editPreferencesMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/preferences16.gif"));
        editMenu.add(editPreferencesMenuItem);
        viewShowGridMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/grids16.gif"));
        viewShowTraverseRatingMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/showTraverseRating16.gif"));
        viewShowGridMenuItem.setMnemonic(MAIN_RESOURCE_BUNDLE.getString("showGrid.Mnemonic").charAt(0));
        viewShowTraverseRatingMenuItem.setMnemonic(MAIN_RESOURCE_BUNDLE.getString("showTraverseRating.Mnemonic").charAt(0));
        viewMenu.add(viewShowGridMenuItem);
        viewMenu.add(viewShowTraverseRatingMenuItem);
        toolsMenu.add(pointerRadioButtonMenuItem);
        toolsMenu.add(selectionRadioButtonMenuItem);
        toolsMenu.add(moveRadioButtonMenuItem);
        toolsMenu.addSeparator();
        toolsMenu.add(fillMenuItem);
        helpHelpTopics.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/helpTopics16.gif"));
        helpAbout.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/about16.gif"));
        helpMenu.add(helpHelpTopics);
        helpMenu.add(helpAbout);
        mapPopupShowGridMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/grids16.gif"));
        mapPopupShowTraverseRatingMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/showTraverseRating16.gif"));
        mapPopupChangeTraverseRatingMenuItem.setIcon(JTUtilities.getImageIcon("com/javathis/mapeditor/icons/changeTraverseRating16.gif"));
        mapPopupShowGridMenuItem.setMnemonic(MAIN_RESOURCE_BUNDLE.getString("showGrid.Mnemonic").charAt(0));
        mapPopupShowTraverseRatingMenuItem.setMnemonic(MAIN_RESOURCE_BUNDLE.getString("showTraverseRating.Mnemonic").charAt(0));
        mapPopupMenu.add(mapPopupShowGridMenuItem);
        mapPopupMenu.add(mapPopupShowTraverseRatingMenuItem);
        mapPopupMenu.addSeparator();
        mapPopupMenu.add(mapPopupChangeTraverseRatingMenuItem);
        mapPopupMenu.setLightWeightPopupEnabled(false);
        this.setJMenuBar(menuBar);
        aboutBox.setName(MAIN_RESOURCE_BUNDLE.getString("aboutBox.Name"));
        aboutBox.setVersion(MAIN_RESOURCE_BUNDLE.getString("aboutBox.Version"));
        aboutBox.setIcon(mapEditorIcon32File);
        JTUtilities.centerOnScreen(this);
    }

    private void registerMapEvents() {
        map.addMouseListener(this);
        map.addMouseMotionListener(this);
    }

    private void unregisterMapEvents() {
        map.removeMouseListener(this);
        map.removeMouseMotionListener(this);
    }

    private void registerEvents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getGlassPane().addKeyListener(new KeyAdapter() {
        });
        getGlassPane().addMouseListener(new MouseAdapter() {
        });
        getGlassPane().setCursor(WAIT_CURSOR);
        addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent f) {
                if (isBusy) setBusy(isBusy);
            }
        });
        mapPopupChangeTraverseRatingMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleChangeTraverseRating();
            }
        });
        mapPopupShowGridMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewShowGridMenuItem.setSelected(mapPopupShowGridMenuItem.isSelected());
                handleShowGrid(mapPopupShowGridMenuItem.isSelected());
            }
        });
        mapPopupShowTraverseRatingMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewShowTraverseRatingMenuItem.setSelected(mapPopupShowTraverseRatingMenuItem.isSelected());
                handleShowTraverseRating(mapPopupShowTraverseRatingMenuItem.isSelected());
            }
        });
        viewShowGridMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapPopupShowGridMenuItem.setSelected(viewShowGridMenuItem.isSelected());
                handleShowGrid(viewShowGridMenuItem.isSelected());
            }
        });
        viewShowTraverseRatingMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapPopupShowTraverseRatingMenuItem.setSelected(viewShowTraverseRatingMenuItem.isSelected());
                handleShowTraverseRating(viewShowTraverseRatingMenuItem.isSelected());
            }
        });
        editPreferencesMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleDisplayPreferences();
            }
        });
        fileExitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileNewMapMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleNewMap();
            }
        });
        fileOpenMapMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleOpenMap();
            }
        });
        fileSaveMapMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleSaveMap();
            }
        });
        helpHelpTopics.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JTUtilities.showFeatureNotSupportedMessage(EditFrame.this);
            }
        });
        helpAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aboutBox.show();
            }
        });
        newMapButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fileNewMapMenuItem.doClick();
            }
        });
        openMapButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fileOpenMapMenuItem.doClick();
            }
        });
        saveMapButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fileSaveMapMenuItem.doClick();
            }
        });
        preferencesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editPreferencesMenuItem.doClick();
            }
        });
        showGridButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewShowGridMenuItem.doClick();
            }
        });
        showTraverseRatingButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewShowTraverseRatingMenuItem.doClick();
            }
        });
        pointerRadioButtonMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (pointerRadioButtonMenuItem.isSelected()) handlePointerTool();
            }
        });
        selectionRadioButtonMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selectionRadioButtonMenuItem.isSelected()) handleSelectionTool();
            }
        });
        moveRadioButtonMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (moveRadioButtonMenuItem.isSelected()) handleMoveTool();
            }
        });
        fillMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleFillTool();
            }
        });
        pointerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pointerRadioButtonMenuItem.doClick();
            }
        });
        selectionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectionRadioButtonMenuItem.doClick();
            }
        });
        fillButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fillMenuItem.doClick();
            }
        });
        moveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                moveRadioButtonMenuItem.doClick();
            }
        });
    }

    private void handleNewMap() {
        final NewMapDialog newMapDialog = new NewMapDialog(this, true, map.getColumnCount(), map.getRowCount());
        newMapDialog.show();
        if (newMapDialog.useDefaultTileSet()) {
            int tempTileColumns = newMapDialog.getMapWidth();
            int tempTileRows = newMapDialog.getMapHeight();
            if (tempTileColumns > 0 && tempTileRows > 0) {
                tileSet = MapEditor.DEFAULT_TILE_SET;
                tileSetImageFile = MapEditor.DEFAULT_IMAGE_FILE;
                mapGuiUnInit();
                unregisterMapEvents();
                mapInit(tempTileColumns, tempTileRows);
                mapGuiInit();
                registerMapEvents();
                this.setTitle(TILE_PREFIX + " " + MAIN_RESOURCE_BUNDLE.getString("newMap.Text"));
            }
        } else {
            int tempTileColumns = newMapDialog.getMapWidth();
            int tempTileRows = newMapDialog.getMapHeight();
            File tempTileSetFile = newMapDialog.getTileSetFile();
            int tempTileSize = newMapDialog.getTileSize();
            int tempDefaultTileIndex = newMapDialog.getDefaultTileIndex();
            if (tempTileSetFile != null && tempTileSize > 0 && tempDefaultTileIndex > -1) {
                tileSetImageFile = tempTileSetFile;
                try {
                    tileSet = new TileSet(JTUtilities.getBufferedImage(tileSetImageFile.getAbsolutePath()), tempTileSize, tempTileSize, tempDefaultTileIndex);
                    tileSize = new Dimension(tempTileSize, tempTileSize);
                    mapGuiUnInit();
                    unregisterMapEvents();
                    tileSetPanel = new TileSetPanel(tileSet, DEFAULT_TILE_SET_PANEL_WRAP_AT);
                    tileSetPanel.repaint();
                    mapInit(tempTileColumns, tempTileRows);
                    mapGuiInit();
                    registerMapEvents();
                    this.setTitle(TILE_PREFIX + " " + MAIN_RESOURCE_BUNDLE.getString("newMap.Text"));
                } catch (IllegalArgumentException e) {
                    JTUtilities.showErrorMessage(this, e.getMessage(), NewMapDialog.MAIN_RESOURCE_BUNDLE.getString("tileSetInvalidError.Title"));
                    newMapDialog.dispose();
                    handleNewMap();
                }
            }
        }
        newMapDialog.dispose();
    }

    private void handlePointerTool() {
        map.setSelectionEndabled(false);
        isSelecting = false;
        isMovingMap = false;
        selectionButton.setEnabled(true);
        pointerButton.setEnabled(false);
        moveButton.setEnabled(true);
        map.setCursor(NORMAL_CURSOR);
        fillButton.setEnabled(false);
        fillMenuItem.setEnabled(false);
    }

    private void handleSelectionTool() {
        isSelecting = true;
        isMovingMap = false;
        map.setSelectionEndabled(true);
        pointerButton.setEnabled(true);
        selectionButton.setEnabled(false);
        moveButton.setEnabled(true);
        map.setCursor(SELECTION_CURSOR);
        fillButton.setEnabled(true);
        fillMenuItem.setEnabled(true);
    }

    private void handleFillTool() {
        map.fillCurrentSelection(tileSetPanel.getSelectedTile());
        handleSelectionTool();
    }

    protected void handleMoveTool() {
        isMovingMap = true;
        isSelecting = true;
        map.setSelectionEndabled(false);
        selectionButton.setEnabled(true);
        pointerButton.setEnabled(true);
        moveButton.setEnabled(false);
        map.setCursor(MOVE_CURSOR);
        fillButton.setEnabled(false);
        fillMenuItem.setEnabled(false);
    }

    private void handleDisplayPreferences() {
        PreferencesDialog dialog = new PreferencesDialog(this, true, currentLookAndFeel);
        dialog.pack();
        JTUtilities.centerRelativeFrom(this, dialog);
        dialog.show();
    }

    private void handleShowGrid(boolean showGrid) {
        map.setGridVisible(showGrid);
    }

    private void handleShowTraverseRating(boolean showTraverseRating) {
        map.setTraverseRatingsVisible(showTraverseRating);
    }

    private void handleChangeTraverseRating() {
        PagedTile pagedTile = map.getTile(lastPoint);
        if (pagedTile != null) {
            Object results = JOptionPane.showInputDialog(this, MAIN_RESOURCE_BUNDLE.getString("traverseRating.Text") + ":", MAIN_RESOURCE_BUNDLE.getString("changeTraverseRating.Text"), JOptionPane.QUESTION_MESSAGE, null, null, "" + pagedTile.getTraverseRating());
            if (results != null) {
                try {
                    pagedTile.setTraverseRating(Integer.valueOf(results.toString()).intValue());
                    pagedTile.setValid(true);
                } catch (NumberFormatException e) {
                    JTUtilities.showErrorMessage(this, MAIN_RESOURCE_BUNDLE.getString("invalidNumberError.Message"), MAIN_RESOURCE_BUNDLE.getString("invalidNumberError.Title"));
                    handleChangeTraverseRating();
                }
                map.setTile(pagedTile, map.toTileIndex(lastPoint));
                map.repaint();
            }
        }
    }

    private void handleSaveMap() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(MAIN_RESOURCE_BUNDLE.getString("saveMap.Text"));
        fileChooser.setFileFilter(new JTFileFilter("map", MAIN_RESOURCE_BUNDLE.getString("mapFileFilter.Description")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            new Thread(new Runnable() {

                public void run() {
                    synchronized (mutex) {
                        setBusy(true);
                        statusBar.setStateVisible(true);
                        statusBar.setProgressVisible(true);
                        statusBar.setStateText(MAIN_RESOURCE_BUNDLE.getString("savingMap.Text") + JTUtilities.ELLIPSIS);
                        statusBar.setProgressMin(0);
                        statusBar.setProgressMax(11);
                        statusBar.setProgressValue(0);
                        try {
                            File savedMapFile = fileChooser.getSelectedFile();
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            String extension = JTUtilities.getFileExtension(savedMapFile);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            if (extension != null && !extension.equalsIgnoreCase("map")) savedMapFile = new File(savedMapFile.getAbsolutePath() + ".map"); else savedMapFile = new File(savedMapFile.getAbsolutePath());
                            statusBar.setStateText(MAIN_RESOURCE_BUNDLE.getString("savingMap.Text") + ": " + savedMapFile.getName());
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            File tempTileSetFile = new File(tempDirectory + FILENAME_TILESET);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            File tempMapFile = new File(tempDirectory + FILENAME_MAP);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            File tempTileSetImageFile = new File(tempDirectory + FILENAME_TILESET_IMAGE);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            tileSet.toFile(tempTileSetFile, EditFrame.this);
                            map.toFile(tempMapFile);
                            statusBar.setProgressVisible(true);
                            statusBar.setStateText(MAIN_RESOURCE_BUNDLE.getString("savingMap.Text") + ": " + savedMapFile.getName());
                            statusBar.setProgressMax(11);
                            statusBar.setProgressValue(6);
                            JTUtilities.copyFile(tileSetImageFile, tempTileSetImageFile);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            JTUtilities.toZip(savedMapFile, new File[] { tempTileSetImageFile, tempTileSetFile, tempMapFile }, MAIN_RESOURCE_BUNDLE.getString("mapFile.Comment"), false, JTUtilities.ZIP_MAX_COMPRESSION);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            tempTileSetFile.delete();
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            tempMapFile.delete();
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            EditFrame.this.setTitle(TILE_PREFIX + savedMapFile.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                            JTUtilities.showErrorMessage(EditFrame.this, e.getMessage(), e.getClass().toString());
                        }
                        statusBar.setProgressVisible(false);
                        statusBar.setStateVisible(false);
                        setBusy(false);
                    }
                }
            }).start();
        }
    }

    private void handleOpenMap() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(MAIN_RESOURCE_BUNDLE.getString("openMap.Text"));
        fileChooser.setFileFilter(new JTFileFilter("map", MAIN_RESOURCE_BUNDLE.getString("mapFileFilter.Description")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(EditFrame.this) == JFileChooser.APPROVE_OPTION) {
            new Thread(new Runnable() {

                public void run() {
                    synchronized (mutex) {
                        setBusy(true);
                        statusBar.setStateVisible(true);
                        statusBar.setProgressVisible(true);
                        statusBar.setStateText(MAIN_RESOURCE_BUNDLE.getString("openingMap.Text") + JTUtilities.ELLIPSIS);
                        statusBar.setProgressMin(0);
                        statusBar.setProgressMax(11);
                        statusBar.setProgressValue(0);
                        try {
                            File savedMapFile = fileChooser.getSelectedFile();
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            String extension = JTUtilities.getFileExtension(savedMapFile);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            if (extension == null) savedMapFile = new File(savedMapFile.getAbsolutePath() + ".map");
                            statusBar.setStateText(MAIN_RESOURCE_BUNDLE.getString("openingMap.Text") + ": " + savedMapFile.getName());
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            JTUtilities.unZip(savedMapFile, new File(tempDirectory));
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            File tempTileSetFile = new File(tempDirectory + FILENAME_TILESET);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            File tempMapFile = new File(tempDirectory + FILENAME_MAP);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            File tempTileSetImageFile = new File(tempDirectory + FILENAME_TILESET_IMAGE);
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            mapGuiUnInit();
                            unregisterMapEvents();
                            tileSet = new TileSet(tempTileSetFile, JTUtilities.getBufferedImage(tempTileSetImageFile.getAbsolutePath()));
                            tileSize = new Dimension(tileSet.getTileWidth(), tileSet.getTileHeight());
                            tileSetImageFile = tempTileSetImageFile;
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            mapInit(tempMapFile);
                            mapGuiInit();
                            registerMapEvents();
                            viewShowGridMenuItem.setSelected(map.isGridVisible());
                            viewShowTraverseRatingMenuItem.setSelected(map.isTraverseRatingsVisible());
                            tempTileSetFile.delete();
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            tempMapFile.delete();
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            tempTileSetImageFile.delete();
                            statusBar.setProgressValue(statusBar.getProgressValue() + 1);
                            EditFrame.this.setTitle(TILE_PREFIX + savedMapFile.getName());
                            tileSetPanel.repaint();
                            EditFrame.this.repaint();
                        } catch (Exception e) {
                            e.printStackTrace();
                            JTUtilities.showErrorMessage(EditFrame.this, e.getMessage(), e.getClass().toString());
                        }
                        statusBar.setProgressVisible(false);
                        statusBar.setStateVisible(false);
                        setBusy(false);
                    }
                }
            }).start();
        }
    }

    public static Object getMutex() {
        return mutex;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
        getGlassPane().setVisible(isBusy);
        tileSetPanel.enableSelection(!isBusy);
        if (isBusy) map.setCursor(WAIT_CURSOR); else map.setCursor(NORMAL_CURSOR);
    }

    protected JTStatusBar getStatusBar() {
        return statusBar;
    }

    protected void setLookAndFeel(String LnF) {
        if (LnF != null && currentLookAndFeel != LnF) {
            currentLookAndFeel = LnF;
            updateLookAndFeel();
        }
    }

    private void updateLookAndFeel() {
        try {
            UIManager.setLookAndFeel(currentLookAndFeel);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
            String[] message = new String[] { MAIN_RESOURCE_BUNDLE.getString("lnfError.Message"), currentLookAndFeel };
            JTUtilities.showErrorMessage(this, message, MAIN_RESOURCE_BUNDLE.getString("lnfError.Title"));
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    private Point lastPoint = new Point();

    private Point scrollToPoint = new Point();

    public void mousePressed(MouseEvent e) {
        lastPoint = (Point) e.getPoint().clone();
        if (!isSelecting) {
            Tile selectedTile = tileSetPanel.getSelectedTile();
            if (SwingUtilities.isLeftMouseButton(e)) map.setTile(selectedTile, lastPoint); else if (SwingUtilities.isRightMouseButton(e)) mapPopupMenu.show(map, lastPoint.x + map.MAP_OFFSET.height, lastPoint.y + map.MAP_OFFSET.width);
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (!isSelecting) {
            Point clickedPoint = e.getPoint();
            Tile selectedTile = tileSetPanel.getSelectedTile();
            if (SwingUtilities.isLeftMouseButton(e)) if (selectedTile != null && clickedPoint != null) map.setTile(selectedTile, clickedPoint);
        }
        if (isMovingMap) {
            Point dragPoint = e.getPoint();
            Point viewPosition = map.getViewPosition();
            Point scrollOffset = new Point(dragPoint.x - lastPoint.x, dragPoint.y - lastPoint.y);
            lastPoint = (Point) dragPoint.clone();
            if (map.viewContains(dragPoint)) {
                scrollToPoint.x = viewPosition.x - scrollOffset.x;
                scrollToPoint.y = viewPosition.y - scrollOffset.y;
                map.setViewPosition(scrollToPoint);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        Point tilePoint = map.toTilePoint(e.getPoint());
        if (tilePoint != null) updateStatusBarTile(tilePoint.x, tilePoint.y); else updateStatusBarTile(-1, -1);
    }

    private void updateStatusBarTile(int column, int row) {
        if (0 <= column && 0 <= row) statusBar.setInfoOneText("(" + column + ", " + row + ")"); else statusBar.setInfoOneText("(col, row)");
    }

    private void updateStatusBarSize(int columns, int rows) {
        if (0 <= columns && 0 <= rows) statusBar.setInfoTwoText("(" + columns + ", " + rows + ")"); else statusBar.setInfoTwoText("(cols, rows)");
    }
}
