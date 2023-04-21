package gleam.docservice.proxy.iaa;

import gleam.docservice.proxy.IAAResult;

/**
 * Interface representing the results of an all-ways F-measure IAA
 * calculation.
 */
public interface AllWaysFMeasureIAAResult extends IAAResult {

    /**
   * The overall F-measure for this calculation.
   */
    public FMeasure getOverallFMeasure();

    /**
   * The overall F-measure for a particular annotation set against the
   * key set. If labels are being used, this is the average over all
   * label values of {@link #getFMeasureForLabel}.
   */
    public FMeasure getFMeasure(String asName);

    /**
   * The F-measure for a particular annotation set against the key set
   * for a particular label value. Returns <code>null</code> if labels
   * are not being used or if the given value is not valid.
   */
    public FMeasure getFMeasureForLabel(String asName, String label);
}
