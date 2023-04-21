package unbbayes.gui.mebn;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import unbbayes.controller.FormulaTreeController;
import unbbayes.controller.MEBNController;
import unbbayes.prs.mebn.OrdinaryVariable;

/**
 * Tree for the user selected what o variable he
 * want for substitute the atual place selected of
 * the formula tree. 
 * 
 * @author Laecio
 *
 */
public class OVariableTreeForReplaceInFormula extends OVariableTree {

    private OrdinaryVariable oVariableActive;

    private FormulaTreeController formulaTreeController;

    public OVariableTreeForReplaceInFormula(final MEBNController controller, FormulaTreeController _formulaTreeController) {
        super(controller);
        formulaTreeController = _formulaTreeController;
    }

    public void addListeners() {
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                int selRow = getRowForLocation(e.getX(), e.getY());
                if (selRow == -1) {
                    return;
                }
                TreePath selPath = getPathForLocation(e.getX(), e.getY());
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                OrdinaryVariable ordinaryVariable = ordinaryVariableMap.get(node);
                if (node.isLeaf() && (ordinaryVariable != null)) {
                    if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                    } else if (e.getClickCount() == 2 && e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                        try {
                            formulaTreeController.addOVariable(ordinaryVariable);
                            controller.updateFormulaActiveContextNode();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (e.getClickCount() == 1) {
                    }
                } else {
                }
            }
        });
    }
}
