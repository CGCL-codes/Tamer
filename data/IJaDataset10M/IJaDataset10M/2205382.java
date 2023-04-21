package at.riemers.zero.widgets.tree;

import at.riemers.zero.widgets.tree.TreeAction;

/**
 *
 * @author tobias
 */
public abstract class AbstractTreeAction implements TreeAction {

    private String name;

    private String action;

    public AbstractTreeAction(String name, String action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
