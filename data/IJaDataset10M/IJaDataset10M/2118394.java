package net.sourceforge.entrainer.gui.thinkgear;

import static net.sourceforge.entrainer.util.Utils.openBrowser;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import net.sourceforge.entrainer.gui.Entrainer;
import net.sourceforge.entrainer.guitools.MigHelper;
import net.sourceforge.entrainer.widgets.IntegerTextField;
import net.sourceforge.entrainer.xml.Settings;

@SuppressWarnings("serial")
public class NeuroskySocketPortDialog extends JDialog {

    private JButton ok = new JButton("Ok");

    private JButton cancel = new JButton("Cancel");

    private IntegerTextField port = new IntegerTextField(6);

    public NeuroskySocketPortDialog() {
        super(Entrainer.getInstance(), "Broadcast Port");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        init();
    }

    private void init() {
        int num = Settings.getInstance().getThinkgearBroadcastPort();
        port.setNumber(num <= 0 ? 12345 : num);
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

    private String getLocalDocAddress() {
        File file = new File(".");
        String path = file.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("."));
        return "file://" + path + "doc/neurosky.html";
    }

    private void setToolTips() {
        port.setToolTipText("Choose a free socket port for broadcasting NeuroSky events (typically > 1000)");
    }

    private void initGui() {
        MigHelper mh = new MigHelper(getContentPane());
        mh.addLast(getPortPanel()).add(getButtonPanel());
    }

    private Component getPortPanel() {
        MigHelper mh = new MigHelper();
        mh.add("Port Number").alignWest().addLast(port);
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
        Settings.getInstance().setThinkgearBroadcastPort((int) port.getNumber());
        JOptionPane.showMessageDialog(this, "NeuroSky broadcast port setting changed.\n\nEntrainer must be restarted for all changes to EntrainerThinkGear library settings", "Entrainer Restart Required", JOptionPane.INFORMATION_MESSAGE);
        close();
    }

    private void showErrorDialog() {
        JOptionPane.showMessageDialog(this, port.getNumber() + " is not a valid port number", "Invalid Port Number", JOptionPane.ERROR_MESSAGE);
    }
}
