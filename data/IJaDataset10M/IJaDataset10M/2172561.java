package tudresden.ocl20.pivot.modelinstancetype.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;
import org.junit.BeforeClass;
import org.junit.Test;
import tudresden.ocl20.pivot.modelinstancetype.exception.AsTypeCastException;
import tudresden.ocl20.pivot.modelinstancetype.exception.CopyForAtPreException;
import tudresden.ocl20.pivot.modelinstancetype.exception.PropertyAccessException;
import tudresden.ocl20.pivot.modelinstancetype.exception.PropertyNotFoundException;
import tudresden.ocl20.pivot.modelinstancetype.test.ModelInstanceTypeTestPlugin;
import tudresden.ocl20.pivot.modelinstancetype.test.ModelInstanceTypeTestServices;
import tudresden.ocl20.pivot.modelinstancetype.test.msg.ModelInstanceTypeTestSuiteMessages;
import tudresden.ocl20.pivot.modelinstancetype.test.testmodel.TestModelTypesNames;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceInteger;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceReal;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceString;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveType;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * Contains test cases to test the implementation of
 * {@link tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceInteger}
 * .
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestModelInstanceInteger {

    /** The {@link Logger} for this class. */
    private static final Logger LOGGER = ModelInstanceTypeTestPlugin.getLogger(TestModelInstanceInteger.class);

    /** A {@link String} used to display and log messages and warnings. */
    private static String msg;

    /** A {@link Type} used in this test class. */
    private static Type type_PrimitiveTypeProviderClass;

    /** A {@link Type} used in this test class. */
    private static PrimitiveType type_integer;

    /** A {@link Type} used in this test class. */
    private static PrimitiveType type_real;

    /** A {@link Type} used in this test class. */
    private static PrimitiveType type_string;

    /** All instances of the <code>PrimitiveTypeProviderClass</code>. */
    private static Set<IModelInstanceObject> instances_PrimitiveTypeProviderClass;

    /** All instances of the {@link PrimitiveTypeKind#INTEGER}. */
    private static Set<IModelInstanceInteger> instances_integer;

    /**
	 * <p>
	 * Loads some objects required during the tests.
	 * </p>
	 */
    @BeforeClass
    public static void setUp() {
        type_integer = (PrimitiveType) ModelInstanceTypeTestServices.getInstance().getModelType(PrimitiveTypeKind.INTEGER.toString());
        type_real = (PrimitiveType) ModelInstanceTypeTestServices.getInstance().getModelType(PrimitiveTypeKind.REAL.toString());
        type_string = (PrimitiveType) ModelInstanceTypeTestServices.getInstance().getModelType(PrimitiveTypeKind.STRING.toString());
        type_PrimitiveTypeProviderClass = ModelInstanceTypeTestServices.getInstance().getModelType(TestModelTypesNames.TYPE_NAME_PRIMITIVE_TYPE_PROVIDER_CLASS);
        instances_PrimitiveTypeProviderClass = ModelInstanceTypeTestServices.getInstance().getModelInstanceObjectsOfType(type_PrimitiveTypeProviderClass);
        if (instances_PrimitiveTypeProviderClass.size() != 0) {
            List<Property> integerProperties;
            integerProperties = new ArrayList<Property>();
            for (Property aProperty : type_PrimitiveTypeProviderClass.getOwnedProperty()) {
                if (aProperty.getName().startsWith("integer")) {
                    integerProperties.add(aProperty);
                }
            }
            instances_integer = new HashSet<IModelInstanceInteger>();
            for (IModelInstanceObject aProviderInstance : instances_PrimitiveTypeProviderClass) {
                for (Property anIntegerProperty : integerProperties) {
                    IModelInstanceElement anIntegerResult;
                    try {
                        anIntegerResult = aProviderInstance.getProperty(anIntegerProperty);
                        if (anIntegerResult != null && anIntegerResult instanceof IModelInstanceInteger) {
                            instances_integer.add((IModelInstanceInteger) anIntegerResult);
                        }
                    } catch (PropertyAccessException e) {
                    } catch (PropertyNotFoundException e) {
                    }
                }
            }
            if (instances_integer.size() == 0 && LOGGER.isDebugEnabled()) {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_NoIntegerInstanceFound;
                LOGGER.warn(msg);
            }
        } else {
            msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_NoProviderClassInstanceFound;
            LOGGER.warn(msg);
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceInteger#asType(Type)}.
	 * </p>
	 */
    @Test
    public void testAsType01() {
        for (IModelInstanceInteger anInteger : instances_integer) {
            IModelInstanceElement anotherInteger;
            IModelInstanceElement aReal;
            IModelInstanceElement aString;
            try {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_AsTypeIsWrong;
                msg = NLS.bind(msg, type_integer);
                anotherInteger = anInteger.asType(type_integer);
                assertTrue(msg, anInteger instanceof IModelInstanceInteger);
                assertEquals(msg, anInteger.getLong(), ((IModelInstanceInteger) anotherInteger).getLong());
            } catch (AsTypeCastException e) {
                fail(msg);
            }
            try {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_AsTypeIsWrong;
                msg = NLS.bind(msg, type_string);
                aString = anInteger.asType(type_string);
                assertTrue(msg, aString instanceof IModelInstanceString);
                assertEquals(msg, anInteger.getLong().toString(), ((IModelInstanceString) aString).getString());
            } catch (AsTypeCastException e) {
                fail(msg);
            }
            try {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_AsTypeIsWrong;
                msg = NLS.bind(msg, type_real);
                aReal = anInteger.asType(type_real);
                assertTrue(msg, aReal instanceof IModelInstanceReal);
                assertEquals(msg, new Double(anInteger.getLong()), ((IModelInstanceReal) aReal).getDouble());
            } catch (AsTypeCastException e) {
                fail(msg);
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceInteger#asType(Type)} with illegal
	 * arguments.
	 * </p>
	 * 
	 * @throws AsTypeCastException
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testAsType02() throws AsTypeCastException {
        for (IModelInstanceInteger anInteger : instances_integer) {
            anInteger.asType(null);
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceInteger#asType(Type)}.
	 * </p>
	 * 
	 * @throws AsTypeCastException
	 */
    @Test
    public void testAsType03() throws AsTypeCastException {
        for (IModelInstanceInteger anInteger : instances_integer) {
            IModelInstanceElement anotherInteger;
            msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_AsTypeIsWrong;
            msg = NLS.bind(msg, type_string);
            anotherInteger = anInteger.asType(type_string).asType(type_integer);
            assertTrue(msg, anotherInteger instanceof IModelInstanceInteger);
            assertEquals(msg, anInteger, anotherInteger);
            msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_AsTypeIsWrong;
            msg = NLS.bind(msg, type_real);
            anotherInteger = anInteger.asType(type_real).asType(type_integer);
            assertTrue(msg, anotherInteger instanceof IModelInstanceInteger);
            assertEquals(msg, anInteger, anotherInteger);
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceInteger#copyForAtPre()}.
	 * </p>
	 */
    @Test
    public void testCopyForAtPre() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_CopyForAtPreIsWrong;
        for (IModelInstanceInteger anInteger : instances_integer) {
            try {
                assertNotNull(msg, anInteger.copyForAtPre());
            } catch (CopyForAtPreException e) {
                fail(msg);
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceInteger#equals(Object)}.
	 * </p>
	 */
    @Test
    public void testEquals() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_EqualsIsWrong;
        for (IModelInstanceInteger anInteger : instances_integer) {
            for (IModelInstanceInteger anotherInteger : instances_integer) {
                if (anInteger.getLong().equals(anotherInteger.getLong())) {
                    assertTrue(msg, anInteger.equals(anotherInteger));
                } else {
                    assertFalse(msg, anInteger.equals(anotherInteger));
                }
                assertFalse(msg, anInteger.equals(null));
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceInteger#getInteger()}.
	 * </p>
	 */
    @Test
    public void testGetInteger() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_GetIntegerIsWrong;
        for (IModelInstanceInteger anInteger : instances_integer) {
            if (anInteger.isUndefined()) {
                assertNull(msg, anInteger.getLong());
            } else {
                assertNotNull(msg, anInteger.getLong());
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceInteger#getTypes()}.
	 * </p>
	 */
    @Test
    public void testGetTypes() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_GetTypesIsWrong;
        for (IModelInstanceInteger anInteger : instances_integer) {
            assertNotNull(msg, anInteger.getType());
            assertTrue(msg, anInteger.getType() instanceof PrimitiveType);
            assertEquals(msg, PrimitiveTypeKind.INTEGER, ((PrimitiveType) anInteger.getType()).getKind());
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#isUndefined()}.
	 * </p>
	 */
    @Test
    public void testIsUndefined() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceInteger_IsUndefinedIsWrong;
        for (IModelInstanceInteger anInteger : instances_integer) {
            if (anInteger.isUndefined()) {
                assertNull(msg, anInteger.getLong());
            } else {
                assertNotNull(msg, anInteger.getLong());
            }
        }
    }
}
