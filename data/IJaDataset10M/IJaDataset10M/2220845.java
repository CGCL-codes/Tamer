package plugin.pretokens.parser;

import pcgen.persistence.lst.prereq.AbstractPrerequisiteListParser;
import pcgen.persistence.lst.prereq.PrerequisiteParserInterface;

/**
 * @author wardc
 *
 */
public class PreSpellParser extends AbstractPrerequisiteListParser implements PrerequisiteParserInterface {

    public String[] kindsHandled() {
        return new String[] { "SPELL" };
    }
}
