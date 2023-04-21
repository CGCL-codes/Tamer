package DE.FhG.IGD.semoa.envision.plugins;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;
import DE.FhG.IGD.util.*;
import DE.FhG.IGD.semoa.net.*;
import DE.FhG.IGD.semoa.agent.*;
import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.semoa.security.*;
import DE.FhG.IGD.semoa.envision.*;

/**
 * Interface used for detecting new selection on the tree item
 */
interface VicinityItemChanged {

    public void evChanged(String url, String info);
}

/**
 *
 */
abstract class AbstractVicinityItemChanged implements VicinityItemChanged {
}

;

/**
 *
 */
public class VicinityTree extends JScrollPane {

    private final Color ERROR_FG_COLOR = new Color(140, 40, 20);

    private final Color SELECTED_FG_COLOR = Color.white;

    private final Color SELECTED_BG_COLOR = new Color(10, 50, 150);

    private final Color DEFAULT_FG_COLOR = Color.black;

    private final Color DEFAULT_BG_COLOR = Color.white;

    private ImageIcon LOCAL_HOST_ICON;

    private ImageIcon DEFAULT_ICON;

    private ImageIcon SELECTED_ICON;

    private Border DEFAULT_BORDER;

    private Border SELECTED_BORDER;

    private JPanel viewport_view;

    private JPanel rows_container;

    private JLabel first_row;

    public Vector selected_server_urls;

    public Vector selected_host_names;

    private int server_counter;

    private VicinityItemChanged onVicinityItemChanged;

    private Vicinity vicinity;

    private boolean multiselect = false;

    private Timer timer;

    /**
     *
     */
    class LinesPanel extends JPanel {

        private boolean row_height_determined;

        private int row_height;

        public LinesPanel() {
            setOpaque(false);
            row_height_determined = false;
        }

        public void paintComponent(Graphics g) {
            Component row;
            if (!row_height_determined) {
                if (server_counter > 0) {
                    row = rows_container.getComponent(0);
                    if (row != null) {
                        row_height = row.getSize().height;
                        row_height_determined = true;
                    }
                }
            }
            for (int i = 1; i <= server_counter; i++) {
                g.drawLine(20, -row_height + 10 + (i * row_height), 20, 12 + (i * row_height));
                g.drawLine(20, 12 + (i * row_height), 36, 12 + (i * row_height));
            }
        }
    }

    /**
     *
     */
    class VicinityItem extends JPanel {

        private VicinityTree tree;

        private boolean is_selected;

        private JLabel label;

        private String server_url;

        private String info;

        private String host_name;

