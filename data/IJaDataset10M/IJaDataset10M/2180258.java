package toxtree.ui.tree.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import toxTree.core.IDecisionMethod;
import toxTree.core.Introspection;
import toxTree.io.MolFileFilter;
import toxTree.io.Tools;
import toxtree.data.DataModule;
import toxtree.data.ToxTreeActions;

/**
 * Loads a decision tree from a file and launches a decision tree editor
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-10-23
 */
public class LoadAndEditTreeAction extends EditDecisionMethodAction {

    public static final String _aEditMethod = "Load from file";

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = -8466744301351947086L;

    /**
	 * @param module
	 */
    public LoadAndEditTreeAction(DataModule module) {
        this(module, _aEditMethod);
    }

    /**
	 * @param module
	 * @param name
	 */
    public LoadAndEditTreeAction(DataModule module, String name) {
        this(module, name, Tools.getImage("folder.png"));
    }

    /**
	 * @param module
	 * @param name
	 * @param icon
	 */
    public LoadAndEditTreeAction(DataModule module, String name, Icon icon) {
        super(module, name, icon);
        putValue(AbstractAction.SHORT_DESCRIPTION, "Loads a tree from a file and launches a decision tree editor");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = ToxTreeActions.selectFile(null, MolFileFilter.toxTree_ext, MolFileFilter.toxTree_ext_descr, true);
        if (file == null) return;
        try {
            FileInputStream in = new FileInputStream(file);
            IDecisionMethod tree = null;
            if (file.getAbsolutePath().toLowerCase().endsWith(".tml")) {
                tree = Introspection.loadRulesXML(in, file.getAbsolutePath());
            } else if (file.getAbsolutePath().toLowerCase().endsWith(".tree")) {
                tree = Introspection.loadRules(in, file.getAbsolutePath());
            } else throw new Exception("Unsupported format");
            if (tree != null) {
                launchEditor(tree);
            }
            try {
                in.close();
            } catch (Exception x) {
                JOptionPane.showMessageDialog(null, x.getMessage(), "Error when loading rules from", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception x) {
            JOptionPane.showMessageDialog(null, x.getMessage(), "Error when loading rules from", JOptionPane.ERROR_MESSAGE);
        }
    }
}
