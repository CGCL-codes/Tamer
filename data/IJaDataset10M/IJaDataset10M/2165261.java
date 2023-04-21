package pcgen.persistence.lst.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;

/**
 * @author wardc
 *
 */
public abstract class AbstractPrerequisiteSimpleParser extends AbstractPrerequisiteParser {

    public Prerequisite parse(String kind, String formula, boolean invertResult, boolean overrideQualify) throws PersistenceLayerException {
        Prerequisite prereq = super.parse(kind, formula, invertResult, overrideQualify);
        prereq.setKey(formula);
        prereq.setOperator(PrerequisiteOperator.EQ);
        if (invertResult) {
            prereq.setOperator(prereq.getOperator().invert());
        }
        return prereq;
    }
}
