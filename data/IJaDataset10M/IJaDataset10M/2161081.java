package mazerunner;

/**
 *
 * @author  new-tim
 */
public class StatsWindow extends javax.swing.JFrame {

    /** Creates new form StatsWindow */
    public StatsWindow() {
        initComponents();
        this.setSize(170, 450);
    }

    private void initComponents() {
        stats = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        setTitle("MazeRunner Stats");
        setBounds(new java.awt.Rectangle(0, 0, 170, 450));
        setMinimumSize(new java.awt.Dimension(170, 450));
        stats.setLayout(null);
        jLabel1.setFont(new java.awt.Font("Dialog", 3, 24));
        jLabel1.setText("Stats");
        stats.add(jLabel1);
        jLabel1.setBounds(50, 10, 70, 20);
        jLabel2.setText("Score:");
        stats.add(jLabel2);
        jLabel2.setBounds(10, 50, 40, 15);
        stats.add(jSeparator1);
        jSeparator1.setBounds(0, 40, 170, 2);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mazerunner/images/coins/coin-green-small.png")));
        jLabel3.setText(":");
        stats.add(jLabel3);
        jLabel3.setBounds(10, 70, 30, 30);
        jLabel4.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel4.setText("0");
        stats.add(jLabel4);
        jLabel4.setBounds(40, 80, 120, 15);
        jLabel5.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel5.setText("0");
        stats.add(jLabel5);
        jLabel5.setBounds(50, 50, 110, 15);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(stats, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(stats, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE));
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new StatsWindow().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JPanel stats;
}
