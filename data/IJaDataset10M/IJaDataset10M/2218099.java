package info.monitorenter.util.units;

/**
 * A system of units ordered by their natural ascending order.
 * <p>
 *
 * @see info.monitorenter.util.units.AUnit
 *
 * @see info.monitorenter.util.units.UnitFactory
 *
 * @see info.monitorenter.util.units.UnitSystemSI
 *
 * @author <a href='mailto:Achim.Westermann@gmx.de'>Achim Westermann </a>
 *
 * @version $Revision: 1.6 $
 */
public interface IUnitSystem {

    /**
   * Returns the different {@link AUnit} classes in the correct order.
   * <p>
   *
   * @return the different {@link AUnit} classes in the correct order.
   */
    public abstract Class<?>[] getUnits();
}
