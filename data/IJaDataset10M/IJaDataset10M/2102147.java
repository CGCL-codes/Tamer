package toxTree.ui.tree.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionMethod;
import toxTree.ui.tree.images.ImageTools;

/**
 * Edits a category
 * 
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-10-23
 */
public class EditCategoryAction extends AbstractTreeAction implements ICategoryAction {

    protected IDecisionCategory category = null;

    /**
	 * 
	 */
    private static final long serialVersionUID = 2505268379464961009L;

    public EditCategoryAction(IDecisionMethod tree) {
        this(tree, "Edit category");
    }

    public EditCategoryAction(IDecisionMethod tree, String name) {
        this(tree, name, ImageTools.getImage("tag_blue_edit.png"));
    }

    public EditCategoryAction(IDecisionMethod tree, String name, Icon icon) {
        super(tree, name, icon);
        putValue(AbstractAction.SHORT_DESCRIPTION, "Modifies category details");
    }

    public void actionPerformed(ActionEvent arg0) {
        if (category == null) return;
        category.getEditor().edit(getParentFrame(), category);
    }

    public IDecisionCategory getCategory() {
        return category;
    }

    public void setCategory(IDecisionCategory category) {
        this.category = category;
    }
}
