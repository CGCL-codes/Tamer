package org.uithin.nodes.logic;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.uithin.AbstractRegistry;
import org.uithin.nodes.Node;
import org.uithin.nodes.unit.LogicLayer;
import zz.utils.SimpleAction;

public abstract class AbstractLogicNodeRegistry extends AbstractRegistry<LogicNode> {

    /**
	 * Returns a list of actions that create ui nodes of each registered class
	 */
    public List<Action> getCreationActions(final LogicLayer aLogicLayer, final Rectangle aBounds) {
        List<ClassInfo<LogicNode>> theClasses = getClasses();
        List<Action> theActions = new ArrayList<Action>();
        for (ClassInfo<LogicNode> theClassInfo : theClasses) {
            final ClassInfo<LogicNode> f = theClassInfo;
            theActions.add(new SimpleAction(theClassInfo.name) {

                public void actionPerformed(ActionEvent aE) {
                    try {
                        LogicNode theNode = Node.create(f.clazz, aLogicLayer.getUnit());
                        aLogicLayer.addNode(theNode, aBounds);
                        aLogicLayer.getUnit().pChildren.add(theNode);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return theActions;
    }
}
