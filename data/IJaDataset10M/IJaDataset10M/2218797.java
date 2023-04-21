package com.dcivision.workflow.applet.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jgraph.JGraph;
import org.jgraph.layout.JGraphLayoutAlgorithm;
import org.jgraph.layout.JGraphLayoutRegistry;
import org.jgraph.layout.JGraphLayoutSettings;
import org.jgraph.pad.resources.Translator;

/**
 * This dialog is shown when the layout function is requested.<br>
 *<br>
 * It offers a list with available layouts to choose from.<br>
 *<br>
 *<br>
 * @author <a href="mailto:Sven.Luzar@web.de">Sven Luzar</a>
 * @since 1.2.2
 * @version 1.0 init
 */
public class LayoutDialog extends javax.swing.JDialog {

    protected JGraph graph;

    protected Hashtable layoutSettings = new Hashtable();

    /**
         * Creates new form LayoutDialog
         */
    public LayoutDialog(Dialog parent, JGraph graph) {
        super(parent, true);
        this.graph = graph;
        init();
    }

    /**
         * Creates new form LayoutDialog
         */
    public LayoutDialog(Frame parent, JGraph graph) {
        super(parent);
        this.graph = graph;
        init();
    }

    public LayoutDialog(JGraph graph) {
        super((Frame) null, true);
        this.graph = graph;
        init();
    }

