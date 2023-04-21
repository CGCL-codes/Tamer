package com.memomics.cytoscape_plugin.panels.scripting;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.alitora.asapi.model.ASAPI_NodeTypes;
import com.alitora.asapi.model.ASAPI_Properties;
import com.alitora.asapi.publicModel.ASAPI_EdgeBase;
import com.alitora.asapi.publicModel.ASAPI_Entity;
import com.alitora.asapi.publicModel.ASAPI_Meme;
import com.alitora.asapi.publicModel.ASAPI_NodeBase;
import com.alitora.asapi.publicModel.ASAPI_Response;
import com.alitora.asapi.publicModel.ASAPI_ScopeType;
import com.alitora.asapi.publicModel.ASAPI_Status;
import com.alitora.asapi.publicModel.properties.ASAPI_Property;
import com.alitora.asapi.publicModel.properties.ASAPI_PropertyString;
import com.alitora.asapi.publicModel.properties.ASAPI_PropertyURI;
import com.alitora.asapi.publicModel.properties.ASAPI_PropertyUntokenizedString;
import com.memomics.cytoscape_plugin.model.Application;
import com.memomics.cytoscape_plugin.model.Utils;
import com.memomics.cytoscape_plugin.panels.NetworkListPanel;
import com.memomics.cytoscape_plugin.panels.PanelUtils;
import com.memomics.cytoscape_plugin.panels.scripting.ActiveQueries.ActiveQueriesListener;
import com.memomics.cytoscape_plugin.panels.scripting.ActiveQueries.ActiveQuery;
import com.memomics.cytoscape_plugin.panels.scripting.ActiveQueries.ActiveQueryListener;
import com.memomics.cytoscape_plugin.panels.scripting.ActiveQueries.QueryState;
import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;

public class ActiveQueriesPanel extends JPanel implements ActiveQueriesListener {

    private static final long serialVersionUID = -3821370155608728362L;

    private JPanel container = new JPanel();

    private NetworkListPanel networkListPanel = new NetworkListPanel();

    private ScriptingFrame parent;

