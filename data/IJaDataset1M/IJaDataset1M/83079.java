package com.k42b3.aletheia.filter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import com.k42b3.aletheia.Aletheia;

/**
 * FilterIn
 *
 * @author     Christoph Kappestein <k42b3.x@gmail.com>
 * @license    http://www.gnu.org/licenses/gpl.html GPLv3
 * @link       http://code.google.com/p/delta-quadrant
 * @version    $Revision: 5 $
 */
public class FilterIn extends JFrame {

    private ArrayList<ConfigFilterAbstract> filtersConfig = new ArrayList<ConfigFilterAbstract>();

    private ArrayList<RequestFilterAbstract> filters = new ArrayList<RequestFilterAbstract>();

    private Aletheia aletheia;

    private ArrayList<RequestFilterAbstract> activeFilters;

    private Logger logger = Logger.getLogger("com.k42b3.aletheia");

    public FilterIn(ArrayList<RequestFilterAbstract> activeFilters, Aletheia aletheia) {
        this.activeFilters = activeFilters;
        this.setTitle("Request filter");
        this.setLocation(100, 100);
        this.setSize(400, 300);
        this.setMinimumSize(this.getSize());
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        JTabbedPane panel = new JTabbedPane();
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        ArrayList<String> filters = new ArrayList<String>();
        filters.add("BasicAuth");
        filters.add("Oauth");
        filters.add("UserAgent");
        for (int i = 0; i < filters.size(); i++) {
            try {
                String clsConfig = "com.k42b3.aletheia.filter.request." + filters.get(i) + "Config";
                String cls = "com.k42b3.aletheia.filter.request." + filters.get(i);
                Class cConfig = Class.forName(clsConfig);
                Class c = Class.forName(cls);
                ConfigFilterAbstract filterConfig = (ConfigFilterAbstract) cConfig.newInstance();
                RequestFilterAbstract filter = (RequestFilterAbstract) c.newInstance();
                filter.setInstance(aletheia);
                for (int j = 0; j < activeFilters.size(); j++) {
                    if (activeFilters.get(j).getClass().getName().equals(filter.getClass().getName())) {
                        filterConfig.onLoad(activeFilters.get(j).getConfig());
                    }
                }
                this.filtersConfig.add(filterConfig);
                this.filters.add(filter);
                JScrollPane scpFilter = new JScrollPane(filterConfig);
                scpFilter.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                scpFilter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scpFilter.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                panel.addTab(filterConfig.getName(), scpFilter);
            } catch (Exception e) {
                Aletheia.handleException(e);
            }
        }
        this.add(panel, BorderLayout.CENTER);
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new HandlerSave());
        panelButtons.add(btnSave);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new HandlerCancel());
        panelButtons.add(btnCancel);
        this.add(panelButtons, BorderLayout.SOUTH);
    }

    public void close() {
        this.setVisible(false);
    }

    private class HandlerSave implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            activeFilters.clear();
            for (int i = 0; i < filtersConfig.size(); i++) {
                if (filtersConfig.get(i).isActive()) {
                    filters.get(i).setConfig(filtersConfig.get(i).onSave());
                    activeFilters.add(filters.get(i));
                }
            }
            close();
        }
    }

    private class HandlerCancel implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            close();
        }
    }
}
