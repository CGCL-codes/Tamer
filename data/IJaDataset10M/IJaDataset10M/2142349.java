package Picasa;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.PhotoEntry;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.StringReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author H3R3T1C
 */
public class imageView extends javax.swing.JFrame {

    private Album album;

    private PicasawebService service;

    private boolean isSelected;

    private long ntime;

    private boolean isOver;

    private imageView viewer;

    public imageView(final Album album, final PicasawebService service, final int i) {
        this.service = service;
        this.album = album;
        isSelected = true;
        viewer = this;
        isOver = false;
        ntime = 0;
        initComponents();
        new Thread(new Runnable() {

            public void run() {
                jList1.add(jPanel3);
                jPanel3.show();
                jPanel3.setLocation(jList1.getWidth() / 2 - 85, jList1.getHeight() / 2 - 25);
                jPanel3.setSize(170, 50);
                jProgressBar1.setMaximum(album.getPhotos(service).size());
                DefaultListModel model = new DefaultListModel();
                for (PhotoEntry e : album.getPhotos(service)) {
                    jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                    model.addElement(new imagePreview(e));
                }
                jPanel3.hide();
                jList1.setModel(model);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(imageView.class.getName()).log(Level.SEVERE, null, ex);
                }
                jList1.setSelectedIndex(i);
                jLabel1.setVisible(false);
            }
        }).start();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel3 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jToolBar4 = new javax.swing.JToolBar();
        jToolBar5 = new javax.swing.JToolBar();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(31, 31, 31).addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jScrollPane2.setBorder(null);
        jEditorPane1.setBorder(null);
        jEditorPane1.setContentType("text/html");
        jEditorPane1.setEditable(false);
        jEditorPane1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jEditorPane1MouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(jEditorPane1);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Image Viewer");
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {

            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
        });
        jPanel2.setBackground(new java.awt.Color(51, 255, 255));
        jPanel2.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
        });
        jList1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setVisibleRowCount(0);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE).addContainerGap()));
        jLabel1.setText("Loading Please Wait...");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(283, 283, 283).addComponent(jLabel1).addContainerGap(282, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addGap(176, 176, 176).addComponent(jLabel1).addContainerGap(213, Short.MAX_VALUE)));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/review.png")));
        jButton8.setToolTipText("Previous");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton8);
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hdd_mount.png")));
        jButton9.setToolTipText("Save");
        jButton9.setEnabled(false);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton9);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Coherence.png")));
        jButton1.setToolTipText("Open With...");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton1);
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play.png")));
        jButton10.setToolTipText("Start Slide Show");
        jButton10.setEnabled(false);
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton10);
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/view.png")));
        jButton11.setToolTipText("Next");
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton11);
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);
        jToolBar4.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jToolBar4MouseEntered(evt);
            }
        });
        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewmag+.png")));
        jButton12.setEnabled(false);
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jButton12);
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewmag_.png")));
        jButton13.setToolTipText("Orignal");
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton13);
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewmag-.png")));
        jButton14.setEnabled(false);
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jButton14);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBar5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE).addContainerGap()))).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        pack();
    }

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {
        setImage();
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        setImage();
    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
        resetBar();
        setImage();
    }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jList1.getSelectedIndex() - 1 == -1) return;
        jList1.setSelectedIndex(jList1.getSelectedIndex() - 1);
    }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jList1.getSelectedIndex() + 1 == jList1.getModel().getSize()) return;
        jList1.setSelectedIndex(jList1.getSelectedIndex() + 1);
    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jList1.getSelectedIndex() == -1 || jPanel3.isShowing()) return;
        new Thread(new Runnable() {

            public void run() {
                try {
                    imagePreview photo = (imagePreview) jList1.getSelectedValue();
                    viewer.setTitle(photo.getName());
                    jScrollPane2.hide();
                    jPanel1.add(jScrollPane2);
                    jPanel1.add(jPanel3);
                    HTMLEditorKit kit = (HTMLEditorKit) jEditorPane1.getEditorKit();
                    HTMLDocument htmlDoc = new HTMLDocument();
                    jEditorPane1.setDocument(htmlDoc);
                    Document doc = jEditorPane1.getDocument();
                    jScrollPane2.show();
                    jPanel3.show();
                    jPanel3.setLocation(jPanel1.getWidth() / 2 - 85, jPanel1.getHeight() / 2 - 25);
                    jPanel3.setSize(170, 50);
                    if (photo.getWidth() >= jPanel1.getWidth() && photo.getHeight() >= jPanel1.getHeight()) {
                        jScrollPane2.setLocation(7, 0);
                        jScrollPane2.setSize(jPanel1.getWidth(), jPanel1.getHeight());
                    } else if (photo.getWidth() >= jPanel1.getWidth()) {
                        jScrollPane2.setLocation(0, jPanel1.getHeight() / 2 - (photo.getHeight() / 2));
                        jScrollPane2.setSize(jPanel1.getWidth(), photo.getHeight() + 10);
                    } else if (photo.getHeight() >= jPanel1.getHeight()) {
                        jScrollPane2.setLocation(jPanel1.getWidth() / 2 - (photo.getWidth() / 2), 0);
                        jScrollPane2.setSize(photo.getWidth() + 10, jPanel1.getHeight());
                    } else {
                        jScrollPane2.setSize(photo.getWidth() + 10, photo.getHeight() + 10);
                        jScrollPane2.setLocation(jPanel1.getWidth() / 2 - (photo.getWidth() / 2), jPanel1.getHeight() / 2 - (photo.getHeight() / 2));
                    }
                    photo.resetSizes();
                    String s = photo.getImage(jProgressBar1);
                    StringReader reader = new StringReader(s);
                    kit.read(reader, doc, 0);
                    jPanel3.hide();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {
        jPanel2.setSize(125, jPanel2.getHeight());
        jPanel2.setBackground(new java.awt.Color(240, 240, 240));
        jScrollPane1.show();
    }

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {
        resetBar();
    }

    private void jEditorPane1MouseEntered(java.awt.event.MouseEvent evt) {
        resetBar();
    }

    private void jToolBar4MouseEntered(java.awt.event.MouseEvent evt) {
        resetBar();
    }

    private void formMouseEntered(java.awt.event.MouseEvent evt) {
        resetBar();
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        System.out.println("ddd");
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        imagePreview prv = (imagePreview) jList1.getSelectedValue();
        Photo photo = prv.getPhoto();
        new OpenWithPicasa("User\\picasa.opw", photo).setVisible(true);
    }

    public void resetBar() {
        jPanel2.setSize(5, jPanel2.getHeight());
        jPanel2.setBackground(new java.awt.Color(51, 255, 255));
    }

    public void setImage() {
        if (jList1.getSelectedIndex() == -1 || jPanel3.isShowing()) return;
        new Thread(new Runnable() {

            public void run() {
                try {
                    imagePreview photo = (imagePreview) jList1.getSelectedValue();
                    viewer.setTitle(photo.getName());
                    jScrollPane2.hide();
                    jPanel1.add(jScrollPane2);
                    jPanel1.add(jPanel3);
                    HTMLEditorKit kit = (HTMLEditorKit) jEditorPane1.getEditorKit();
                    HTMLDocument htmlDoc = new HTMLDocument();
                    jEditorPane1.setDocument(htmlDoc);
                    Document doc = jEditorPane1.getDocument();
                    jScrollPane2.show();
                    jPanel3.show();
                    jPanel3.setLocation(jPanel1.getWidth() / 2 - 85, jPanel1.getHeight() / 2 - 25);
                    jPanel3.setSize(170, 50);
                    if (photo.getWidth() >= jPanel1.getWidth() && photo.getHeight() >= jPanel1.getHeight()) {
                        jScrollPane2.setLocation(7, 0);
                        jScrollPane2.setSize(jPanel1.getWidth(), jPanel1.getHeight());
                        photo.setHight(jPanel1.getHeight() - 15);
                        photo.setWidth(jPanel1.getWidth() - 15);
                        viewer.setTitle(photo.getName() + " (Scaled)");
                    } else if (photo.getWidth() >= jPanel1.getWidth()) {
                        jScrollPane2.setLocation(0, jPanel1.getHeight() / 2 - (photo.getHeight() / 2));
                        jScrollPane2.setSize(jPanel1.getWidth(), photo.getHeight() + 10);
                        photo.setWidth(jPanel1.getWidth() - 20);
                        viewer.setTitle(photo.getName() + " (Scaled - Width)");
                    } else if (photo.getHeight() >= jPanel1.getHeight()) {
                        jScrollPane2.setLocation(jPanel1.getWidth() / 2 - (photo.getWidth() / 2), 0);
                        jScrollPane2.setSize(photo.getWidth() + 10, jPanel1.getHeight());
                        photo.setHight(jPanel1.getHeight() - 20);
                        viewer.setTitle(photo.getName() + " (Scaled - Height)");
                    } else {
                        photo.resetSizes();
                        jScrollPane2.setSize(photo.getWidth() + 10, photo.getHeight() + 10);
                        jScrollPane2.setLocation(jPanel1.getWidth() / 2 - (photo.getWidth() / 2), jPanel1.getHeight() / 2 - (photo.getHeight() / 2));
                    }
                    String s = photo.getImage(jProgressBar1);
                    StringReader reader = new StringReader(s);
                    kit.read(reader, doc, 0);
                    jPanel3.hide();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton10;

    private javax.swing.JButton jButton11;

    private javax.swing.JButton jButton12;

    private javax.swing.JButton jButton13;

    private javax.swing.JButton jButton14;

    private javax.swing.JButton jButton8;

    private javax.swing.JButton jButton9;

    private javax.swing.JEditorPane jEditorPane1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JList jList1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JProgressBar jProgressBar1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JToolBar jToolBar2;

    private javax.swing.JToolBar jToolBar4;

    private javax.swing.JToolBar jToolBar5;
}
