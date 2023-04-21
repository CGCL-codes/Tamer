package com.calipso.reportgenerator.usermanager;

import com.calipso.reportgenerator.common.exception.InfoException;
import com.calipso.reportgenerator.common.ShowExceptionMessageDialog;
import com.calipso.reportgenerator.common.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.*;

/**
 * frame que se invoca para agregar o modificar un rol
 */
public class RolManagerModifyFrame extends JDialog {

    private javax.swing.JButton accept;

    private javax.swing.JButton cancel;

    private javax.swing.JLabel JLabelRolId;

    private javax.swing.JButton add;

    private javax.swing.JButton del;

    private javax.swing.JLabel jLabelUsers;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTextField jTextFieldRolId;

    private JList listUsers;

    private DefaultListModel listModel;

    private UserManager userManager;

    private Rol rol;

    /**
     * Constructor para crear un rol nuevo
     * @param parent
     * @param userManager
     */
    public RolManagerModifyFrame(JFrame parent, UserManager userManager) {
        super(parent, true);
        setResizable(false);
        this.userManager = userManager;
        initComponents();
        listModel = new DefaultListModel();
        listUsers = new JList(listModel);
        listUsers.setVisible(true);
        JScrollPane scrollpane = new JScrollPane(listUsers);
        jPanel1.add(scrollpane, new AbsoluteConstraints(80, 35, 210, 225));
        accept.addActionListener(new ButtonListener());
        add.addActionListener(new ButtonListener());
        del.addActionListener(new ButtonListener());
    }

    /**
     * Constructor para modificar un rol ya existente
     * @param parent
     * @param userManager
     * @param rol
     */
    public RolManagerModifyFrame(JFrame parent, UserManager userManager, Rol rol) {
        super(parent, true);
        setResizable(false);
        initComponents();
        this.rol = rol;
        this.userManager = userManager;
        jTextFieldRolId.setText(rol.getId());
        jTextFieldRolId.setEditable(false);
        getUsers();
        listUsers.setVisible(true);
        JScrollPane scrollpane = new JScrollPane(listUsers);
        jPanel1.add(scrollpane, new AbsoluteConstraints(80, 35, 210, 225));
        add.addActionListener(new ButtonListener());
        del.addActionListener(new ButtonListener());
        accept.addActionListener(new ButtonListener());
    }

    /**
   * obtiene los usuarios del rol
   */
    private void getUsers() {
        listModel = new DefaultListModel();
        java.util.List list;
        try {
            list = (java.util.List) userManager.getUsersByRol(rol);
            for (int i = 0; i < list.size(); i++) {
                listModel.addElement(list.get(i));
            }
            listUsers = new JList(listModel);
        } catch (Exception e) {
            new ShowExceptionMessageDialog(LanguageTraslator.traslate("441"), e);
        }
    }

    /**
   *obtiene los usuario disponibles para agregarse  a un rol
   * @return ArrayList
   */
    private ArrayList getUsersAvailable() {
        ArrayList list = new ArrayList();
        Object[] o;
        boolean flag;
        try {
            o = userManager.getUsers().toArray();
            if (listModel.size() == 0) {
                list.addAll(userManager.getUsers());
                return list;
            }
            for (int i = 0; i < userManager.getUsers().size(); i++) {
                flag = false;
                for (int j = 0; j < listModel.size(); j++) {
                    if (((User) o[i]).compareTo((User) listModel.get(j)) == 0) {
                        flag = true;
                    }
                }
                if (flag != true) {
                    list.add(o[i]);
                }
            }
        } catch (Exception e) {
            new ShowExceptionMessageDialog(LanguageTraslator.traslate("433"), e);
        }
        return list;
    }

