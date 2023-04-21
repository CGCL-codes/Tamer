package org.argouml.uml.ui.behavior.common_behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;

/**
 *
 */
public class UMLLinkConnectionListModel extends UMLModelElementOrderedListModel2 {

    private static final long serialVersionUID = 4459749162218567926L;

    /**
     * Constructor for UMLInstanceLinkEndListModel.
     */
    public UMLLinkConnectionListModel() {
        super("linkEnd");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getConnections(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().getConnections(getTarget()).contains(element);
    }

    protected void moveDown(int index) {
        Object link = getTarget();
        List c = new ArrayList(Model.getFacade().getConnections(link));
        if (index < c.size() - 1) {
            Collections.swap(c, index, index + 1);
            Model.getCoreHelper().setConnections(link, c);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToBottom(int)
     */
    @Override
    protected void moveToBottom(int index) {
        Object link = getTarget();
        List c = new ArrayList(Model.getFacade().getConnections(link));
        if (index < c.size() - 1) {
            Object mem = c.get(index);
            c.remove(mem);
            c.add(mem);
            Model.getCoreHelper().setConnections(link, c);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToTop(int)
     */
    @Override
    protected void moveToTop(int index) {
        Object link = getTarget();
        List c = new ArrayList(Model.getFacade().getConnections(link));
        if (index > 0) {
            Object mem = c.get(index);
            c.remove(mem);
            c.add(0, mem);
            Model.getCoreHelper().setConnections(link, c);
        }
    }
}
