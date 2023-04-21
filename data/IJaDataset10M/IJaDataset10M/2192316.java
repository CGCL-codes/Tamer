package scrabble.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 *
 * @author Tin-moon
 */
public class NewGameDialog extends JDialog {

    public enum Players {

        two, three, four
    }

    Players p;

    public static String namePlayer1;

    public static String namePlayer2;

    public static String namePlayer3;

    public static String namePlayer4;

    public static int countPlayer;

    public static int getCountPlayer() {
        return countPlayer;
    }

    public static String getNamePlayer1() {
        return namePlayer1;
    }

    public static String getNamePlayer2() {
        return namePlayer2;
    }

    public static String getNamePlayer3() {
        return namePlayer3;
    }

    public static String getNamePlayer4() {
        return namePlayer4;
    }

    public NewGameDialog(final mainJFrame parent) {
        super(parent, "Эрудит - Новая игра", true);
        initComponents();
        p = Players.two;
        jRadioButton1.setEnabled(true);
        jRadioButton2.setEnabled(true);
        jRadioButton3.setEnabled(true);
        jRadioButton1.setFocusable(true);
        jRadioButton1.setSelected(true);
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        jTextField3.setVisible(false);
        jTextField4.setVisible(false);
        jButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switch(p) {
                    case two:
                        if (jTextField1.getText().isEmpty() | jTextField2.getText().isEmpty()) {
                            jButton1.setEnabled(false);
                        } else if (jTextField1.getText().equals(jTextField2.getText())) {
                            jButton1.setEnabled(false);
                        } else {
                            jButton1.setEnabled(true);
                            namePlayer1 = jTextField1.getText();
                            namePlayer2 = jTextField2.getText();
                            namePlayer3 = null;
                            namePlayer4 = null;
                            countPlayer = 2;
                            parent.setConfigFromDialog(getCountPlayer(), getNamePlayer1(), getNamePlayer2(), getNamePlayer3(), getNamePlayer4());
                            setVisible(false);
                        }
                        break;
                    case three:
                        if (jTextField1.getText().isEmpty() | jTextField2.getText().isEmpty() | jTextField3.getText().isEmpty()) {
                            jButton1.setEnabled(false);
                        } else if (jTextField1.getText().equals(jTextField2.getText()) | jTextField1.getText().equals(jTextField3.getText()) | jTextField2.getText().equals(jTextField3.getText())) {
                            jButton1.setEnabled(false);
                        } else {
                            jButton1.setEnabled(true);
                            namePlayer1 = jTextField1.getText();
                            namePlayer2 = jTextField2.getText();
                            namePlayer3 = jTextField3.getText();
                            namePlayer4 = null;
                            countPlayer = 3;
                            parent.setConfigFromDialog(getCountPlayer(), getNamePlayer1(), getNamePlayer2(), getNamePlayer3(), getNamePlayer4());
                            setVisible(false);
                        }
                        break;
                    case four:
                        if (jTextField1.getText().isEmpty() | jTextField2.getText().isEmpty() | jTextField3.getText().isEmpty() | jTextField4.getText().isEmpty()) {
                            jButton1.setEnabled(false);
                        } else if (jTextField1.getText().equals(jTextField2.getText()) | jTextField1.getText().equals(jTextField3.getText()) | jTextField1.getText().equals(jTextField4.getText()) | jTextField2.getText().equals(jTextField3.getText()) | jTextField2.getText().equals(jTextField4.getText()) | jTextField3.getText().equals(jTextField4.getText())) {
                            jButton1.setEnabled(false);
                        } else {
                            jButton1.setEnabled(true);
                            namePlayer1 = jTextField1.getText();
                            namePlayer2 = jTextField2.getText();
                            namePlayer3 = jTextField3.getText();
                            namePlayer4 = jTextField3.getText();
                            countPlayer = 4;
                            parent.setConfigFromDialog(getCountPlayer(), getNamePlayer1(), getNamePlayer2(), getNamePlayer3(), getNamePlayer4());
                            setVisible(false);
                        }
                        break;
                }
            }
        });
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jRadioButton2.setText("3 игрока");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jRadioButton1.setText("2 игрока");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jRadioButton3.setText("4 игрока");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        jLabel1.setText("Количество участников");
        jLabel2.setText("Имя 1 игрока");
        jLabel3.setText("Имя 2 игрока");
        jLabel4.setText("Имя 3 игрока");
        jLabel5.setText("Имя 4 игрока");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel2).addGap(18, 18, 18).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel3).addGap(18, 18, 18).addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel4).addGap(18, 18, 18).addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel5).addGap(18, 18, 18).addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jButton1.setText("Начать игру");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
        });
        jButton2.setText("Отмена");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton2).addComponent(jRadioButton1).addComponent(jRadioButton3)).addGap(18, 18, 18).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGap(117, 117, 117).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jRadioButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jRadioButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jRadioButton3)).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton1.setSelected(false);
        jRadioButton3.setSelected(false);
        jLabel4.setVisible(true);
        jLabel5.setVisible(false);
        jTextField3.setVisible(true);
        jTextField4.setVisible(false);
        p = Players.three;
        jTextField4.setText("");
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton2.setSelected(false);
        jRadioButton3.setSelected(false);
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        jTextField3.setVisible(false);
        jTextField4.setVisible(false);
        p = Players.two;
        jTextField3.setText("");
        jTextField4.setText("");
    }

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton1.setSelected(false);
        jRadioButton2.setSelected(false);
        jLabel4.setVisible(true);
        jLabel5.setVisible(true);
        jTextField3.setVisible(true);
        jTextField4.setVisible(true);
        p = Players.four;
    }

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {
        jButton1.setEnabled(true);
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JRadioButton jRadioButton3;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField4;
}
