package org.digitall.projects.apps.dbadmin_091.interfases;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JDialog;
import org.digitall.common.components.combos.JCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.JEntry;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPasswordField;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.sql.LibSQL;
import org.digitall.lib.ssl.MD5;

public class NewUserPanel extends BasicDialog {

    private AssignButton btnChangePassword = new AssignButton();

    private AcceptButton btnAccept = new AcceptButton();

    private BasicPanel jPanel1 = new BasicPanel("Datos del Usuario");

    private BasicLabel lblUser = new BasicLabel();

    private BasicLabel jLabel1 = new BasicLabel();

    private BasicLabel lblReTypePass = new BasicLabel();

    private BasicLabel jLabel2 = new BasicLabel();

    private CBInput cbPeople = new CBInput(0, "Person");

    private JCombo cbCostsCentre = new JCombo();

    private JEntry tfUser = new JEntry();

    private BasicPasswordField pfPassword = new BasicPasswordField();

    private BasicPasswordField pfRePassword = new BasicPasswordField();

    private BasicCheckBox chkLoginAllowed = new BasicCheckBox();

    private int error = 0;

    private String group = "";

    private int idUser;

    private char[] letrasPerm = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    static Set teclasPermitidas = new HashSet();

    private Set allowedChars = new HashSet();

    static Set teclas = new HashSet();

    private String userName = "";

    private int idPerson;

    private boolean newUser = true;

