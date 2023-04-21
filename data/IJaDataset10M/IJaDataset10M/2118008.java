package pcgen.persistence.lst;

import java.net.URL;
import java.util.Map;
import java.util.StringTokenizer;
import pcgen.core.GameMode;
import pcgen.core.SystemCollections;
import pcgen.core.UnitSet;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.SystemLoader;
import pcgen.util.Logging;

/**
 * <code>UnitSetLoader</code>.
 *
 * @author Stefan Radermacher <stefan@zaister.de>
 * @version $Revision: 1.15 $
 */
public final class UnitSetLoader extends LstLineFileLoader {

    /** Creates a new instance of UnitSetLoader */
    public UnitSetLoader() {
    }

    /**
	 * @deprecated This is the old style, to be removed in 5.11.1  
	 */
    public void parseLine(String lstLine, URL sourceURL) {
        Logging.errorPrint("Warning: unitset.lst deprecated. use UNITSET in miscinfo.lst instead (GameMode: " + getGameMode() + ")");
        UnitSet unitSet = null;
        final StringTokenizer aTok = new StringTokenizer(lstLine, "\t");
        int iCount = 0;
        while (aTok.hasMoreElements()) {
            final String colString = (String) aTok.nextElement();
            try {
                switch(iCount) {
                    case 0:
                        unitSet = SystemCollections.getUnitSet(colString, getGameMode());
                        unitSet.setName(colString);
                        break;
                    case 1:
                        unitSet.setHeightUnit(colString);
                        break;
                    case 2:
                        unitSet.setHeightFactor(Double.parseDouble(colString));
                        break;
                    case 3:
                        unitSet.setHeightDisplayPattern(colString);
                        break;
                    case 4:
                        unitSet.setDistanceUnit(colString);
                        break;
                    case 5:
                        unitSet.setDistanceFactor(Double.parseDouble(colString));
                        break;
                    case 6:
                        unitSet.setDistanceDisplayPattern(colString);
                        break;
                    case 7:
                        unitSet.setWeightUnit(colString);
                        break;
                    case 8:
                        unitSet.setWeightFactor(Double.parseDouble(colString));
                        break;
                    case 9:
                        unitSet.setWeightDisplayPattern(colString);
                        break;
                    default:
                        Logging.errorPrint("Unexpected token '" + colString + "' in " + sourceURL.toString());
                        break;
                }
            } catch (NumberFormatException e) {
                Logging.errorPrint("Illegal unit set info '" + lstLine + "' in " + sourceURL.toString());
            }
            iCount += 1;
        }
    }

    public void parseLine(GameMode gameMode, String lstLine) throws PersistenceLayerException {
        StringTokenizer colToken = new StringTokenizer(lstLine, SystemLoader.TAB_DELIM);
        UnitSet unitSet = null;
        Map tokenMap = TokenStore.inst().getTokenMap(UnitSetLstToken.class);
        while (colToken.hasMoreTokens()) {
            final String colString = colToken.nextToken().trim();
            final int idxColon = colString.indexOf(':');
            String key = "";
            try {
                key = colString.substring(0, idxColon);
            } catch (StringIndexOutOfBoundsException e) {
            }
            UnitSetLstToken token = (UnitSetLstToken) tokenMap.get(key);
            if (key.equals("UNITSET")) {
                final String value = colString.substring(idxColon + 1).trim();
                unitSet = SystemCollections.getUnitSet(value, gameMode.getName());
                unitSet.setName(value);
            } else if (token != null) {
                final String value = colString.substring(idxColon + 1).trim();
                LstUtils.deprecationCheck(token, "Unit Set", "miscinfo.lst from the " + gameMode.getName() + " Game Mode", value);
                if (!token.parse(unitSet, value)) {
                    Logging.errorPrint("Error parsing unit set:" + "miscinfo.lst from the " + gameMode.getName() + " Game Mode" + ':' + colString + "\"");
                }
            } else {
                Logging.errorPrint("Invalid sub tag " + token + " on UNITSET line");
                throw new PersistenceLayerException("Invalid sub tag " + token + " on UNITSET line");
            }
        }
    }
}