    public void setTitulo(String titulo) {
        setTitle(titulo);
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jTextFieldRolId = new JTextField();
        JLabelRolId = new JLabel();
        jLabelUsers = new JLabel();
        add = new JButton();
        del = new JButton();
        accept = new JButton();
        cancel = new JButton();
        getContentPane().setLayout(new AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        jPanel1.setLayout(new AbsoluteLayout());
        jPanel1.add(jTextFieldRolId, new AbsoluteConstraints(80, 10, 210, -1));
        JLabelRolId.setText(LanguageTraslator.traslate("426"));
        jPanel1.add(JLabelRolId, new AbsoluteConstraints(10, 10, 80, 20));
        jLabelUsers.setText(LanguageTraslator.traslate("418"));
        jPanel1.add(jLabelUsers, new AbsoluteConstraints(10, 35, 80, 20));
        add.setText(LanguageTraslator.traslate("130"));
        jPanel1.add(add, new AbsoluteConstraints(300, 35, 80, 25));
        del.setText(LanguageTraslator.traslate("421"));
        jPanel1.add(del, new AbsoluteConstraints(300, 65, 80, 25));
        accept.setText(LanguageTraslator.traslate("112"));
        jPanel1.add(accept, new AbsoluteConstraints(80, 275, 85, 25));
        cancel.setText(LanguageTraslator.traslate("113"));
        jPanel1.add(cancel, new AbsoluteConstraints(205, 275, 85, 25));
        getContentPane().add(jPanel1, new AbsoluteConstraints(0, 0, 390, 310));
        Dimension scrn = getToolkit().getScreenSize();
        this.setLocation((scrn.width - getWidth()) / 3 + 80, (scrn.height - getHeight()) / 3 + 80);
        cancel.addActionListener(new ButtonListener());
        pack();
    }

    /**
    * agrega o elimina los usuarios de la lista al userManager
    */
    private void addUsersToRol() throws InfoException {
        java.util.List list;
        ArrayList repositoryUserAdd = new ArrayList();
        ArrayList repositoyUserRemove = new ArrayList();
        try {
            list = userManager.getUsersByRol(rol);
        } catch (Exception e) {
            throw new InfoException(LanguageTraslator.traslate("441"), e);
        }
        for (int i = 0; i < listModel.size(); i++) {
            if (!list.contains(listModel.getElementAt(i))) {
                repositoryUserAdd.add((User) listModel.getElementAt(i));
            }
        }
        for (int j = 0; j < list.size(); j++) {
            if (!listModel.contains(list.get(j))) {
                repositoyUserRemove.add((User) list.get(j));
            }
        }
        for (int i = 0; i < repositoryUserAdd.size(); i++) {
            try {
                userManager.addUsersToCollectionRol(rol, (User) repositoryUserAdd.get(i));
            } catch (Exception e) {
                new InfoException(LanguageTraslator.traslate("442") + " " + (User) repositoryUserAdd.get(i), e);
            }
        }
        for (int i = 0; i < repositoyUserRemove.size(); i++) {
            try {
                userManager.removeRolToUser(rol, (User) repositoyUserRemove.get(i));
            } catch (Exception e) {
                throw new InfoException(LanguageTraslator.traslate("436") + " " + (User) repositoyUserRemove.get(i), e);
            }
        }
    }

    /**
     * verifica los campo vacios
     * @param jLabel
     * @param jLabel
     * @return
     */
    private boolean emptyTextField(JTextField jTextField, JLabel jLabel) {
        if (jTextField.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, LanguageTraslator.traslate("422") + " " + jLabel.getText());
            return true;
        }
        return false;
    }

    class ButtonListener implements ActionListener {

        public ButtonListener() {
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancel) {
                setVisible(false);
            } else if (e.getSource() == accept && rol == null) {
                acceptNewRol();
            } else if (e.getSource() == accept && rol != null) {
                try {
                    addUsersToRol();
                } catch (InfoException el) {
                    new ShowExceptionMessageDialog(LanguageTraslator.traslate("446"), el);
                }
                setVisible(false);
            } else if (e.getSource() == add) {
                addUserToListRol();
            } else if (e.getSource() == del && listUsers.getSelectedValues().length > 0) {
                delUserToListRol();
            }
        }
    }

    /**
   * invoca un AddUsertoRolFrame, frame para agregar usuarios a la lista, del rol
   */
    private void addUserToListRol() {
        if (getUsersAvailable().size() > 0) {
            AddUsertoRolFrame dialog = new AddUsertoRolFrame(this, getUsersAvailable());
            dialog.setTitle(LanguageTraslator.traslate("427"));
            dialog.setVisible(true);
            if (dialog.getUsers() != null) {
                for (int i = 0; i < dialog.getUsers().length; i++) {
                    listModel.addElement((User) dialog.getUsers()[i]);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, LanguageTraslator.traslate("450"));
        }
    }

    /**
   * elimina usuario de la lista del rol
   */
    private void delUserToListRol() {
        while (listUsers.getSelectedValues().length > 0) {
            listModel.removeElement((User) listUsers.getSelectedValues()[0]);
        }
    }

    /**
   * metodo privado que se invoca cuando el usuario acepta agregar un nuevo rol
   */
    private void acceptNewRol() {
        if (emptyTextField(jTextFieldRolId, JLabelRolId)) {
            return;
        }
        rol = new Rol(jTextFieldRolId.getText(), jTextFieldRolId.getText());
        try {
            userManager.addRol(rol);
        } catch (Exception e) {
            new ShowExceptionMessageDialog(LanguageTraslator.traslate("444"), e);
            rol = null;
            return;
        }
        addUsersToNewRol();
        setVisible(false);
    }

    /**
   * metodo privado para agregar usuarios a la lista del nuevo rol
   */
    private void addUsersToNewRol() {
        int i = 0;
        try {
            for (i = 0; i < listModel.size(); i++) {
                userManager.addUsersToCollectionRol(rol, (User) listModel.getElementAt(i));
            }
        } catch (Exception e) {
            new ShowExceptionMessageDialog(LanguageTraslator.traslate("442") + " " + (User) listModel.getElementAt(i), e);
        }
    }
}
