package pl.edu.agh.ssm.monitor.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import pl.edu.agh.ssm.monitor.SessionMonitorApp;
import pl.edu.agh.ssm.monitor.data.RTSPPacket;
import pl.edu.agh.ssm.monitor.data.SessionConnection;
import pl.edu.agh.ssm.monitor.data.SessionMediaType;
import pl.edu.agh.ssm.monitor.utils.IconUtil;

/**
 *
 * @author  aneezka
 */
@SuppressWarnings("serial")
public class GraphConnectionInfoPanel extends javax.swing.JPanel implements UpdatePanel, IconPanel {

    /** Creates new form GraphConnectionInfoPanel */
    public GraphConnectionInfoPanel(SessionConnection connection) {
        this.connection = connection;
        initComponents();
        JPanel panel = new ConnectionGeneralInfoPanel(connection);
        final JScrollPane pane = new JScrollPane(panel);
        pane.setBorder(new TitledBorder(panel.getName()));
        jPanel2.add(pane);
        panels.add((UpdatePanel) panel);
        if (connection.getMediaDesc().getMediaType().equals(SessionMediaType.RTSP_CONTROL)) {
            rtspPanel = new RTSPTablePanel(connection);
            panel = rtspPanel;
            jButton2.setEnabled(true);
            jButton3.setEnabled(true);
            final JScrollPane pane2 = new JScrollPane(panel);
            pane2.setBorder(new TitledBorder(panel.getName()));
            jPanel2.add(pane2);
            panels.add((UpdatePanel) panel);
            rtspPanel.addListener(new UpdatePanelListener() {

                public void panelUpdated(UpdatePanel panel) {
                }

                public void panelRemoved(UpdatePanel panel) {
                    jPanel2.remove(pane2);
                    jPanel2.revalidate();
                    panels.remove(panel);
                }
            });
        } else if (connection.getReceiverReport() != null) {
            addRRPanel(new ReceiverReportPanel(connection));
        }
    }

    public void update() {
        jLabel1.setText("Connection " + connection.getConnectionAddress().getHostAddress() + ":" + connection.getConnectionPort());
        if (connection.getReceiverReport() != null && rrPanel == null) {
            addRRPanel(new ReceiverReportPanel(connection));
        }
        for (UpdatePanel p : panels) {
            p.update();
        }
        for (UpdatePanelListener l : listeners) {
            l.panelUpdated(this);
        }
    }

    public void remove() {
        for (UpdatePanelListener l : listeners) {
            l.panelRemoved(this);
        }
    }

    public void addListener(UpdatePanelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(UpdatePanelListener listener) {
        listeners.remove(listener);
    }

    public Icon getIcon() {
        return (new IconUtil()).getConnectionIcon(connection);
    }

    private void addRRPanel(ReceiverReportPanel panel) {
        rrPanel = panel;
        final JScrollPane pane2 = new JScrollPane(panel);
        pane2.setBorder(new TitledBorder(panel.getName()));
        jPanel2.add(pane2);
        panels.add((UpdatePanel) panel);
        ((UpdatePanel) panel).addListener(new UpdatePanelListener() {

            public void panelUpdated(UpdatePanel panel) {
            }

            public void panelRemoved(UpdatePanel panel) {
                jPanel2.remove(pane2);
                jPanel2.revalidate();
                panels.remove(panel);
                rrPanel = null;
            }
        });
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        setName("Form");
        jPanel1.setName("jPanel1");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pl.edu.agh.ssm.monitor.SessionMonitorApp.class).getContext().getResourceMap(GraphConnectionInfoPanel.class);
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText("Connection " + connection.getConnectionAddress().getHostAddress() + ":" + connection.getConnectionPort());
        jLabel1.setName("jLabel1");
        jButton1.setIcon(resourceMap.getIcon("jButton1.icon"));
        jButton1.setText(resourceMap.getString("jButton1.text"));
        jButton1.setToolTipText(resourceMap.getString("jButton1.toolTipText"));
        jButton1.setName("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setIcon(resourceMap.getIcon("jButton2.icon"));
        jButton2.setText(resourceMap.getString("jButton2.text"));
        jButton2.setToolTipText(resourceMap.getString("jButton2.toolTipText"));
        jButton2.setEnabled(false);
        jButton2.setName("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton3.setIcon(resourceMap.getIcon("jButton3.icon"));
        jButton3.setText(resourceMap.getString("jButton3.text"));
        jButton3.setToolTipText(resourceMap.getString("jButton3.toolTipText"));
        jButton3.setEnabled(false);
        jButton3.setName("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE).addComponent(jButton3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1).addComponent(jButton2).addComponent(jButton1).addComponent(jButton3)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2.setName("jPanel2");
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)));
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        SessionMonitorApp.getApplication().getView().updateBottomTab(this);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                RTSPTablePanel panel = new RTSPTablePanel(rtspPanel.getConnection());
                frame.getContentPane().add(panel);
                frame.setLocationRelativeTo(SessionMonitorApp.getApplication().getView().getFrame());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setTitle("RTSP Packets");
                frame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowActivated(WindowEvent e) {
                        super.windowActivated(e);
                        rtspPanel.update();
                    }
                });
                frame.setSize(600, 400);
                frame.setVisible(true);
            }
        });
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (rtspPanel.getSelectedPacket() != null) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    JFrame frame = new JFrame();
                    JTextArea textArea = new JTextArea();
                    RTSPPacket packet = rtspPanel.getSelectedPacket();
                    textArea.setText("RTSP Packet:\n" + packet.getFullPacket() + "\n" + "RTSP Answer:\n" + (packet.getAnswer() == null ? "" : packet.getAnswer().getFullPacket()));
                    frame.getContentPane().add(new JScrollPane(textArea));
                    frame.setLocationRelativeTo(SessionMonitorApp.getApplication().getView().getFrame());
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setTitle("RTSP Packet");
                    frame.setSize(400, 400);
                    frame.setVisible(true);
                }
            });
        }
    }

    private SessionConnection connection;

    private List<UpdatePanel> panels = new LinkedList<UpdatePanel>();

    private List<UpdatePanelListener> listeners = new LinkedList<UpdatePanelListener>();

    private RTSPTablePanel rtspPanel;

    private ReceiverReportPanel rrPanel = null;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;
}
