package com.rapidminer.operator.learner.treekernel.kernel.tools;

import edu.stanford.nlp.trees.Tree;

public abstract class AbstractTreeKernelStructure implements KernelStructure<Tree> {

    public abstract int numberOfNodes();

    public void print() {
        System.out.println(getRoot().toString());
    }
}
