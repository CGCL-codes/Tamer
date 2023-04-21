package VGL;

import java.util.ArrayList;
import GeneticModels.Cage;
import GeneticModels.GeneticModel;

/**
 * data class for passing result of parsed work file
 * from the WorkFileProcessor to VGL
 * @author brian
 *
 */
public class GeneticModelAndCageSet {

    private GeneticModel geneticModel;

    private ArrayList<Cage> cages;

    public GeneticModelAndCageSet(GeneticModel geneticModel, ArrayList<Cage> cages) {
        this.geneticModel = geneticModel;
        this.cages = cages;
    }

    public GeneticModel getGeneticModel() {
        return geneticModel;
    }

    public ArrayList<Cage> getCages() {
        return cages;
    }
}
