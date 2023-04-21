package net.openchrom.chromatogram.msd.ui.swt.internal.components.massspectrum;

import java.util.List;

/**
 * @author Philip (eselmeister) Wenig
 * 
 */
public interface IBarSeriesIons {

    void add(IBarSeriesIon barSeriesIon);

    void clear();

    List<IBarSeriesIon> getIonsWithHighestAbundance(int amount);

    IBarSeriesIon getBarSeriesIon(int index);

    int size();
}
