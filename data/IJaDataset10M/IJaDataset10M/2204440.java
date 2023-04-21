package GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import GUI.GUIReviewers.mainComponents.ReviewerEntryMenu;
import Repository.RepositoryAuthors.Connect;

/**
 *
 * @author lazy
 */
public class UserCommentsSubmit extends javax.swing.JFrame {

    private String sId;

    private static JFrame rem;

    /**
	 * 
	 */
    private static final long serialVersionUID = -1152405182233893064L;

    /** Creates new form UserCommentsSubmit */
    public UserCommentsSubmit(String sId, JFrame rem) {
        this.sId = sId;
        this.rem = rem;
        initComponents();
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                UserCommentsSubmit.rem.setVisible(true);
            }

            public void windowClosed(WindowEvent evt) {
                UserCommentsSubmit.rem.setVisible(true);
            }
        });
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Submit Comment");
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);
        jLabel1.setText("Subject");
        jLabel2.setText("Comment");
        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButton(evt);
            }
        });
        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButton(evt);
            }
        });
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Submit Opinion");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(26, 26, 26).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1).addComponent(jLabel2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTextField1).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButton2).addGap(104, 104, 104).addComponent(jButton1)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1).addComponent(jButton2)).addGap(20, 20, 20)));
        pack();
    }

    private void closeButton(java.awt.event.ActionEvent evt) {
        this.dispose();
        UserCommentsSubmit.rem.setVisible(true);
    }

    private void submitButton(java.awt.event.ActionEvent evt) {
        if (jTextArea1.getText().compareTo("") == 0 || jTextField1.getText().compareTo("") == 0) {
            JOptionPane.showMessageDialog(null, "Subject or message missing.", "Info", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String title = jTextField1.getText();
        String msg = jTextArea1.getText();
        try {
            Connect.update("insert into opinions(subject, comment, date_comm, id_login) values ('" + title + "', '" + msg + "', now(), " + sId + ")");
            JOptionPane.showMessageDialog(null, "Opinion submited!", "Info", JOptionPane.INFORMATION_MESSAGE);
            closeButton(null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextField jTextField1;
}
