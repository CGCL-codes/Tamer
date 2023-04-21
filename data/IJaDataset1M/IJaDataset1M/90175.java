package net.sourceforge.processdash.tool.export.ui;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import net.sourceforge.processdash.ProcessDashboard;
import net.sourceforge.processdash.hier.DashHierarchy;
import net.sourceforge.processdash.hier.ui.SelectableHierarchyTree;
import net.sourceforge.processdash.i18n.Resources;
import net.sourceforge.processdash.tool.export.mgr.ExportManager;
import net.sourceforge.processdash.tool.export.mgr.ExportMetricsFileInstruction;
import net.sourceforge.processdash.ui.help.PCSH;
import net.sourceforge.processdash.ui.lib.ProgressDialog;

/** This class displays a dialog allowing the user to export data to a file.
 * 
 * This class contains the "deprecated" old functionality that used to be
 * present in the ImportExport class.  That class was refactored to separate
 * business logic, and the UI logic is now present here.
 */
public class ManualExportDialog extends JDialog implements ActionListener {

    static final Resources resource = Resources.getDashBundle("ImportExport");

    static final int X_DATA = 0;

    static final int X_LIST = 1;

    static String FILE_SEP = null;

    ProcessDashboard parent;

    DashHierarchy props;

    SelectableHierarchyTree tree;

    int operation = X_DATA;

    public ManualExportDialog(ProcessDashboard dash) {
        super(dash, resource.getString("Export"));
        PCSH.enableHelpKey(this, "ExportingData");
        parent = dash;
        props = parent.getHierarchy();
        tree = new SelectableHierarchyTree(props);
        JScrollPane sp = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.getViewport().add(tree);
        getContentPane().add(sp, "Center");
        ButtonGroup bg = new ButtonGroup();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        JRadioButton button;
        button = new JRadioButton(resource.getString("ExportData"));
        button.setActionCommand("XData");
        button.addActionListener(this);
        button.setSelected(true);
        button.setAlignmentX(0.5f);
        bg.add(button);
        buttonBox.add(button);
        button = new JRadioButton(resource.getString("ExportHierarchy"));
        button.setActionCommand("XList");
        button.addActionListener(this);
        button.setAlignmentX(0.5f);
        bg.add(button);
        buttonBox.add(button);
        buttonBox.add(Box.createVerticalStrut(4));
        buttonBox.add(Box.createVerticalGlue());
        Box btnBox = new Box(BoxLayout.X_AXIS);
        btnBox.add(Box.createHorizontalStrut(4));
        JButton btn = new JButton(resource.getString("Export"));
        btn.setActionCommand("Apply");
        btn.addActionListener(this);
        btnBox.add(btn);
        btn = new JButton(resource.getString("Close"));
        btn.setActionCommand("Close");
        btn.addActionListener(this);
        btnBox.add(btn);
        btnBox.add(Box.createHorizontalStrut(4));
        buttonBox.add(btnBox);
        buttonBox.add(Box.createVerticalStrut(2));
        getContentPane().add(buttonBox, "East");
        pack();
        show();
        Properties prop = System.getProperties();
        FILE_SEP = prop.getProperty("file.separator");
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        FileDialog fd;
        String lastFile, lastDir;
        boolean fail = false;
        if (cmd.equals("XData")) {
            operation = X_DATA;
        } else if (cmd.equals("XList")) {
            operation = X_LIST;
        } else if (cmd.equals("Close")) {
            setVisible(false);
        } else if (cmd.equals("Apply")) {
            DefaultMutableTreeNode dmn;
            Vector v = tree.getSelectedPaths();
            switch(operation) {
                case X_DATA:
                    fd = new FileDialog(parent, resource.getString("ExportDataTo"), FileDialog.SAVE);
                    fd.setFile("dash.txt");
                    fd.show();
                    lastDir = fd.getDirectory();
                    lastFile = fd.getFile();
                    if (lastFile != null) exportInteractively(v, new File(lastDir, lastFile));
                    break;
                case X_LIST:
                    fd = new FileDialog(parent, resource.getString("ExportHierarchyTo"), FileDialog.SAVE);
                    fd.setFile("hierarch.txt");
                    fd.show();
                    lastFile = fd.getFile();
                    if (lastFile != null) {
                        JDialog working;
                        working = new JDialog(parent, resource.getString("ExportExportingDots"));
                        working.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        JLabel lab = new JLabel(resource.getString("ExportExportingMessage"));
                        working.getContentPane().add(lab, "Center");
                        working.pack();
                        working.show();
                        Thread.yield();
                        lastDir = fd.getDirectory();
                        try {
                            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(lastDir + FILE_SEP + lastFile)));
                            parent.getHierarchy().orderedDump(out, v);
                            out.close();
                        } catch (IOException ioe) {
                            fail = true;
                            System.out.println("IOException: " + e);
                        }
                        ;
                        lab.setText(resource.getString("ExportComplete"));
                        working.invalidate();
                    }
                    break;
            }
        }
    }

    public void exportInteractively(Vector filter, File dest) {
        ProgressDialog p = ProgressDialog.create(this, resource.getString("ExportExporting"), resource.getString("ExportExportingDataDots"));
        ExportMetricsFileInstruction instr = new ExportMetricsFileInstruction(dest.getAbsolutePath(), filter);
        p.addTask(ExportManager.getInstance().getExporter(instr));
        p.setCompletionMessage(resource.getString("ExportDone"));
        p.run();
    }
}
