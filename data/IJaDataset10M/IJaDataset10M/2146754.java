package fi.vtt.noen.mfw.bundle.server.plugins.webui.bmreportspage;

import fi.vtt.noen.mfw.bundle.server.shared.datamodel.BMReport;
import java.util.Comparator;

/**
 * @author Teemu Kanstr�n
 */
public class ValueComparator implements Comparator<BMReport> {

    private final String sortKey;

    private final boolean ascending;

    public ValueComparator(String sortKey, boolean ascending) {
        this.sortKey = sortKey;
        this.ascending = ascending;
    }

    public int compare(BMReport result1, BMReport result2) {
        int result = 0;
        if (sortKey.equals("measure_uri")) {
            result = result1.getMeasureURI().compareTo(result2.getMeasureURI());
        }
        if (sortKey.equals("time")) {
            result = result1.getMeasureTime().compareTo(result2.getMeasureTime());
        }
        if (!ascending) {
            result = 0 - result;
        }
        return result;
    }
}
