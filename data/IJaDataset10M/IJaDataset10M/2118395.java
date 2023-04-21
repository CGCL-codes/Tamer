package org.SCAraide.GUI;

import java.awt.Dimension;
import org.SCAraide.GUI.Controller.Events.ProjectEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import org.SCAraide.GUI.Controller.Events.ControllerEventListener;
import org.SCAraide.GUI.Model.ProjectsTreeModel.TreeNode;
import org.SCAraide.GUI.UI.GraphicalComponentFactory;
import org.SCAraide.SCAProjectXMLBinding.GCDDiagram;
import org.SCAraide.gcd.Diagram;
import org.SCAraide.gcd.artifacts.Component;
import org.SCAraide.gcd.artifacts.Property;
import org.SCAraide.gcd.artifacts.Reference;
import org.SCAraide.gcd.artifacts.Service;
import org.jdesktop.application.Application;
import org.jdesktop.application.SessionStorage;

/**
 * The application's main frame.
 */
public class SCAraideView extends FrameView implements ControllerEventListener {

    protected Map<String, Diagram> diagrams;

    public SCAraideView(SingleFrameApplication app) {
        super(app);
        initComponents();
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("permanentMessage".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.stop();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        initPerso();
    }

    public JTree getProjectsTree() {
        return projectsTree;
    }

    private void initPerso() {
        diagrams = new HashMap<String, Diagram>();
        SessionStorage ss = Application.getInstance(SCAraideApp.class).getContext().getSessionStorage();
        ss.putProperty(JTabbedPane.class, null);
        projectsTree.setModel(SCAraideApp.getModel().getProjectsTreeModel());
    }

    public void testGCD() {
        Diagram diagramTab[] = new Diagram[5];
        for (int diagIndex = 0; diagIndex < diagramTab.length; diagIndex++) {
            diagramTab[diagIndex] = new Diagram();
            diagramTab[diagIndex].setName("Diagram " + (diagIndex + 1));
            Property propTab[] = new Property[10];
            for (int propIndex = 0; propIndex < propTab.length; propIndex++) {
                propTab[propIndex] = new Property();
                propTab[propIndex].setName("Test de property " + (propIndex + 1));
                propTab[propIndex].setLocation(propIndex * 10, propIndex * 10);
            }
            for (int propIndex = 0; propIndex < propTab.length; propIndex++) {
                diagramTab[diagIndex].add(propTab[propIndex]);
            }
            Service srvTab[] = new Service[10];
            for (int srvIndex = 0; srvIndex < srvTab.length; srvIndex++) {
                srvTab[srvIndex] = new Service();
                srvTab[srvIndex].setName("Test de service " + (srvIndex + 1));
                srvTab[srvIndex].setLocation(srvIndex * 10, 300 + srvIndex * 10);
            }
            for (int srvIndex = 0; srvIndex < srvTab.length; srvIndex++) {
                diagramTab[diagIndex].add(srvTab[srvIndex]);
            }
            Reference refTab[] = new Reference[10];
            for (int refIndex = 0; refIndex < refTab.length; refIndex++) {
                refTab[refIndex] = new Reference();
                refTab[refIndex].setName("Test de Reference " + (refIndex + 1));
                refTab[refIndex].setLocation(100 + refIndex * 10, 300 + refIndex * 10);
            }
            for (int refIndex = 0; refIndex < refTab.length; refIndex++) {
                diagramTab[diagIndex].add(refTab[refIndex]);
            }
            Component compTab[] = new Component[10];
            for (int compIndex = 0; compIndex < compTab.length; compIndex++) {
                compTab[compIndex] = new Component();
                compTab[compIndex].setName("Test de composant " + (compIndex + 1));
                compTab[compIndex].setPreferredSize(new Dimension(200, 200));
                compTab[compIndex].setSize(200, 200);
                compTab[compIndex].setLocation(compIndex * 10, compIndex * 10);
            }
            for (int compIndex = 0; compIndex < compTab.length; compIndex++) {
                diagramTab[diagIndex].add(compTab[compIndex]);
            }
            org.SCAraide.gcd.artifacts.Composite compositeTab[] = new org.SCAraide.gcd.artifacts.Composite[10];
            for (int compIndex = 0; compIndex < compositeTab.length; compIndex++) {
                compositeTab[compIndex] = new org.SCAraide.gcd.artifacts.Composite();
                compositeTab[compIndex].setName("Test de composite " + (compIndex + 1));
                compositeTab[compIndex].setPreferredSize(new Dimension(300, 300));
                compositeTab[compIndex].setSize(300, 300);
                compositeTab[compIndex].setLocation(400 + compIndex * 10, compIndex * 10);
            }
            for (int compIndex = 0; compIndex < compositeTab.length; compIndex++) {
                diagramTab[diagIndex].add(compositeTab[compIndex]);
            }
            JScrollPane tab = new JScrollPane(diagramTab[diagIndex]);
            mainPanel.addTab(diagramTab[diagIndex].getName(), tab);
        }
    }

    @Override
    public void onProjectEvent(ProjectEvent event) {
        switch(event.type) {
            case ADD:
                if (SCAraideApp.getModel().getProjectsCount() == 1) {
                    projectsTree.setRootVisible(false);
                }
                TreeNode projectNode = SCAraideApp.getModel().getNode(event.project);
                TreePath projectPath = new TreePath(SCAraideApp.getModel().getProjectsTreeModel().getPathToRoot(projectNode));
                projectsTree.expandPath(projectPath);
                GraphicalComponentFactory factory = new GraphicalComponentFactory();
                for (GCDDiagram gcdDiagram : event.project.getXmlProject().getGCDDiagram()) {
                    Diagram diagram = factory.createDiagram(gcdDiagram);
                    diagrams.put(event.project.getFileName() + "|" + diagram.getName(), diagram);
                    mainPanel.add(diagram.getName(), new JScrollPane(diagram));
                }
                break;
            case REMOVE:
                if (SCAraideApp.getModel().getProjectsCount() == 0) {
                    projectsTree.collapseRow(0);
                    projectsTree.setRootVisible(true);
                }
                for (GCDDiagram gcdDiagram : event.project.getXmlProject().getGCDDiagram()) {
                    String key = event.project.getFileName() + "|" + gcdDiagram.getName();
                    Diagram diagram = diagrams.get(key);
                    mainPanel.remove(diagram.getParent().getParent());
                    diagrams.remove(key);
                }
                break;
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SCAraideApp.getApplication().getMainFrame();
            aboutBox = new SCAraideAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SCAraideApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        splitPane = new javax.swing.JSplitPane();
        mainPanel = new javax.swing.JTabbedPane();
        treePane = new javax.swing.JScrollPane();
        projectsTree = new javax.swing.JTree();
        toolBarProject = new org.SCAraide.GUI.UI.ToolBarProject();
        menuBar.setName("menuBar");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.SCAraide.GUI.SCAraideApp.class).getContext().getResourceMap(SCAraideView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text"));
        fileMenu.setName("fileMenu");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.SCAraide.GUI.SCAraideApp.class).getContext().getActionMap(SCAraideView.class, this);
        exitMenuItem.setAction(actionMap.get("quit"));
        exitMenuItem.setName("exitMenuItem");
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        helpMenu.setText(resourceMap.getString("helpMenu.text"));
        helpMenu.setName("helpMenu");
        aboutMenuItem.setAction(actionMap.get("showAboutBox"));
        aboutMenuItem.setName("aboutMenuItem");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        statusPanel.setName("statusPanel");
        statusPanelSeparator.setName("statusPanelSeparator");
        statusMessageLabel.setName("statusMessageLabel");
        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel");
        progressBar.setName("progressBar");
        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(statusMessageLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 565, Short.MAX_VALUE).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(statusAnimationLabel).addContainerGap()));
        statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(statusPanelLayout.createSequentialGroup().addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(statusMessageLabel).addComponent(statusAnimationLabel).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(3, 3, 3)));
        splitPane.setName("splitPane");
        mainPanel.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        mainPanel.setName("mainPanel");
        splitPane.setRightComponent(mainPanel);
        treePane.setMinimumSize(new java.awt.Dimension(150, 150));
        treePane.setName("treePane");
        projectsTree.setName("projectsTree");
        treePane.setViewportView(projectsTree);
        splitPane.setLeftComponent(treePane);
        toolBarProject.setRollover(true);
        toolBarProject.setName("toolBarProject");
        setComponent(splitPane);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBarProject);
    }

    private javax.swing.JTabbedPane mainPanel;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JProgressBar progressBar;

    private javax.swing.JTree projectsTree;

    private javax.swing.JSplitPane splitPane;

    private javax.swing.JLabel statusAnimationLabel;

    private javax.swing.JLabel statusMessageLabel;

    private javax.swing.JPanel statusPanel;

    private org.SCAraide.GUI.UI.ToolBarProject toolBarProject;

    private javax.swing.JScrollPane treePane;

    private final Timer messageTimer;

    private final Timer busyIconTimer;

    private final Icon idleIcon;

    private final Icon[] busyIcons = new Icon[15];

    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
