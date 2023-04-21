package GA.geneticOperator.Selection;

import Population.Population;

/**
 *
 * @author 郝国生  HAO Guo-Sheng, HAO Guo Sheng, HAO GuoSheng
 */
public class RouletteWheelSelection extends SelectionMediator {

    public RouletteWheelSelection(Population pop) {
        this.scf = new SelectionCommonFunction(pop);
    }
}
