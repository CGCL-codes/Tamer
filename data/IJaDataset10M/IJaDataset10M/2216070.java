package net.openchrom.chromatogram.msd.model.internal.xic.comparator;

import java.util.Comparator;
import net.openchrom.chromatogram.msd.model.xic.ITotalIonSignal;

public class TotalIonSignalComparator implements Comparator<ITotalIonSignal> {

    @Override
    public int compare(ITotalIonSignal totalIonSignal1, ITotalIonSignal totalIonSignal2) {
        if (totalIonSignal1 == null || totalIonSignal2 == null) {
            return 0;
        }
        if (totalIonSignal1.getTotalSignal() == totalIonSignal2.getTotalSignal()) {
            return 0;
        }
        return (totalIonSignal1.getTotalSignal() < totalIonSignal2.getTotalSignal()) ? -1 : 1;
    }
}
