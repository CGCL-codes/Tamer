package plugin.converter;

import pcgen.rules.persistence.token.AbstractPreEqualConvertPlugin;

public class PreSkillInvertedConvertPlugin extends AbstractPreEqualConvertPlugin {

    public String getProcessedToken() {
        return "!PRESKILL";
    }
}
