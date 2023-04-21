package com.ibm.maf.rmi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import com.ibm.maf.MAFFinder;
import com.ibm.maf.Name;

public class MAFFinderViewer extends Frame implements WindowListener, ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -7549221609473733118L;

    private String _finder_url = "rmi://localhost:4435/MAFFinder";

    private MAFFinder _finder = null;

    private Button _update_button;

    private Button _connect_button;

    private TextField _finder_host;

    private TextField _finder_name;

    private TextField _finder_port;

    private List _list_agents;

    private List _list_places;

    private List _list_agent_systems;

    public MAFFinderViewer() {
        this.setTitle("MAFFinderViewer: NOT CONNECTED");
        this.addWindowListener(this);
        this.setLayout(new BorderLayout());
        Font f = Font.decode("Courier");
        this._list_agents = new List();
        this._list_places = new List();
        this._list_agent_systems = new List();
        this._list_agents.setFont(f);
        this._list_places.setFont(f);
        this._list_agent_systems.setFont(f);
        Panel panel_agents = new Panel(new BorderLayout());
        panel_agents.add(new Label("Agents"), "North");
        panel_agents.add(this._list_agents);
        Panel panel_places = new Panel(new BorderLayout());
        Panel panel_agent_systems = new Panel(new BorderLayout());
        panel_places.add(new Label("Places"), "North");
        panel_places.add(this._list_places, "Center");
        panel_agent_systems.add(new Label("Agent Systems"), "North");
        panel_agent_systems.add(this._list_agent_systems, "Center");
        Panel list_sub_panel = new Panel(new GridLayout(1, 2));
        list_sub_panel.add(panel_places);
        list_sub_panel.add(panel_agent_systems);
        Panel list_panel = new Panel(new GridLayout(2, 1));
        list_panel.add(panel_agents);
        list_panel.add(list_sub_panel);
        this._update_button = new Button("Update");
        this._update_button.addActionListener(this);
        this._connect_button = new Button("Reconnect");
        this._connect_button.addActionListener(this);
        Panel top_panel = new Panel();
        GridBagLayout gl = new GridBagLayout();
        GridBagConstraints cst = new GridBagConstraints();
        top_panel.setLayout(gl);
        Label lbl;
        lbl = new Label("host name");
        cst.gridx = 1;
        cst.gridy = 0;
        cst.gridwidth = 1;
        cst.fill = GridBagConstraints.NONE;
        cst.weightx = 0.0;
        gl.setConstraints(lbl, cst);
        top_panel.add(lbl);
        lbl = new Label("port");
        cst.gridx = 2;
        cst.gridy = 0;
        cst.gridwidth = 1;
        cst.fill = GridBagConstraints.NONE;
        cst.weightx = 0.0;
        gl.setConstraints(lbl, cst);
        top_panel.add(lbl);
        lbl = new Label("name");
        cst.gridx = 3;
        cst.gridy = 0;
        cst.gridwidth = 1;
        cst.fill = GridBagConstraints.NONE;
        cst.weightx = 0.0;
        gl.setConstraints(lbl, cst);
        top_panel.add(lbl);
        lbl = new Label("Finder Address");
        cst.gridwidth = 1;
        cst.gridx = 0;
        cst.gridy = 1;
        cst.fill = GridBagConstraints.NONE;
        gl.setConstraints(lbl, cst);
        top_panel.add(lbl);
        this._finder_host = new TextField("localhost", 20);
        cst.gridx = 1;
        cst.gridy = 1;
        cst.gridwidth = 1;
        cst.fill = GridBagConstraints.HORIZONTAL;
        cst.weightx = 1.0;
        gl.setConstraints(this._finder_host, cst);
        top_panel.add(this._finder_host);
        this._finder_port = new TextField(Integer.toString(MAFFinder_RMIImpl.REGISTRY_PORT), 4);
        cst.fill = GridBagConstraints.HORIZONTAL;
        cst.gridx = 2;
        cst.weightx = 1.0;
        gl.setConstraints(this._finder_port, cst);
        top_panel.add(this._finder_port);
        this._finder_name = new TextField(MAFFinder_RMIImpl.REGISTRY_NAME, 10);
        cst.gridx = 3;
        gl.setConstraints(this._finder_name, cst);
        top_panel.add(this._finder_name);
        cst.gridx = 4;
        gl.setConstraints(this._connect_button, cst);
        top_panel.add(this._connect_button);
        this.add(top_panel, "North");
        this.add(list_panel, "Center");
        Panel bottom_panel = new Panel();
        bottom_panel.add(this._update_button);
        this.add(bottom_panel, "South");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Update".equals(e.getActionCommand())) {
            this.update_lists();
            this.repaint();
        } else if ("Reconnect".equals(e.getActionCommand())) {
            this.setFinderAddress(this._finder_host.getText().trim(), this._finder_port.getText().trim(), this._finder_name.getText().trim());
            try {
                this.connect();
                this.update_lists();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            this.repaint();
        }
    }

    private void connect() throws Exception {
        this._finder = (MAFFinder) java.rmi.Naming.lookup(this.getFinderAddress());
    }

    private String getFinderAddress() {
        return this._finder_url;
    }

    public static void main(String[] args) throws Exception {
        MAFFinderViewer viewer = new MAFFinderViewer();
        if (args.length > 0) {
            try {
                viewer.readProperties(args[0]);
            } catch (IOException ex) {
                System.err.println("Cannot read property file: " + args[0]);
                System.exit(1);
            }
        }
        try {
            viewer.connect();
            viewer.update_lists();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        viewer.pack();
        viewer.show();
    }

    private void readProperties(String file) throws IOException {
        InputStream is = new FileInputStream(file);
        Properties props = new Properties();
        props.load(is);
        String host = props.getProperty("maf.finder.host", "localhost");
        String port = props.getProperty("maf.finder.port", Integer.toString(MAFFinder_RMIImpl.REGISTRY_PORT));
        String name = props.getProperty("maf.finder.name", MAFFinder_RMIImpl.REGISTRY_NAME);
        this.setFinderAddress(host, port, name);
    }

    private void setFinderAddress(String host, String port, String name) {
        this._finder_url = "rmi://" + host;
        if (port != null) {
            this._finder_url += ":" + port;
        }
        this._finder_url += "/" + name;
    }

    private void update_list_agent_systems() throws Exception {
        if (this._finder != null) {
            this._list_agent_systems.removeAll();
            Hashtable tab = this._finder.list_agent_system_entries();
            for (Enumeration e = tab.keys(); e.hasMoreElements(); ) {
                Name agent = (Name) e.nextElement();
                LocationInfo loc_info = (LocationInfo) tab.get(agent);
                this._list_agent_systems.add(agent.toString() + " = " + loc_info.getLocation());
            }
        }
    }

    private void update_list_agents() throws Exception {
        if (this._finder != null) {
            this._list_agents.removeAll();
            Hashtable tab = this._finder.list_agent_entries();
            for (Enumeration e = tab.keys(); e.hasMoreElements(); ) {
                Name agent = (Name) e.nextElement();
                LocationInfo loc_info = (LocationInfo) tab.get(agent);
                this._list_agents.add(agent.toString() + " = " + loc_info.getLocation());
            }
        }
    }

    private void update_list_places() throws Exception {
        if (this._finder != null) {
            this._list_places.removeAll();
            Hashtable tab = this._finder.list_place_entries();
            for (Enumeration e = tab.keys(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();
                this._list_places.add(name);
            }
        }
    }

    private void update_lists() {
        try {
            this.setTitle("MAFFinder: " + this.getFinderAddress());
            this.update_list_agents();
            this.update_list_places();
            this.update_list_agent_systems();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
