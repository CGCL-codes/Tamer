package com.rapidminer.operator.learner.treekernel.kernel;

import java.util.Iterator;
import com.rapidminer.example.table.struct.tree.stanford.Production;
import com.rapidminer.operator.learner.stringkernel.svm.examples.SVMExamplesNLP;
import com.rapidminer.operator.learner.treekernel.kernel.tools.AbstractTreeKernelStructure;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.Tools;
import edu.stanford.nlp.trees.Tree;

/**
 * Tree Kernel as published by Collins and Duffy
 * 
 * @author Stefan Rueping, Ingo Mierswa, Felix Jungermann
 */
public class KernelCollins extends KernelStruct {

    private double lambda = 1.0d;

    private static final long serialVersionUID = -6384697098131949237L;

    /** Class constructor */
    public KernelCollins() {
    }

    /** Output as String */
    @Override
    public String toString() {
        return ("tree");
    }

    /**
	 * Class constructor
	 * 
	 * @param examples
	 *            Container for the examples.
	 */
    public KernelCollins(SVMExamplesNLP examples, int cacheSize) {
        init(examples, cacheSize);
    }

    @Override
    public String getDistanceFormula(double[] x, String[] attributeConstructions) {
        StringBuffer result = new StringBuffer();
        boolean first = true;
        for (int i = 0; i < x.length; i++) {
            double value = x[i];
            if (!Tools.isZero(value)) {
                if (value < 0.0d) {
                    if (first) result.append("-" + Math.abs(value) + " * " + attributeConstructions[i]); else result.append(" - " + Math.abs(value) + " * " + attributeConstructions[i]);
                } else {
                    if (first) result.append(value + " * " + attributeConstructions[i]); else result.append(" + " + value + " * " + attributeConstructions[i]);
                }
                first = false;
            }
        }
        return result.toString();
    }

    public void setParameters(double lambda) {
        this.lambda = lambda;
    }

    @Override
    public double calculate_K(AbstractTreeKernelStructure x, AbstractTreeKernelStructure y) {
        double out = getCollinsDuffy(x, y);
        return out;
    }

    private double getCollinsDuffy(AbstractTreeKernelStructure x, AbstractTreeKernelStructure y) {
        Iterator<Tree> iterX = x.iterator();
        Iterator<Tree> iterY = y.iterator();
        Tree tempY = null;
        Tree tempX = null;
        double out = 0.0d;
        if (iterY.hasNext()) tempY = iterY.next();
        while (iterX.hasNext()) {
            tempX = iterX.next();
            if (!tempX.isLeaf() && !tempY.isLeaf()) {
                out += calculate_C(tempX, tempY);
            }
            if (!iterX.hasNext()) {
                if (iterY.hasNext()) {
                    iterX = x.iterator();
                    tempY = iterY.next();
                } else break;
            }
        }
        return out;
    }

    private double calculate_C(Tree a, Tree b) {
        if (!a.value().equals(b.value())) return 0.0d; else {
            Production aProd = new Production(a);
            Production bProd = new Production(b);
            if (!aProd.equals(bProd)) return 0.0d; else {
                if (a.isPreTerminal() && b.isPreTerminal()) return lambda; else {
                    double out = 1.0d;
                    for (int j = 0; j < aProd.size(); j++) {
                        out *= (1.0d + calculate_C(a.getChild(j), b.getChild(j)));
                    }
                    return lambda * out;
                }
            }
        }
    }

    @Override
    public double calculate_K(int[] xIndex, double[] xAtt, int[] yIndex, double[] yAtt) {
        LogService.getGlobal().log("This method should not be called!", LogService.FATAL);
        return 0;
    }
}

;
