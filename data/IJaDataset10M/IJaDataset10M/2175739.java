package info.monitorenter.util.units;

/**
 * Mega unit, 10 <sup>6 </sup>.
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
public final class UnitMega extends AUnit {

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 4026532760136824163L;

    /**
   * Defcon.
   * <p>
   *
   */
    public UnitMega() {
        this.m_factor = 1000000;
        this.m_unitName = "M";
    }
}
