package boc;

import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author  BOC TEAM
 */
public class servidorForm extends javax.swing.JFrame {

    private window apuntador;

    /** Creates new form servidorForm */
    public servidorForm() {
        initComponents();
    }

    public servidorForm(window R) {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        apuntador = R;
        try {
            System.out.println("mostrandoIP");
            jLabel5.setText(java.net.InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");
        setAlwaysOnTop(true);
        setBounds(new java.awt.Rectangle(200, 200, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        jLabel1.setText("Mines number:");
        jLabel2.setText("LifePacks number:");
        jTextField1.setText("20");
        jTextField2.setText("5");
        jButton1.setText("Run Server");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jLabel3.setText("Your Name:");
        jTextField3.setText("Server");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jLabel4.setText("Su IP es: ");
        jLabel5.setText("127.127.127.127");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(17, 17, 17).addComponent(jLabel1)).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE).addComponent(jButton1)).addGroup(layout.createSequentialGroup().addGap(83, 83, 83).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(25, 25, 25).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2))).addGroup(layout.createSequentialGroup().addGap(19, 19, 19).addComponent(jButton1))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jLabel5)).addContainerGap(13, Short.MAX_VALUE)));
        pack();
    }

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {
        if (jTextField3.getText().equals("")) {
            jTextField3.setText("Server");
        }
    }

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {
        if (jTextField3.getText().equals("Server")) {
            jTextField3.setText("");
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
    }

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
        this.setVisible(false);
        apuntador.serverItem.setEnabled(false);
        apuntador.clientItem.setEnabled(false);
        apuntador.setMines(Integer.parseInt(jTextField1.getText()));
        apuntador.setLife(Integer.parseInt(jTextField2.getText()));
        apuntador.myName = jTextField3.getText();
        apuntador.setSock(true, "");
        apuntador.renderStart();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new servidorForm().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;
}
