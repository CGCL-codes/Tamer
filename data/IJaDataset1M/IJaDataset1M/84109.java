package es.gavab.jmh.exact.bb;

import es.gavab.jmh.Instance;
import es.gavab.jmh.Solution;
import es.gavab.jmh.util.ArraysUtil;
import es.gavab.jmh.util.Id;

/**
 * Esta clase implementa el algoritmo de construcción del árbol de Pisinger para problemas de decisión. 
 * Los problemas de decisión no pretenden obtener la solución óptima, sólo devuelven un valor booleano indicando si
 * existe una solución que cumpla unas determinadas características. Esto cambia un poco el esquema de funcionamiento.
 * Es genérica para un límite de m elementos
 * seleccionados. Esto es debido a que no explora
 * soluciones con más de m nodos seleccionados, bien porque están realmente seleccionados o
 * bien porque el número de nodos no seleccionados es igual a (n-m). 
 * @author mica
 *
 */
public class DecisionBinaryTreeBBStandard<S extends Solution<I>, I extends Instance> {

    private DecisionBoundCalcBinaryTreeBBStandard<I> decisionCalc = null;

    protected SolutionManager<S, I> solutionManager = null;

    protected I instance;

    protected int n;

    protected int m;

    protected int numVisitedNodes = 0;

    @SuppressWarnings("unchecked")
    public S execute(I instance) {
        this.instance = instance;
        this.n = solutionManager.getNumNodes(instance);
        this.m = solutionManager.getNumSelectedNodes(instance);
        this.numVisitedNodes = 0;
        this.decisionCalc.setInstance(instance);
        try {
            processNode(0);
            return null;
        } catch (SolutionCalculatedException e) {
            return (S) e.getSolution();
        }
    }

    private void processNode(int numNode) {
        numVisitedNodes++;
        int numFreeNodes = n - decisionCalc.getNumFixedNodes();
        int numSelectedNodes = decisionCalc.getNumSelectedNodes();
        if (numSelectedNodes + numFreeNodes < m + 1) {
            boolean[] selectedNodes = decisionCalc.getSelectedNodes();
            int[] solutionNodes = new int[m];
            int counter = 0;
            for (int i = 0; i < selectedNodes.length; i++) {
                boolean selected = selectedNodes[i];
                if (selected) {
                    solutionNodes[counter] = i;
                    counter++;
                }
            }
            for (int i = decisionCalc.getNumFixedNodes(); i < n; i++) {
                solutionNodes[counter] = i;
                counter++;
            }
            solutionCalculated(solutionNodes);
        } else if (numSelectedNodes == m) {
            solutionCalculated(ArraysUtil.toIntArray(decisionCalc.getSelectedNodes()));
        } else {
            decisionCalc.fixNextNode(true);
            if (!prone()) {
                processNode(numNode + 1);
            }
            decisionCalc.freeLastFixedNode();
            decisionCalc.fixNextNode(false);
            if (!prone()) {
                processNode(numNode + 1);
            }
            decisionCalc.freeLastFixedNode();
        }
    }

    private boolean prone() {
        return this.decisionCalc.prone();
    }

    private void solutionCalculated(int[] solutionNodes) {
        throw new SolutionCalculatedException(solutionManager.createSolution(solutionNodes, instance));
    }

    @Id
    public DecisionBoundCalcBinaryTreeBBStandard<I> getDecisionCalc() {
        return decisionCalc;
    }

    public DecisionBinaryTreeBBStandard<S, I> setBoundCalculator(DecisionBoundCalcBinaryTreeBBStandard<I> boundCalc) {
        this.decisionCalc = boundCalc;
        return this;
    }

    public void setSolutionManager(SolutionManager<S, I> solutionManager) {
        this.solutionManager = solutionManager;
    }
}
