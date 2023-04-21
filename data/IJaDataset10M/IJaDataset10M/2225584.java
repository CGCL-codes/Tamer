package roomallocationplanner;

import java.util.Vector;

public class RoomVIEW extends javax.swing.JFrame {

    private static Vector<Room> raumvector = new Vector<Room>();

    /** Creates new form RoomVIEW */
    public RoomVIEW() {
        initComponents();
        jTextArea1.setVisible(false);
    }

    public static void addRaum(Room r) {
        raumvector.add(r);
    }

    public static void overrideRaum(Room r) {
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                wo(evt);
            }
        });
        jLabel1.setFont(new java.awt.Font("Arial", 0, 24));
        jLabel1.setText("Existierende Räume");
        jButton1.setText("Details");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("Bearbeiten");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(56, 56, 56).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(14, 14, 14).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton1).addComponent(jButton2)).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGap(25, 25, 25).addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jButton2)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))).addGap(25, 25, 25)));
        jButton3.setText("Abbrechen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(62, 62, 62).addComponent(jLabel1)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(65, 65, 65)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jButton3))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(32, 32, 32).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3)));
        pack();
    }

    private void wo(java.awt.event.WindowEvent evt) {
        jList1.setListData(raumvector);
    }

    private void close(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        Room rBea = (Room) jList1.getSelectedValue();
        raumerstellenVIEW reV = new raumerstellenVIEW();
        reV.erstellenFalse();
        reV.speichernTrue();
        reV.setKap(rBea.getKap());
        reV.setRaumbez(rBea.getRaumbezeichnung());
        reV.setRaumnr((rBea.getRNr()));
        reV.setComp(rBea.getComp());
        reV.setLoet(rBea.getLoet());
        reV.setTaf(rBea.getTaf());
        reV.setBeam(rBea.getBeam());
        reV.js1true();
        reV.js2true();
        reV.setVisible(true);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Room rrr = (Room) jList1.getSelectedValue();
        jTextArea1.setVisible(true);
        jTextArea1.setText(rrr.getRaumbezeichnung() + "\n" + "Raumnummer: " + rrr.getRNr() + "\nKapazität: " + rrr.getKap());
    }

    /**
     * @param args the command line arguments
     */
    public static void rvmain() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RoomVIEW().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JList jList1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextArea jTextArea1;
}
