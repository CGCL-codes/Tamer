package net.sf.echopm.panel.editor.tree;

import net.sf.echopm.navigation.event.EventDispatcher;
import echopointng.tree.MutableTreeNode;

/**
 * @author ron
 */
public class EditorTreeDragAndDropNodeFactoryDecorator implements EditorTreeNodeFactory {

    private final EditorTreeNodeFactory decoratedFactory;

    /**
	 * @param decoratedFactory
	 */
    public EditorTreeDragAndDropNodeFactoryDecorator(EditorTreeNodeFactory decoratedFactory) {
        super();
        this.decoratedFactory = decoratedFactory;
    }

    /**
	 * @see net.sf.echopm.panel.editor.tree.EditorTreeNodeFactory#createTreeNode(net.sf.echopm.navigation.event.EventDispatcher,
	 *      net.sf.echopm.panel.editor.tree.EditorTree, java.lang.Object)
	 */
    public MutableTreeNode createTreeNode(EventDispatcher eventDispatcher, EditorTree tree, Object object) {
        return new EditorTreeNodeDragAndDropDecorator(decoratedFactory.createTreeNode(eventDispatcher, tree, object), tree);
    }

    /**
	 * @see net.sf.echopm.panel.editor.tree.EditorTreeNodeFactory#getTargetType()
	 */
    public Class<?> getTargetType() {
        return decoratedFactory.getTargetType();
    }
}
