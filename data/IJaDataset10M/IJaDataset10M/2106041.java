package gg.de.sbmp3.console.dialogs;

import charva.awt.Container;
import charva.awt.Frame;
import charva.awt.GridBagConstraints;
import charva.awt.GridBagLayout;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charvax.swing.*;
import gg.de.sbmp3.backend.BEFactory;
import gg.de.sbmp3.backend.UserBE;
import gg.de.sbmp3.backend.data.UserBean;
import gg.de.sbmp3.backend.data.UserRoleBean;
import gg.de.sbmp3.common.Util;
import java.util.Calendar;

/**
 * Created: 23.05.2004  18:54:54
 */
public class UserDlg extends JDialog implements ActionListener {

    private JTextField emailTF;

    private JTextField passTF;

    private JTextField usernameTF;

    private JCheckBox adminCK;

    private int currentUid = 0;

    public UserDlg(Frame owner, int uid) {
        super(owner, "Users Details");
        currentUid = uid;
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        UserBean user;
        boolean isAdmin = false;
        if (uid > 0) {
            UserBE userBe = BEFactory.getUserBE(new UserBean(""));
            user = userBe.getUser(uid);
            if (user.getRoles() != null) for (int i = 0; i < user.getRoles().length; i++) if (user.getRoles()[i].getName().equals("ADMIN")) isAdmin = true;
        } else {
            user = new UserBean();
            user.setName("");
            user.setEmail("");
            user.setId(0);
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.ipadx = 2;
        gbc.ipady = 2;
        gbc.anchor = GridBagConstraints.WEST;
        if (uid > 0) {
            gbc.gridx = 0;
            contentPane.add(new JLabel("UserID: "), gbc);
            gbc.gridx++;
            JLabel idL = new JLabel(user.getId() + "");
            contentPane.add(idL, gbc);
        }
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(new JLabel("Username: "), gbc);
        gbc.gridx++;
        usernameTF = new JTextField(user.getName(), 30);
        contentPane.add(usernameTF, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(new JLabel("Email: "), gbc);
        gbc.gridx++;
        emailTF = new JTextField(user.getEmail(), 30);
        contentPane.add(emailTF, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        if (uid > 0) contentPane.add(new JLabel("Password:* "), gbc); else contentPane.add(new JLabel("Password: "), gbc);
        gbc.gridx++;
        passTF = new JTextField("", 30);
        contentPane.add(passTF, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(new JLabel("Admin: "), gbc);
        gbc.gridx++;
        adminCK = new JCheckBox("has admin rights", isAdmin);
        contentPane.add(adminCK, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(new JLabel(""), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy++;
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        contentPane.add(closeButton, gbc);
        if (uid > 0) {
            gbc.gridx++;
            JButton editButton = new JButton("Edit");
            editButton.addActionListener(this);
            contentPane.add(editButton, gbc);
            gbc.gridx++;
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(this);
            contentPane.add(deleteButton, gbc);
        } else {
            gbc.gridx++;
            JButton addButton = new JButton("Add");
            addButton.addActionListener(this);
            contentPane.add(addButton, gbc);
        }
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(new JLabel(""), gbc);
        if (uid > 0) {
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 3;
            JLabel noteL = new JLabel("* leave blank to keep the current password");
            contentPane.add(noteL, gbc);
        }
        pack();
        setLocation(1, 1);
        setSize(78, 22);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Close")) {
            hide();
        } else if (ae.getActionCommand().equals("Add")) {
            UserBE userBe = BEFactory.getUserBE(new UserBean(""));
            UserBean user = new UserBean();
            user.setName(usernameTF.getText());
            user.setEmail(emailTF.getText());
            user.setPassword(Util.digestMD5(passTF.getText()));
            user.setSignupDate(Calendar.getInstance().getTime());
            user.setActivationDate(Calendar.getInstance().getTime());
            if (adminCK.isSelected()) {
                UserRoleBean[] roles = new UserRoleBean[2];
                roles[0] = new UserRoleBean("ADMIN");
                roles[1] = new UserRoleBean("USER");
                user.setRoles(roles);
            }
            userBe.add(user);
            hide();
        } else if (ae.getActionCommand().equals("Edit")) {
            UserBE userBe = BEFactory.getUserBE(new UserBean(""));
            UserBean user = userBe.getUser(currentUid);
            user.setName(usernameTF.getText());
            user.setEmail(emailTF.getText());
            if (!passTF.getText().equals("")) user.setPassword(Util.digestMD5(passTF.getText()));
            if (adminCK.isSelected()) {
                UserRoleBean[] roles = new UserRoleBean[2];
                roles[0] = new UserRoleBean("ADMIN");
                roles[1] = new UserRoleBean("USER");
                user.setRoles(roles);
            } else {
                user.setRoles(new UserRoleBean[0]);
            }
            userBe.edit(user);
            hide();
        } else if (ae.getActionCommand().equals("Delete")) {
            UserBE userBe = BEFactory.getUserBE(new UserBean(""));
            userBe.remove(userBe.getUser(currentUid));
            hide();
        }
    }
}
