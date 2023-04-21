package darwin.geneticOperation;

import java.util.ArrayList;
import java.util.List;
import darwin.nodeFilter.*;
import darwin.population.Individual;
import darwin.population.Node;
import darwin.population.TypeMismatchException;

public class HoistMutation implements GeneticOperation {

    @Override
    public int getNumberInputs() {
        return 1;
    }

    @Override
    public Individual[] performOperation(Individual[] inputs, int maxSize, boolean function) throws UnableToPerformOperationException {
        Individual father = inputs[0];
        int choice = (int) (Math.random() * father.getNumTrees());
        Node tree = father.getTree(choice);
        List<Node> yList = new ArrayList<Node>();
        if (function) yList = tree.filterSubtree(new Functions(father.getTreeType(choice)), true);
        if (yList.size() == 0) yList = tree.filterSubtree(new ReturnType(father.getTreeType(choice)), true);
        int random = (int) (Math.random() * yList.size());
        Node yChromosome = yList.get(random);
        try {
            father.setTree(choice, yChromosome);
        } catch (TypeMismatchException e) {
            e.printStackTrace();
        }
        return new Individual[] { father };
    }
}
