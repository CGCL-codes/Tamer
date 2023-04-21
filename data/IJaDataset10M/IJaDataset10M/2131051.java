package Ihm;

import javax.swing.TransferHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.ImageIcon;
import org.farng.mp3.*;

/**
 * The application's main frame.
 */
public class ihm extends JFrame {

    private actionFile action;

    private MP3File mp3file;

    public ihm() {
        super("ITunes Like");
        initPanel();
        initFrame();
    }

    private void initPanel() {
        statusPanel = (JPanel) getContentPane();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        jList1 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree(new FileSystemModel());
        jTree1.setDragEnabled(true);
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        try {
            jTree2 = new javax.swing.JTree(new FileBddModel());
        } catch (Exception exc) {
        }
        jTree2.setTransferHandler(new transferH());
        jTree2.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    TreePath selPath = jTree1.getPathForLocation(e.getX(), e.getY());
                    if (selPath.getLastPathComponent().toString().endsWith(".mp3")) {
                        File leMp3 = new File(selPath.getLastPathComponent().toString());
                        try {
                            ihm.this.mp3file = new MP3File(leMp3);
                            jList1.setModel(new javax.swing.AbstractListModel() {

                                String[] strings = { "Titre : " + ihm.this.mp3file.getID3v1Tag().getTitle(), "Artiste : " + ihm.this.mp3file.getID3v1Tag().getArtist(), "Album : " + ihm.this.mp3file.getID3v1Tag().getAlbum(), "Genre : " + ihm.this.mp3file.getID3v1Tag().getGenre(), "Année : " + ihm.this.mp3file.getID3v1Tag().getYear() };

                                public int getSize() {
                                    return strings.length;
                                }

                                public Object getElementAt(int i) {
                                    return strings[i];
                                }
                            });
                        } catch (Exception etoto) {
                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int selRow = jTree2.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = jTree2.getPathForLocation(e.getX(), e.getY());
                    ihm.this.action = new actionFile(selPath, ihm.this);
                    if (selRow != -1) {
                        ihm.this.jTree2.setSelectionPath(selPath);
                        JPopupMenu jPopupMenu1 = new JPopupMenu();
                        JMenuItem item = new JMenuItem("Créer une nouvelle playlist");
                        item.addActionListener(ihm.this.action);
                        jPopupMenu1.add(item);
                        if (selPath.getLastPathComponent().toString().endsWith(".mp3")) {
                            item = new JMenuItem("Supprimer de la Playlist");
                            item.addActionListener(ihm.this.action);
                            jPopupMenu1.add(item);
                        } else {
                            item = new JMenuItem("Supprimer la Playlist");
                            item.addActionListener(ihm.this.action);
                            jPopupMenu1.add(item);
                        }
                        jPopupMenu1.show(ihm.this.jTree2, e.getX(), e.getY());
                    }
                }
            }
        });
        statusPanel.setName("statusPanel");
        statusMessageLabel.setName("statusMessageLabel");
        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel");
        jScrollPane1.setName("jScrollPane1");
        jTree1.setName("jTree1");
        jTree1.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    TreePath selPath = jTree1.getPathForLocation(e.getX(), e.getY());
                    if (selPath.getLastPathComponent().toString().endsWith(".mp3")) {
                        File leMp3 = new File(selPath.getLastPathComponent().toString());
                        try {
                            ihm.this.mp3file = new MP3File(leMp3);
                            jList1.setModel(new javax.swing.AbstractListModel() {

                                String[] strings = { "Titre : " + ihm.this.mp3file.getID3v1Tag().getTitle(), "Artiste : " + ihm.this.mp3file.getID3v1Tag().getArtist(), "Album : " + ihm.this.mp3file.getID3v1Tag().getAlbum(), "Genre : " + ihm.this.mp3file.getID3v1Tag().getGenre(), "Année : " + ihm.this.mp3file.getID3v1Tag().getYear() };

                                public int getSize() {
                                    return strings.length;
                                }

                                public Object getElementAt(int i) {
                                    return strings[i];
                                }
                            });
                        } catch (Exception etoto) {
                            etoto.printStackTrace();
                        }
                    }
                }
            }
        });
        jScrollPane1.setViewportView(jTree1);
        jScrollPane2.setName("jScrollPane2");
        jTree2.setName("jTree2");
        jScrollPane2.setViewportView(jTree2);
        jList1.setName("jList1");
        jScrollPane3.setViewportView(jList1);
        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(statusPanelLayout.createSequentialGroup().addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(statusMessageLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 832, Short.MAX_VALUE).addComponent(statusAnimationLabel)).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addGap(9, 9, 9).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)))));
        statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE).addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(statusMessageLabel).addComponent(statusAnimationLabel)).addGap(3, 3, 3)).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    private void initFrame() {
        pack();
        setVisible(true);
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JList jList1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTree jTree1;

    public javax.swing.JTree jTree2;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JLabel statusAnimationLabel;

    private javax.swing.JLabel statusMessageLabel;

    private javax.swing.JPanel statusPanel;
}
