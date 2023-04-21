package org.fao.gast.gui.dialogs;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.TPanel;
import org.fao.gast.localization.Messages;

public class AccountPanel extends TPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4995520020414212659L;

    public AccountPanel() {
        super(Messages.getString("accountPanelTitle"));
        FlexLayout fl = new FlexLayout(3, 4);
        fl.setColProp(2, FlexLayout.EXPAND);
        setLayout(fl);
        add("0,0,x,c,3", jrbNoAuth);
        add("0,1,x,c,3", jrbAuth);
        add("1,2", new JLabel(Messages.getString("username")));
        add("2,2,x", txtUser);
        add("1,3", new JLabel(Messages.getString("password")));
        add("2,3,x", txtPass);
        btgAccount.add(jrbNoAuth);
        btgAccount.add(jrbAuth);
        btgAccount.setSelected(jrbNoAuth.getModel(), true);
    }

    public boolean useAccount() {
        return jrbAuth.isSelected();
    }

    public String getUsername() {
        return txtUser.getText();
    }

    public String getPassword() {
        return txtPass.getText();
    }

    private ButtonGroup btgAccount = new ButtonGroup();

    private JRadioButton jrbNoAuth = new JRadioButton(Messages.getString("noAuth"));

    private JRadioButton jrbAuth = new JRadioButton(Messages.getString("thisAcct"));

    private JTextField txtUser = new JTextField(20);

    private JPasswordField txtPass = new JPasswordField(20);
}
