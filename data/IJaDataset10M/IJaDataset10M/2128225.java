package edu.iastate.enumtree.treevisitor;

public class DeepCoCalculator<T> extends GeneCalculator<T> {

    @Override
    public int getDistance() {
        return m_TotalDeepCo - m_TotalEdges;
    }
}
