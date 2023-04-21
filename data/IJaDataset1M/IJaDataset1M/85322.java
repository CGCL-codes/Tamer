package de.fzi.harmonia.commons.fitnesscalculator;

import java.util.Properties;
import org.semanticweb.owlapi.model.OWLEntity;
import de.fzi.harmonia.commons.basematchers.BaseMatcher;
import de.fzi.harmonia.commons.basematchers.InfeasibleBaseMatcherException;
import de.fzi.kadmos.api.Alignment;
import de.fzi.kadmos.api.Correspondence;

/**
 * Correct dummy base matcher. The base matcher is correct, because it implements
 * the {@link BaseMatcher} interface.
 * 
 * @author bock
 *
 */
public class CorrectMockBaseMatcher implements BaseMatcher {

    @Override
    public void init(Alignment alignment) {
    }

    @Override
    public double getDistance(Correspondence<? extends OWLEntity> correspondence) throws InfeasibleBaseMatcherException {
        return 0;
    }

    @Override
    public <T extends OWLEntity> double getDistance(T entity1, T entity2) throws InfeasibleBaseMatcherException {
        return 0;
    }

    @Override
    public void setParameters(Properties params) {
    }
}
