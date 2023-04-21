package nz.ac.massey.cs.barrio.preferences;

import nz.ac.massey.cs.barrio.Activator;

public abstract class PreferenceRetriver {

    public static String getBarrioFolder() {
        return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.FOLDER_NAME);
    }

    public static String getGraphChoice() {
        return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_CHOICE);
    }
}
