package phsperformance.gui;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import phsperformance.PhsPerformanceMain;
import phsperformance.data.InstanceInfo;
import phsperformance.event.SetInstanceEvent;
import phsperformance.util.IconCollection;

public class PhsPerformanceStatus extends JPanel implements Observer {

    private JLabel m_server;

    private JLabel m_worker;

    private JProgressBar m_progressBar;

    private int m_progressBarValue;

    public PhsPerformanceStatus() {
        super();
        setBorder(new EmptyBorder(4, 3, 3, 2));
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);
        m_server = new JLabel(" ", IconCollection.SERVER_IC0N, JLabel.CENTER);
        add(m_server);
        add(Box.createHorizontalGlue());
        m_progressBar = new JProgressBar(0, 100);
        m_progressBar.setVisible(false);
        add(m_progressBar);
        add(Box.createHorizontalGlue());
        m_worker = new JLabel("", IconCollection.SESSION_ICON, JLabel.CENTER);
        add(m_worker);
    }

    public void update(Observable arg0, Object event) {
        if (event instanceof SetInstanceEvent) {
            SetInstanceEvent instanceEvent = (SetInstanceEvent) event;
            InstanceInfo ii = instanceEvent.getInstanceInfo();
            m_server.setText(ii.getServerHost() + ":" + ii.getServerPort() + "/" + ii.getInstanceWorker());
            m_worker.setText(ii.getUserName());
        }
    }
}