    public NewUserPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(392, 158));
        this.setSize(new Dimension(382, 254));
        this.setTitle("Nuevo Usuario");
        pfPassword.setEnabled(false);
        pfRePassword.setEnabled(false);
        cbPeople.setBounds(new Rectangle(15, 25, 335, 35));
        tfUser.setBounds(new Rectangle(15, 80, 145, 20));
        pfPassword.setBounds(new Rectangle(15, 120, 145, 20));
        pfPassword.setSize(new Dimension(145, 18));
        pfRePassword.setBounds(new Rectangle(170, 120, 145, 18));
        lblUser.setText("Usuario:");
        lblUser.setBounds(new Rectangle(15, 65, 140, 18));
        jLabel1.setText("Clave:");
        jLabel1.setBounds(new Rectangle(15, 105, 140, 18));
        lblReTypePass.setText("Comprobar clave:");
        lblReTypePass.setBounds(new Rectangle(170, 105, 140, 18));
        chkLoginAllowed.setText("Puede conectarse");
        chkLoginAllowed.setBounds(new Rectangle(170, 80, 145, 18));
        btnAccept.setBounds(new Rectangle(340, 200, 30, 25));
        btnAccept.setSize(new Dimension(30, 25));
        btnAccept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnAccept_actionPerformed(e);
            }
        });
        jPanel1.add(btnChangePassword, null);
        jPanel1.add(cbPeople, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(cbCostsCentre, null);
        jPanel1.add(chkLoginAllowed, null);
        jPanel1.add(lblReTypePass, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(lblUser, null);
        jPanel1.add(pfRePassword, null);
        jPanel1.add(pfPassword, null);
        jPanel1.add(tfUser, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(btnAccept, null);
        chkLoginAllowed.setSelected(true);
        cbCostsCentre.setBounds(new Rectangle(15, 165, 330, 20));
        jLabel2.setText("Centro de Costos");
        jLabel2.setBounds(new Rectangle(15, 145, 140, 18));
        jPanel1.setBounds(new Rectangle(5, 5, 365, 190));
        jPanel1.setLayout(null);
        btnChangePassword.setBounds(new Rectangle(320, 117, 30, 25));
        btnChangePassword.setSize(new Dimension(30, 25));
        btnChangePassword.setEnabled(false);
        btnChangePassword.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnChangePassword_actionPerformed(e);
            }
        });
        setearTeclasMinusculas();
        cbPeople.loadJCombo("org.getallpersonsforuser", -1);
        cbPeople.setSelectedID(-1);
        cbCostsCentre.loadJCombo("SELECT idcostcentre, code ||' - '|| name AS name,0 FROM cashflow.costscentres WHERE estado<>'*' ORDER BY name");
        cbCostsCentre.setSelectedID(-1);
        tfUser.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent focusEvent) {
                disableAllButtons();
            }

            public void focusLost(FocusEvent focusEvent) {
                checkUserExists();
            }
        });
        tfUser.requestFocus();
        pfPassword.setEnabled(false);
        pfRePassword.setEnabled(false);
    }

    private void setearTeclasMinusculas() {
        for (int i = 0; i < letrasPerm.length; i++) {
            allowedChars.add("" + letrasPerm[i]);
        }
    }

    public void setGroup(String _group) {
        group = _group;
    }

    private String makeInsertUser() {
        String filtroLogin, filtroGrupo = "";
        if (chkLoginAllowed.isSelected()) {
            filtroLogin = " LOGIN ";
        } else {
            filtroLogin = " NOLOGIN ";
        }
        if (group.equals("")) {
            filtroGrupo = "";
        } else {
            filtroGrupo = " ; GRANT " + group + " TO " + tfUser.getText().trim() + " ;";
        }
        return "CREATE USER " + tfUser.getText().trim() + filtroLogin + " ENCRYPTED PASSWORD 'md5" + MD5.getMD5(new String(pfPassword.getPassword()) + tfUser.getText().trim()) + "' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE " + filtroGrupo;
    }

    private boolean loadData() {
        try {
            tfUser.setText(userName);
            ResultSet rs = LibSQL.exFunction("org.getPersonByUserName", "'" + userName + "'");
            if (rs.next()) {
                this.setTitle("Modificar datos del Usuario");
                idPerson = rs.getInt("idperson");
                idUser = rs.getInt("iduser");
                cbPeople.setSelectedValue(rs.getString("lastname"));
                tfUser.setText(rs.getString("username"));
                if (cbCostsCentre.getItemCount() > 0) {
                    cbCostsCentre.setSelectedID(rs.getString("idcostscentre"));
                }
                pfPassword.setEnabled(false);
                pfRePassword.setEnabled(false);
                btnChangePassword.setEnabled(true);
                chkLoginAllowed.setSelected(rs.getBoolean("canlogin"));
                cbPeople.loadJCombo("org.getallpersonsforuser", idUser);
                cbPeople.setSelectedID(idPerson);
                newUser = false;
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int addUserData() {
        String params = "0,'','" + cbPeople.getSelectedItem() + "',0,0,0,0,0,'','','',null,null,0,0,'',''";
        if (newUser) {
            params = cbPeople.getSelectedValue() + ",'" + tfUser.getText().trim() + "'";
            idUser = LibSQL.getInt("org.addUser", params);
        } else {
            params = idUser + "," + cbPeople.getSelectedValue() + "," + cbCostsCentre.getSelectedValue() + ",'" + tfUser.getText().trim() + "'";
            idUser = LibSQL.getInt("org.setUser", params);
        }
        params = idPerson + "," + cbCostsCentre.getSelectedValue();
        return idUser;
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
        boolean result = false;
        if (control()) {
            if (newUser) {
                String insertUser = makeInsertUser();
                result = LibSQL.exActualizar('a', insertUser);
            } else {
                result = changeUserData();
            }
            if (result) {
                if (addUserData() >= 0) {
                    this.dispose();
                } else {
                    Advisor.messageBox("Ocurri� un error al guardar los datos", "Accion cancelada");
                }
            } else {
                Advisor.messageBox("Ocurri� un error al guardar los datos", "Accion cancelada");
            }
        } else {
            errores();
        }
    }

    private boolean changeUserData() {
        boolean result = true;
        if (!userName.equals(tfUser.getText().trim())) {
            LibSQL.exActualizar('a', "ALTER ROLE \"" + userName + "\" RENAME TO \"" + tfUser.getText() + "\";");
        }
        LibSQL.exActualizar('a', "ALTER ROLE \"" + userName + "\"" + (chkLoginAllowed.isSelected() ? " LOGIN " : " NOLOGIN ") + ";");
        if (pfPassword.isEnabled()) {
            result = LibSQL.getBoolean("setpassword", "'" + tfUser.getText() + "','md5" + MD5.getMD5(new String(pfPassword.getPassword()) + tfUser.getText().trim()) + "'");
        }
        return result;
    }

    private boolean control() {
        boolean valido = false;
        if (cbPeople.getSelectedIndex() < 0) {
            error = 2;
        } else if (tfUser.getText().trim().equals("")) {
            error = 3;
        } else if (!tfUser.getText().trim().equals(userName) && LibSQL.getBoolean("org.userexists", "'" + tfUser.getText() + "'")) {
            error = 4;
        } else if (!comparePassword() && pfPassword.isEnabled()) {
        } else {
            valido = true;
        }
        return valido;
    }

    private boolean comparePassword() {
        if (pfPassword.getPassword().length < 6) {
            error = 5;
            return false;
        } else if (!MD5.getMD5(new String(pfPassword.getPassword())).equals(MD5.getMD5(new String(pfRePassword.getPassword())))) {
            error = 6;
            return false;
        } else return true;
    }

    private void errores() {
        switch(error) {
            case 1:
                Advisor.messageBox("Last name required", "Error");
                break;
            case 2:
                Advisor.messageBox("First name required", "Error");
                break;
            case 3:
                Advisor.messageBox("User name required", "Error");
                break;
            case 4:
                Advisor.messageBox("User name already exists, please type a different name", "Error");
                break;
            case 5:
                Advisor.messageBox("Password length must be greather than 5 characters", "Error");
                break;
            case 6:
                Advisor.messageBox("Passwords doesn't match", "Error");
                break;
        }
    }

    public boolean setUserName(String _userName) {
        if (_userName != null) {
            this.userName = _userName;
        }
        return loadData();
    }

    private void btnChangePassword_actionPerformed(ActionEvent e) {
        pfPassword.setEnabled(true);
        pfRePassword.setEnabled(true);
    }

    private void checkUserExists() {
        if (tfUser.getText().trim().length() > 0) {
            if (newUser) {
                if (LibSQL.getBoolean("org.userexists", "'" + tfUser.getText() + "'")) {
                    Advisor.messageBox("El usuario " + tfUser.getText() + " ya existe", "Error");
                    tfUser.requestFocus();
                    pfPassword.setEnabled(false);
                    pfRePassword.setEnabled(false);
                    btnAccept.setEnabled(false);
                    btnChangePassword.setEnabled(false);
                    chkLoginAllowed.setEnabled(false);
                } else {
                    pfPassword.setEnabled(true);
                    pfRePassword.setEnabled(true);
                    btnAccept.setEnabled(true);
                    btnChangePassword.setEnabled(true);
                    chkLoginAllowed.setEnabled(true);
                }
            } else {
                if (!tfUser.getText().trim().equals(userName) && LibSQL.getBoolean("org.userexists", "'" + tfUser.getText() + "'")) {
                    Advisor.messageBox("El usuario " + tfUser.getText() + " ya existe", "Error");
                    tfUser.requestFocus();
                    pfPassword.setEnabled(false);
                    pfRePassword.setEnabled(false);
                    btnAccept.setEnabled(false);
                    btnChangePassword.setEnabled(false);
                    chkLoginAllowed.setEnabled(false);
                } else {
                    btnAccept.setEnabled(true);
                    btnChangePassword.setEnabled(true);
                    chkLoginAllowed.setEnabled(true);
                }
            }
        } else if (userName.trim().length() > 0) {
            btnAccept.setEnabled(true);
            btnChangePassword.setEnabled(true);
            chkLoginAllowed.setEnabled(true);
        }
    }

    private void disableAllButtons() {
        pfPassword.setEnabled(false);
        pfRePassword.setEnabled(false);
        btnAccept.setEnabled(false);
        btnChangePassword.setEnabled(false);
        chkLoginAllowed.setEnabled(false);
    }
}
