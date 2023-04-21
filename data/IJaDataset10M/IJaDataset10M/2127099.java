package listino;

import globali.jcFunzioni;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;

/**
 *
 * @author  sasch
 */
public class jdCambiaCategoria extends javax.swing.JDialog {

    public int catID = -1;

    private Vector categorieID = new Vector();

    /** Creates new form jdCambiaCategoria */
    public jdCambiaCategoria(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jcbCategorie = new javax.swing.JComboBox();
        jbAnnulla = new javax.swing.JButton();
        jbProcedi = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("jMagazzino - Nuova categoria");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Nuova categoria"));
        jcbCategorie.setMaximumRowCount(30);
        jcbCategorie.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jcbCategorie, 0, 523, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jcbCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(10, Short.MAX_VALUE)));
        jbAnnulla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/cancella_20.png")));
        jbAnnulla.setText("annulla");
        jbAnnulla.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbAnnullaMousePressed(evt);
            }
        });
        jbProcedi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/procedi.png")));
        jbProcedi.setText("procedi");
        jbProcedi.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbProcediMousePressed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jbProcedi).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbAnnulla))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbAnnulla).addComponent(jbProcedi)).addGap(11, 11, 11)));
        pack();
    }

    private void jbProcediMousePressed(java.awt.event.MouseEvent evt) {
        catID = (Integer) categorieID.get(jcbCategorie.getSelectedIndex());
        this.dispose();
    }

    private void jbAnnullaMousePressed(java.awt.event.MouseEvent evt) {
        catID = -1;
        this.dispose();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (screenSize.width / 2 - this.getWidth() / 2), (int) (screenSize.height / 2 - this.getHeight() / 2));
        jcFunzioni.caricaCategorie(-1, jcbCategorie, categorieID);
        jcbCategorie.setRenderer(new globali.renderComboBox.renderCbCategorie());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                jdCambiaCategoria dialog = new jdCambiaCategoria(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton jbAnnulla;

    private javax.swing.JButton jbProcedi;

    private javax.swing.JComboBox jcbCategorie;
}
