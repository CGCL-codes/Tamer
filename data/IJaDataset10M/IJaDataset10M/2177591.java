package mars4stars.testing;

import mars4stars.*;
import org.jm.utils.testing.BitMaskImmutableTest;

/**
 * Junit TestCase for class ShipType
 * 
 * <br><br>Copyright (C) 2004 - Licenced under the GNU GPL
 * @author James McGuigan
 */
public class ShipTypeTest extends BitMaskImmutableTest {

    Universe uni = new Universe();

    /**
	 * Class under test for void ShipType()
	 */
    public final void testShipType() {
        assertNotNull(new ShipType());
    }

    /**
	 * Class under test for void ShipType(BitMaskImmutable)
	 */
    public final void testShipTypeBitMaskImmutable() {
        super.testBitMaskImmutableBitMask();
    }

    /**
	 * Class under test for void ShipType(int)
	 */
    public final void testShipTypeint() {
        super.testBitMaskImmutableint();
    }

    public final void testIsArmed() {
        assertEquals(true, (new ShipType(ShipType.ARMED).isArmed()));
        assertEquals(true, (new ShipType(0xFFFF).isArmed()));
        assertEquals(false, (new ShipType(ShipType.STARBASE).isArmed()));
    }

    public final void testIsUnarmed() {
        assertEquals(false, (new ShipType(ShipType.ARMED).isUnarmed()));
        assertEquals(false, (new ShipType(0xFFFF).isUnarmed()));
        assertEquals(true, (new ShipType(ShipType.STARBASE).isUnarmed()));
    }

    public final void testIsBomber() {
        assertEquals(true, (new ShipType(ShipType.BOMBER).isBomber()));
        assertEquals(true, (new ShipType(0xFFFF).isBomber()));
        assertEquals(false, (new ShipType(ShipType.STARBASE).isBomber()));
    }

    public final void testIsFreighter() {
        assertEquals(true, (new ShipType(ShipType.FREIGHTER).isFreighter()));
        assertEquals(true, (new ShipType(0xFFFF).isFreighter()));
        assertEquals(false, (new ShipType(ShipType.STARBASE).isFreighter()));
    }

    public final void testIsFuelTransport() {
        assertEquals(true, (new ShipType(ShipType.FREIGHTER).isFreighter()));
        assertEquals(true, (new ShipType(0xFFFF).isFreighter()));
        assertEquals(false, (new ShipType(ShipType.STARBASE).isFreighter()));
    }

    public final void testIsStarbase() {
        assertEquals(true, (new ShipType(ShipType.STARBASE).isStarbase()));
        assertEquals(true, (new ShipType(0xFFFF).isStarbase()));
        assertEquals(false, (new ShipType(ShipType.FREIGHTER).isStarbase()));
    }
}
