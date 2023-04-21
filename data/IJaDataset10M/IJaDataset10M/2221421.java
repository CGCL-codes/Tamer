package net.openchrom.chromatogram.msd.model.core.support;

import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.core.IPeak;
import net.openchrom.chromatogram.msd.model.core.ISupplierMassSpectrum;

/**
 * Get the selected chromatogram values. The {@link IChromatogramSelection} represents a selected part of a chromatogram, e.g. to integrate only between
 * a retention time of 10 - 15 minutes.<br/>
 * The selection can also be used to declare a part of a chromatogram, where a
 * filter should be applied.<br/>
 * This interface declares only the getter methods. There is another interface {@link IChromatogramSelectionSetter} which extends this interface and
 * declares the setter methods.<br/>
 * This should prevent from unauthorized manipulation of the chromatogram
 * selection.<br/>
 * Start and stop scan are not provided as they can be calculated by the
 * retention time.
 * 
 * @author eselmeister
 */
public interface IChromatogramSelection {

    /**
	 * Returns the corresponding chromatogram.
	 * 
	 * @return {@link IChromatogram}
	 */
    IChromatogram getChromatogram();

    /**
	 * Returns the start retention time.<br/>
	 * The retention time is stored in milliseconds.
	 * 
	 * @return int
	 */
    int getStartRetentionTime();

    /**
	 * Returns the stop retention time.<br/>
	 * The retention time is stored in milliseconds.
	 * 
	 * @return int
	 */
    int getStopRetentionTime();

    /**
	 * Returns the start abundance.
	 * 
	 * @return float
	 */
    float getStartAbundance();

    /**
	 * Returns the stop abundance.
	 * 
	 * @return float
	 */
    float getStopAbundance();

    /**
	 * Returns the selected scan of the current chromatogram or null, if none is
	 * stored.
	 * 
	 * @return {@link ISupplierMassSpectrum}
	 */
    ISupplierMassSpectrum getSelectedScan();

    /**
	 * Returns the selected peak of the current chromatogram or null, if none is
	 * stored.
	 * 
	 * @return {@link IPeak}
	 */
    IPeak getSelectedPeak();

    /**
	 * Returns a list of selected ions.
	 * 
	 * @return IMarkedIons
	 */
    IMarkedIons getSelectedIons();

    /**
	 * Returns a list of excluded ions.
	 * 
	 * @return IMarkedIons
	 */
    IMarkedIons getExcludedIons();
}
