package org.gaea.ui.graphic.options.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import org.gaea.common.config.ConfigLogin;
import org.gaea.common.config.ConfigManager;
import org.gaea.common.config.sub.LoginData;
import org.gaea.ui.language.Messages;
import org.gaea.ui.utilities.PanelGrid;

/**
 * Login options
 * 
 * @author jsgoupil
 */
public class OptionPanelLogin extends OptionPanel {

    /**
	 * Auto Generated
	 */
    private static final long serialVersionUID = -3286761333245353071L;

    private JComboBox _cmbAutoLogin;

    private JLabel _lblProtocolV;

    private JLabel _lblURLV;

    private JLabel _lblUserV;

    private JLabel _lblPassV;

    private JLabel _lblDatabaseV;

    private JButton _btnAutoLoginDelete;

    /**
	 * Constructor creating its GUI
	 */
    public OptionPanelLogin() {
        super(Messages.getString("Common.Login"), "/org/gaea/ui/graphic/images/option-icon-login.png");
        createAndShowGUI();
    }

    /**
	 * Creates the GUI and shows it.
	 */
    private void createAndShowGUI() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        PanelGrid panGrid = new PanelGrid(constraints);
        panGrid.setOpaque(false);
        JLabel lblAutoLogin = new JLabel(Messages.getString("OptionPanelLogin.AutoLogin"));
        _cmbAutoLogin = new JComboBox();
        _cmbAutoLogin.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    LoginData loginData = new LoginData();
                    loginData.createFromLine((String) e.getItem());
                    _lblProtocolV.setText(loginData.Protocol);
                    _lblURLV.setText(loginData.URL);
                    _lblUserV.setText(loginData.Username);
                    _lblPassV.setText(loginData.Password);
                    _lblDatabaseV.setText(loginData.Database);
                }
            }
        });
        _btnAutoLoginDelete = new JButton(Messages.getString("Common.Delete"));
        _btnAutoLoginDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LoginData loginData = new LoginData();
                loginData.createFromLine((String) _cmbAutoLogin.getSelectedItem());
                ((DefaultComboBoxModel) _cmbAutoLogin.getModel()).removeElementAt(_cmbAutoLogin.getSelectedIndex());
                fireFieldChanged(_cmbAutoLogin, true);
                checkDeletable();
            }
        });
        lblAutoLogin.setLabelFor(_cmbAutoLogin);
        JLabel lblProtocol = new JLabel(Messages.getString("Common.Protocol"));
        lblProtocol.setPreferredSize(new Dimension(75, 10));
        JLabel lblURL = new JLabel(Messages.getString("Common.URL"));
        lblURL.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel lblUser = new JLabel(Messages.getString("Common.User"));
        JLabel lblPass = new JLabel(Messages.getString("Common.Pass"));
        JLabel lblDatabase = new JLabel(Messages.getString("Common.Database"));
        _lblProtocolV = new JLabel();
        _lblURLV = new JLabel();
        _lblUserV = new JLabel();
        _lblPassV = new JLabel();
        _lblDatabaseV = new JLabel();
        PanelGrid groupInformation = new PanelGrid(constraints);
        groupInformation.setOpaque(false);
        groupInformation.setBorder(new TitledBorder(Messages.getString("OptionPanelLogin.ConnectionInformation")));
        constraints.insets = new Insets(3, 5, 3, 10);
        constraints.weightx = 0.1;
        groupInformation.addGB(lblProtocol, 0, 0);
        groupInformation.addGB(lblURL, 0, 1);
        groupInformation.addGB(lblUser, 0, 2);
        groupInformation.addGB(lblPass, 0, 3);
        groupInformation.addGB(lblDatabase, 0, 4);
        constraints.weightx = 0.9;
        groupInformation.addGB(_lblProtocolV, 1, 0);
        groupInformation.addGB(_lblURLV, 1, 1);
        groupInformation.addGB(_lblUserV, 1, 2);
        groupInformation.addGB(_lblPassV, 1, 3);
        groupInformation.addGB(_lblDatabaseV, 1, 4);
        constraints.weightx = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        panGrid.addGB(lblAutoLogin, 0, 0);
        Box boxAutoLogin = Box.createHorizontalBox();
        boxAutoLogin.add(_cmbAutoLogin);
        boxAutoLogin.add(Box.createHorizontalStrut(5));
        boxAutoLogin.add(_btnAutoLoginDelete);
        panGrid.addGB(boxAutoLogin, 0, 1);
        constraints.insets = new Insets(10, 0, 0, 0);
        panGrid.addGB(groupInformation, 0, 2);
        constraints.weighty = 1.0;
        panGrid.addGB(Box.createVerticalStrut(1), 0, 3);
        panGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addPanel(panGrid);
    }

    @Override
    public void loadOptionValue() {
        _cmbAutoLogin.removeAllItems();
        ConfigLogin configLogin = (ConfigLogin) ConfigManager.getInstance().getConfig(ConfigLogin.class);
        if (configLogin != null) {
            Vector<LoginData> loginDatas = (Vector<LoginData>) configLogin.getLogins().clone();
            Collections.reverse(loginDatas);
            for (LoginData loginData : loginDatas) {
                ((DefaultComboBoxModel) _cmbAutoLogin.getModel()).addElement(loginData.createLineWithoutPassword());
            }
        }
        checkDeletable();
    }

    /**
	 * Enables delete button if the combobox is not empty.
	 */
    private void checkDeletable() {
        if (_cmbAutoLogin.getItemCount() > 0) {
            _btnAutoLoginDelete.setEnabled(true);
        } else {
            _lblProtocolV.setText("");
            _lblURLV.setText("");
            _lblUserV.setText("");
            _lblPassV.setText("");
            _lblDatabaseV.setText("");
            _btnAutoLoginDelete.setEnabled(false);
        }
    }

    @Override
    public boolean apply() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) _cmbAutoLogin.getModel();
        int c = model.getSize();
        LoginData[] loginDatas = new LoginData[c];
        ConfigLogin configLogin = (ConfigLogin) ConfigManager.getInstance().getConfig(ConfigLogin.class);
        for (int i = 0; i < c; i++) {
            loginDatas[i] = new LoginData();
            loginDatas[i].createFromLine((String) model.getElementAt(i));
            loginDatas[i].Password = configLogin.findPassword(loginDatas[i]);
            configLogin.addLogin(loginDatas[i]);
        }
        configLogin.setLogins(new Vector<LoginData>(Arrays.asList(loginDatas)));
        return true;
    }
}
