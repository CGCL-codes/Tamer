package me.buick.util.jmeter.snmpvisualizers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.MonitorListener;
import org.apache.jmeter.visualizers.MonitorModel;

public class SNMPMonitorCPUPanel extends JPanel implements MonitorListener, Clearable {

    private JPanel GRAPHPANEL;

    SNMPCPUOverviewPanel cpuPanel;

    private SNMPMonitorSNMPAccumModel MODEL;

    private SampleResult ROOTSAMPLE;

    public static final String PERF_TITLE = JMeterUtils.getResString("monitor_performance_title");

    public static final String SERVER_TITLE = JMeterUtils.getResString("monitor_performance_servers");

    protected Font plaintext = new Font("plain", Font.TRUETYPE_FONT, 10);

    /**
	 * 
	 */
    public SNMPMonitorCPUPanel(SNMPMonitorSNMPAccumModel model) {
        super();
        this.MODEL = model;
        this.MODEL.addListener(this);
        init();
    }

    /**
	 * init() will create all the necessary swing panels, labels and icons for
	 * the performance panel.
	 */
    void init() {
        this.setLayout(new BorderLayout());
        JLabel title = new JLabel(" " + PERF_TITLE);
        title.setPreferredSize(new Dimension(200, 40));
        cpuPanel = new SNMPCPUOverviewPanel();
        this.add(cpuPanel, BorderLayout.CENTER);
    }

    public synchronized void addSample(MonitorModel model) {
        cpuPanel.updateCPUInfo(model);
    }

    /**
	 * clear will remove all child nodes from the ROOTNODE, clear the HashMap,
	 * update the graph and jpanel for the server tree.
	 */
    public void clearData() {
        cpuPanel.clearData();
    }
}
