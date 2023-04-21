package com.agentfactory.visualiser.platformViewer;

import com.agentfactory.platform.service.PlatformService;
import com.agentfactory.platform.service.PlatformServiceManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author remcollier
 */
public class ServiceViewPanel extends JPanel implements Observer {

    private JList services;

    public ServiceViewPanel(PlatformServiceManager manager) {
        DefaultListModel model = new DefaultListModel();
        services = new JList(model);
        services.setCellRenderer(new DefaultListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean iss, boolean chf) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, iss, chf);
                if (value instanceof PlatformService) {
                    PlatformService service = (PlatformService) value;
                    label.setText(service.getName());
                }
                return label;
            }
        });
        Iterator it = manager.getServiceNames().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            model.addElement(manager.getService(name));
        }
        JScrollPane pane = new JScrollPane(services);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, pane);
        manager.addObserver(this);
    }

    public void update(Observable source, Object event) {
        if (source instanceof PlatformServiceManager) {
            DefaultListModel model = (DefaultListModel) services.getModel();
            model.addElement(event);
        } else {
            System.out.println("Source: " + source);
            System.out.println("Event: " + event);
        }
    }
}
