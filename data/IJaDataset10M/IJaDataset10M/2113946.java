package org.databene.domain.math;

import org.databene.benerator.NonNullGenerator;
import org.databene.benerator.distribution.LongBasedSequence;
import org.databene.benerator.distribution.Sequence;

/**
 * {@link Sequence}-based implementation of the 
 * <a href="http://en.wikipedia.org/wiki/Padovan_sequence">Padovan Sequence</a><br/>
 * <br/>
 * Created at 03.07.2009 13:14:05
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class PadovanSequence extends LongBasedSequence {

    @Override
    protected NonNullGenerator<Long> createLongGenerator(Long min, Long max, Long granularity, boolean unique) {
        return new PadovanLongGenerator(min, max, unique);
    }
}
