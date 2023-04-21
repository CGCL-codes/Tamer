package pcgen.io.exporttoken;

import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;

/**
 * Deals with HP token
 */
public class HPToken extends Token {

    /** Token name */
    public static final String TOKENNAME = "HP";

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
        return getHPToken(pc) + "";
    }

    /**
	 * Get the HP Token
	 * @param pc
	 * @return the HP Token
	 */
    public static int getHPToken(PlayerCharacter pc) {
        return pc.hitPoints();
    }
}
