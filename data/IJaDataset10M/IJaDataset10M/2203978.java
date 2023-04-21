package uk.org.ogsadai.convertors.tuple.kml;

import uk.org.ogsadai.convertors.tuple.kml.LabelTupleToKMLConvertor;

/**
 * OGSA-DAI tuple to KML convertor.
 * 
 * This interface is for classes that construct a KML document which renders
 * markers that present information about a count or summary of some "thing".
 *
 * @author The OGSA-DAI Project Team.
 */
public interface SummaryTupleToKMLConvertor extends LabelTupleToKMLConvertor {

    /**
     * Set column index of the summary field.
     * 
     * @param summaryIndex
     * @throws IllegalArgumentException
     *     If <code>summaryIndex</code> < 0.
     */
    public void setSummaryIndex(int summaryIndex);

    /**
     * Get column index of summary field.
     * 
     * @return index.
     */
    public int getSummaryIndex();
}