        public VicinityItem(VicinityTree tree, String server_url, String info) {
            this.tree = tree;
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 0, 1));
            label = new JLabel("", DEFAULT_ICON, JLabel.LEADING);
            label.setOpaque(true);
            label.setBorder(DEFAULT_BORDER);
            label.setBackground(DEFAULT_BG_COLOR);
            setServerUrl(server_url, info);
            is_selected = false;
            label.addMouseListener(new VicinityItemListener());
            add(label);
        }

        public String getServerUrl() {
            return server_url;
        }

        public void setServerUrl(String server_url, String info) {
            this.server_url = server_url;
            this.info = info;
            host_name = server_url.substring(server_url.indexOf("//") + 2, server_url.lastIndexOf(":"));
            try {
                host_name = InetAddress.getByName(host_name).getHostName();
                int npos = host_name.indexOf(".");
                if (npos >= 0) {
                    host_name = host_name.substring(0, npos);
                }
            } catch (Exception exc) {
                exc.printStackTrace(System.out);
            }
            label.setText(host_name);
        }

        public String getHostName() {
            return host_name;
        }

        public boolean getSelected() {
            return is_selected;
        }

        public void setSelected(boolean value) {
            if (value && !tree.multiselect) {
                for (int idx = 0; idx < tree.server_counter; idx++) {
                    VicinityItem vItem = (VicinityItem) tree.rows_container.getComponent(idx);
                    vItem.setSelected(false);
                }
            }
            if (is_selected != value) {
                is_selected = value;
                if (is_selected) {
                    label.setForeground(SELECTED_FG_COLOR);
                    label.setBackground(SELECTED_BG_COLOR);
                    label.setBorder(SELECTED_BORDER);
                    label.setIcon(SELECTED_ICON);
                    selected_server_urls.add(server_url);
                    selected_host_names.add(host_name);
                    tree.evChanged(server_url, info);
                } else {
                    label.setForeground(DEFAULT_FG_COLOR);
                    label.setBackground(DEFAULT_BG_COLOR);
                    label.setBorder(DEFAULT_BORDER);
                    label.setIcon(DEFAULT_ICON);
                    selected_server_urls.remove(server_url);
                    selected_host_names.remove(host_name);
                }
            }
        }

        /**
         *
         */
        class VicinityItemListener extends MouseAdapter {

            public void mousePressed(MouseEvent evt) {
                setSelected(!tree.multiselect || !is_selected);
            }
        }
    }

    public VicinityTree(Vicinity vicinity, ImageIcon iLocalHost, ImageIcon iDefault, ImageIcon iSelected) {
        this.vicinity = vicinity;
        LOCAL_HOST_ICON = iLocalHost;
        DEFAULT_ICON = iDefault;
        SELECTED_ICON = iSelected;
        DEFAULT_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 4);
        SELECTED_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(1, 1, 1, 3));
        LinesPanel north_panel = new LinesPanel();
        north_panel.setLayout(new BorderLayout());
        first_row = new JLabel("Initializing ...");
        first_row.setOpaque(true);
        first_row.setBackground(Color.white);
        first_row.setForeground(new Color(10, 120, 10));
        rows_container = new JPanel();
        rows_container.setOpaque(false);
        rows_container.setLayout(new GridLayout(0, 1));
        rows_container.setBorder(BorderFactory.createEmptyBorder(5, 40, 0, 0));
        north_panel.add(first_row, BorderLayout.NORTH);
        north_panel.add(rows_container, BorderLayout.SOUTH);
        JPanel center_panel = new JPanel();
        center_panel.setOpaque(false);
        viewport_view = new JPanel();
        viewport_view.setLayout(new BorderLayout());
        viewport_view.setBackground(Color.white);
        viewport_view.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
        viewport_view.add(north_panel, BorderLayout.NORTH);
        viewport_view.add(center_panel, BorderLayout.CENTER);
        JScrollBar sb = getHorizontalScrollBar();
        sb.setUnitIncrement(10);
        setHorizontalScrollBar(sb);
        sb = getVerticalScrollBar();
        sb.setUnitIncrement(10);
        setVerticalScrollBar(sb);
        server_counter = -1;
        setViewportView(viewport_view);
        final VicinityTree tree = this;
        timer = new Timer(0, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tree.refreshList(false);
            }
        });
        timer.stop();
    }

    private String getHostName() {
        String sHost = "";
        try {
            sHost = InetAddress.getLocalHost().getHostName() + " ";
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
        return sHost + "(Local Host)";
    }

    /**
     * Timer for looking up for new semoa servers in vicinity
     */
    public void startTimer(int delay) {
        if (timer.isRunning() == (delay <= 0)) {
            if (delay > 0) {
                timer.setDelay(delay);
                timer.setInitialDelay(delay);
                timer.start();
            } else {
                timer.stop();
            }
        }
    }

    public void showNoServer() {
        first_row.setForeground(ERROR_FG_COLOR);
        first_row.setText("No other server running at this moment ...");
        first_row.setIcon(null);
        rows_container.removeAll();
        server_counter = 0;
        evChanged(null, null);
        setViewportView(viewport_view);
    }

    public void showLocalHost() {
        first_row.setForeground(Color.black);
        first_row.setText(getHostName());
        first_row.setIcon(LOCAL_HOST_ICON);
        rows_container.removeAll();
        server_counter = 0;
        selected_server_urls = new Vector();
        selected_host_names = new Vector();
        setViewportView(viewport_view);
    }

    public void addServer(String server_url, String info) {
        rows_container.add(new VicinityItem(this, server_url, info));
        server_counter++;
    }

    public Vector getSelectedHostNames() {
        return selected_host_names;
    }

    public Vector getSelectedServerUrls() {
        return selected_server_urls;
    }

    public VicinityItemChanged getOnVicinityItemChanged() {
        return onVicinityItemChanged;
    }

    /**
     * set a reference to the interface which implements the evChanged
     */
    public void setOnVicinityItemChanged(VicinityItemChanged event) {
        onVicinityItemChanged = event;
    }

    public void evChanged(String url, String info) {
        if (onVicinityItemChanged != null) {
            onVicinityItemChanged.evChanged(url, info);
        }
    }

    private void filterUrlMap(Map map) {
        Map.Entry entry;
        URL url;
        Iterator i;
        for (i = map.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            url = (URL) entry.getValue();
            if (url == null || url.getProtocol().equalsIgnoreCase("lsp")) {
                i.remove();
            }
        }
    }

    public void refreshList(boolean bForce) {
        Map map;
        Map.Entry entry;
        URL url;
        String key;
        Iterator i;
        int idx;
        map = vicinity.getContactTable();
        filterUrlMap(map);
        if (map.size() == 0) {
            if (bForce || (server_counter != 0)) {
                showNoServer();
            }
        } else {
            if (bForce || (server_counter <= 0)) {
                showLocalHost();
            }
            for (i = map.entrySet().iterator(), idx = 0; i.hasNext(); idx++) {
                entry = (Map.Entry) i.next();
                key = (String) entry.getKey();
                url = (URL) entry.getValue();
                if (idx < server_counter) {
                    VicinityItem vItem = (VicinityItem) rows_container.getComponent(idx);
                    if (url.toString().compareToIgnoreCase(vItem.server_url) != 0) {
                        vItem.setServerUrl(url.toString(), key);
                    }
                } else {
                    rows_container.add(new VicinityItem(this, url.toString(), key));
                    server_counter++;
                }
            }
            while (idx < server_counter) {
                rows_container.remove(--server_counter);
            }
        }
    }
}
