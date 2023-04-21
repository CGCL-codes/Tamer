package net.sourceforge.exclusive.client.gui.statusbar;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.sourceforge.exclusive.client.ADTListener;
import net.sourceforge.exclusive.client.ActiveDownloadInfo;
import net.sourceforge.exclusive.client.config.ClientConf;
import net.sourceforge.exclusive.util.SpeedCalculator;

public class StatusBar extends JPanel {

    private static final long serialVersionUID = -6761956112914814903L;

    private JLabel statusLabel;

    private UploadDownloadStatus upDownSpeedStatus;

    private ConnectionStatus connectionStatus;

    private SpeedCalculator calculator;

    private long lastLabelUpdate;

    private Thread refreshThread;

    public StatusBar() {
        super();
        calculator = new SpeedCalculator();
        lastLabelUpdate = System.currentTimeMillis();
        connectionStatus = new ConnectionStatus();
        statusLabel = new JLabel(" ");
        upDownSpeedStatus = new UploadDownloadStatus();
        setLayout(new BorderLayout());
        add(statusLabel, BorderLayout.CENTER);
        Box b = Box.createHorizontalBox();
        b.add(new JSeparator(JSeparator.VERTICAL));
        b.add(connectionStatus);
        b.add(new JSeparator(JSeparator.VERTICAL));
        b.add(upDownSpeedStatus);
        add(b, BorderLayout.EAST);
        refreshThread = new Thread() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                    ;
                    upDownSpeedStatus.setDownloadSpeed(calculator.getDownloadSpeed());
                    upDownSpeedStatus.setUploadSpeed(calculator.getUploadSpeed());
                    if (System.currentTimeMillis() - lastLabelUpdate > 10000) statusLabel.setText(" ");
                }
            }
        };
        refreshThread.start();
    }

    public void setNumberOfFriendsNUsers(int friends, int users) {
        connectionStatus.setNumberOfFriendsNUsers(friends, users);
    }

    public void setConnectStatus(int connectStatus, String reason) {
        connectionStatus.setConnected(connectStatus, reason);
    }

    public void setText(String txt) {
        statusLabel.setText(txt);
        lastLabelUpdate = System.currentTimeMillis();
    }

    public void pieceUploaded() {
        calculator.addUpload();
    }

    public void byteUploaded() {
        calculator.addUpload();
    }

    public ADTListener getADTListener() {
        return new ADTListener() {

            @Override
            public void errorHappened(String reason) {
            }

            @Override
            public void pieceTransfered() {
                for (int i = 0; i < ClientConf.PACKETSIZE; i++) calculator.addDownload();
            }

            @Override
            public void byteTransfered() {
                calculator.addDownload();
            }

            @Override
            public void statusChanged(ActiveDownloadInfo.Status state) {
            }
        };
    }
}
