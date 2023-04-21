package pcgen.core.kit;

import java.io.Serializable;
import java.util.List;
import pcgen.core.Constants;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.Race;
import pcgen.core.Kit;
import pcgen.core.SettingsHandler;

/**
 * Deals with apply RACE via a KIT
 */
public class KitRace extends BaseKit implements Serializable, Cloneable {

    private static final long serialVersionUID = 1;

    private String raceStr = null;

    private transient Race theRace = null;

    /**
	 * Constructor
	 * @param aRace
	 */
    public KitRace(final String aRace) {
        raceStr = aRace;
    }

    /**
	 * Actually applies the race to this PC.
	 *
	 * @param aPC The PlayerCharacter the alignment is applied to
	 */
    public void apply(PlayerCharacter aPC) {
        setPCRace(aPC);
    }

    /**
	 * testApply
	 *
	 * @param aPC PlayerCharacter
	 * @param aKit Kit
	 * @param warnings List
	 * TODO Implement this pcgen.core.kit.BaseKit method
	 */
    public boolean testApply(Kit aKit, PlayerCharacter aPC, List warnings) {
        theRace = null;
        if (Constants.s_NONESELECTED.equals(raceStr)) {
            return false;
        }
        theRace = Globals.getRaceNamed(raceStr);
        if (theRace == null) {
            warnings.add("RACE: Race " + raceStr + " not found.");
            return false;
        }
        setPCRace(aPC);
        return true;
    }

    public Object clone() {
        KitRace aClone = (KitRace) super.clone();
        aClone.raceStr = raceStr;
        return aClone;
    }

    public String getObjectName() {
        return "Race";
    }

    public String toString() {
        return raceStr;
    }

    private void setPCRace(PlayerCharacter aPC) {
        boolean tempShowHP = SettingsHandler.getShowHPDialogAtLevelUp();
        SettingsHandler.setShowHPDialogAtLevelUp(false);
        boolean tempFeatDlg = SettingsHandler.getShowFeatDialogAtLevelUp();
        SettingsHandler.setShowFeatDialogAtLevelUp(false);
        aPC.setRace(theRace);
        SettingsHandler.setShowFeatDialogAtLevelUp(tempFeatDlg);
        SettingsHandler.setShowHPDialogAtLevelUp(tempShowHP);
    }
}
