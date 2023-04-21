package plugin.exporttokens;

import pcgen.core.PlayerCharacter;
import pcgen.core.pclevelinfo.PCLevelInfo;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.SkillToken;
import pcgen.util.Logging;

public class SkillLevelToken extends SkillToken {

    /** token name */
    public static final String TOKEN_NAME = "SKILLLEVEL";

    /**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
    public String getTokenName() {
        return TOKEN_NAME;
    }

    /**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
    public String getToken(String tokenSource, PlayerCharacter pc, ExportHandler eh) {
        SkillDetails details = buildSkillDetails(tokenSource);
        if (details.getProperties().length > 0 && "TOTAL".equals(details.getProperties()[0])) {
            final int aLevelOffset;
            try {
                aLevelOffset = Integer.parseInt(details.getSkillId()) - 1;
                if ((aLevelOffset >= pc.getLevelInfoSize()) || (aLevelOffset < 0)) {
                    return "0";
                }
                final PCLevelInfo wLevelInfo = (PCLevelInfo) pc.getLevelInfo().get(aLevelOffset);
                final int wOutput = wLevelInfo.getSkillPointsGained();
                return Integer.toString(wOutput);
            } catch (NumberFormatException nfe) {
                Logging.errorPrint("Error replacing SKILLLEVEL." + tokenSource, nfe);
                return "";
            }
        }
        return "";
    }
}
