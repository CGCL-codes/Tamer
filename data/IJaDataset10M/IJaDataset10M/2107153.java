package medisnap.gui;

import medisnap.*;
import medisnap.dblayer.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.print.attribute.*;
import javax.print.*;
import java.awt.print.*;
import java.util.logging.*;

/**
 *
 * @author  Elle
 */
public class Drucken extends panelBaseClass implements medisnap.gui.isvisible {

    public static final long serialVersionUID = 1;

    public MediSnap m;

    private DragSource ds;

    private DefaultComboBoxModel dcbm;

    private Point lastMousePress;

    private Point lastMouseDrag;

    public void freeMemory() {
        if (jpanelDruckvorschau != null) {
            ((DruckVorschau) jpanelDruckvorschau).freeMemory();
            jpanelDruckvorschau = null;
        }
        ds = null;
        m = null;
    }

    public void isNowVisible() {
        dcbm = new DefaultComboBoxModel(DBLayer.getAllDrucklayouts());
        jcbLayout2.setModel(dcbm);
        if (MediSnap.activeValueDrucklayout != null) {
            jcbLayout2.setSelectedItem(MediSnap.activeValueDrucklayout);
        } else {
            jcbLayout2.setSelectedIndex(0);
        }
        jcbLayout2ActionPerformed(null);
        treeLokalisationen.requestFocusInWindow();
        m.ked = new MyDispatcher();
        m.manager.addKeyEventDispatcher(m.ked);
        redrawTree();
        treeLokalisationen.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeLokalisationen.setCellRenderer(new TreeCellRenderer());
    }

    private class MyDispatcher implements KeyEventDispatcher {

