package org.easyway.editor.forms.southPanel;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.easyway.editor.analyzer.Analyzer;
import org.easyway.editor.forms.EditorCore;
import org.easyway.interfaces.base.ITexture;
import org.easyway.system.StaticRef;
import org.easyway.tiles.Tile;
import org.easyway.tiles.TileManager;
import org.easyway.tiles.TileManagerEditor;

/**
 * @author Proprietario
 * 
 */
public class TiledMapPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JComboBox tiledmap = null;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private JLabel widthMap = null;

    private JLabel heightMap = null;

    private JLabel jLabel2 = null;

    private JLabel jLabel21 = null;

    private JTextField widthTile = null;

    private JTextField heightTile = null;

    private JButton addTile = null;

    private JButton removeTile = null;

    private JButton createTile = null;

    private JButton refresh = null;

    public static TileManager tiledMap;

    /**
	 * This is the default constructor
	 */
    public TiledMapPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        jLabel21 = new JLabel();
        jLabel21.setBounds(new Rectangle(298, 29, 67, 16));
        jLabel21.setText("Height Tile");
        jLabel2 = new JLabel();
        jLabel2.setBounds(new Rectangle(298, 7, 67, 16));
        jLabel2.setText("Width Tile");
        heightMap = new JLabel();
        heightMap.setBounds(new Rectangle(247, 29, 38, 16));
        heightMap.setText("1000");
        widthMap = new JLabel();
        widthMap.setBounds(new Rectangle(247, 7, 38, 16));
        widthMap.setText("1000");
        jLabel1 = new JLabel();
        jLabel1.setBounds(new Rectangle(188, 29, 54, 16));
        jLabel1.setText("Height");
        jLabel = new JLabel();
        jLabel.setBounds(new Rectangle(188, 7, 54, 16));
        jLabel.setText("Width");
        this.setSize(930, 82);
        this.setLayout(null);
        this.add(getTiledmap(), null);
        this.add(jLabel, null);
        this.add(jLabel1, null);
        this.add(widthMap, null);
        this.add(heightMap, null);
        this.add(jLabel2, null);
        this.add(jLabel21, null);
        this.add(getWidthTile(), null);
        this.add(getHeightTile(), null);
        this.add(getAddTile(), null);
        this.add(getRemoveTile(), null);
        this.add(getCreateTile(), null);
        this.add(getRefresh(), null);
        this.add(getChangeGrid(), null);
        this.add(getNewMap(), null);
        this.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentShown(java.awt.event.ComponentEvent e) {
            }
        });
    }

    /**
	 * This method initializes tiledmap
	 * 
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getTiledmap() {
        if (tiledmap == null) {
            tiledmap = new JComboBox();
            tiledmap.setBounds(new Rectangle(66, 14, 103, 16));
            tiledmap.addComponentListener(new java.awt.event.ComponentAdapter() {

                public void componentShown(java.awt.event.ComponentEvent e) {
                }
            });
            tiledmap.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    selectTileMap((TileManager) e.getItem());
                }
            });
        }
        return tiledmap;
    }

    int oldWidthTile, oldHeightTile;

    private JCheckBox changeGrid = null;

    private JButton newMap = null;

    /**
	 * This method initializes widthTile
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getWidthTile() {
        if (widthTile == null) {
            widthTile = new JTextField();
            widthTile.setBounds(new Rectangle(370, 7, 36, 16));
            widthTile.setBackground(Color.green);
            widthTile.addCaretListener(new javax.swing.event.CaretListener() {

                public void caretUpdate(javax.swing.event.CaretEvent e) {
                    setNewTileWidth();
                }
            });
        }
        return widthTile;
    }

    protected void setNewTileWidth() {
        int newWidth = oldWidthTile;
        try {
            newWidth = Integer.parseInt(widthTile.getText());
            if (newWidth > 0) {
                if (tiledMap != null) {
                    tiledMap.setTileWidth(newWidth);
                    if (changeGrid.isSelected()) {
                        EditorCore.setWidthGrid(newWidth);
                    }
                }
                widthTile.setBackground(Color.GREEN);
            } else {
                widthTile.setBackground(Color.PINK);
            }
        } catch (Exception ex) {
            widthTile.setBackground(Color.PINK);
        }
    }

    /**
	 * This method initializes heightTile
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getHeightTile() {
        if (heightTile == null) {
            heightTile = new JTextField();
            heightTile.setBounds(new Rectangle(370, 29, 36, 16));
            heightTile.setBackground(Color.green);
            heightTile.addCaretListener(new javax.swing.event.CaretListener() {

                public void caretUpdate(javax.swing.event.CaretEvent e) {
                    setNewTileHeight();
                }
            });
        }
        return heightTile;
    }

    protected void setNewTileHeight() {
        int newHeight = oldHeightTile;
        try {
            newHeight = Integer.parseInt(heightTile.getText());
            if (newHeight > 0) {
                if (tiledMap != null) {
                    tiledMap.setTileWidth(newHeight);
                    if (changeGrid.isSelected()) {
                        EditorCore.setHeightGrid(newHeight);
                    }
                }
                heightTile.setBackground(Color.GREEN);
            } else {
                heightTile.setBackground(Color.PINK);
            }
        } catch (Exception ex) {
            heightTile.setBackground(Color.PINK);
        }
    }

    /**
	 * This method initializes addTile
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getAddTile() {
        if (addTile == null) {
            addTile = new JButton();
            addTile.setBounds(new Rectangle(416, 7, 108, 16));
            addTile.setText("Add Tile");
            addTile.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (tiledMap == null) return;
                    Analyzer.removeFromLPanel();
                    try {
                        Analyzer.addStaticMethod(TileManagerEditor.class.getMethod("multipleTile", new Class[] { Tile.class })).command();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return addTile;
    }

    /**
	 * This method initializes removeTile
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getRemoveTile() {
        if (removeTile == null) {
            removeTile = new JButton();
            removeTile.setBounds(new Rectangle(416, 29, 108, 16));
            removeTile.setText("Remove Tile");
            removeTile.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (tiledMap != null) {
                        TileManagerEditor.removeTile(tiledMap.getType().toString());
                    }
                }
            });
        }
        return removeTile;
    }

    /**
	 * This method initializes createTile
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getCreateTile() {
        if (createTile == null) {
            createTile = new JButton();
            createTile.setBounds(new Rectangle(529, 7, 108, 16));
            createTile.setText("create Tile");
            createTile.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Analyzer.removeFromLPanel();
                    try {
                        Analyzer.addStaticMethod(TileManagerEditor.class.getMethod("createTile", new Class[] { ITexture.class, String.class })).command();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return createTile;
    }

    /**
	 * This method initializes refresh
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getRefresh() {
        if (refresh == null) {
            refresh = new JButton();
            refresh.setBounds(new Rectangle(66, 36, 103, 16));
            refresh.setText("Refresh");
            refresh.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    refreshList();
                }
            });
        }
        return refresh;
    }

    protected void refreshList() {
        tiledmap.removeAllItems();
        for (TileManager tilem : StaticRef.coreCollision.tileGroups) {
            tiledmap.addItem(tilem);
        }
        selectIndex(0);
    }

    public boolean selectIndex(int index) {
        if (index < 0 || index >= tiledmap.getItemCount()) return false;
        tiledmap.setSelectedIndex(index);
        TileManager tm = TileManager.getTileManager(tiledmap.getSelectedItem().toString());
        return selectTileMap(tm);
    }

    public boolean selectTileMap(TileManager tm) {
        tiledMap = TileManager.getTileManager(tiledmap.getSelectedItem().toString());
        if (tiledMap == null) {
            new Exception("null tiled map");
            return false;
        }
        getWidthTile().setText("" + tiledMap.getTileWidth());
        getHeightTile().setText("" + tiledMap.getTileHeight());
        widthMap.setText("" + tiledMap.getNumX());
        heightMap.setText("" + tiledMap.getNumY());
        return true;
    }

    /**
	 * This method initializes changeGrid
	 * 
	 * @return javax.swing.JCheckBox
	 */
    private JCheckBox getChangeGrid() {
        if (changeGrid == null) {
            changeGrid = new JCheckBox();
            changeGrid.setBounds(new Rectangle(298, 44, 108, 16));
            changeGrid.setText("update Grid");
            changeGrid.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setNewTileHeight();
                    setNewTileWidth();
                }
            });
        }
        return changeGrid;
    }

    /**
	 * This method initializes newMap
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getNewMap() {
        if (newMap == null) {
            newMap = new JButton();
            newMap.setBounds(new Rectangle(11, 14, 45, 39));
            newMap.setText("ADD");
            newMap.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Analyzer.removeFromLPanel();
                    Analyzer.searchConstructors(TileManager.class);
                }
            });
        }
        return newMap;
    }
}
