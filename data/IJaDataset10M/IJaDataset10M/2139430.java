package org.fao.gast.gui.panels.config.dbms;

import javax.swing.JLabel;
import javax.swing.JTextField;
import org.dlib.gui.FlexLayout;
import org.fao.gast.lib.Lib;
import org.fao.gast.localization.Messages;

public class MckoiPanel extends DbmsPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7290449012011589464L;

    public MckoiPanel() {
        FlexLayout fl = new FlexLayout(3, 1);
        fl.setColProp(1, FlexLayout.EXPAND);
        setLayout(fl);
        add("0,0", new JLabel(Messages.getString("port")));
        add("1,0", txtPort);
        add("2,0", new JLabel("<html><font color='red'>(REQ)</font>"));
        txtPort.setText("9157");
        txtPort.setToolTipText(Messages.getString("mckoi.defaultPort"));
    }

    public String getLabel() {
        return Messages.getString("mckoi");
    }

    public boolean isJNDI() {
        return false;
    }

    public boolean matches(String url, boolean isJNDI) {
        if (!isJNDI) {
            return url.startsWith(PREFIX);
        } else {
            return false;
        }
    }

    public void retrieve() {
        txtPort.setText(Lib.embeddedDB.getPort());
    }

    public void save(boolean createNew) throws Exception {
        String port = txtPort.getText();
        String user = Lib.embeddedDB.getUser();
        String pass = Lib.embeddedDB.getPassword();
        if (!Lib.type.isInteger(port)) throw new Exception(Messages.getString("portInt"));
        Lib.config.setupDbmsConfig(createNew, false);
        Lib.config.setDbmsDriver("com.mckoi.JDBCDriver");
        Lib.config.setDbmsURL(PREFIX + "//localhost:" + port + "/");
        Lib.config.setDbmsUser(user);
        Lib.config.setDbmsPassword(pass);
        Lib.config.setDbmsPoolSize("10");
        Lib.config.setDbmsValidQuery("SELECT 1");
        Lib.config.addActivator();
        Lib.config.save();
        Lib.embeddedDB.setPort(port);
        Lib.embeddedDB.save();
    }

    private JTextField txtPort = new JTextField(6);

    private static final String PREFIX = "jdbc:mckoi:";
}
