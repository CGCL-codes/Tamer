package org.objectstyle.wolips.ruleeditor.model;

import java.util.Map;

/**
 * @author uli
 */
public class Rule extends AbstractClassElement {

    private static final String RHS_KEY = "rhs";

    private static final String LHS_KEY = "lhs";

    private static final String AUTHOR_KEY = "author";

    private RightHandSide rightHandSide;

    private LeftHandSide leftHandSide;

    public Rule(D2WModel model, Map map) {
        super(model, map);
    }

    public RightHandSide getRightHandSide() {
        if (rightHandSide != null) {
            return rightHandSide;
        }
        Map map = (Map) this.getMap().get(RHS_KEY);
        rightHandSide = new RightHandSide(this.getModel(), map);
        return rightHandSide;
    }

    public void setRightHandSide(RightHandSide rightHandSide) {
        this.getMap().put(RHS_KEY, rightHandSide.getMap());
        this.getModel().setHasUnsavedChanges(true);
    }

    public LeftHandSide getLeftHandSide() {
        if (leftHandSide != null) {
            return leftHandSide;
        }
        Map map = (Map) this.getMap().get(LHS_KEY);
        if (map == null) {
            return null;
        }
        leftHandSide = new LeftHandSide(this.getModel(), map);
        return leftHandSide;
    }

    public void setLeftHandSide(LeftHandSide leftHandSide) {
        this.getMap().put(LHS_KEY, leftHandSide.getMap());
        this.getModel().setHasUnsavedChanges(true);
    }

    public String getPriority() {
        Object priority = this.getMap().get(AUTHOR_KEY);
        if (priority == null) {
            return null;
        }
        return priority.toString();
    }

    public void setPriority(String priority) {
        this.getMap().put(AUTHOR_KEY, priority);
        this.getModel().setHasUnsavedChanges(true);
    }
}
