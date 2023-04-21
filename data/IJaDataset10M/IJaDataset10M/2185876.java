package nl.alterra.openmi.sdk.backbone;

import junit.framework.TestCase;
import org.openmi.standard.IQuantity;
import org.openmi.standard.IDimension;

public class TestQuantity extends TestCase {

    Quantity quantity;

    public void setUp() {
        quantity = new Quantity(new Unit("UnitID", 1.0, 0.0, "Description"), "Description", "ID", IQuantity.ValueType.Scalar, new Dimension());
    }

    public void testConstructor() {
        Quantity quantity2 = new Quantity(quantity);
        assertTrue(quantity.describesSameAs(quantity2));
    }

    public void testID() {
        assertEquals("ID", quantity.getID());
        quantity.setID("new");
        assertEquals("new", quantity.getID());
    }

    public void testDescription() {
        assertEquals("Description", quantity.getDescription());
        quantity.setDescription("new");
        assertEquals("new", quantity.getDescription());
    }

    public void testUnit() {
        assertTrue(new Unit("UnitID", 1.0, 0.0, "Description").equals(quantity.getUnit()));
        quantity.setUnit(new Unit("UnitID2", 1.0, 0.0, "Description"));
        assertTrue(new Unit("UnitID2", 1.0, 0.0, "Description").equals(quantity.getUnit()));
    }

    public void testValuetype() {
        assertEquals(IQuantity.ValueType.Scalar, quantity.getValueType());
        quantity.setValueType(IQuantity.ValueType.Vector);
        assertEquals(IQuantity.ValueType.Vector, quantity.getValueType());
    }

    public void testEquals() {
        Quantity quantity2 = new Quantity(new Unit("UnitID", 1.0, 0.0, "Description"), "Description", "ID", IQuantity.ValueType.Scalar, new Dimension());
        assertTrue(quantity.describesSameAs(quantity2));
    }

    public void testEqualsUnit() {
        Quantity quantity2 = new Quantity(new Unit("UnitID2", 1.0, 0.0, "Description"), "Description", "ID", IQuantity.ValueType.Scalar, new Dimension());
        assertTrue(quantity.equals(quantity2));
    }

    public void testEqualsDescription() {
        Quantity quantity2 = new Quantity(new Unit("UnitID", 1.0, 0.0, "Description"), "Description2", "ID", IQuantity.ValueType.Scalar, new Dimension());
        assertFalse(quantity.equals(quantity2));
    }

    public void testEqualsID() {
        Quantity quantity2 = new Quantity(new Unit("UnitID", 1.0, 0.0, "Description"), "Description", "ID2", IQuantity.ValueType.Scalar, new Dimension());
        assertFalse(quantity.equals(quantity2));
    }

    public void testEqualsValueType() {
        Quantity quantity2 = new Quantity(new Unit("UnitID", 1.0, 0.0, "Description"), "Description", "ID", IQuantity.ValueType.Vector, new Dimension());
        assertFalse(quantity.equals(quantity2));
    }

    public void testRealQuantities() {
        Dimension speed = new Dimension();
        speed.setPower(IDimension.DimensionBase.Length, 1);
        speed.setPower(IDimension.DimensionBase.Time, -1);
        Quantity velocity1 = new Quantity(new Unit("m/s", 1.0, 0.0), "Velocity", "Velocity", IQuantity.ValueType.Scalar, speed);
        Quantity velocity2 = new Quantity(new Unit("km/h", 0.277778, 0.0), "Velocity", "Velocity", IQuantity.ValueType.Scalar, speed);
        assertTrue(velocity1.equals(velocity2));
    }
}
