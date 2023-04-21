package net.openchrom.chromatogram.msd.integrator.core.settings;

import net.openchrom.chromatogram.msd.model.core.support.IBaselineModel;

public interface IBaselineSupport {

    /**
	 * Holds the baseline from the start to the stop retention time at the level
	 * of the start retention time.<br/>
	 * Other values of baseline support will may be overridden.
	 * 
	 * @param startRetentionTime
	 * @param stopRetentionTime
	 */
    void setBaselineHoldOn(int startRetentionTime, int stopRetentionTime);

    /**
	 * Holds the baseline over the whole chromatogram a the level of the given
	 * retention time.<br/>
	 * Other values of baseline support will may be overridden.
	 * 
	 * @param retentionTime
	 */
    void setBaselineNow(int retentionTime);

    /**
	 * Uses the baseline at the given retention time only from the retention
	 * time backwards.<br/>
	 * Other values of baseline support will may be overridden.
	 * 
	 * @param retentionTime
	 */
    void setBaselineBack(int retentionTime);

    /**
	 * If a new baseline model will be stored, all previous changes will be
	 * discarded.
	 * 
	 * @param baselineModel
	 */
    void setBaselineModel(IBaselineModel baselineModel);

    /**
	 * Returns the baseline model.
	 * 
	 * @return IBaselineModel
	 */
    IBaselineModel getBaselineModel();

    /**
	 * Sets the baseline to the default value.
	 */
    void reset();

    /**
	 * Returns the baseline at the given retention time corrected by the given
	 * baseline support settings.
	 * 
	 * @param retentionTime
	 * @return float
	 */
    float getBackgroundAbundance(int retentionTime);
}
