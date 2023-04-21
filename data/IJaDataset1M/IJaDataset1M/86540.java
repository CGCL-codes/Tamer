package plugin.exporttokens;

import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.Token;

/**
 * Deal with CR Token
 */
public class CRToken extends Token {

    /** Token Name */
    public static final String TOKENNAME = "CR";

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
        return getCRToken(pc);
    }

    /**
	 * Get CR Token
	 * @param pc
	 * @return CR Token
	 */
    public static String getCRToken(PlayerCharacter pc) {
        String retString = "";
        int cr = pc.calcCR();
        if (cr < 0) {
            retString = "1/";
            cr = -cr;
        }
        return retString + cr;
    }
}
