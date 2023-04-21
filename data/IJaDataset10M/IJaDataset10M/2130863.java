package algorithms.dfbnb;

import java.util.Stack;

/**
 *
 * @author Matt
 */
public class BruteForceUtilitySearch<E> extends AbsSearch<E> {

    protected Stack<InfNode<E>> m_openList;

    public BruteForceUtilitySearch(InfNode<E> root) {
        super(root);
        m_openList = new Stack<InfNode<E>>();
        m_openList.push(m_root);
    }

    @Override
    protected boolean isPruned(InfNode<E> treeNode) {
        return false;
    }

    @Override
    public boolean isFeasible(InfGroup<E> group) {
        return true;
    }

    @Override
    public boolean isBest(InfGroup<E> group) {
        return (group.getUtility() > m_bestGroup.getUtility());
    }

    @Override
    protected boolean offerNode(InfNode<E> node, boolean accept) {
        m_openList.push(node);
        return true;
    }

    @Override
    protected InfNode<E> pullNode() {
        return m_openList.pop();
    }

    @Override
    public boolean isSearchDone() {
        return m_openList.isEmpty();
    }

    @Override
    public InfGroup<E> getCurrentGroup() {
        InfNode<E> m_currentNode = m_openList.peek();
        return m_currentNode.getGroup();
    }
}
