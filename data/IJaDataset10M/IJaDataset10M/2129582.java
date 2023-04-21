package plugin.pretokens.parser;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.prereq.AbstractPrerequisiteParser;
import pcgen.persistence.lst.prereq.PrerequisiteParserInterface;

/**
 * @author zaister
 *
 */
public class PreReachParser extends AbstractPrerequisiteParser implements PrerequisiteParserInterface {

    public String[] kindsHandled() {
        return new String[] { "REACH", "REACHEQ", "REACHGT", "REACHGTEQ", "REACHLT", "REACHLTEQ", "REACHNEQ" };
    }

    @Override
    public Prerequisite parse(String kind, String formula, boolean invertResult, boolean overrideQualify) throws PersistenceLayerException {
        Prerequisite prereq = super.parse(kind, formula, invertResult, overrideQualify);
        try {
            prereq.setKind("reach");
            String compType = kind.substring(5);
            if (compType.length() == 0) {
                compType = "gteq";
            }
            prereq.setOperator(compType);
            prereq.setOperand(formula);
            if (invertResult) {
                prereq.setOperator(prereq.getOperator().invert());
            }
        } catch (PrerequisiteException pe) {
            throw new PersistenceLayerException("Unable to parse the prerequisite :'" + kind + ":" + formula + "'. " + pe.getLocalizedMessage());
        }
        return prereq;
    }
}
