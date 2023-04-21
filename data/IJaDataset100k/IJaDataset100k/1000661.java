package jmsngr;

/**
 *
 * @author  PHELIZOT Yvan
 */
public class JDialog extends javax.swing.JFrame {

    private YClient m_yclient = null;

    private String m_contact = null;

    /** Creates new form JDialog */
    public JDialog(YClient yclient, String contact) {
        initComponents();
        m_yclient = yclient;
        m_contact = contact;
        m_yclient.ajouterComposantNotifiantEcriture(m_contact, jEPMsg);
        setTitle("Discussion avec " + m_contact);
        setVisible(true);
    }

    public void recevoirMessageDe(String msg) {
        System.out.println("[I] Message \"" + jEPMsg.getText() + "\" recu de " + m_contact);
        jEPRep.setText(jEPRep.getText() + "\n" + m_contact + " : " + msg);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jEPRep = new javax.swing.JEditorPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEPMsg = new javax.swing.JEditorPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(200, 200));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        jEPRep.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jEPRep.setEnabled(false);
        jEPRep.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setViewportView(jEPRep);
        jEPMsg.setMinimumSize(new java.awt.Dimension(0, 0));
        jEPMsg.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jEPMsgKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jEPMsg);
        jButton1.setText("Envoyer");
        jButton1.setActionCommand("envoyer");
        jButton1.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton2.setText("Buzz!");
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton2KeyPressed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)).addContainerGap()));
        pack();
    }

    private void jButton2KeyPressed(java.awt.event.KeyEvent evt) {
        if (m_yclient != null) {
            m_yclient.sendBuzz(m_contact);
        }
    }

    private void jEPMsgKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jEPRep.setText(jEPRep.getText() + "\n" + m_yclient.getId() + " : " + jEPMsg.getText());
            System.out.println("[I] Envoi de message \"" + jEPMsg.getText() + "\" a " + m_contact);
            m_yclient.envoyerMessageA(m_contact, jEPMsg.getText());
            jEPMsg.setText("");
        }
    }

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
        if (m_yclient != null) {
            jEPRep.setText(jEPRep.getText() + "\n" + m_yclient.getId() + " : " + jEPMsg.getText());
            m_yclient.envoyerMessageA(m_contact, jEPMsg.getText());
            System.out.println("[I] Envoi de message \"" + jEPMsg.getText() + "\" a " + m_contact);
            jEPMsg.setText("");
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        m_yclient.fermerDialogueAvec(m_contact);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new JDialog(null, "").setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JEditorPane jEPMsg;

    private javax.swing.JEditorPane jEPRep;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;
}
