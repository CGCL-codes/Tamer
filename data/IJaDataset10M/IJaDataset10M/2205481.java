package org.ultracalendar.standaloneclient.views;

/**
 * Das About-Fenster.
 * 
 * @author Hannes John
 * @since 21.05.2009 18:04:58
 */
public class AboutFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = -2176247320142382161L;

    /** Die einzige Instanz (Singleton) */
    private static final AboutFrame instance = new AboutFrame();

    /** Creates new form AboutFrame */
    private AboutFrame() {
        initComponents();
    }

    public static AboutFrame getInstance() {
        return instance;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        setTitle("About UltraCalendar");
        setAlwaysOnTop(true);
        setBackground(java.awt.Color.white);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                centerAboutFrame(evt);
            }
        });
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo-128.png")));
        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        jLabel2.setText("UltraCalendar");
        jLabel3.setText("A demonstration on how Enterprise Java Beans (EJBs) can be used");
        jLabel4.setText("This project emerged from the CBSE lecture at TU Dresden in 2009.");
        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        jLabel5.setText("Project members:");
        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                closeAboutFrame(evt);
            }
        });
        jLabel6.setText("Hannes John");
        jLabel7.setText("Robert Schulze");
        jLabel9.setText("together with the Cheesman Daniels Process.");
        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(511, Short.MAX_VALUE).addComponent(jButton1).addContainerGap()).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel9).addComponent(jLabel4).addComponent(jLabel5).addComponent(jLabel7).addComponent(jLabel6)).addContainerGap(16, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addGap(18, 18, 18).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel7)).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE).addComponent(jButton1).addContainerGap()));
        pack();
    }

    private void closeAboutFrame(final java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void centerAboutFrame(final java.awt.event.WindowEvent evt) {
        this.setLocationRelativeTo(MainFrame.getInstance());
    }

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AboutFrame.getInstance().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel9;
}
