package client.gui;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class NameFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtName;

    private JButton btnCancel;

    private JButton btnOk;

    private NameListener listener;

    public NameFrame() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setVisible(true);
            getContentPane().setLayout(null);
            {
                txtName = new JTextField();
                getContentPane().add(txtName);
                txtName.setBounds(28, 35, 158, 21);
                txtName.requestFocus();
                txtName.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        btnOkActionPerformed(evt);
                    }
                });
            }
            {
                btnOk = new JButton();
                getContentPane().add(btnOk);
                btnOk.setText("Ok");
                btnOk.setBounds(28, 68, 59, 21);
                btnOk.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        btnOkActionPerformed(evt);
                    }
                });
            }
            {
                btnCancel = new JButton();
                getContentPane().add(btnCancel);
                btnCancel.setText("Cancel");
                btnCancel.setBounds(117, 68, 69, 21);
            }
            pack();
            setSize(400, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNameListener(NameListener listener) {
        this.listener = listener;
    }

    public void nameGood() {
        listener.setName(txtName.getText().toString());
        this.dispose();
    }

    public void nameBad() {
        JOptionPane.showMessageDialog(null, "Name already exists");
        txtName.setText("");
        txtName.requestFocus();
    }

    public void btnOkActionPerformed(ActionEvent e) {
        listener.sendName(txtName.getText().toString());
    }
}
