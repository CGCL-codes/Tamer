package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.NumberType;
import com.volantis.shared.metadata.type.UnitType;
import com.volantis.shared.metadata.type.MetaDataTypeFactory;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.type.mutable.MutableQuantityType;
import com.volantis.shared.metadata.type.mutable.MutableNumberType;
import com.volantis.shared.metadata.type.mutable.MutableUnitType;
import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.value.mutable.MutableQuantityValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.QuantityUnits;
import java.util.Collection;
import java.util.List;

/**
 * Test case for {@link QuantityTypeImpl}.
 */
public class QuantityTypeImplTestCase extends MetaDataTypeImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test.
     */
    public QuantityTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    protected MutableInhibitor getMutableInhibitor() {
        return new MutableQuantityTypeImpl();
    }

    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableQuantityTypeImpl();
    }

    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableQuantityType fieldDefinition1 = createMutableUnitTypeForTestingEquals();
        MutableQuantityType fieldDefinition2 = createMutableUnitTypeForTestingEquals();
        assertEquals("Object 1 should be equal to object 2", fieldDefinition1, fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesEqual(fieldDefinition1, fieldDefinition2);
        fieldDefinition2.setMagnitudeType(null);
        MetaDataTestCaseHelper.assertNotEquals(fieldDefinition1, fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(fieldDefinition1, fieldDefinition2);
        fieldDefinition2 = createMutableUnitTypeForTestingEquals();
        fieldDefinition2.setUnitType(null);
        MetaDataTestCaseHelper.assertNotEquals(fieldDefinition1, fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(fieldDefinition1, fieldDefinition2);
    }

    /**
     * Helper method which creates a <code>MutableUnitType</code> which can be used for
     * testing.
     * @return a mutable unit type.
     */
    private MutableQuantityType createMutableUnitTypeForTestingEquals() {
        MutableQuantityType mutableQuantityType = new MutableQuantityTypeImpl();
        MetaDataTypeFactory factory = MetaDataFactory.getDefaultInstance().getTypeFactory();
        NumberType numberType = factory.createNumberType();
        mutableQuantityType.setMagnitudeType(numberType);
        UnitType unitType = factory.createUnitType();
        mutableQuantityType.setUnitType(unitType);
        return mutableQuantityType;
    }

    public void testVerify() {
        final MutableQuantityType quantityType = TYPE_FACTORY.createQuantityType();
        final MutableNumberType numberType = TYPE_FACTORY.createNumberType();
        final ImmutableNumberSubTypeConstraint subTypeConstraint = CONSTRAINT_FACTORY.getNumberSubTypeConstraint(Integer.class);
        numberType.setNumberSubTypeConstraint(subTypeConstraint);
        quantityType.setMagnitudeType(numberType);
        final MutableUnitType unitType = TYPE_FACTORY.createUnitType();
        final MutableEnumeratedConstraint enumeratedConstraint = CONSTRAINT_FACTORY.createEnumeratedConstraint();
        final List enumeratedValues = enumeratedConstraint.getMutableEnumeratedValues();
        enumeratedValues.add(QuantityUnits.MILLIMETERS);
        enumeratedValues.add(QuantityUnits.CENTIMETERS);
        enumeratedValues.add(QuantityUnits.INCHES);
        unitType.setEnumeratedConstraint(enumeratedConstraint);
        quantityType.setUnitType(unitType);
        MutableQuantityValue quantityValue = VALUE_FACTORY.createQuantityValue();
        final MutableNumberValue magnitudeValue = VALUE_FACTORY.createNumberValue();
        magnitudeValue.setValue(new Integer(123));
        quantityValue.setMagnitudeValue(magnitudeValue);
        quantityValue.setUnitValue(QuantityUnits.CENTIMETERS);
        Collection errors = quantityType.verify(quantityValue);
        assertEquals(0, errors.size());
        magnitudeValue.setValue(new Byte((byte) 42));
        quantityValue.setMagnitudeValue(magnitudeValue);
        errors = quantityType.verify(quantityValue);
        assertEquals(1, errors.size());
        VerificationError error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION, error.getType());
        assertEquals("[@magnitude]", error.getLocation());
        assertEquals(magnitudeValue, error.getInvalidValue());
        assertEquals(subTypeConstraint, error.getConstraint());
        magnitudeValue.setValue(new Integer(42));
        quantityValue.setMagnitudeValue(magnitudeValue);
        quantityValue.setUnitValue(QuantityUnits.PIXELS);
        errors = quantityType.verify(quantityValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION, error.getType());
        assertEquals("[@unit]", error.getLocation());
        assertEquals(QuantityUnits.PIXELS, error.getInvalidValue());
        assertEquals(enumeratedConstraint, error.getConstraint());
        BooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        errors = quantityType.verify(booleanValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_INVALID_IMPLEMENTATION, error.getType());
        assertEquals("", error.getLocation());
        assertEquals(booleanValue, error.getInvalidValue());
        assertNull(error.getConstraint());
    }
}
