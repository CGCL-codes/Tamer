package jaxlib.unit;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: DerivedUnit.java 2594 2008-04-21 23:22:57Z joerg_wassmer $
 */
public abstract class DerivedUnit<Q> extends Unit<Q> {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    DerivedUnit(final Dimension dimension) {
        super(dimension);
    }
}
