package plugin.exporttokens;

import pcgen.core.PlayerCharacter;
import pcgen.core.SkillUtilities;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.Token;

public class MaxSkillLevelToken extends Token {

    public static final String TOKENNAME = "MAXSKILLLEVEL";

    /**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
    public String getTokenName() {
        return TOKENNAME;
    }

    /**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
    public String getToken(String tokenSource, PlayerCharacter pc, ExportHandler eh) {
        return getMaxSkillLevelToken(pc) + "";
    }

    public static int getMaxSkillLevelToken(PlayerCharacter pc) {
        return SkillUtilities.maxClassSkillForLevel(pc.getTotalLevels(), pc).intValue();
    }
}
