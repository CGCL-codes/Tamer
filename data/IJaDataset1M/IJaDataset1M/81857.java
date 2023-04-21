package pcgen.persistence.lst;

import pcgen.core.Globals;
import pcgen.core.SystemCollections;
import pcgen.core.character.EquipSlot;
import pcgen.persistence.SystemLoader;
import pcgen.util.Logging;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * @author  Jayme Cox <jaymecox@users.sourceforge.net>
 * @version $Revision: 1.22 $
 **/
final class EquipSlotLoader extends LstLineFileLoader {

    /** Creates a new instance of EquipSlotLoader */
    public EquipSlotLoader() {
    }

    /**
	 * @see LstLineFileLoader#parseLine(String, URL)
	 */
    public void parseLine(String lstLine, URL sourceURL) {
        final EquipSlot eqSlot = new EquipSlot();
        final StringTokenizer aTok = new StringTokenizer(lstLine, SystemLoader.TAB_DELIM);
        while (aTok.hasMoreTokens()) {
            final String colString = aTok.nextToken().trim();
            if (lstLine.startsWith("NUMSLOTS:")) {
                final StringTokenizer eTok = new StringTokenizer(lstLine.substring(9), SystemLoader.TAB_DELIM);
                while (eTok.hasMoreTokens()) {
                    final String cString = eTok.nextToken().trim();
                    final StringTokenizer cTok = new StringTokenizer(cString, ":");
                    if (cTok.countTokens() == 2) {
                        final String eqSlotType = cTok.nextToken();
                        final String aNum = cTok.nextToken();
                        Globals.setEquipSlotTypeCount(eqSlotType, aNum);
                    }
                }
            } else if (colString.startsWith("EQSLOT:")) {
                eqSlot.setSlotName(colString.substring(7));
            } else if (colString.startsWith("CONTAINS:")) {
                final StringTokenizer bTok = new StringTokenizer(colString.substring(9), "=");
                if (bTok.countTokens() == 2) {
                    final String aType = bTok.nextToken();
                    final String numString = bTok.nextToken();
                    final int aNum;
                    if (numString.equals("*")) {
                        aNum = 9999;
                    } else {
                        aNum = Integer.parseInt(numString);
                    }
                    eqSlot.setContainType(aType);
                    eqSlot.setContainNum(aNum);
                }
            } else if (colString.startsWith("NUMBER:")) {
                eqSlot.setSlotNumType(colString.substring(7));
            } else {
                Logging.errorPrint("Illegal slot info '" + lstLine + "' in " + sourceURL.toString());
            }
        }
        SystemCollections.addToEquipSlotsList(eqSlot, getGameMode());
    }
}
