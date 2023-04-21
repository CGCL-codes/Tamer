package org.databene.benerator.distribution;

import org.databene.script.WeightedSample;

/**
 * {@link IndividualWeight} implementation that weighs objects individually. 
 * It requires a WeightedSampleGenerator as source.<br/>
 * <br/>
 * Created at 30.06.2009 18:41:11
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class AttachedWeight<E> extends IndividualWeight<WeightedSample<E>> {

    @Override
    public double weight(WeightedSample<E> object) {
        return object.getWeight();
    }
}
