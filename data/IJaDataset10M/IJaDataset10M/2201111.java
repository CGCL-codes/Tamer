package plugin.exporttokens;

import pcgen.core.PCClass;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.SpellListToken;

/**
 * <code>SpellListMemorizeToken</code> outputs "true" if the specified spell class
 * needs to memorize spells, "false" otherwise.
 *
 * Last Editor: $Author: jdempsey $
 * Last Edited: $Date: 2005/10/16 13:13:15 $
 *
 * @author Stefan Radermacher <zaister@users.sourceforge.net>
 * @version $Revision: 1.4 $
 */
public class SpellListMemorizeToken extends SpellListToken {

    /** Token name */
    public static final String TOKENNAME = "SPELLLISTMEMORIZE";

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
        StringBuffer retValue = new StringBuffer();
        SpellListTokenParams params = new SpellListTokenParams(tokenSource, SpellListToken.SPELLTAG_MEMORIZE);
        final PObject aObject = pc.getSpellClassAtIndex(params.getClassNum());
        if (aObject != null) {
            PCClass aClass = null;
            if (aObject instanceof PCClass) {
                aClass = (PCClass) aObject;
            }
            if (aClass != null) {
                retValue.append(aClass.getMemorizeSpells());
            }
        }
        return retValue.toString();
    }
}
