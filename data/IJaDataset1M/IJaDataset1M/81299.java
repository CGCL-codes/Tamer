package com.rapidminer.operator.learner.treekernel.tools.jnisvmlight;

import com.rapidminer.operator.learner.treekernel.kernel.tools.AbstractTreeKernelStructure;

/**
 * A labeled feature vector.
 * 
 * @author Felix Jungermann
 */
public class LabeledTreeFeatureVector extends LabeledFeatureVector implements java.io.Serializable {

    TreeKernel tk = null;

    /**
	 * 
	 */
    private static final long serialVersionUID = 4700114146045337111L;

    protected LabeledTreeFeatureVector() {
        this(null, 0, null, null);
    }

    public LabeledTreeFeatureVector(AbstractTreeKernelStructure t, double label, int size) {
        super(label, size);
        this.m_tree = t;
    }

    public LabeledTreeFeatureVector(AbstractTreeKernelStructure t, double label, int[] dims, double[] vals) {
        super(label, dims, vals);
        this.m_tree = t;
    }

    public double evaluate(LabeledTreeFeatureVector b) {
        if (tk == null) tk = new TreeKernel();
        return tk.evaluate(this, b);
    }

    @Override
    public void setTree(LabeledFeatureVector t) {
        this.m_tree = ((LabeledTreeFeatureVector) t).getTree();
    }

    @Override
    public AbstractTreeKernelStructure getTree() {
        return m_tree;
    }

    public String getTreeString() {
        return m_tree.getRoot().toString();
    }

    @Override
    public void getString() {
        try {
            System.out.println(m_tree.getRoot().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
