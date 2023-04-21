package corina.cross;

import corina.Sample;
import corina.core.App;
import corina.ui.I18n;

public class Weiserjahre extends Cross {

    Weiserjahre() {
    }

    public Weiserjahre(Sample fixed, Sample moving) {
        super(fixed, moving);
    }

    public String getFormat() {
        return App.prefs.getPref("corina.cross.weiserjahre.format", "0.0%") + "of 0000";
    }

    public boolean isSignificant(float score, int overlap) {
        return score > 0.65f;
    }

    public float getMinimumSignificant() {
        return 0.65f;
    }

    public String getName() {
        return I18n.getText("weiserjahre");
    }

    private int signifigantcount = 0;

    public int getSignifigant() {
        return signifigantcount;
    }

    public float compute(int offset_fixed, int offset_moving) {
        if (getFixed().count == null || getFixed().incr == null) {
            String problem = "The fixed sample must be a sum,\n" + "with count and Weiserjahre data,\n" + "to run a WJ cross.";
            throw new IllegalArgumentException(problem);
        }
        int i = offset_fixed;
        int j = offset_moving;
        int synchroTrends = 0;
        int totalSigs = 0;
        while (i < getFixed().data.size() - 1 && j < getMoving().data.size() - 1) {
            int n = ((Integer) getFixed().count.get(i)).intValue();
            double pct = ((Number) getFixed().incr.get(i)).doubleValue() / (double) n;
            if (n > 3 && (pct <= 0.25 || pct >= 0.75) && j > 0) {
                totalSigs++;
                int fixedTrend = 0;
                if (pct <= 0.25) fixedTrend = -1; else if (pct >= 0.75) fixedTrend = +1;
                int movingTrend = 0;
                if (((Number) getMoving().data.get(j - 1)).intValue() < ((Number) getMoving().data.get(j)).intValue()) movingTrend = +1; else if (((Number) getMoving().data.get(j - 1)).intValue() > ((Number) getMoving().data.get(j)).intValue()) movingTrend = -1;
                if (fixedTrend == movingTrend) synchroTrends++;
            }
            i++;
            j++;
        }
        signifigantcount = totalSigs;
        if (totalSigs == 0) return 0;
        return (float) synchroTrends / (float) totalSigs;
    }
}
