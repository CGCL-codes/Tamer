package SDClient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

public class GetPasswordDialog extends javax.swing.JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9028433929103127824L;

    private JPasswordField jPasswordField1;

    private JButton jButtonCancel;

    private JButton jButtonOK;

    private JLabel jLabel1;

    private String result;

    /**
	* Auto-generated main method to display this JDialog
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GetPasswordDialog inst = new GetPasswordDialog(frame);
        inst.setVisible(true);
    }

    public GetPasswordDialog(JFrame frame) {
        super(frame);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        initGUI();
    }

    private void initGUI() {
        try {
            {
                getContentPane().setLayout(null);
                this.setLocationByPlatform(true);
                this.setModal(true);
            }
            {
                jPasswordField1 = new JPasswordField();
                getContentPane().add(jPasswordField1);
                jPasswordField1.setBounds(14, 35, 182, 21);
                jPasswordField1.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent evt) {
                        jKeyReleased(evt);
                    }
                });
            }
            {
                jLabel1 = new JLabel();
                getContentPane().add(jLabel1);
                jLabel1.setText("Enter artefact password");
                jLabel1.setBounds(14, 14, 175, 21);
            }
            {
                jButtonOK = new JButton();
                getContentPane().add(jButtonOK);
                jButtonOK.setText("OK");
                jButtonOK.setBounds(14, 77, 84, 21);
                jButtonOK.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        jButtonOKActionPerformed(evt);
                    }
                });
                jButtonOK.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent evt) {
                        jKeyReleased(evt);
                    }
                });
            }
            {
                jButtonCancel = new JButton();
                getContentPane().add(jButtonCancel);
                jButtonCancel.setText("Cancel");
                jButtonCancel.setBounds(112, 77, 84, 21);
                jButtonCancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        jButtonCancelActionPerformed(evt);
                    }
                });
                jButtonCancel.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent evt) {
                        jKeyReleased(evt);
                    }
                });
            }
            this.setSize(218, 146);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void jKeyReleased(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.jButtonOKActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.jButtonCancelActionPerformed(null);
        }
    }

    protected void jButtonCancelActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private void jButtonOKActionPerformed(ActionEvent evt) {
        result = new String(jPasswordField1.getPassword());
        setVisible(false);
    }

    public String getResult() {
        return result;
    }
}
