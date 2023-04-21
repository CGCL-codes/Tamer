package bitWave.physics;

/**
 * The Atmosphere interface provides access to density measurements in the atmosphere,
 * which are a requisite for drag computations. For information on atmosphere models,
 * consult the links listed on <a href="http://en.wikipedia.org/wiki/Earth%27s_atmosphere">Wikipedia's atmosphere article</a>.
 * @author fw
 */
public interface Atmosphere {

    /**
	 * Calculates atmospheric density in [kg/m3] at the given altitude in [m], based
	 * on the atmosphere's state.
     * @param altitude The altitude in [m] for which the atmospheric density is calculated.
     * @return Atmospheric density at the given altitude in [kg/m^3]
	 */
    double getDensityAtAltitude(final double altitude);
}
