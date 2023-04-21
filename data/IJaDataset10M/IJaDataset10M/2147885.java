package pcgen.persistence.lst;

import java.util.Map;
import java.util.StringTokenizer;
import pcgen.core.Ability;
import pcgen.core.Globals;
import pcgen.core.PObject;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.SystemLoader;
import pcgen.util.Logging;

/**
 *
 * @author  David Rice <david-pcgen@jcuz.com>
 * @version $Revision: 1.12 $
 */
public class AbilityLoader extends LstObjectFileLoader {

    /** Creates a new instance of AbilityLoader */
    public AbilityLoader() {
        super();
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#parseLine(pcgen.core.PObject, java.lang.String, pcgen.persistence.lst.CampaignSourceEntry)
	 */
    public PObject parseLine(PObject target, String lstLine, CampaignSourceEntry source) throws PersistenceLayerException {
        Ability anAbility = (Ability) target;
        if (anAbility == null) {
            anAbility = new Ability();
        } else if (anAbility.getCategory() == null || anAbility.getCategory().length() == 0) {
            anAbility.setCategory("BROKENABILTYNOCATEGORYSET");
        }
        final StringTokenizer colToken = new StringTokenizer(lstLine, SystemLoader.TAB_DELIM);
        int col = 0;
        Map tokenMap = TokenStore.inst().getTokenMap(AbilityLstToken.class);
        while (colToken.hasMoreTokens()) {
            final String colString = colToken.nextToken().trim();
            final int idxColon = colString.indexOf(':');
            String key = "";
            try {
                key = colString.substring(0, idxColon);
            } catch (StringIndexOutOfBoundsException e) {
            }
            AbilityLstToken token = (AbilityLstToken) tokenMap.get(key);
            if (col == 0) {
                anAbility.setName(colString);
                anAbility.setSourceCampaign(source.getCampaign());
                anAbility.setSourceFile(source.getFile());
            } else if (token != null) {
                final String value = colString.substring(idxColon + 1);
                LstUtils.deprecationCheck(token, anAbility, value);
                if (!token.parse(anAbility, value)) {
                    Logging.errorPrint("Error parsing ability " + anAbility.getName() + ':' + source.getFile() + ':' + colString + "\"");
                }
            } else if (PObjectLoader.parseTag(anAbility, colString)) {
                continue;
            } else if (colString.startsWith("ADD:")) {
                anAbility.setAddString(colString.substring(4));
            } else {
                Logging.errorPrint("Unknown tag '" + colString + "' in " + source.getFile());
            }
            ++col;
        }
        finishObject(anAbility);
        return null;
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#getObjectNamed(java.lang.String)
	 */
    protected PObject getObjectNamed(String baseName) {
        return Globals.getAbilityNamed("ALL", baseName);
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#finishObject(pcgen.core.PObject)
	 */
    protected void finishObject(PObject target) {
        if (includeObject(target)) {
            Ability searchFor = (Ability) target;
            final Ability anAbility = Globals.getAbilityKeyed(searchFor.getCategory(), searchFor.getKeyName());
            if (anAbility == null) {
                Globals.addAbility(searchFor);
            }
        }
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#performForget(pcgen.core.PObject)
	 */
    protected void performForget(PObject objToForget) {
        String aCat = ((Ability) objToForget).getCategory();
        String aKey = ((Ability) objToForget).getKeyName();
        Globals.removeAbilityKeyed(aCat, aKey);
    }
}
