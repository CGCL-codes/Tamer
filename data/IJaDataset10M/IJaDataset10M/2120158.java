package ba_leipzig_lending_and_service_control_system.view;

import ba_leipzig_lending_and_service_control_system.conroller.ctrlDatabase;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlLayout;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlMain;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlTools;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlXML;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.security.MessageDigest;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.apache.xerces.impl.dv.util.Base64;

/**
 * GUI-Class to show the detailed dialog for editing users
 *
 * @author Chris Hagen
 */
public class viewUserDetail extends JDialog {

    private final int WIDTH = 400;

    private final int HEIGHT = 230;

    private JTextField txtID = null;

    private JTextField txtName = null;

    private JTextField txtVName = null;

    private JPasswordField txtPWD = null;

    private JComboBox cboRight = null;

    private JButton btnAbbr = null;

    private final viewUserDetail viewuserdetail;

    private boolean isPasswordChanged = false;

    /**
     *  Creates a new instance of viewUserDetail
     *
     *  @param  parent parent object
     *  @param  type type shortcut for the presentation of the dialog:
     *               N - create new user
     *               B - edit user
     *               A - show user in non editable mode
     *  @param  lauid user id
     */
    public viewUserDetail(Component parent, final char type, final String lauid) {
        super((JDialog) parent);
        viewuserdetail = this;
        try {
            String str = "";
            if (type == 'N') str = ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "statcreate"); else if (type == 'B') str = ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "statedit"); else if (type == 'A') str = ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "statview");
            ctrlLayout.getDialogLayout(this, WIDTH, HEIGHT, str);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowListener() {

                public void windowClosing(WindowEvent e) {
                    try {
                        if (type == 'A' || ctrlTools.showYesNoQuestionMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "reallyexit")) == JOptionPane.YES_OPTION) {
                            if (type == 'B') ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER SET LAUXART='N' WHERE LAUID='" + lauid + "'");
                            viewuserdetail.dispose();
                        }
                    } catch (Exception ex) {
                        ctrlTools.showErrorMessage(viewuserdetail, ex);
                    }
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowClosed(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowOpened(WindowEvent e) {
                }
            });
            JLabel lblID = new JLabel(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblid") + ":");
            lblID.setBounds(10, 10, 100, 20);
            lblID.setFont(ctrlMain.getFont());
            this.add(lblID);
            txtID = new JTextField();
            txtID.setBounds(130, 10, 100, 20);
            txtID.setFont(ctrlMain.getFont());
            txtID.setDocument(ctrlTools.getInstance().getDefDoc(16));
            this.add(txtID);
            JLabel lblName = new JLabel(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblname") + ":");
            lblName.setBounds(10, 40, 100, 20);
            lblName.setFont(ctrlMain.getFont());
            this.add(lblName);
            txtName = new JTextField();
            txtName.setBounds(130, 40, 250, 20);
            txtName.setFont(ctrlMain.getFont());
            txtName.setDocument(ctrlTools.getInstance().getDefDoc(50));
            this.add(txtName);
            JLabel lblVName = new JLabel(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblfname") + ":");
            lblVName.setBounds(10, 70, 100, 20);
            lblVName.setFont(ctrlMain.getFont());
            this.add(lblVName);
            txtVName = new JTextField();
            txtVName.setBounds(130, 70, 250, 20);
            txtVName.setFont(ctrlMain.getFont());
            txtVName.setDocument(ctrlTools.getInstance().getDefDoc(30));
            this.add(txtVName);
            JLabel lblPWD = new JLabel(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblpassword") + ":");
            lblPWD.setBounds(10, 100, 100, 20);
            lblPWD.setFont(ctrlMain.getFont());
            this.add(lblPWD);
            txtPWD = new JPasswordField();
            txtPWD.setBounds(130, 100, 250, 20);
            txtPWD.setFont(ctrlMain.getFont());
            txtPWD.setDocument(ctrlTools.getInstance().getDefDoc(32));
            txtPWD.setEchoChar('*');
            txtPWD.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    isPasswordChanged = true;
                    txtPWD.setText("");
                }

                public void focusLost(FocusEvent e) {
                }
            });
            this.add(txtPWD);
            JLabel lblRight = new JLabel(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblright") + ":");
            lblRight.setBounds(10, 130, 130, 20);
            lblRight.setFont(ctrlMain.getFont());
            this.add(lblRight);
            cboRight = new JComboBox();
            cboRight.setBounds(130, 130, 250, 20);
            cboRight.setFont(ctrlMain.getFont());
            for (int i = 0; i <= 3; i++) cboRight.addItem(i + " - " + ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cboright[@right='" + i + "'"));
            this.add(cboRight);
            JButton btnOK = new JButton(ctrlXML.getInstance().getLanguageDataValue("all", "ok"));
            btnOK.setFont(ctrlMain.getFont());
            btnOK.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (type == 'A') {
                            viewuserdetail.dispose();
                            return;
                        }
                        if (ctrlTools.showYesNoQuestionMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "reallyapply")) == JOptionPane.YES_OPTION) {
                            if (txtID.getText().trim().length() == 0) {
                                ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "missingvalue") + ctrlXML.getInstance().getLanguageDataValue(viewuserdetail.getClass().getSimpleName(), "lblid"));
                                txtID.requestFocus();
                                return;
                            }
                            if (txtName.getText().trim().length() == 0) {
                                ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "missingvalue") + ctrlXML.getInstance().getLanguageDataValue(viewuserdetail.getClass().getSimpleName(), "lblname"));
                                txtName.requestFocus();
                                return;
                            }
                            if (new String(txtPWD.getPassword()).trim().length() == 0) {
                                ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "missingvalue") + ctrlXML.getInstance().getLanguageDataValue(viewuserdetail.getClass().getSimpleName(), "lblpassword"));
                                txtPWD.requestFocus();
                                return;
                            }
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            byte[] digest = md.digest(new String(txtPWD.getPassword()).getBytes());
                            String pwd = Base64.encode(digest);
                            if (type == 'N') {
                                if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAUSER", "UPPER(LAUID)='" + txtID.getText().trim().toUpperCase() + "'") > 0) {
                                    ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "existingid"));
                                    txtID.requestFocus();
                                    return;
                                }
                                ctrlDatabase.executeQuery(ctrlMain.getConnection(), "INSERT INTO LASCS.LAUSER(LAUSTAT, LAUID, " + "LAUNAME, LAUFNAME, LAURIGHT, LAUPWD, LAUXNTZ) " + "VALUES('A', '" + txtID.getText() + "', '" + txtName.getText().trim() + "', '" + ((txtVName.getText() == null || txtVName.getText().trim().length() == 0) ? " " : txtVName.getText()) + "', " + cboRight.getSelectedItem().toString().substring(0, 1) + ", '" + pwd + "', '" + ctrlMain.getUser() + "')");
                            }
                            if (type == 'B') {
                                ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER SET " + "LAUNAME='" + txtName.getText() + "', LAUFNAME='" + ((txtVName.getText() == null || txtVName.getText().trim().length() == 0) ? " " : txtVName.getText()) + "', LAURIGHT=" + cboRight.getSelectedItem().toString().substring(0, 1) + (isPasswordChanged ? ", LAUPWD='" + pwd + "'" : "") + ", LAUXNTZ='" + ctrlMain.getUser() + "', LAUXART='N' WHERE LAUID='" + txtID.getText().trim() + "'");
                            }
                            viewuserdetail.dispose();
                        }
                    } catch (Exception ex) {
                        ctrlTools.showErrorMessage(viewuserdetail, ex);
                    }
                }
            });
            btnOK.setBounds(10, 170, 110, 25);
            this.add(btnOK);
            btnAbbr = new JButton(ctrlXML.getInstance().getLanguageDataValue("all", "cancel"));
            btnAbbr.setFont(ctrlMain.getFont());
            btnAbbr.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (ctrlTools.showYesNoQuestionMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "reallyexit")) == JOptionPane.YES_OPTION) {
                            if (type == 'B') ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER SET LAUXART='N' WHERE LAUID='" + lauid + "'");
                            viewuserdetail.dispose();
                        }
                    } catch (Exception ex) {
                        ctrlTools.showErrorMessage(viewuserdetail, ex);
                    }
                }
            });
            btnAbbr.setBounds(130, 170, 110, 25);
            this.add(btnAbbr);
            setDefaults(type, lauid);
            this.setModal(true);
            this.setVisible(true);
        } catch (Exception ex) {
            ctrlTools.showErrorMessage(this, ex);
        }
    }

    /**
     *  Sets the default values of the database record for the controls
     *
     *  @param  type type shortcut for the presentation of the dialog:
     *               N - create new user
     *               B - edit user
     *               A - show user in non editable mode
     *  @param  lauid user id
     */
    private void setDefaults(char type, String lauid) {
        try {
            if (type == 'N') {
                txtID.setEditable(true);
                txtName.setEditable(true);
                txtVName.setEditable(true);
                cboRight.setEnabled(true);
                txtPWD.setEditable(true);
                btnAbbr.setVisible(true);
            }
            if (type == 'B') {
                txtID.setEditable(false);
                txtName.setEditable(true);
                txtVName.setEditable(true);
                cboRight.setEnabled(true);
                txtPWD.setEditable(true);
                btnAbbr.setVisible(true);
                txtID.setText(lauid);
                txtName.setText(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAUNAME", "LAUSER", "LAUID='" + lauid + "'"));
                txtVName.setText(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAUFNAME", "LAUSER", "LAUID='" + lauid + "'"));
                String str = ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAURIGHT", "LAUSER", "LAUID='" + lauid + "'");
                for (int i = 0; i < cboRight.getItemCount(); i++) {
                    if (cboRight.getItemAt(i).toString().substring(0, 1).equals(str)) cboRight.setSelectedIndex(i);
                }
                txtPWD.setText("*****");
            }
            if (type == 'A') {
                txtID.setEditable(false);
                txtName.setEditable(false);
                txtVName.setEditable(false);
                cboRight.setEnabled(false);
                txtPWD.setEditable(false);
                btnAbbr.setVisible(false);
                txtID.setText(lauid);
                txtName.setText(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAUNAME", "LAUSER", "LAUID='" + lauid + "'"));
                txtVName.setText(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAUFNAME", "LAUSER", "LAUID='" + lauid + "'"));
                String str = ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAURIGHT", "LAUSER", "LAUID='" + lauid + "'");
                for (int i = 0; i < cboRight.getItemCount(); i++) {
                    if (cboRight.getItemAt(0).toString().substring(0, 1).equals(str)) cboRight.setSelectedIndex(i);
                }
                txtPWD.setText("*****");
            }
        } catch (Exception ex) {
            ctrlTools.showErrorMessage(this, ex);
        }
    }
}
