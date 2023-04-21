package net.sourceforge.entrainer.gui;

import static net.sourceforge.entrainer.util.Utils.openBrowser;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import net.sourceforge.entrainer.guitools.MigHelper;
import net.sourceforge.entrainer.widgets.IntegerTextField;
import net.sourceforge.entrainer.xml.Settings;

@SuppressWarnings("serial")
public class SocketPortDialog extends JDialog {

    private JButton ok = new JButton("Ok");

    private JButton cancel = new JButton("Cancel");

    private JCheckBox deltaSocketMessage = new JCheckBox("Delta Messages");

    private IntegerTextField port = new IntegerTextField(6);

    private JTextField ipAddress = new JTextField(10);

    public SocketPortDialog() throws UnknownHostException {
        super(Entrainer.getInstance(), "Choose Socket Port");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        init();
    }

    private void init() throws UnknownHostException {
        port.setNumber(Settings.getInstance().getSocketPort());
        String address = Settings.getInstance().getSocketIPAddress();
        if (address == null || address.trim().length() == 0) address = initIPAddress();
        ipAddress.setText(address);
        deltaSocketMessage.setSelected(Settings.getInstance().isDeltaSocketMessage());
        initListener();
        initGui();
        setToolTips();
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown() && e.getClickCount() == 1) {
                    openBrowser(getLocalDocAddress());
                }
            }
        });
    }

    private String initIPAddress() throws UnknownHostException {
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        Settings.getInstance().setSocketIPAddress(ipAddress);
        return ipAddress;
    }

    private String getLocalDocAddress() {
        File file = new File(".");
        String path = file.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("."));
        return "file://" + path + "doc/sockets.html";
    }

    private void setToolTips() {
        port.setToolTipText("Choose a free socket port for Entrainer (typically > 1000)");
        ipAddress.setToolTipText("Set the hostname or ip address if known, leave blank otherwise");
        deltaSocketMessage.setToolTipText("Send Entrainer's entire state (unchecked) or just the delta change (checked) per message");
    }

    private void initGui() {
        MigHelper mh = new MigHelper(getContentPane());
        mh.addLast(getPortPanel()).addLast(deltaSocketMessage).add(getButtonPanel());
    }

    private Component getPortPanel() {
        MigHelper mh = new MigHelper();
        mh.add("Port Number").alignWest().addLast(port);
        mh.add("Host Address").alignWest().addLast(ipAddress);
        return mh.getContainer();
    }

    private Component getButtonPanel() {
        MigHelper mh = new MigHelper();
        mh.add(ok).add(cancel);
        return mh.getContainer();
    }

    private void initListener() {
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                validateAndSavePort();
            }
        });
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                close();
            }
        });
        deltaSocketMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Settings.getInstance().setDeltaSocketMessage(deltaSocketMessage.isSelected());
            }
        });
    }

    private void close() {
        setVisible(false);
        dispose();
    }

    protected void validateAndSavePort() {
        if (port.getNumber() <= 0) {
            showErrorDialog();
            return;
        }
        Settings.getInstance().setSocketPort((int) port.getNumber());
        Settings.getInstance().setSocketIPAddress(ipAddress.getText());
        close();
    }

    private void showErrorDialog() {
        JOptionPane.showMessageDialog(this, port.getNumber() + " is not a valid port number", "Invalid Port Number", JOptionPane.ERROR_MESSAGE);
    }
}
