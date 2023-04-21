package toxTree.ui.tree.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionMethodsList;
import toxTree.core.Introspection;
import toxTree.data.DecisionMethodsDataModule;
import toxTree.data.ToxTreeActions;
import toxTree.exceptions.IntrospectionException;
import toxTree.io.MolFileFilter;
import toxTree.ui.actions.DataModuleAction;
import toxTree.ui.tree.images.ImageTools;

public class LoadTreeAction extends DataModuleAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3106015502941829943L;

    public LoadTreeAction(DecisionMethodsDataModule module) {
        this(module, "Load from file");
    }

    public LoadTreeAction(DecisionMethodsDataModule module, String name) {
        this(module, name, ImageTools.getImage("folder.png"));
    }

    public LoadTreeAction(DecisionMethodsDataModule module, String name, Icon icon) {
        super(module, name, icon);
        putValue(AbstractAction.SHORT_DESCRIPTION, "Loads a tree from user selected file (*.tml | *.tree)");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        File file = ToxTreeActions.selectFile(null, MolFileFilter.toxTree_ext, MolFileFilter.toxTree_ext_descr, true);
        if (file == null) return;
        try {
            FileInputStream in = new FileInputStream(file);
            if (file.getAbsolutePath().toLowerCase().endsWith(".tml")) {
                IDecisionMethod tree = Introspection.loadRulesXML(in, file.getAbsolutePath());
                if (tree != null) ((DecisionMethodsDataModule) module).getMethods().addDecisionMethod(tree); else throw new IntrospectionException("Decision tree not loaded!");
            } else if (file.getAbsolutePath().toLowerCase().endsWith(".tree")) {
                IDecisionMethod tree = Introspection.loadRules(in, file.getAbsolutePath());
                if (tree != null) ((DecisionMethodsDataModule) module).getMethods().addDecisionMethod(tree); else throw new IntrospectionException("Decision tree not loaded!");
            } else if (file.getAbsolutePath().toLowerCase().endsWith(".fml")) {
                IDecisionMethodsList trees = Introspection.loadForestXML(in);
                if (trees != null) for (int i = 0; i < trees.size(); i++) ((DecisionMethodsDataModule) module).getMethods().addDecisionMethod(trees.getMethod(i)); else throw new IntrospectionException("Forest not loaded!");
            } else if (file.getAbsolutePath().toLowerCase().endsWith(".forest")) {
                IDecisionMethodsList trees = Introspection.loadForest(in);
                if (trees != null) for (int i = 0; i < trees.size(); i++) ((DecisionMethodsDataModule) module).getMethods().addDecisionMethod(trees.getMethod(i)); else throw new IntrospectionException("Forest not loaded!");
            }
            try {
                in.close();
            } catch (IOException x) {
                JOptionPane.showMessageDialog(module.getActions().getFrame(), x.getMessage(), "Error when loading rules", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (FileNotFoundException x) {
            JOptionPane.showMessageDialog(module.getActions().getFrame(), x.getMessage(), "Error when loading rules", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException x) {
            JOptionPane.showMessageDialog(module.getActions().getFrame(), x.getMessage(), "Error when loading rules", JOptionPane.INFORMATION_MESSAGE);
        } catch (IntrospectionException x) {
            JOptionPane.showMessageDialog(module.getActions().getFrame(), x.getMessage(), "Error creating rules", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void run() {
    }
}
