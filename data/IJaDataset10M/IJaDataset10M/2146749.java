package plugin.pretokens.parser;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.prereq.AbstractPrerequisiteParser;
import pcgen.persistence.lst.prereq.PrerequisiteParserInterface;
import java.util.StringTokenizer;

/**
 * @author wardc
 *
 */
public class PreSpellSchoolSubParser extends AbstractPrerequisiteParser implements PrerequisiteParserInterface {

    private static final String prereqKind = "spell.subschool";

    public String[] kindsHandled() {
        return new String[] { "SPELLSCHOOLSUB" };
    }

    public Prerequisite parse(String kind, String formula, boolean invertResult, boolean overrideQualify) throws PersistenceLayerException {
        Prerequisite prereq = super.parse(kind, formula, invertResult, overrideQualify);
        prereq.setKind(prereqKind);
        boolean bError = false;
        final StringTokenizer aTok = new StringTokenizer(formula, ",");
        String aString = aTok.nextToken();
        try {
            Integer.parseInt(aString);
            if (aTok.hasMoreTokens()) {
                Prerequisite subreq;
                prereq.setOperand(aString);
                final int totalTokens = aTok.countTokens();
                if (totalTokens > 1) {
                    prereq.setKind(null);
                }
                while (aTok.hasMoreTokens()) {
                    aString = aTok.nextToken();
                    final int eqIdx = aString.indexOf('=');
                    if (eqIdx < 0) {
                        bError = true;
                        break;
                    }
                    if (totalTokens == 1) {
                        subreq = prereq;
                    } else {
                        subreq = new Prerequisite();
                        prereq.addPrerequisite(subreq);
                        subreq.setKind(prereqKind);
                        subreq.setOperand("1");
                        subreq.setCountMultiples(true);
                    }
                    subreq.setKey(aString.substring(0, eqIdx));
                    subreq.setSubKey(aString.substring(eqIdx + 1));
                    subreq.setOperator(PrerequisiteOperator.GTEQ);
                }
            } else {
                bError = true;
            }
        } catch (NumberFormatException nfe) {
            if (aTok.countTokens() == 2) {
                prereq.setKey(aString);
                prereq.setOperand(aTok.nextToken());
                prereq.setSubKey(aTok.nextToken());
                prereq.setOperator(PrerequisiteOperator.GTEQ);
            } else {
                bError = true;
            }
        }
        if (bError) {
            throw new PersistenceLayerException("PRE" + kindsHandled()[0] + " formula '" + formula + "' is not valid.");
        }
        if (invertResult) {
            prereq.setOperator(prereq.getOperator().invert());
        }
        return prereq;
    }
}