    public ActiveQueriesPanel(ScriptingFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        ActiveQueries.get().addListener(this);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(container, BorderLayout.NORTH);
        JScrollPane scrollPanel = new JScrollPane(wrapper, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.add(new JLabel("Target network:"));
        leftPanel.add(networkListPanel);
        northPanel.add(leftPanel, BorderLayout.WEST);
        add(northPanel, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);
        JPanel testingConsole = new JPanel();
        testingConsole.setLayout(new BoxLayout(testingConsole, BoxLayout.X_AXIS));
        JButton addNewTaskButton = new JButton("ADD TASK");
        addNewTaskButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ActiveQuery activeQuery = new ActiveQuery();
                activeQuery.setState(QueryState.STOPPED);
                String query = "XML";
                ActiveQueries.get().addNewUnnamedActiveQuery(query);
            }
        });
        testingConsole.add(addNewTaskButton);
    }

    @Override
    public void onQueriesListChanged(List<ActiveQuery> queries) {
        boolean componentsListChanged = false;
        for (Component c : container.getComponents()) {
            QueryPanel panel = (QueryPanel) c;
            if (!queries.contains(panel.activeQuery)) {
                container.remove(c);
                componentsListChanged = true;
            }
        }
        for (int i = 0; i < queries.size(); i++) {
            ActiveQuery activeQuery = queries.get(i);
            QueryPanel currentPanel = null;
            int currentIndex = -1;
            for (int j = i; j < container.getComponentCount(); j++) {
                if (j < container.getComponentCount()) {
                    QueryPanel panel = (QueryPanel) container.getComponent(j);
                    if (activeQuery == panel.activeQuery) {
                        currentPanel = panel;
                        currentIndex = j;
                    }
                }
            }
            if (currentPanel != null) {
                if (currentIndex != i) {
                    container.remove(currentPanel);
                    container.add(currentPanel, i);
                    componentsListChanged = true;
                }
            } else {
                container.add(new QueryPanel(activeQuery), i);
                componentsListChanged = true;
            }
        }
        if (componentsListChanged) {
            validate();
        }
    }

    private class QueryPanel extends JPanel implements ActiveQueryListener, ActionListener {

        private static final long serialVersionUID = -2179512976120517950L;

        private ActiveQuery activeQuery;

        private JLabel nameLabel = new JLabel();

        private JLabel statusLabel = new JLabel();

        private JButton stopButton = new JButton("Stop");

        private JButton removeButton = new JButton("Remove");

        private JButton runButton = new JButton("Run");

        private JButton importButton = new JButton("Import Results");

        private JButton saveButton = new JButton("Save Query");

        public QueryPanel(ActiveQuery activeQuery) {
            this.activeQuery = activeQuery;
            activeQuery.setListener(this);
            setBorder(new EmptyBorder(3, 5, 3, 5));
            setLayout(new BorderLayout());
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(nameLabel);
            leftPanel.add(statusLabel);
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
            saveButton.addActionListener(this);
            rightPanel.add(saveButton);
            stopButton.addActionListener(this);
            rightPanel.add(stopButton);
            removeButton.addActionListener(this);
            rightPanel.add(removeButton);
            runButton.addActionListener(this);
            rightPanel.add(runButton);
            importButton.addActionListener(this);
            rightPanel.add(importButton);
            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.EAST);
            onActiveQueryStateChanged();
        }

        @Override
        public synchronized void onActiveQueryStateChanged() {
            QueryState s = activeQuery.getState();
            String color = null;
            String state = null;
            ASAPI_Response response = activeQuery.getResponse();
            if (s == QueryState.STOPPED) {
                state = "Stopped";
                color = "#FFA500";
            } else if (s == QueryState.ERROR) {
                String errorString = "";
                if (response != null) {
                    errorString = response.getStatusCode().toString() + " " + response.getErrorMessage();
                }
                state = "Error: " + errorString;
                color = "red";
            } else if (s == QueryState.RUNNING) {
                state = "Running";
                color = "blue";
            } else if (s == QueryState.COMPLETE) {
                color = "green";
                state = "Complete, " + response.getObjects().size() + " results";
            }
            nameLabel.setText(activeQuery.getLabel());
            statusLabel.setText("<html>status: <span style=\"color: " + color + "\">" + state + "</span>");
            runButton.setVisible(s == QueryState.STOPPED || s == QueryState.ERROR);
            stopButton.setVisible(s == QueryState.RUNNING);
            removeButton.setVisible(s == QueryState.STOPPED || s == QueryState.ERROR || s == QueryState.COMPLETE);
            importButton.setVisible(s == QueryState.COMPLETE);
            saveButton.setVisible(activeQuery.getUnnamedQueryLabel() != null);
            validate();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == runButton) {
                activeQuery.run();
            } else if (src == stopButton) {
                activeQuery.stop();
            } else if (src == removeButton) {
                ActiveQueries.get().removeQuery(activeQuery);
            } else if (src == importButton) {
                CyNetwork cyNetwork = networkListPanel.getSelectedNetwork();
                ASAPI_Response response = activeQuery.getResponse();
                List<ASAPI_Entity> entities = new ArrayList<ASAPI_Entity>();
                for (Object object : response.getObjects()) {
                    if (object instanceof ASAPI_NodeBase) {
                        ASAPI_NodeBase node = (ASAPI_NodeBase) object;
                        entities.add(node);
                    }
                }
                Utils.placeNodesInTheNetwork(cyNetwork, entities);
                boolean edgeAdded = false;
                for (Object object : response.getObjects()) {
                    if (object instanceof ASAPI_EdgeBase) {
                        ASAPI_EdgeBase eb = (ASAPI_EdgeBase) object;
                        CyNode destNode = Utils.getCyNodeForUMISInTheNetwork(cyNetwork, eb.getDestUMIS());
                        CyNode srcNode = Utils.getCyNodeForUMISInTheNetwork(cyNetwork, eb.getSrcUMIS());
                        if (destNode != null && srcNode != null) {
                            CyEdge edge = Cytoscape.getCyEdge(srcNode, destNode, Semantics.INTERACTION, eb.getUMIS(), true, true);
                            Utils.setEdgeAttributes(edge.getIdentifier(), eb);
                            CyEdge addEdge = Cytoscape.getCurrentNetwork().addEdge(edge);
                            if (addEdge == null) {
                                System.err.println("Edge with UMIS:" + eb.getUMIS() + " wasn't created!");
                            } else {
                                edgeAdded = true;
                            }
                        }
                    }
                }
                if (edgeAdded) {
                    Cytoscape.firePropertyChange(Cytoscape.NETWORK_MODIFIED, null, cyNetwork);
                }
            } else if (src == saveButton) {
                String s = (String) JOptionPane.showInputDialog(ActiveQueriesPanel.this, "Enter the name for this query:", "Enter query name", JOptionPane.PLAIN_MESSAGE);
                if ((s != null) && (s.length() > 0)) {
                    ASAPI_Meme newMeme = new ASAPI_Meme();
                    newMeme.setActive(true);
                    newMeme.setNodeTypeUMIS(ASAPI_NodeTypes.QUERY_MEME);
                    List<ASAPI_Property<?>> props = new ArrayList<ASAPI_Property<?>>();
                    props.add(new ASAPI_PropertyString(ASAPI_Properties.CANONICAL_NAME, s));
                    props.add(new ASAPI_PropertyUntokenizedString(ASAPI_Properties.QUERY_XML_STRING, activeQuery.getUnnamedQueryXML()));
                    props.add(new ASAPI_PropertyURI(ASAPI_Properties.RDF_TYPE, URI.create(ASAPI_NodeTypes.QUERY_MEME_RDF_TYPE)));
                    newMeme.setProps(props);
                    newMeme.setScopeType(ASAPI_ScopeType.PUBLIC);
                    ASAPI_Response createRS = Application.get().createMeme(newMeme);
                    if (createRS.getStatusCode().equals(ASAPI_Status.STATUS_OK)) {
                        ASAPI_Meme editedMeme = (ASAPI_Meme) createRS.getFirstObject();
                        activeQuery.persist(editedMeme);
                        parent.refreshStoredQueries();
                    } else {
                        JOptionPane.showConfirmDialog(ActiveQueriesPanel.this, "Error when creating query meme: " + createRS.getStatusCode() + " " + createRS.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                }
            } else {
                throw new RuntimeException("Unhandled case: " + src);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("QUERIES PANEL TEST");
        frame.setSize(800, 600);
        PanelUtils.center(frame);
        ActiveQueriesPanel aqp = new ActiveQueriesPanel(null);
        frame.add(aqp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
