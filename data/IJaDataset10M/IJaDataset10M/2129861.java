package pcgen.persistence.lst.prereq;

/**
 * @author wardc
 *
 */
public class PreItemParser extends AbstractPrerequisiteListParser implements PrerequisiteParserInterface {

    public String[] kindsHandled() {
        return new String[] { "ITEM" };
    }
}