    /** initializes the dialog
         */
    protected void init() {
        initComponents();
        fillList();
        try {
            lstLayoutControllers.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

    /** Fills the List with the LayoutControllers
         *  
         *  @see LayoutRegistry
         */
    protected void fillList() {
        try {
            DefaultListModel model = new DefaultListModel();
            Iterator all = JGraphLayoutRegistry.getSharedJGraphLayoutRegistry().getLayouts().iterator();
            while (all.hasNext()) {
                JGraphLayoutAlgorithm controller = (JGraphLayoutAlgorithm) all.next();
                if (!"Spring Embedded".equals(controller.toString())) {
                    model.addElement(controller);
                }
            }
            lstLayoutControllers.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
         * Execute the algorithm
         */
    protected void execute() {
        cmdFinished.setEnabled(false);
        final JGraphLayoutAlgorithm controller = getSelectedLayoutController();
        if (controller == null) return;
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Performing Layout...", "", 0, controller.getMaximumProgress());
        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(1000);
        final Timer updater = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                progressMonitor.setProgress(controller.getProgress());
                controller.setAllowedToRun(!progressMonitor.isCanceled());
            }
        });
        Thread t = new Thread("Layout Algorithm " + controller.toString()) {

            public void run() {
                try {
                    Object[] cells;
                    List cellList = new ArrayList();
                    if (isApplyLayoutToAll()) {
                        graph.setSelectionCells(graph.getRoots());
                    }
                    cells = graph.getSelectionCells();
                    if (cells != null && cells.length > 0) {
                        updater.start();
                        JGraphLayoutAlgorithm.applyLayout(graph, controller, cells, null);
                    }
                } finally {
                    progressMonitor.close();
                    updater.stop();
                    cmdFinished.setEnabled(true);
                }
            }
        };
        t.start();
    }

    public synchronized JDialog getLayoutSettingsDialog(JGraphLayoutAlgorithm layout) {
        JDialog dlg = (JDialog) layoutSettings.get(layout);
        if (dlg == null) {
            final JGraphLayoutSettings settings = layout.createSettings();
            if (settings != null) {
                final JDialog dialog = JGraphLayoutAlgorithm.createDialog(settings, this, "Configure", "Close", "Apply");
                dialog.pack();
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((double) (screen.width - this.getWidth()) / 2.0);
                int y = (int) ((double) (screen.height - this.getHeight()) / 2.0);
                dialog.setLocation(x, y);
                layoutSettings.put(layout, dialog);
                dlg = dialog;
            }
        }
        return dlg;
    }

    /** Will call if the user clicks on the configuration button.
         *  if the layout controller is configurable the method
         *  calls the configure method at the controller.
         * 
         *  @see LayoutController#configure
         *
         */
    protected void configure() {
        try {
            JGraphLayoutAlgorithm controller = (JGraphLayoutAlgorithm) lstLayoutControllers.getSelectedValue();
            JDialog dialog = getLayoutSettingsDialog(controller);
            if (dialog instanceof Component) {
                dialog.setVisible(true);
            } else {
                String message = Translator.getString("Error.ThisLayoutCannotBeConfigured");
                JOptionPane.showMessageDialog(this, message, null, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Returns true 
         *      if the user 
         *  wants to apply the Algorithm for 
         *  all nodes.
         */
    public boolean isApplyLayoutToAll() {
        return cmdAllNodes.isSelected();
    }

    /** Returns the selected 
         *  LayoutController of null if
         *  no LayoutController was selected
         *
         */
    public JGraphLayoutAlgorithm getSelectedLayoutController() {
        try {
            return (JGraphLayoutAlgorithm) lstLayoutControllers.getSelectedValue();
        } catch (Exception e) {
            return null;
        }
    }

    /** initializes the GUI
         *
         */
    protected void initComponents() {
        cmdGrpApplyTo = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        pnlApplyTo = new javax.swing.JPanel();
        lblApplyTo = new javax.swing.JLabel();
        cmdAllNodes = new javax.swing.JRadioButton();
        cmdSelectedNodes = new javax.swing.JRadioButton();
        pnlButtons = new javax.swing.JPanel();
        layoutHint = new JLabel("Hint:");
        cmdConfigure = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdFinished = new javax.swing.JButton();
        pnlLayoutControllers = new javax.swing.JPanel();
        scrollLayoutControllers = new javax.swing.JScrollPane();
        lstLayoutControllers = new javax.swing.JList();
        cmdConfigure.setEnabled(false);
        lstLayoutControllers.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                JGraphLayoutAlgorithm layout = getSelectedLayoutController();
                if (layout != null) {
                    String hint = layout.getHint();
                    if (hint != null && hint.length() > 0) layoutHint.setText("Hint: " + hint); else layoutHint.setText("Hint:");
                    JDialog dialog = getLayoutSettingsDialog(layout);
                    cmdConfigure.setEnabled(dialog != null);
                } else {
                    layoutHint.setText("Hint:");
                    cmdConfigure.setEnabled(false);
                }
            }
        });
        setTitle(Translator.getString("Layout"));
        setName("Layout");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                dispose();
            }
        });
        pnlMain.setLayout(new java.awt.BorderLayout());
        lblApplyTo.setText(Translator.getString("Apply to"));
        lblApplyTo.setName("ApplyTo");
        pnlApplyTo.add(lblApplyTo);
        cmdAllNodes.setFont(new java.awt.Font("Dialog", 0, 12));
        cmdAllNodes.setText(Translator.getString("AllNodes"));
        cmdAllNodes.setName("AllNodes");
        cmdGrpApplyTo.add(cmdAllNodes);
        pnlApplyTo.add(cmdAllNodes);
        cmdSelectedNodes.setFont(new java.awt.Font("Dialog", 0, 12));
        cmdSelectedNodes.setSelected(true);
        cmdSelectedNodes.setText(Translator.getString("SelectedNodes"));
        cmdSelectedNodes.setName("SelectedNodes");
        cmdGrpApplyTo.add(cmdSelectedNodes);
        pnlApplyTo.add(cmdSelectedNodes);
        pnlMain.add(pnlApplyTo, java.awt.BorderLayout.NORTH);
        cmdConfigure.setText(Translator.getString("Configure"));
        cmdConfigure.setName("Configure");
        cmdConfigure.setFocusPainted(false);
        cmdConfigure.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configure();
            }
        });
        pnlButtons.add(cmdConfigure);
        cmdFinished.setText("Execute");
        cmdFinished.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                execute();
            }
        });
        pnlButtons.add(cmdFinished);
        pnlButtons.add(cmdCancel);
        getRootPane().setDefaultButton(cmdFinished);
        cmdCancel.setText(Translator.getString("Component.FileClose.Text"));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        JPanel panelHintAndButtons = new JPanel(new BorderLayout());
        panelHintAndButtons.add(layoutHint, java.awt.BorderLayout.NORTH);
        panelHintAndButtons.add(pnlMain, java.awt.BorderLayout.CENTER);
        pnlMain.add(pnlButtons, java.awt.BorderLayout.SOUTH);
        getContentPane().add(panelHintAndButtons, java.awt.BorderLayout.SOUTH);
        pnlLayoutControllers.setLayout(new java.awt.BorderLayout());
        scrollLayoutControllers.setViewportView(lstLayoutControllers);
        pnlLayoutControllers.add(scrollLayoutControllers, java.awt.BorderLayout.CENTER);
        getContentPane().add(pnlLayoutControllers, java.awt.BorderLayout.CENTER);
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
    }

    /** GUI object */
    private javax.swing.JPanel pnlApplyTo;

    /** GUI object */
    private javax.swing.JButton cmdConfigure;

    /** GUI object */
    private javax.swing.JPanel pnlLayoutControllers;

    /** GUI object */
    private javax.swing.JScrollPane scrollLayoutControllers;

    /** GUI object */
    private javax.swing.JRadioButton cmdSelectedNodes;

    /** GUI object */
    private javax.swing.ButtonGroup cmdGrpApplyTo;

    /** GUI object */
    private javax.swing.JRadioButton cmdAllNodes;

    /** GUI object */
    private javax.swing.JList lstLayoutControllers;

    /** GUI object */
    private javax.swing.JPanel pnlButtons;

    /** GUI object */
    private javax.swing.JButton cmdCancel;

    /** GUI object */
    private javax.swing.JPanel pnlMain;

    /** GUI object */
    private javax.swing.JLabel lblApplyTo, layoutHint;

    /** GUI object */
    private javax.swing.JButton cmdFinished, closeButton;
}
