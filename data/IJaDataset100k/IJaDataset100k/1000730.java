package genetic;

import genetic.component.context.ContextModel;

/**
 *
 * @author Calvin Ashmore
 */
public abstract class BreederAction<T extends GeneticComponent> {

    public abstract T breed(ContextModel model, T target1, T target2);
}
