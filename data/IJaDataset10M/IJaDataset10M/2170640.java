package pcgen.persistence.lst.prereq;

/**
 * @author wardc
 *
 */
public class PreEquippedTwoWeaponParser extends AbstractPrerequisiteListParser implements PrerequisiteParserInterface {

    public String[] kindsHandled() {
        return new String[] { "EQUIPTWOWEAPON" };
    }
}
