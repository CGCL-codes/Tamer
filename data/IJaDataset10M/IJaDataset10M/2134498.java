package toxTree.ui.tree.actions;

import java.awt.Component;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;
import toxTree.core.IDecisionMethod;
import toxTree.ui.tree.images.ImageTools;

/**
 * An {@link javax.swing.AbstractAction} descendant, performing an action on a preset {@link toxTree.core.IDecisionMethod}
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-10-9
 */
public abstract class AbstractTreeAction extends AbstractAction implements ITreeAction {

    protected IDecisionMethod tree;

    public static String PARENTKEY = "PARENTKEY";

    /**
	 * 
	 */
    public AbstractTreeAction(IDecisionMethod tree) {
        this(tree, "Tree");
    }

    /**
	 * @param name
	 */
    public AbstractTreeAction(IDecisionMethod tree, String name) {
        this(tree, name, ImageTools.getImage("tree.png"));
    }

    /**
	 * @param name
	 * @param icon
	 */
    public AbstractTreeAction(IDecisionMethod tree, String name, Icon icon) {
        super(name, icon);
        this.tree = tree;
    }

    public IDecisionMethod getTree() {
        return tree;
    }

    public void setTree(IDecisionMethod tree) {
        this.tree = tree;
    }

    public JFrame getParentFrame() {
        JFrame frame = null;
        Object p = getValue(AbstractTreeAction.PARENTKEY);
        if (p != null) {
            if (p instanceof JFrame) frame = (JFrame) p; else while (p != null) {
                p = ((Component) p).getParent();
                if (p instanceof JFrame) {
                    frame = (JFrame) p;
                    break;
                }
            }
        }
        return frame;
    }
}
