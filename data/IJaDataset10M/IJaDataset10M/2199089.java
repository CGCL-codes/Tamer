package jset.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import jset.JSETConstants;
import jset.JSETMain;
import jset.JavaListenerData;
import jset.collator.simple.SimpleGUIStructureCollator;
import jset.model.code.JClass;
import jset.model.code.JMethod;
import jset.model.gui.GUIComparedTreeNode;
import jset.model.gui.GUIElement;
import jset.model.gui.GUIStructureUtil;
import jset.project.Project;
import jset.view.cg.DeltaMethodPanel;
import jset.view.cg.builder.AbstractGraphBuilder;
import jset.view.cg.builder.DeltaGraphBuilder;
import jset.view.cg.builder.GraphBuilder;
import jset.view.widgetinfo.WidgetInfoContext;
import jset.view.widgetinfo.WidgetInfoView;
import jset.view.widgetinfo.event.PropertySelectionEvent;
import jset.view.widgetinfo.event.PropertySelectionListener;
import org.apache.log4j.Logger;

public class MainWindow extends JFrame implements Runnable, TreeSelectionListener, PropertySelectionListener {

    private JMenuBar menuBar;

    private JSplitPane mainSplitPane;

    private JSplitPane rightSplitPane;

    private GUITreePanel guiTreePanel;

    private JTabbedPane listenersTabbedPane;

    private WidgetInfoView wcp;

    Project p1;

    Project p2;

    public MainWindow() {
        super();
        this.p1 = JSETMain.getFirstProject();
        this.p2 = JSETMain.getSecondProject();
        setTitle(buildWindowTitle());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private String buildWindowTitle() {
        StringBuilder sb = new StringBuilder("jSET - ");
        if (p2 != null) {
            sb.append("Project Comparison Mode: " + p1.getName() + " -> " + p2.getName());
        } else {
            sb.append("Project Exploration Mode: " + p1.getName());
        }
        return sb.toString();
    }

    private void initGUI() {
        mainSplitPane = new JSplitPane();
        DefaultTreeModel model1 = (DefaultTreeModel) GUIStructureUtil.buildGUITreeModel(p1.getGui());
        if (JSETMain.isProjectExplorationModeActive() == false) {
            DefaultTreeModel model2 = (DefaultTreeModel) GUIStructureUtil.buildGUITreeModel(p2.getGui());
            SimpleGUIStructureCollator.collate(p1.getRepository(), p2.getRepository(), model1, model2);
        }
        guiTreePanel = new GUITreePanel(!JSETMain.isProjectExplorationModeActive());
        guiTreePanel.getGuiTree().setModel(model1);
        guiTreePanel.getGuiTree().addTreeSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(guiTreePanel);
        scrollPane.setPreferredSize(new Dimension(250, 600));
        mainSplitPane.setLeftComponent(new JScrollPane(guiTreePanel));
        rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        WidgetInfoContext wic = new WidgetInfoContext();
        wic.put(WidgetInfoContext.PROJECTS, (p2 != null ? new Project[] { p1, p2 } : new Project[] { p1 }));
        wcp = new WidgetInfoView(wic);
        wcp.addPropertySelectionListener(this);
        JPanel pPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1d;
        c.weighty = 1d;
        c.fill = GridBagConstraints.BOTH;
        listenersTabbedPane = new JTabbedPane();
        listenersTabbedPane.setPreferredSize(new Dimension(600, 200));
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(quitMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About...");
        aboutMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainWindow.this, JSETConstants.ABOUT_JSET, "About...", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        pPanel.add(wcp, c);
        rightSplitPane.setTopComponent(pPanel);
        rightSplitPane.setBottomComponent(listenersTabbedPane);
        mainSplitPane.setRightComponent(rightSplitPane);
        c = new GridBagConstraints();
        c.weightx = 1d;
        c.weighty = 1d;
        c.fill = GridBagConstraints.BOTH;
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(mainSplitPane, c);
        pack();
    }

    public void run() {
        initGUI();
        setVisible(true);
    }

    public void valueChanged(TreeSelectionEvent e) {
        GUIComparedTreeNode node = (GUIComparedTreeNode) guiTreePanel.getGuiTree().getLastSelectedPathComponent();
        if (node == null || node.getComponentPair() == null) {
            return;
        }
        GUIElement leftComponent = null;
        GUIElement rightComponent = null;
        if (node.getNodeState() == GUIComparedTreeNode.COMPONENT_NEW) {
            leftComponent = null;
            rightComponent = node.getComponentPair().first;
        }
        if (node.getNodeState() == GUIComparedTreeNode.COMPONENT_DELETED || node.getNodeState() == GUIComparedTreeNode.COMPONENT_SAME) {
            leftComponent = node.getComponentPair().first;
            rightComponent = null;
        }
        if (node.getNodeState() == GUIComparedTreeNode.COMPONENT_MODIFIED) {
            leftComponent = node.getComponentPair().first;
            rightComponent = node.getComponentPair().second;
        }
        wcp.setDisplayedComponents(leftComponent, rightComponent);
    }

    @Override
    public void valueChanged(PropertySelectionEvent event) {
        if (event.getPropertyName() == null && event.getPropertyValue() == null) {
            return;
        }
        String listenerType = event.getPropertyName();
        if (listenerType.indexOf("Listeners") < 0) {
            return;
        }
        String listenerClass = event.getPropertyValue();
        List<String> listenerMethods = JavaListenerData.getInstance().get(listenerType);
        listenersTabbedPane.removeAll();
        if (listenerMethods == null) {
            Logger.getLogger(MainWindow.class).error("Listener type not known " + listenerType);
            return;
        }
        for (String s : listenerMethods) {
            JClass clazz = p1.getRepository().get(listenerClass);
            JMethod startMethod = p1.getRepository().getMethodFromHierarchy(s, clazz);
            AbstractGraphBuilder builder = null;
            if (JSETMain.isProjectExplorationModeActive() == true) {
                builder = new GraphBuilder(p1.getRepository(), startMethod);
            } else {
                builder = new DeltaGraphBuilder(p1.getRepository(), p2.getRepository(), startMethod);
            }
            listenersTabbedPane.addTab(s, new DeltaMethodPanel(builder.build(), p1, p2));
        }
    }
}
