package pcgen.persistence.lst.prereq;

/**
 * @author wardc
 *
 */
public class PreRegionParser extends AbstractPrerequisiteSimpleParser implements PrerequisiteParserInterface {

    public String[] kindsHandled() {
        return new String[] { "REGION" };
    }
}
