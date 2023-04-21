package algorithms.dfbnb.openList;

import java.util.Comparator;
import java.util.PriorityQueue;
import algorithms.dfbnb.BudgetedUtilitySearch_ol;
import algorithms.dfbnb.InfNode;
import algorithms.dfbnb.LDSNode;

@SuppressWarnings("serial")
public class LDS_OpenList<E> extends PriorityQueue<InfNode<E>> implements InfOpenList<InfNode<E>> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    protected BudgetedUtilitySearch_ol<E> m_implementation;

    protected int m_tieBreakerType;

    public LDS_OpenList(BudgetedUtilitySearch_ol<E> implementation, int tieBreakerType) {
        super(DEFAULT_INITIAL_CAPACITY, new Comparator<InfNode<E>>() {

            public int compare(InfNode<E> node1, InfNode<E> node2) {
                int eNode1 = ((LDSNode<E>) node1).getErrors();
                int eNode2 = ((LDSNode<E>) node2).getErrors();
                if (eNode1 == eNode2) {
                    double tNode1 = ((LDSNode<E>) node1).getTieBreaker();
                    double tNode2 = ((LDSNode<E>) node2).getTieBreaker();
                    return (int) Math.signum(tNode2 - tNode1);
                } else return eNode1 - eNode2;
            }
        });
        this.m_implementation = implementation;
        this.m_tieBreakerType = tieBreakerType;
    }

    @Override
    public LDSNode<E> getNext() {
        return (LDSNode<E>) poll();
    }

    @Override
    public boolean insert(InfNode<E> newNode, boolean accept) {
        if (accept) newNode.setG(newNode.getGroup().getUtility());
        newNode.setH(m_implementation.getUtilityH(newNode));
        if (!(newNode instanceof LDSNode)) throw new IllegalArgumentException("Node type must be LDSNode");
        switch(m_tieBreakerType) {
            case 0:
                ((LDSNode<E>) newNode).setTieBreaker(newNode.getF());
                break;
            case 1:
                ((LDSNode<E>) newNode).setTieBreaker(newNode.getG());
                break;
        }
        return add((LDSNode<E>) newNode);
    }

    @Override
    public boolean removeItem(InfNode<E> toRemove) {
        return remove(toRemove);
    }

    @Override
    public void updateBestUtility(double groupUtility) {
    }
}
