package jp.seraph.cui;

import java.util.ArrayList;
import java.util.List;
import jp.seraph.cui.message.MessageUtil;

public abstract class AbstractMode extends AbstractNode implements Mode {

    public AbstractMode(Mode aParent, InternalConsole aConsole) {
        super(aParent, aConsole);
        mChildren = new ArrayList<SystemNode>();
    }

    private List<SystemNode> mChildren;

    /**
     *
     * @see jp.seraph.cui.Mode#goTo(java.lang.String)
     */
    public SystemNode goTo(String aName) {
        if (".".equals(aName)) return this;
        if ("..".equals(aName)) return this.getParent();
        for (SystemNode tNode : getChildren()) {
            if (tNode.getName().equals(aName)) return tNode;
        }
        return null;
    }

    /**
     *
     * @see jp.seraph.cui.SystemNode#getChildren()
     */
    public List<SystemNode> getChildren() {
        List<SystemNode> tResult = new ArrayList<SystemNode>(mChildren);
        return tResult;
    }

    /**
     *
     * @see jp.seraph.cui.SystemNode#getChildren(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public <E extends SystemNode> List<E> getChildren(E aDummy) {
        List<E> tResult = new ArrayList<E>();
        Class<? extends SystemNode> tDummyClass = aDummy.getClass();
        for (SystemNode tElement : mChildren) {
            if (tDummyClass.isInstance(tElement)) tResult.add((E) tElement);
        }
        return tResult;
    }

    /**
     *
     * @see jp.seraph.cui.SystemNode#getChildren(java.lang.Class)
     */
    public List<SystemNode> getChildren(Class<? extends SystemNode> aType) {
        List<SystemNode> tResult = new ArrayList<SystemNode>();
        Class<? extends SystemNode> tType = aType;
        for (SystemNode tElement : mChildren) {
            if (tType.isInstance(tElement)) tResult.add(tElement);
        }
        return tResult;
    }

    /**
     *
     * @see jp.seraph.cui.SystemNode#getChildren(jp.seraph.cui.SystemNodeType)
     */
    public List<SystemNode> getChildren(SystemNodeType aType) {
        List<SystemNode> tResult = new ArrayList<SystemNode>();
        SystemNodeType tType = aType;
        for (SystemNode tElement : mChildren) {
            if (tType.isCompatible(tElement.getType())) tResult.add(tElement);
        }
        return tResult;
    }

    public void addChild(SystemNode aNode) {
        for (SystemNode tNode : this.getChildren()) {
            if (tNode.getName().equals(aNode.getName())) throw new IllegalArgumentException(MessageUtil.nodeNameDuplicateError(aNode.getName()));
        }
        mChildren.add(aNode);
        aNode.setParent(this);
    }

    /**
     *
     * @see jp.seraph.cui.Mode#deleteChild(jp.seraph.cui.SystemNode)
     */
    public void deleteChild(SystemNode aChild) {
        mChildren.remove(aChild);
    }

    /**
     *
     * @see jp.seraph.cui.SystemNode#getChild(java.lang.String)
     */
    public SystemNode getChild(String aName) {
        for (SystemNode tNode : this.getChildren()) {
            if (tNode.getName().equals(aName)) return tNode;
        }
        return null;
    }
}
