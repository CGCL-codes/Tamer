package info.monitorenter.util.units;

/**
 * Peta unit, 10 <sup>15 </sup>.
 * <p>
 *
 * @see info.monitorenter.util.units.AUnit
 *
 * @see info.monitorenter.util.units.UnitFactory
 *
 * @see info.monitorenter.util.units.IUnitSystem
 *
 * @see info.monitorenter.util.units.UnitSystemSI
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.4 $
 */
public final class UnitPeta extends AUnit {

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -2800158957584369273L;

    /**
   * Defcon.
   * <p>
   *
   */
    public UnitPeta() {
        this.m_factor = 1000000000000000d;
        this.m_unitName = "P";
    }
}
