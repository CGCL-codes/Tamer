package toxtree.ui.tree.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import toxTree.core.IDecisionMethod;
import toxTree.io.Tools;
import toxtree.data.DecisionMethodsDataModule;
import toxtree.data.ToxTreeModule;
import toxtree.ui.actions.DataModuleAction;
import toxtree.ui.tree.ListTableModel;
import toxtree.ui.tree.SelectListDialog;

/**
 * 
 * Selects a tree from available decision trees
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-10-23
 */
public class SelectTreeAction extends DataModuleAction {

    private static final long serialVersionUID = -3751562922607916214L;

    public static final String _aSelectMethod = "Select a decision tree";

    public SelectTreeAction(DecisionMethodsDataModule module) {
        this(module, _aSelectMethod);
    }

    public SelectTreeAction(DecisionMethodsDataModule module, String name) {
        this(module, name, Tools.getImage("plugin.png"));
    }

    public SelectTreeAction(DecisionMethodsDataModule module, String name, Icon icon) {
        super(module, name, icon);
        putValue(Action.SHORT_DESCRIPTION, "Select a new decision tree from the available ones.");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        ActionMap actions = new ActionMap();
        actions.put("Open", new LoadTreeAction((DecisionMethodsDataModule) module, "Load from file"));
        Object o = SelectListDialog.selectFromList(module.getActions().getFrame(), "Select a tree", "Available decision trees", new ListTableModel(((DecisionMethodsDataModule) module).getMethods()), actions, new Dimension(450, 250));
        addTree((IDecisionMethod) o);
    }

    public void addTree(IDecisionMethod tree) {
        if (tree != null) {
            ((ToxTreeModule) module).setRules(tree);
        }
    }

    @Override
    public void run() throws Exception {
    }
}
