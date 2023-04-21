package server.campaign.operations;

import java.util.Properties;
import java.util.TreeMap;
import common.DefaultOperation;
import common.CampaignData;

public class Operation {

    public static int TYPE_SHORTONLY = 0;

    public static int TYPE_SHORTANDLONG = 1;

    public static int MODS_NOTACCEPTED = 0;

    public static int MODS_ACCEPTED = 1;

    private int type_indicator;

    private int mods_indicator;

    TreeMap<String, ModifyingOperation> modifyingOperations;

    Properties opValues;

    DefaultOperation opsDefaults;

    String opName;

    /**
	 * Operation CONSTRUCTOR. Takes a name (used to assemble
	 * filenames for param loading) and a set of default vals.
	 * 
	 * Operations are constructed in OperationLoader.java
	 */
    public Operation(String opName, DefaultOperation defaults, Properties params) {
        this.opName = opName;
        opsDefaults = defaults;
        type_indicator = Operation.TYPE_SHORTONLY;
        mods_indicator = Operation.MODS_NOTACCEPTED;
        modifyingOperations = new TreeMap<String, ModifyingOperation>();
        opValues = params;
    }

    /**
	 * Method which attempts to look up the value of a given Paramater
	 * in an Operation's local Tree. If the value is unavailable, for
	 * any reason (typo, intentionally unset), a default value is checked
	 * and returned.
	 */
    public String getValue(String valToGet) {
        String toReturn = (String) opValues.get(valToGet);
        if (toReturn == null) toReturn = opsDefaults.getDefault(valToGet);
        if (toReturn == null) {
            CampaignData.mwlog.errLog("Failed getting value \"" + valToGet + "\" from " + this.getName() + " and DefaultOp. Returning null.");
            try {
                throw new Exception();
            } catch (Exception ex) {
                CampaignData.mwlog.errLog(ex);
            }
        }
        return toReturn;
    }

    public boolean getBooleanValue(String valToGet) {
        try {
            return Boolean.parseBoolean(getValue(valToGet));
        } catch (Exception ex) {
            return false;
        }
    }

    public int getIntValue(String valToGet) {
        try {
            return Integer.parseInt(getValue(valToGet));
        } catch (Exception ex) {
            return -1;
        }
    }

    public double getDoubleValue(String valToGet) {
        try {
            return Double.parseDouble(getValue(valToGet));
        } catch (Exception ex) {
            return -1;
        }
    }

    public float getFloatValue(String valToGet) {
        try {
            return Float.parseFloat(getValue(valToGet));
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
	 * Method which adds a mod op to this operation's
	 * tree of valid mods. Set from OperationManager @
	 * load time, drawn from modops' target params.
	 * 
	 * Toggle mods indicator to show that this op does
	 * have potential mods to check for @ startup and
	 * during resolution.
	 */
    public void addModifyingOperation(ModifyingOperation m) {
        modifyingOperations.put(m.getName(), m);
        mods_indicator = Operation.MODS_ACCEPTED;
    }

    /**
	 * Methods which return and set type info via a
	 * boolean (short only, long+short, etc.)
	 */
    public int getTypeIndicator() {
        return type_indicator;
    }

    public void setTypeIndicator(int i) {
        type_indicator = i;
    }

    /**
	 * Methods which set and returns modifier status
	 * (accepts or no-mods, etc)
	 */
    public int getModsIndicator() {
        return mods_indicator;
    }

    public void setModsIndicator(int i) {
        mods_indicator = i;
    }

    /**
	 * Method which returns name of an operation,
	 * as drawn from filename.
	 */
    public String getName() {
        return this.opName;
    }
}
