package ui.panel;

import domain.Login;
import domain.manager.Adapter;
import domain.manager.Config;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * User: tzdram
 * Date: 06.11.2009
 * Time: 09:26:47
 */
public class LoginPnl {

    private JTextField number;

    private JPasswordField password;

    private JButton loginButton;

    private JCheckBox proxyCheckBox;

    private JCheckBox automatischEinloggenCheckBox;

    private JPanel proxyPanel;

    private JPanel panel;

    private JLabel statusLbl;

    private Login login;

    private Adapter adapter;

    private ProxyPnl proxyPnl;

    public LoginPnl(Adapter adapter, Login login) {
        this.login = login;
        this.adapter = adapter;
        setListener();
        setObserver();
    }

    private void setObserver() {
        login.addObserver(new Observer() {

            public void update(Observable observable, Object o) {
                Integer returnCode = (Integer) o;
                switch(returnCode) {
                    case -2:
                        statusLbl.setText("Unbekannter Fehler");
                        break;
                    case -1:
                        statusLbl.setText("Verbindung fehlgeschlagen");
                        break;
                    case 0:
                        statusLbl.setText("Benutzername oder Passwort falsch");
                        break;
                    case 1:
                        statusLbl.setText("Erfolgreich eingelogt");
                        adapter.loadContacts();
                        break;
                }
            }
        });
    }

    private void setListener() {
        proxyCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                proxyPanel.setVisible(proxyCheckBox.isSelected());
            }
        });
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                login();
            }
        });
    }

    private void login() {
        statusLbl.setText("Proxy wird gesetzt...");
        proxyPnl.setProxy();
        statusLbl.setText("Bitte warten...");
        login.login(number.getText(), String.valueOf(password.getPassword()));
    }

    private void createUIComponents() {
        proxyPnl = new ProxyPnl();
        proxyPanel = proxyPnl.getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void saveToConfig(Config config) {
        config.setLoginNumber(number.getText());
        config.setLoginPassword(password.getPassword());
        config.setUseProxy(proxyCheckBox.isSelected());
        config.setAutoLogin(automatischEinloggenCheckBox.isSelected());
        proxyPnl.saveToConfig(config);
    }

    public void loadFromConfig(Config config) {
        number.setText(config.getLoginNumber());
        password.setText(String.valueOf(config.getLoginPassword()));
        proxyCheckBox.setSelected(config.isUseProxy());
        automatischEinloggenCheckBox.setSelected(config.isAutoLogin());
        proxyPanel.setVisible(proxyCheckBox.isSelected());
        proxyPnl.loadFromConfig(config);
        if (config.isAutoLogin()) {
            login();
        }
    }
}
