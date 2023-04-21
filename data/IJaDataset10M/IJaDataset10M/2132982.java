package org.rubypeople.rdt.refactoring.core.generateaccessors;

import java.util.ArrayList;
import java.util.Collection;
import org.jruby.ast.BlockNode;
import org.jruby.ast.FCallNode;
import org.jruby.ast.Node;
import org.rubypeople.rdt.refactoring.core.NodeFactory;
import org.rubypeople.rdt.refactoring.editprovider.InsertEditProvider;
import org.rubypeople.rdt.refactoring.nodewrapper.AttrAccessorNodeWrapper;
import org.rubypeople.rdt.refactoring.nodewrapper.ClassNodeWrapper;
import org.rubypeople.rdt.refactoring.nodewrapper.VisibilityNodeWrapper;
import org.rubypeople.rdt.refactoring.offsetprovider.IOffsetProvider;

public class GeneratedAccessor extends InsertEditProvider {

    public static final int TYPE_SIMPLE_ACCESSOR = 1;

    public static final int TYPE_METHOD_ACCESSOR = 2;

    public static final int DEFAULT_TYPE = TYPE_SIMPLE_ACCESSOR;

    public String definitionName;

    private int type;

    private String attrName;

    private ClassNodeWrapper classNode;

    public GeneratedAccessor(String definitionName, String instVarName, int type, ClassNodeWrapper classNode) {
        super(true);
        this.definitionName = definitionName;
        this.attrName = instVarName;
        this.type = type;
        this.classNode = classNode;
    }

    /**
	 * Visibility changed from private to public for testing.
	 */
    public boolean isWriter() {
        return definitionName.equals(AttrAccessorNodeWrapper.ATTR_WRITER);
    }

    /**
	 * Visibility changed from private to public for testing.
	 */
    public boolean isReader() {
        return definitionName.equals(AttrAccessorNodeWrapper.ATTR_READER);
    }

    /**
	 * Visibility changed from private to public for testing.
	 */
    public boolean isAccessor() {
        return definitionName.equals(AttrAccessorNodeWrapper.ATTR_ACCESSOR);
    }

    protected BlockNode getInsertNode(int offset, String document) {
        boolean needsNewLineAtEndOfBlock = lastEditInGroup && !isNextLineEmpty(offset, document);
        if (type == TYPE_SIMPLE_ACCESSOR) {
            return NodeFactory.createBlockNode(needsNewLineAtEndOfBlock, getSimpleInsertNode());
        }
        return NodeFactory.createBlockNode(needsNewLineAtEndOfBlock, getMethodInsertNode());
    }

    private Node getSimpleInsertNode() {
        FCallNode accessorNode = NodeFactory.createSimpleAccessorNode(definitionName, attrName);
        return NodeFactory.createNewLineNode(accessorNode);
    }

    private Node[] getMethodInsertNode() {
        Collection<Node> methodNodes = new ArrayList<Node>();
        if (isReader() || isAccessor()) methodNodes.add(NodeFactory.createGetterSetter(attrName, false, VisibilityNodeWrapper.METHOD_VISIBILITY.PUBLIC));
        if (isAccessor()) {
            methodNodes.add(NodeFactory.createNewLineNode(null));
        }
        if (isWriter() || isAccessor()) methodNodes.add(NodeFactory.createGetterSetter(attrName, true, VisibilityNodeWrapper.METHOD_VISIBILITY.PUBLIC));
        return methodNodes.toArray(new Node[methodNodes.size()]);
    }

    protected int getOffset(String document) {
        IOffsetProvider offsetProvider = new AccessorOffsetProvider(classNode, type, document);
        return offsetProvider.getOffset();
    }

    public String getInstVarName() {
        return attrName;
    }
}
