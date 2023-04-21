package mil.army.usace.ehlschlaeger.rgik.core;

import mil.army.usace.ehlschlaeger.digitalpopulations.PumsHousehold;

/**
 * Evaluate the quality of the location of a point. A value of zero indicates
 * optimal placement. Precise location is not examined; rather, only the
 * containing tract is important.
 * <p>
 * Copyright <a href="http://faculty.wiu.edu/CR-Ehlschlaeger2/">Charles R.
 * Ehlschlaeger</a>, work: 309-298-1841, fax: 309-298-3003, This software is
 * freely usable for research and educational purposes. Contact C. R.
 * Ehlschlaeger for permission for other purposes. Use of this software requires
 * appropriate citation in all published and unpublished documentation.
 */
public interface TractSpatialStatistic extends SpatialStatistic {

    /**
     * Modifies spatial statistic based on creating a point at some unspecified
     * location within a tract.
     * 
     * @param house archtype for household that was added
     * @param tractID code for tract into which this point has been placed
     * @param mapNumber which sub-map received this house
     */
    public abstract void modifySS4NewPt(PumsHousehold house, int tractID, int mapNumber);

    /**
     * Modifies spatial statistic based on removing a point from a tract.
     * 
     * @param house archtype for household that was added
     * @param tractID code for tract into which this point has been placed
     * @param mapNumber which sub-map received this house
     */
    public abstract void modifySS4RemovedPt(PumsHousehold house, int tractID, int mapNumber);

    /**
     * Return average difference in counts of "interesting" objects over all
     * tracts in our map(s). "Interesting" is defined by the implementation.
     * 
     * @param statistic
     *            other object to compare against. In general, should be of the
     *            same class as the one being called.
     * @return value indicating average difference
     */
    public abstract double averageOff(TractSpatialStatistic statistic);
}
