package jaxlib.unit;

import jaxlib.math.DecimalConst;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: UnitConstants.java 3002 2011-10-18 00:11:08Z joerg_wassmer $
 */
public final class UnitConstants extends Object {

    private UnitConstants() throws InstantiationException {
        throw new InstantiationException();
    }

    static final double daysPerMonth = 30.4375;

    static final double daysPerYear = 365.2425;

    static final DecimalConst metersPerAngstrom = DecimalConst.intern(1E-10);

    static final DecimalConst metersPerAstronomicalUnit = DecimalConst.intern(149597870660L);

    static final DecimalConst metersPerLightYear = DecimalConst.intern(9460536207068016L);

    static final DecimalConst metersPerNauticMile = DecimalConst.intern(1852L);

    static final DecimalConst metersPerParsec = DecimalConst.intern(3.0856775807e16);

    static final DecimalConst metersPerYard = DecimalConst.intern(0.9144);

    /**
   * The current best estimate of this number as of 2007-06-22 is
   * <tt>(6.0221415 ± 0.0000010) × 10<sup>23</sup></tt>.<br/>
   * The Avogadro constant, also called the Avogadro number and, in German scientific literature, sometimes
   * also known as the Loschmidt constant/number, is formally defined to be the number of "entities" in one
   * mole, that is the number of carbon-12 atoms in 12 grams (0.012 kg) of unbound carbon-12 in its ground
   * state.
   * <p>
   * <h3>Note</h3><br/>
   *  In future versions of this class this constant will be updated according to the scientific
   *  publications.
   * </p>
   *
   * @see <a href="http://www.americanscientist.org/template/AssetDetail/assetid/54773">
   *  American Scientist: An Exact Value for Avogadro's Number</a>
   * @see <a href="http://www.wikipedia.org/wiki/Avogadro's_number">Wikipedia: Avogadros number</a>
   * @see Mass#u
   *
   * @since JaXLib 1.0
   */
    public static final DecimalConst AVOGADRO = DecimalConst.intern(6.0221415e23);

    /**
   * The inverse of the {@link #AVOGADRO} constant as used in this library:
   * <tt>1.6605389 × 10<sup>-24</sup></tt>.
   * <p>
   * <h3>Note</h3><br/>
   *  In future versions of this class this constant will be updated according to the scientific
   *  publications.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final DecimalConst AVOGADRO_INV = DecimalConst.intern(1.6605389e-24);
}
