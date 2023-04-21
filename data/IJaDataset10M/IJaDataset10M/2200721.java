package toxTree.ui.tree.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import toxTree.core.IDecisionMethod;
import toxTree.core.Introspection;
import toxTree.data.ToxTreeActions;
import toxTree.io.MolFileFilter;
import toxTree.tree.DecisionMethodsList;
import toxTree.tree.PDFTreePrinter;
import toxTree.tree.ReportTreePrinter;
import toxTree.tree.SimpleTreePrinter;
import toxTree.ui.tree.images.ImageTools;

/**
 * Saves a {@link toxTree.core.IDecisionMethod} to a file
 * Uses serialization mechanism.
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-10-23
 */
public class SaveTreeAction extends AbstractTreeAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8497168231048381619L;

    public SaveTreeAction(IDecisionMethod tree) {
        this(tree, "Save");
    }

    public SaveTreeAction(IDecisionMethod tree, String name) {
        this(tree, name, ImageTools.getImage("save.png"));
    }

    public SaveTreeAction(IDecisionMethod tree, String name, Icon icon) {
        super(tree, name, icon);
        StringBuffer b = new StringBuffer();
        b.append("<html>");
        b.append("Saves the tree to a file:");
        for (int i = 0; i < MolFileFilter.toxTree_ext_save.length; i++) {
            b.append("<br>");
            b.append(MolFileFilter.toxTree_ext_descr_save[i]);
        }
        b.append("</html>");
        putValue(AbstractAction.SHORT_DESCRIPTION, b.toString());
    }

    public void exportRules(OutputStream stream, String filename) {
        try {
            if (filename.toLowerCase().endsWith(".tml")) {
                FileOutputStream os = new FileOutputStream(filename);
                Introspection.saveRulesXML(tree, os);
                os.close();
            } else if (filename.toLowerCase().endsWith(".fml")) {
                DecisionMethodsList forest = NewRuleAction.forestFromTree(tree);
                FileOutputStream os = new FileOutputStream(filename);
                Thread.currentThread().setContextClassLoader(Introspection.getLoader());
                XMLEncoder encoder = new XMLEncoder(os);
                encoder.writeObject(forest);
                encoder.close();
            } else if (filename.toLowerCase().endsWith(".csv")) {
                FileOutputStream file = new FileOutputStream(filename);
                SimpleTreePrinter printer = new SimpleTreePrinter(file);
                printer.setDelimiter(',');
                tree.walkRules(tree.getTopRule(), printer);
            } else if (filename.toLowerCase().endsWith(".txt")) {
                FileOutputStream file = new FileOutputStream(filename);
                SimpleTreePrinter printer = new SimpleTreePrinter(file);
                printer.setDelimiter('\t');
                tree.walkRules(tree.getTopRule(), printer);
            } else if (filename.toLowerCase().endsWith(".html")) {
                File file = new File(filename);
                ReportTreePrinter printer = new ReportTreePrinter(file);
                tree.walkRules(tree.getTopRule(), printer);
            } else if (filename.toLowerCase().endsWith(".pdf")) {
                FileOutputStream file = new FileOutputStream(filename);
                PDFTreePrinter printer = new PDFTreePrinter(file);
                tree.walkRules(tree.getTopRule(), printer);
            } else new ObjectOutputStream(stream).writeObject(tree);
            JOptionPane.showMessageDialog(null, "Decision tree \"" + tree.toString() + "\"\nsaved to \n" + filename, tree.toString(), JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error when saving rules to ", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent arg0) {
        Component parent = getParentFrame();
        VerifyUnreachableRulesAction a = new VerifyUnreachableRulesAction(tree);
        a.actionPerformed(arg0);
        File file = ToxTreeActions.selectFile(parent, MolFileFilter.toxTree_ext_save, MolFileFilter.toxTree_ext_descr_save, false);
        if (file == null) return;
        try {
            if (file.exists() && (JOptionPane.showConfirmDialog(parent, "File " + file.getAbsolutePath() + " already exists.\nOverwrite?", "Please confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)) return;
            FileOutputStream out = new FileOutputStream(file);
            exportRules(out, file.getAbsolutePath());
            tree.setTitle(file.getAbsolutePath());
            tree.setModified(false);
            try {
                out.close();
            } catch (IOException x) {
                JOptionPane.showMessageDialog(parent, "Error on saving rules", x.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        } catch (FileNotFoundException x) {
            JOptionPane.showMessageDialog(parent, "Error on saving rules", x.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
