package mocapoptions;

import java.util.prefs.Preferences;

/**
 * option parameters for choose frames
 * @author a.heinrich
 */
public final class FramesOptions {

    /** the preferences object */
    private Preferences prefs;

    /** number of markers */
    private String numMarkers;

    public FramesOptions() {
        prefs = Preferences.userNodeForPackage(this.getClass());
        numMarkers = prefs.get("numMarkers", "18");
    }

    /**
     * @return the number of markers
     */
    public String getNumMarkers() {
        return numMarkers;
    }

    /**
     * @param numMarkers the numMarkers to set
     */
    public void setNumMarkers(String numMarkers) {
        this.numMarkers = numMarkers;
        prefs.put("numMarkers", numMarkers);
    }
}
