package saga.editor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import saga.control.Control;
import saga.model.HeroFodder;
import saga.model.Item;
import saga.model.Loc;
import saga.model.Mob;
import saga.model.map.BaseMap;
import saga.model.map.BaseTile;
import saga.model.map.IslandMap;
import saga.model.map.Map;
import saga.model.map.MapFactory;
import saga.model.map.MobFactory;
import saga.model.map.ShipTile;
import saga.model.map.Terrain;
import saga.model.map.Tile;
import saga.model.map.TopTile;
import saga.model.map.TownTile;
import saga.util.IconFactory;
import saga.view.GroundLayer;
import saga.view.InventoryView;
import saga.view.LayeredScrollable;
import saga.view.WalkLayer;

/**
 * @version $Id: MapEditor.java,v 1.1 2004/04/18 07:00:37 marion Exp $
 */
public class MapEditor extends JPanel {

    /** The map display, holding the current map. */
    MapDisplay mapView;

    /** The map selector. */
    MapSelection mapSel;

    /** Fields to display map name and file name. */
    JTextField mapName, fileName;

    /** The current terrain. */
    InventoryView terrain;

    /** Creates a new instance of MapEditor */
    public MapEditor() {
        IslandMap islandMap = IslandMap.create();
        setLayout(new BorderLayout());
        mapView = new MapDisplay();
        mapView.setMap(islandMap);
        LayeredScrollable ls = new LayeredScrollable();
        ls.add(mapView, LayeredScrollable.DEFAULT_LAYER);
        ls.add(new MobDisplay(), new Integer(10));
        add(ls, BorderLayout.CENTER);
        JPanel name = new JPanel(new GridLayout(1, 0));
        mapName = new JTextField(islandMap.getName(), 20);
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapPanel.setBorder(new TitledBorder("Map Name"));
        mapPanel.add(mapName, BorderLayout.CENTER);
        name.add(mapPanel);
        fileName = new JTextField(islandMap.getSaveFileName(), 20);
        JPanel file = new JPanel(new BorderLayout());
        file.setBorder(new TitledBorder("File Name"));
        file.add(fileName, BorderLayout.CENTER);
        name.add(file);
        add(name, BorderLayout.NORTH);
        mapSel = new MapSelection();
        add(mapSel, BorderLayout.EAST);
        terrain = new InventoryView();
        terrain.setColumnCount(12);
        terrain.setInventory(Terrain.getTerrainFor("Terrain,Features,Custom,Extra"));
        terrain.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Item i = terrain.getSelectedItem();
                if (i != null) {
                    mapSel.setIcon(i.getName());
                }
            }
        });
        fileName.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent ev) {
                mapSel.checkForMap();
            }

            public void insertUpdate(DocumentEvent ev) {
                mapSel.checkForMap();
            }

            public void removeUpdate(DocumentEvent ev) {
                mapSel.checkForMap();
            }
        });
        add(terrain, BorderLayout.SOUTH);
        mapSel.checkForMap();
    }

    public class MapSelection extends JPanel {

        /** The mob choice. */
        JComboBox mobChoice;

        private JLabel xLabel, yLabel;

        private JLabel icon;

        private JButton save, load, saveb, loadb, prepb;

        private JSpinner mobLevel;

        private JTextArea questInfo;

        private ComboBoxModel mobChoiceModel;

        private DefaultComboBoxModel mapChoiceModel;

        /** The current tile coordinates. */
        private int x, y;

        /** The currently selected tile. */
        private Tile tile;

        public MapSelection() {
            xLabel = new JLabel("X = 00");
            yLabel = new JLabel("Y = 00");
            icon = new JLabel(IconFactory.getInstance().getIcon("terrain.tree1"));
            icon.setText("terrain.?????");
            icon.setHorizontalAlignment(JLabel.LEFT);
            save = new JButton("Save Island Map");
            save.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    IslandMap islandMap = (IslandMap) mapView.getMap();
                    String filename = fileName.getText();
                    islandMap.setSaveFileName(filename);
                    String name = mapName.getText();
                    islandMap.setName(name);
                    Control.saveMap(islandMap);
                }
            });
            load = new JButton("Load Island Map");
            load.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String filename = fileName.getText();
                    IslandMap map = Control.loadMap(filename);
                    if (map != null) {
                        mapName.setText(map.getName());
                        mapView.setMap(map);
                        clearCoord();
                    }
                }
            });
            loadb = new JButton("Load Base Map");
            loadb.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String filename = fileName.getText();
                    BaseMap map = MapFactory.loadBaseMap(filename);
                    if (map != null) {
                        mapView.setMap(map);
                        clearCoord();
                    }
                }
            });
            saveb = new JButton("Save Base Map");
            saveb.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String filename = fileName.getText();
                    MapFactory.saveBaseMap(filename, (BaseMap) mapView.getMap());
                }
            });
            prepb = new JButton("Prepare Base Map");
            prepb.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Item item = terrain.getSelectedItem();
                    if (item != null) {
                        String name = item.getName();
                        fileName.setText(name);
                        BaseMap baseMap = new BaseMap();
                        int w = baseMap.getWidth();
                        int h = baseMap.getHeight();
                        for (int i = 0; i < w; i++) {
                            for (int j = 0; j < h; j++) {
                                baseMap.setTile(i, j, new BaseTile(name));
                            }
                        }
                        mapView.setMap(baseMap);
                        clearCoord();
                    }
                }
            });
            mobChoice = new JComboBox();
            mobChoice.setEditable(false);
            mobChoice.setEnabled(false);
            mobChoice.addItem("peaceful");
            String[] choices = MobFactory.getMonsterChoices();
            for (int i = 0; i < choices.length; i++) {
                mobChoice.addItem(choices[i]);
            }
            mobChoiceModel = mobChoice.getModel();
            mobChoice.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (tile instanceof TopTile) {
                        TopTile topTile = ((TopTile) tile);
                        String name = (String) mobChoice.getSelectedItem();
                        Mob boss = topTile.getNextBossMob();
                        if (boss != null) {
                            if (name.equals(getTypeName(boss.getClass()))) {
                                return;
                            }
                        } else if (name.equals("peaceful")) {
                            return;
                        }
                        topTile.clearMobs();
                        if (!name.equals("peaceful")) {
                            addBossMob(topTile, name);
                        } else {
                            mapView.repaintLoc(x, y);
                        }
                    } else if (tile instanceof ShipTile) {
                        Object item = mobChoice.getSelectedItem();
                        String file = item == null ? null : item.toString();
                        ShipTile shipTile = (ShipTile) tile;
                        String oldFile = shipTile.getShip().getDestination();
                        if (oldFile == file || (file != null && oldFile != null && file.equals(oldFile))) {
                            return;
                        }
                        shipTile.getShip().setDestination(file);
                    } else {
                        return;
                    }
                    terrain.clearSelection();
                }
            });
            File[] files = new File("islands").listFiles(new FileFilter() {

                public boolean accept(File f) {
                    return f.getName().endsWith(".map");
                }
            });
            int scan = files == null ? 0 : files.length;
            String[] names = new String[scan];
            while (--scan >= 0) {
                names[scan] = files[scan].getName();
            }
            mapChoiceModel = new DefaultComboBoxModel(names);
            mobLevel = new JSpinner();
            mobLevel.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    setMobLevel(getMobLevel());
                }
            });
            questInfo = new JTextArea();
            questInfo.setEnabled(false);
            JPanel inner = new JPanel();
            inner.setLayout(new GridLayout(0, 1));
            inner.add(xLabel);
            inner.add(yLabel);
            inner.add(icon);
            inner.add(mobChoice);
            inner.add(mobLevel);
            inner.add(new JSeparator(JSeparator.HORIZONTAL));
            inner.add(save);
            inner.add(load);
            inner.add(saveb);
            inner.add(loadb);
            inner.add(prepb);
            add(inner);
            checkForMap();
        }

        protected void addBossMob(TopTile topTile, String name) {
            try {
                Class type = MobFactory.getMobClass(name);
                int min = MobFactory.getMinLevel(type);
                int max = MobFactory.getMaxLevel(type);
                int now = getMobLevel();
                int level = now;
                if (level < min) {
                    level = min;
                }
                if (level > max) {
                    level = max;
                }
                Mob boss = createBoss(type, level);
                topTile.addBossMob(boss);
                if (boss instanceof HeroFodder && questInfo.isEnabled()) {
                    String info = getQuestInfo();
                    if (info != null) {
                        ((HeroFodder) boss).setQuestInfo(getQuestInfo());
                    }
                }
                mapView.repaintLoc(x, y);
                updateMobChoice();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e, "Error creating boss", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * @param i
         */
        protected void setMobLevel(int level) {
            if (tile instanceof TopTile) {
                TopTile topTile = ((TopTile) tile);
                Mob mob = topTile.getNextBossMob();
                if (mob != null && mob.getLevel() != level) {
                    topTile.clearMobs();
                    String name = (String) mobChoice.getSelectedItem();
                    addBossMob(topTile, name);
                    terrain.clearSelection();
                }
            } else if (tile instanceof ShipTile) {
                ShipTile shipTile = (ShipTile) tile;
                if (level != shipTile.getMinLevel()) {
                    shipTile.setMinLevel(level);
                    terrain.clearSelection();
                }
            }
        }

        protected int getMobLevel() {
            return ((Number) mobLevel.getValue()).intValue();
        }

        protected String getQuestInfo() {
            String info = questInfo.getText();
            return (info == null || info.length() == 0) ? null : info;
        }

        /**
         * @param name
         * @return
         */
        protected Mob createBoss(Class type, int level) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
            return MobFactory.createBossMob(type, level);
        }

        /**
         * 
         */
        protected void clearCoord() {
            tile = null;
            xLabel.setText("X = ...");
            yLabel.setText("Y = ...");
            updateMobChoice();
        }

        public void setCoord(int xi, int yi) {
            x = xi;
            y = yi;
            xLabel.setText("X = " + xi);
            yLabel.setText("Y = " + yi);
            tile = mapView.getMap().getTile(xi, yi);
            updateMobChoice();
            if (terrain.getSelectedItem() == null) {
                icon.setText(tile.getName());
                icon.setIcon(IconFactory.getInstance().getIcon(tile.getName()));
            }
        }

        protected String getTypeName(Class type) {
            String name = type.getName();
            return name.substring(name.lastIndexOf('.') + 1);
        }

        /**
         * 
         */
        private void updateMobChoice() {
            if (tile instanceof TopTile) {
                mobChoice.setModel(mobChoiceModel);
                mobChoice.setEditable(false);
                mobChoice.setEnabled(true);
                Mob mob = ((TopTile) tile).getNextBossMob();
                if (mob != null) {
                    Class type = mob.getClass();
                    String name = getTypeName(type);
                    mobChoice.setSelectedItem(name);
                    SpinnerNumberModel snm = (SpinnerNumberModel) mobLevel.getModel();
                    snm.setValue(new Integer(mob.getLevel()));
                    snm.setMinimum(new Integer(MobFactory.getMinLevel(type)));
                    snm.setMaximum(new Integer(MobFactory.getMaxLevel(type)));
                    mobLevel.setEnabled(true);
                    if (mob instanceof HeroFodder) {
                        questInfo.setText(((HeroFodder) mob).getQuestInfo());
                        questInfo.setEnabled(true);
                    } else {
                        questInfo.setEnabled(false);
                    }
                } else {
                    mobChoice.setSelectedItem("peaceful");
                    mobLevel.setEnabled(false);
                    questInfo.setEnabled(false);
                }
            } else if (tile instanceof ShipTile) {
                ShipTile shipTile = (ShipTile) tile;
                String file = shipTile.getShip().getDestination();
                if (mapChoiceModel.getIndexOf(file) < 0) {
                    mapChoiceModel.addElement(file);
                }
                mobChoice.setModel(mapChoiceModel);
                mobChoice.setSelectedItem(file);
                mobChoice.setEditable(true);
                mobChoice.setEnabled(true);
                SpinnerNumberModel snm = (SpinnerNumberModel) mobLevel.getModel();
                snm.setValue(new Integer(shipTile.getMinLevel()));
                snm.setMinimum(new Integer(0));
                snm.setMaximum(new Integer(100));
                mobLevel.setEnabled(true);
                questInfo.setEnabled(false);
            } else {
                mobChoice.setSelectedItem("peaceful");
                mobChoice.setEnabled(false);
                mobLevel.setEnabled(false);
                questInfo.setEnabled(false);
            }
        }

        public void setIcon(String s) {
            icon.setIcon(IconFactory.getInstance().getIcon(s));
            icon.setText(s);
            checkForMap();
        }

        public void checkForMap() {
            if (fileName != null) {
                String name = fileName.getText();
                load.setEnabled(Control.isExistingMap(name));
                loadb.setEnabled(Control.isExistingMap(name + ".bas"));
                boolean isIslandMap = mapView.getMap() instanceof IslandMap;
                save.setEnabled(isIslandMap);
                saveb.setEnabled(!isIslandMap);
            }
        }

        /**
         * 
         */
        protected void multiClick() {
            if (questInfo.isEnabled()) {
                Mob mob = ((TopTile) tile).getNextBossMob();
                if (mob instanceof HeroFodder) {
                    HeroFodder fodder = (HeroFodder) mob;
                    if (JOptionPane.showConfirmDialog(this, new JScrollPane(questInfo), "Quest Info for " + mob.getName(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        fodder.setQuestInfo(getQuestInfo());
                    }
                }
            }
        }
    }

    public class MapDisplay extends GroundLayer {

        public MapDisplay() {
            addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    x = x / 32;
                    y = y / 32;
                    Item i = terrain.getSelectedItem();
                    if (i != null) {
                        Tile tile;
                        if (getMap() instanceof IslandMap) {
                            IslandMap islandMap = (IslandMap) getMap();
                            if (i.getName().equals("town.ltower")) {
                                tile = TownTile.create();
                                islandMap.town = new Loc(x, y);
                            } else if ("Blue Turtle Ship".equals(i.getName())) {
                                tile = ShipTile.create();
                            } else {
                                tile = new TopTile(i.getName());
                            }
                            islandMap.setTile(x, y, tile);
                        } else {
                            tile = new BaseTile(i.getName());
                            ((BaseMap) getMap()).setTile(x, y, tile);
                        }
                        repaint();
                    }
                    mapSel.setCoord(x, y);
                    if (e.getClickCount() > 1) {
                        mapSel.multiClick();
                    }
                }
            });
        }

        /**
         * Public setter for map.
         * @param map the new map.
         */
        public void setMap(Map map) {
            super.setMap(map);
            if (mapSel != null) {
                mapSel.checkForMap();
            }
        }
    }

    public class MobDisplay extends WalkLayer {

        /** Paints this layer.  */
        protected void paintLayer(Graphics g) {
            Map map = mapView.getMap();
            if (map instanceof IslandMap) {
                int x = map.getWidth();
                Shape clip = g.getClip();
                while (--x >= 0) {
                    int y = map.getHeight();
                    while (--y >= 0) {
                        Tile t = map.getTile(x, y);
                        if (t instanceof TopTile) {
                            Mob boss = ((TopTile) t).getNextBossMob();
                            if (boss != null) {
                                int cx = x * DEFAULT_TILE_SIZE;
                                int cy = y * DEFAULT_TILE_SIZE;
                                if (clip.intersects((double) cx, (double) cy, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE)) {
                                    getIcon(boss).paintIcon(this, g, cx, cy);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Map Editor 0.1");
        MapEditor editor = new MapEditor();
        frame.getContentPane().add(editor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
