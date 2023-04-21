package net.sf.myra.antree;

import static net.sf.myra.antree.NoHeuristic.DEFAULT;
import java.util.HashMap;
import java.util.Map;
import net.sf.myra.antree.representation.Branch;
import net.sf.myra.datamining.data.Attribute;
import net.sf.myra.datamining.data.Dataset;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision$ $Date::                    #$
 */
public class C45Heuristic implements Heuristic {

    private Map<Attribute, Integer> indexes = new HashMap<Attribute, Integer>();

    private Map<Integer, double[]> information = new HashMap<Integer, double[]>();

    public C45Heuristic(Dataset dataset) {
        Initialiser initialiser = new Initialiser(dataset, this);
        initialiser.build();
        int index = 0;
        for (Attribute attribute : dataset.getMetadata().getPredictor()) {
            indexes.put(attribute, index);
            index++;
        }
    }

    public double valueFor(Branch branch, Attribute attribute) {
        double[] values = information.get(branch == null ? null : branch.getCode());
        return (values == null) ? DEFAULT : values[indexes.get(attribute)];
    }

    public void set(Branch branch, double[] values) {
        information.put(branch == null ? null : branch.getCode(), values);
    }
}
