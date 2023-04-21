package mil.army.usace.ehlschlaeger.digitalpopulations;

import java.util.Map;
import mil.army.usace.ehlschlaeger.rgik.core.CumulativeDistributionFunction;

/**
 * Produce a sequence of realizations from a sequence of archtypes. An iterator
 * is fed to the constructor, and next() will produce realizations in every
 * region requested by each archtype.
 * <P>
 * This is essentially obsolete; {@link ConstrainedRealizer} will build and
 * cache CDF maps automatically, and otherwise does everything this class does.
 * 
 * @author William R. Zwicky
 */
public class CDFRealizer extends Realizer {

    protected Map<Integer, CumulativeDistributionFunction> cdfMaps;

    /**
     * Construct instance.
     * 
     * @param cdfMaps
     *            map of region code to CumulativeDistributionFunction. These
     *            will be used to place households that want to goto into a
     *            certain region.
     */
    public CDFRealizer(Map<Integer, CumulativeDistributionFunction> cdfMaps) {
        this.cdfMaps = cdfMaps;
    }

    /**
     * Construct realization of an archtype. A "realization" is a household with
     * easting and northing, which inherits its attributes from an archtype.
     * This method constructs a new realization instance, and gives it a
     * location based on descriptors held by the archtype.
     * 
     * @param arch
     *            archtype to realize
     * @param which
     *            index into archtype's array of descriptors. Also serves as a
     *            unique ID for this archtype's realizations.
     * 
     * @return new realization
     */
    protected PumsHouseholdRealization realize(PumsHousehold arch, int which) {
        PumsHouseholdRealization rzn = new PumsHouseholdRealization(arch, which, 0, 0);
        CumulativeDistributionFunction cdf = cdfMaps.get(arch.getRealizationTract(which));
        cdf.locateRandomly(rzn, random);
        return rzn;
    }
}
