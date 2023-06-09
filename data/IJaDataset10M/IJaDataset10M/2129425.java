package dr.app.pathogen;

import dr.evolution.io.*;
import dr.evolution.tree.Tree;
import dr.evolution.tree.FlexibleTree;
import dr.evolution.util.TaxonList;
import dr.app.tools.NexusExporter;
import dr.app.pathogen.TemporalRooting;
import dr.util.NumberFormatter;
import jam.framework.DocumentFrame;
import jam.framework.Exportable;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

/**
 * @author Andrew Rambaut
 * @author Alexei Drummond
 * @version $Id: BeautiFrame.java,v 1.22 2006/09/09 16:07:06 rambaut Exp $
 */
public class PathogenFrame extends DocumentFrame {

    /**
     *
     */
    private static final long serialVersionUID = 2114148696789612509L;

    private JTabbedPane tabbedPane = new JTabbedPane();

    private JLabel statusLabel = new JLabel("No data loaded");

    private SamplesPanel samplesPanel;

    private TreesPanel treesPanel;

    TaxonList taxa = null;

    java.util.List<Tree> trees = new ArrayList<Tree>();

    public PathogenFrame(String title) {
        super();
        setTitle(title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getOpenAction().setEnabled(true);
        getSaveAction().setEnabled(false);
        getSaveAsAction().setEnabled(false);
        getFindAction().setEnabled(false);
        getZoomWindowAction().setEnabled(false);
    }

    public void initializeComponents() {
        samplesPanel = new SamplesPanel(this, taxa);
        treesPanel = new TreesPanel(this, trees.get(0));
        tabbedPane.addTab("Sample Dates", samplesPanel);
        tabbedPane.addTab("Analysis", treesPanel);
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(new BorderUIResource.EmptyBorderUIResource(new Insets(12, 12, 12, 12)));
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        getContentPane().setLayout(new BorderLayout(0, 0));
        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(new Dimension(1024, 768));
        setStatusMessage();
    }

    public void timeScaleChanged() {
        treesPanel.timeScaleChanged();
        setStatusMessage();
    }

    protected boolean readFromFile(File file) throws IOException {
        Reader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();
        while (line != null && line.length() == 0) {
            line = bufferedReader.readLine();
        }
        boolean isNexus = (line != null && line.toUpperCase().contains("#NEXUS"));
        reader = new FileReader(file);
        Tree tree = null;
        try {
            if (isNexus) {
                NexusImporter importer = new NexusImporter(reader);
                tree = importer.importTree(taxa);
            } else {
                NewickImporter importer = new NewickImporter(reader);
                tree = importer.importTree(taxa);
            }
        } catch (Importer.ImportException ime) {
            JOptionPane.showMessageDialog(this, "Error parsing imported file: " + ime, "Error reading file", JOptionPane.ERROR_MESSAGE);
            ime.printStackTrace();
            return false;
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(this, "File I/O Error: " + ioex, "File I/O Error", JOptionPane.ERROR_MESSAGE);
            ioex.printStackTrace();
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fatal exception: " + ex, "Error reading file", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
        if (tree == null) {
            JOptionPane.showMessageDialog(this, "The file is not in a suitable format or contains no trees.", "Error reading file", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        FlexibleTree binaryTree = new FlexibleTree(tree, true);
        binaryTree.resolveTree();
        trees.add(binaryTree);
        if (taxa == null) {
            taxa = binaryTree;
        }
        getExportTreeAction().setEnabled(true);
        getExportDataAction().setEnabled(true);
        return true;
    }

    protected boolean writeToFile(File file) throws IOException {
        return false;
    }

    protected void doExportTree() {
        FileDialog dialog = new FileDialog(this, "Export Tree File...", FileDialog.SAVE);
        dialog.setVisible(true);
        if (dialog.getFile() != null) {
            File file = new File(dialog.getDirectory(), dialog.getFile());
            PrintStream ps = null;
            try {
                ps = new PrintStream(file);
                writeTreeFile(ps, false);
                ps.close();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Error writing tree file: " + ioe.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void writeTreeFile(PrintStream ps, boolean newickFormat) throws IOException {
        Tree tree = treesPanel.getTreeAsViewed();
        NexusExporter nexusExporter = new NexusExporter(new PrintStream(ps));
        nexusExporter.exportTree(tree);
    }

    protected void doExportData() {
        FileDialog dialog = new FileDialog(this, "Export Data File...", FileDialog.SAVE);
        dialog.setVisible(true);
        if (dialog.getFile() != null) {
            File file = new File(dialog.getDirectory(), dialog.getFile());
            Writer writer = null;
            try {
                writer = new PrintWriter(file);
                treesPanel.writeDataFile(writer);
                writer.close();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Error writing data file: " + ioe.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setStatusMessage() {
        Tree tree = treesPanel.getTree();
        if (tree != null) {
            String message = "";
            message += "Tree loaded, " + tree.getTaxonCount() + " taxa";
            TemporalRooting tr = treesPanel.getTemporalRooting();
            if (tr.isContemporaneous()) {
                message += ", contemporaneous tips";
            } else {
                NumberFormatter nf = new NumberFormatter(3);
                message += ", dated tips with range " + nf.format(tr.getDateRange());
            }
            statusLabel.setText(message);
        }
    }

    public JComponent getExportableComponent() {
        JComponent exportable = null;
        Component comp = tabbedPane.getSelectedComponent();
        if (comp instanceof Exportable) {
            exportable = ((Exportable) comp).getExportableComponent();
        } else if (comp instanceof JComponent) {
            exportable = (JComponent) comp;
        }
        return exportable;
    }

    public Action getExportTreeAction() {
        return exportTreeAction;
    }

    public Action getExportDataAction() {
        return exportDataAction;
    }

    protected AbstractAction exportTreeAction = new AbstractAction("Export Tree...") {

        public void actionPerformed(ActionEvent ae) {
            doExportTree();
        }
    };

    protected AbstractAction exportDataAction = new AbstractAction("Export Data...") {

        public void actionPerformed(ActionEvent ae) {
            doExportData();
        }
    };
}
