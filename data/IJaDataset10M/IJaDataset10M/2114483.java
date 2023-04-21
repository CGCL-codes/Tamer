package net.assimilator.cybernode.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.rmi.RemoteException;
import net.assimilator.cybernode.*;
import net.assimilator.entry.ComputeResourceUtilization;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.entry.Entry;
import net.assimilator.core.provision.ServiceRecord;

/**
 * A ServiceUI for the Cybernode
 */
public class CybernodeUI extends JPanel implements Runnable {

    private ServiceTable serviceTable;

    private ServiceItem item;

    private JTextField maxSvcCount, currentSvcCount, utilizationTF;

    private Cybernode cybernode;

    private CybernodeAdmin cybernodeAdmin;

    private JButton apply;

    private JButton replicate;

    private Integer svcCount, maxCount;

    private Hashtable idTable;

    private java.util.List queue = Collections.synchronizedList(new LinkedList());

    private boolean keepAlive = true;

    private transient Thread thread;

    public CybernodeUI(final Object arg) {
        super();
        getAccessibleContext().setAccessibleName("Cybernode admin");
        this.item = (ServiceItem) arg;
        cybernode = (Cybernode) item.service;
        try {
            cybernodeAdmin = (CybernodeAdmin) cybernode.getAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        idTable = new Hashtable();
        JPanel qPanel = new JPanel();
        qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.X_AXIS));
        qPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Compute Resource"), BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        qPanel.add(new JLabel("Description"));
        qPanel.add(Box.createHorizontalStrut(8));
        ComputeResourceUtilization computeResource = getComputeResourceUtilization(item.attributeSets);
        String field = "unknown: system error";
        if (computeResource != null) field = computeResource.description;
        JTextField descTF = new JTextField(field);
        descTF.setEnabled(false);
        qPanel.add(descTF);
        qPanel.add(Box.createHorizontalStrut(8));
        qPanel.add(new JLabel("Utilization"));
        qPanel.add(Box.createHorizontalStrut(8));
        if (computeResource != null) field = computeResource.utilization.toString();
        utilizationTF = new JTextField(field);
        utilizationTF.setEnabled(false);
        qPanel.add(utilizationTF);
        JPanel cPanel = new JPanel();
        cPanel.setLayout(new GridLayout(0, 3, 4, 4));
        cPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Cybernode Attributes"), BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        maxSvcCount = new JTextField();
        currentSvcCount = new JTextField();
        currentSvcCount.setEnabled(false);
        apply = new JButton("Apply");
        apply.addActionListener(new ApplyHandler());
        maxCount = new Integer(0);
        svcCount = new Integer(0);
        try {
            maxCount = cybernodeAdmin.getServiceLimit();
            maxSvcCount.setText(maxCount.toString());
            svcCount = cybernodeAdmin.getServiceCount();
            currentSvcCount.setText(svcCount.toString());
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        cPanel.add(new JLabel("Contained Services"));
        cPanel.add(currentSvcCount);
        cPanel.add(new JLabel(""));
        cPanel.add(new JLabel("Service Limit"));
        cPanel.add(maxSvcCount);
        cPanel.add(apply);
        JPanel cq = new JPanel();
        cq.setLayout(new BorderLayout());
        cq.add(BorderLayout.NORTH, qPanel);
        cq.add(BorderLayout.CENTER, cPanel);
        serviceTable = new ServiceTable();
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add("North", cq);
        add("Center", serviceTable);
        thread = new Thread(this);
        thread.start();
        synchronized (queue) {
            queue.add(new Object());
            queue.notifyAll();
        }
    }

    /**
     * Override to ensure the thread that is created is interrupted
     */
    public void removeNotify() {
        terminate();
    }

    public void run() {
        if (cybernode == null) {
            showError("Reference to Cybernode is null");
            return;
        }
        while (!thread.isInterrupted() && keepAlive) {
            try {
                serviceTable.clear();
                ServiceRecord[] records = cybernode.getServiceRecords(ServiceRecord.ACTIVE_SERVICE_RECORD);
                for (int i = 0; i < records.length; i++) serviceTable.addService(records[i]);
                currentSvcCount.setText(getCybernodeCount().toString());
                Double utilization = new Double(cybernodeAdmin.getUtilization());
                utilizationTF.setText(utilization.toString());
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    if (!keepAlive) break;
                }
            } catch (Exception e) {
                if (!keepAlive) break;
                e.printStackTrace();
                showError(e.toString());
                if (apply != null) apply.setEnabled(false);
                if (replicate != null) replicate.setEnabled(false);
                break;
            }
        }
    }

    public void terminate() {
        keepAlive = false;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        if (apply != null) apply.setEnabled(false);
        if (replicate != null) replicate.setEnabled(false);
    }

    protected void showInformation(String text) {
        JOptionPane.showMessageDialog(this, text, "Information Message", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showError(String text) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(this, text, "System Error", JOptionPane.ERROR_MESSAGE);
    }

    JTextField createAttrTextField() {
        JTextField tf = new JTextField();
        tf.setEnabled(false);
        return (tf);
    }

    Integer getCybernodeCount() {
        try {
            svcCount = cybernodeAdmin.getServiceCount();
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        return (svcCount);
    }

    ComputeResourceUtilization getComputeResourceUtilization(Entry[] attrs) {
        for (int x = 0; x < attrs.length; x++) {
            if (attrs[x] instanceof ComputeResourceUtilization) {
                return (ComputeResourceUtilization) attrs[x];
            }
        }
        return (null);
    }

    class ApplyHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                Integer newCount = null;
                Integer count = cybernodeAdmin.getServiceLimit();
                String s = maxSvcCount.getText();
                try {
                    newCount = new Integer(s);
                } catch (Throwable t) {
                    showError("You must enter a valid number");
                    return;
                }
                if (count.equals(newCount)) return;
                String message = "Set service limit to [" + s + "] ?";
                String title = "Cybernode Service Configuration Change Confirmation";
                int answer = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.NO_OPTION) return;
                cybernodeAdmin.setServiceLimit(newCount);
                maxCount = cybernodeAdmin.getServiceLimit();
            } catch (RemoteException re) {
                re.printStackTrace();
                showError(re.getMessage());
            }
        }
    }
}