        MyDispatcher() {
        }

        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.isControlDown() && e.getID() == KeyEvent.KEY_PRESSED) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_D:
                        if (jbDrucken.isEnabled()) {
                            jbDruckenActionPerformed(null);
                            return true;
                        }
                        break;
                    case KeyEvent.VK_O:
                        if (buttonFotoanzeige.isEnabled()) {
                            buttonFotoanzeigeActionPerformed(null);
                            return true;
                        }
                        break;
                }
            }
            return false;
        }
    }

    /** Creates new form Bereichszuordnung */
    public Drucken(MediSnap _m) {
        m = _m;
        m.createTreeNodes();
        initComponents();
        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(jlistBilderListe, 1, new DragGestureListener() {

            public void dragGestureRecognized(DragGestureEvent dge) {
                valueBildImage vbi = (valueBildImage) (jlistBilderListe.getSelectedValue());
                dge.getDragSource().startDrag(dge, null, vbi, new DragSourceListener() {

                    public void dragDropEnd(DragSourceDropEvent dsde) {
                    }

                    public void dragEnter(DragSourceDragEvent dsde) {
                    }

                    public void dragExit(DragSourceEvent dse) {
                    }

                    public void dragOver(DragSourceDragEvent dsde) {
                    }

                    public void dropActionChanged(DragSourceDragEvent dsde) {
                    }
                });
            }
        });
    }

    private void initComponents() {
        panelMain = new javax.swing.JPanel();
        panelLinks = new javax.swing.JPanel();
        panelTree = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeLokalisationen = new javax.swing.JTree(m.deftreemodel);
        jPanel12 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jcbLayout2 = new javax.swing.JComboBox();
        jPanel22 = new javax.swing.JPanel();
        jbKopfNeu2 = new javax.swing.JButton();
        jbKopfBearbeiten2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jb1Picture = new javax.swing.JButton();
        jb2Pictures = new javax.swing.JButton();
        jb4Pictures = new javax.swing.JButton();
        panelAktuelleLokalisation = new javax.swing.JPanel();
        panelBilder = new javax.swing.JPanel();
        panelLokation = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlTextMitte = new javax.swing.JLabel();
        jpanelButtonsLokalisation = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jbRotateLeft = new javax.swing.JButton();
        jbRotateRight = new javax.swing.JButton();
        jtbHand = new javax.swing.JToggleButton();
        jtbZoom = new javax.swing.JToggleButton();
        jbDrucken = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jpanelDruckvorschau = new DruckVorschau(m);
        panelBilderListe = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlistBilderListe = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        buttonFotoanzeige = new javax.swing.JButton();
        buttonBeenden = new javax.swing.JButton();
        panelOben = new javax.swing.JPanel();
        setOpaque(false);
        setLayout(new java.awt.BorderLayout());
        panelMain.setOpaque(false);
        panelMain.setLayout(new java.awt.BorderLayout(2, 0));
        panelLinks.setOpaque(false);
        panelLinks.setLayout(new java.awt.BorderLayout());
        panelTree.setOpaque(false);
        panelTree.setLayout(new java.awt.BorderLayout());
        jPanel10.setOpaque(false);
        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel11.setOpaque(false);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/icon_lokalisation.png")));
        jLabel1.setText("Vorhandene Lokalisationen:");
        jLabel1.setIconTextGap(10);
        jPanel11.add(jLabel1);
        jPanel10.add(jPanel11, java.awt.BorderLayout.WEST);
        panelTree.add(jPanel10, java.awt.BorderLayout.NORTH);
        jScrollPane2.setOpaque(false);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(200, 322));
        treeLokalisationen.setRowHeight(20);
        treeLokalisationen.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeLokalisationenValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(treeLokalisationen);
        panelTree.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jPanel12.setMinimumSize(new java.awt.Dimension(3, 10));
        jPanel12.setOpaque(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(3, 10));
        panelTree.add(jPanel12, java.awt.BorderLayout.WEST);
        panelLinks.add(panelTree, java.awt.BorderLayout.CENTER);
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Layout"));
        jPanel21.setOpaque(false);
        jPanel21.setLayout(new java.awt.BorderLayout());
        jcbLayout2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jcbLayout2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbLayout2ActionPerformed(evt);
            }
        });
        jPanel21.add(jcbLayout2, java.awt.BorderLayout.NORTH);
        jPanel22.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel22.setOpaque(false);
        jPanel22.setLayout(new java.awt.GridLayout(1, 0, 3, 3));
        jbKopfNeu2.setText("<html><u>N</u>eu</html>");
        jbKopfNeu2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbKopfNeu2ActionPerformed(evt);
            }
        });
        jPanel22.add(jbKopfNeu2);
        jbKopfBearbeiten2.setText("<html>Bearbe<u>i</u>ten</html>");
        jbKopfBearbeiten2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbKopfBearbeiten2ActionPerformed(evt);
            }
        });
        jPanel22.add(jbKopfBearbeiten2);
        jPanel21.add(jPanel22, java.awt.BorderLayout.CENTER);
        jPanel6.setOpaque(false);
        jToolBar2.setFloatable(false);
        jToolBar2.setOpaque(false);
        jb1Picture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout1picture_sel.png")));
        jb1Picture.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb1PictureActionPerformed(evt);
            }
        });
        jToolBar2.add(jb1Picture);
        jb2Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout2picture.png")));
        jb2Pictures.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb2PicturesActionPerformed(evt);
            }
        });
        jToolBar2.add(jb2Pictures);
        jb4Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout4picture.png")));
        jb4Pictures.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb4PicturesActionPerformed(evt);
            }
        });
        jToolBar2.add(jb4Pictures);
        jPanel6.add(jToolBar2);
        jPanel21.add(jPanel6, java.awt.BorderLayout.SOUTH);
        panelLinks.add(jPanel21, java.awt.BorderLayout.SOUTH);
        panelMain.add(panelLinks, java.awt.BorderLayout.WEST);
        panelAktuelleLokalisation.setOpaque(false);
        panelAktuelleLokalisation.setLayout(new java.awt.BorderLayout());
        panelBilder.setOpaque(false);
        panelBilder.setLayout(new java.awt.BorderLayout(2, 0));
        panelLokation.setOpaque(false);
        panelLokation.setLayout(new java.awt.BorderLayout());
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel2.setOpaque(false);
        jlTextMitte.setFont(new java.awt.Font("Dialog", 0, 14));
        jlTextMitte.setForeground(new java.awt.Color(0, 0, 102));
        jlTextMitte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/icon_print.png")));
        jlTextMitte.setText("Druckvorschau");
        jlTextMitte.setIconTextGap(10);
        jPanel2.add(jlTextMitte);
        jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);
        panelLokation.add(jPanel1, java.awt.BorderLayout.NORTH);
        jpanelButtonsLokalisation.setOpaque(false);
        jpanelButtonsLokalisation.setLayout(new java.awt.BorderLayout());
        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.GridLayout(2, 1));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jToolBar1.setFloatable(false);
        jToolBar1.setOpaque(false);
        jbRotateLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/RotateLeft24.png")));
        jbRotateLeft.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRotateLeftActionPerformed(evt);
            }
        });
        jToolBar1.add(jbRotateLeft);
        jbRotateRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/RotateRight24.png")));
        jbRotateRight.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRotateRightActionPerformed(evt);
            }
        });
        jToolBar1.add(jbRotateRight);
        jtbHand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/icon_hand.png")));
        jtbHand.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtbHandMousePressed(evt);
            }
        });
        jtbHand.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jtbHandMouseDragged(evt);
            }
        });
        jtbHand.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbHandActionPerformed(evt);
            }
        });
        jToolBar1.add(jtbHand);
        jtbZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/Zoom24.gif")));
        jtbZoom.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbZoomActionPerformed(evt);
            }
        });
        jToolBar1.add(jtbZoom);
        jPanel3.add(jToolBar1, java.awt.BorderLayout.WEST);
        jPanel4.add(jPanel3);
        jbDrucken.setForeground(new java.awt.Color(102, 0, 0));
        jbDrucken.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/icon_print.png")));
        jbDrucken.setText("<html> <u>D</u>rucken</html>");
        jbDrucken.setIconTextGap(10);
        jbDrucken.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDruckenActionPerformed(evt);
            }
        });
        jPanel4.add(jbDrucken);
        jpanelButtonsLokalisation.add(jPanel4, java.awt.BorderLayout.NORTH);
        panelLokation.add(jpanelButtonsLokalisation, java.awt.BorderLayout.SOUTH);
        jpanelDruckvorschau.setMinimumSize(new java.awt.Dimension(100, 10));
        jpanelDruckvorschau.setPreferredSize(new java.awt.Dimension(200, 10));
        jpanelDruckvorschau.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpanelDruckvorschauMousePressed(evt);
            }
        });
        jpanelDruckvorschau.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jpanelDruckvorschauMouseDragged(evt);
            }
        });
        jScrollPane3.setViewportView(jpanelDruckvorschau);
        panelLokation.add(jScrollPane3, java.awt.BorderLayout.CENTER);
        panelBilder.add(panelLokation, java.awt.BorderLayout.CENTER);
        panelBilderListe.setMinimumSize(new java.awt.Dimension(300, 158));
        panelBilderListe.setOpaque(false);
        panelBilderListe.setLayout(new java.awt.BorderLayout());
        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel7.setOpaque(false);
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel6.setForeground(new java.awt.Color(0, 0, 102));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/icon_kamera.png")));
        jLabel6.setText("Fotos dieser Lokalisation:");
        jLabel6.setIconTextGap(10);
        jPanel7.add(jLabel6);
        jPanel5.add(jPanel7, java.awt.BorderLayout.WEST);
        panelBilderListe.add(jPanel5, java.awt.BorderLayout.NORTH);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setOpaque(false);
        jlistBilderListe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlistBilderListe.setCellRenderer(new ListeBilderCellRenderer());
        jlistBilderListe.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlistBilderListeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jlistBilderListe);
        panelBilderListe.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel8.setOpaque(false);
        jPanel8.setLayout(new java.awt.GridLayout(0, 1, 3, 3));
        buttonFotoanzeige.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/icon_kamera.png")));
        buttonFotoanzeige.setText("<html>F<u>o</u>to anzeigen</html>");
        buttonFotoanzeige.setIconTextGap(10);
        buttonFotoanzeige.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFotoanzeigeActionPerformed(evt);
            }
        });
        jPanel8.add(buttonFotoanzeige);
        buttonBeenden.setText("<html><u>Z</u>urück</html>");
        buttonBeenden.setMinimumSize(new java.awt.Dimension(200, 23));
        buttonBeenden.setPreferredSize(new java.awt.Dimension(200, 23));
        buttonBeenden.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBeendenActionPerformed(evt);
            }
        });
        jPanel8.add(buttonBeenden);
        panelBilderListe.add(jPanel8, java.awt.BorderLayout.SOUTH);
        panelBilder.add(panelBilderListe, java.awt.BorderLayout.EAST);
        panelAktuelleLokalisation.add(panelBilder, java.awt.BorderLayout.CENTER);
        panelOben.setOpaque(false);
        panelOben.setLayout(new java.awt.BorderLayout());
        panelAktuelleLokalisation.add(panelOben, java.awt.BorderLayout.NORTH);
        panelMain.add(panelAktuelleLokalisation, java.awt.BorderLayout.CENTER);
        add(panelMain, java.awt.BorderLayout.CENTER);
    }

    private void jb4PicturesActionPerformed(java.awt.event.ActionEvent evt) {
        jb1Picture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout1picture.png")));
        jb2Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout2picture.png")));
        jb4Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout4picture_sel.png")));
        ((DruckVorschau) jpanelDruckvorschau).setCountPictures(2);
    }

    private void jb2PicturesActionPerformed(java.awt.event.ActionEvent evt) {
        jb1Picture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout1picture.png")));
        jb2Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout2picture_sel.png")));
        jb4Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout4picture.png")));
        ((DruckVorschau) jpanelDruckvorschau).setCountPictures(1);
    }

    private void jb1PictureActionPerformed(java.awt.event.ActionEvent evt) {
        jb1Picture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout1picture_sel.png")));
        jb2Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout2picture.png")));
        jb4Pictures.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/layout4picture.png")));
        ((DruckVorschau) jpanelDruckvorschau).setCountPictures(0);
    }

    private void jbKopfBearbeiten2ActionPerformed(java.awt.event.ActionEvent evt) {
        m.showForm(MediSnapFormular.TextFelderBearbeiten);
    }

    private void jcbLayout2ActionPerformed(java.awt.event.ActionEvent evt) {
        MediSnap.activeValueDrucklayout = (valueDrucklayout) jcbLayout2.getSelectedItem();
        this.jpanelDruckvorschau.repaint();
    }

    private void jbKopfNeu2ActionPerformed(java.awt.event.ActionEvent evt) {
        m.showForm(MediSnapFormular.TextFelder);
    }

    private void jlistBilderListeMouseClicked(java.awt.event.MouseEvent evt) {
        if (jlistBilderListe.getSelectedValue() != null) {
            MediSnap.activeValueBild = ((valueBildImage) jlistBilderListe.getSelectedValue()).vb;
            if (evt.getClickCount() >= 2) {
                m.showForm(MediSnapFormular.FotoAnsichtDruck);
            }
        }
    }

    private void treeLokalisationenValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        m.activeTreeNode = (DefaultMutableTreeNode) treeLokalisationen.getLastSelectedPathComponent();
        if (m.activeTreeNode == null) return;
        m.treeselection = treeLokalisationen.getSelectionPaths();
        if (m.activeTreeNode.getChildCount() > 0) {
            TreePath path = new TreePath(m.activeTreeNode.getPath());
            treeLokalisationen.expandPath(path);
        }
        Object o = m.activeTreeNode.getUserObject();
        valueAnzBilder vlAnzBild;
        if (o instanceof valueAnzBilder) {
            vlAnzBild = (valueAnzBilder) o;
            m.activeValueLokalisation = vlAnzBild.lokation;
            m.activeValueBildImage = valueBildImage.createValueBildImageVector(DBLayer.getBilderZuLokalisationDatum(vlAnzBild), m.activeValueLokalisation);
        } else {
            m.activeValueLokalisation = (valueLokalisation) o;
            Integer aLId = m.activeValueLokalisation.getId();
            Vector<valueBild> vec_b = DBLayer.getBilderZuLokalisation(aLId);
            if (vec_b != null) {
                m.activeValueBildImage = valueBildImage.createValueBildImageVector(vec_b, m.activeValueLokalisation);
            } else {
                System.out.println("null value bild image");
                m.activeValueBildImage = new Vector<valueBildImage>();
            }
        }
        refreshListe();
    }

    private void buttonFotoanzeigeActionPerformed(java.awt.event.ActionEvent evt) {
        m.showForm(MediSnapFormular.FotoAnsichtDruck);
    }

    private void buttonBeendenActionPerformed(java.awt.event.ActionEvent evt) {
        m.showForm(MediSnapFormular.Bereichszuordnung);
    }

    private void jbDruckenActionPerformed(java.awt.event.ActionEvent evt) {
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PrinterJob pj = PrinterJob.getPrinterJob();
        Paper p = pj.getPageFormat(aset).getPaper();
        p.setImageableArea(0, 0, p.getWidth(), p.getHeight());
        PageFormat pf = pj.getPageFormat(aset);
        pf.getPaper().setImageableArea(0, 0, p.getWidth(), p.getHeight());
        try {
            pj.setPrintable((DruckVorschau) this.jpanelDruckvorschau, pf);
            if (pj.printDialog(aset) == true) {
                pj.print(aset);
            }
        } catch (PrinterException pe) {
            MediSnap.log.warning("Fehler beim Drucken: " + pe);
            javax.swing.JOptionPane.showMessageDialog(null, "Es konnte nicht gedruckt werden!\n" + "Bitte überprüfen sie ihre Druckereinstellungen und" + "versuchen sie es erneut.", "DruckFehler", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        this.repaint();
    }

    private void jbRotateLeftActionPerformed(java.awt.event.ActionEvent evt) {
        ((DruckVorschau) jpanelDruckvorschau).rotateCurrentLeft();
    }

    private void jbRotateRightActionPerformed(java.awt.event.ActionEvent evt) {
        ((DruckVorschau) jpanelDruckvorschau).rotateCurrentRight();
    }

    private void jtbHandMouseDragged(java.awt.event.MouseEvent evt) {
    }

    private void jtbHandMousePressed(java.awt.event.MouseEvent evt) {
    }

    private void jpanelDruckvorschauMousePressed(java.awt.event.MouseEvent evt) {
    }

    private void jpanelDruckvorschauMouseDragged(java.awt.event.MouseEvent evt) {
    }

    private void jtbHandActionPerformed(java.awt.event.ActionEvent evt) {
        this.jtbZoom.setSelected(false);
        this.jtbHand.setSelected(true);
        ((DruckVorschau) jpanelDruckvorschau).setDoZoom(false);
    }

    private void jtbZoomActionPerformed(java.awt.event.ActionEvent evt) {
        this.jtbZoom.setSelected(true);
        this.jtbHand.setSelected(false);
        ((DruckVorschau) jpanelDruckvorschau).setDoZoom(true);
    }

    private void refreshListe() {
        if (m.activeValueBildImage.size() > 0) {
            jlistBilderListe.setListData(m.activeValueBildImage);
            jlistBilderListe.setSelectedIndex(0);
            m.activeValueBild = m.activeValueBildImage.elementAt(0).vb;
        } else {
            jlistBilderListe.setListData(new Vector<valueBild>());
            m.activeValueBild = null;
        }
    }

    private void redrawTree() {
        if (m.treeselection[0] != null) {
            treeLokalisationen.expandPath(m.treeselection[0]);
            treeLokalisationen.setSelectionPath(m.treeselection[0]);
        } else {
            int[] x = { 0 };
            treeLokalisationen.setSelectionRows(x);
        }
    }

    private javax.swing.JButton buttonBeenden;

    private javax.swing.JButton buttonFotoanzeige;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel11;

    private javax.swing.JPanel jPanel12;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel21;

    private javax.swing.JPanel jPanel22;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JToolBar jToolBar2;

    private javax.swing.JButton jb1Picture;

    private javax.swing.JButton jb2Pictures;

    private javax.swing.JButton jb4Pictures;

    private javax.swing.JButton jbDrucken;

    private javax.swing.JButton jbKopfBearbeiten2;

    private javax.swing.JButton jbKopfNeu2;

    private javax.swing.JButton jbRotateLeft;

    private javax.swing.JButton jbRotateRight;

    private javax.swing.JComboBox jcbLayout2;

    private javax.swing.JLabel jlTextMitte;

    private javax.swing.JList jlistBilderListe;

    private javax.swing.JPanel jpanelButtonsLokalisation;

    private javax.swing.JPanel jpanelDruckvorschau;

    private javax.swing.JToggleButton jtbHand;

    private javax.swing.JToggleButton jtbZoom;

    private javax.swing.JPanel panelAktuelleLokalisation;

    private javax.swing.JPanel panelBilder;

    private javax.swing.JPanel panelBilderListe;

    private javax.swing.JPanel panelLinks;

    private javax.swing.JPanel panelLokation;

    private javax.swing.JPanel panelMain;

    private javax.swing.JPanel panelOben;

    private javax.swing.JPanel panelTree;

    private javax.swing.JTree treeLokalisationen;
}
