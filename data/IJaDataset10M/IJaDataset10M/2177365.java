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
 * {@link tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceReal}
 * .
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestModelInstanceReal {

    /** The {@link Logger} for this class. */
    private static final Logger LOGGER = ModelInstanceTypeTestPlugin.getLogger(TestModelInstanceReal.class);

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

    /** All instances of the {@link PrimitiveTypeKind#REAL}. */
    private static Set<IModelInstanceReal> instances_real;

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
            List<Property> realProperties;
            realProperties = new ArrayList<Property>();
            for (Property aProperty : type_PrimitiveTypeProviderClass.getOwnedProperty()) {
                if (aProperty.getName().startsWith("real")) {
                    realProperties.add(aProperty);
                }
            }
            instances_real = new HashSet<IModelInstanceReal>();
            for (IModelInstanceObject aProviderInstance : instances_PrimitiveTypeProviderClass) {
                for (Property aRealProperty : realProperties) {
                    IModelInstanceElement aRealResult;
                    try {
                        aRealResult = aProviderInstance.getProperty(aRealProperty);
                        if (aRealResult != null && aRealResult instanceof IModelInstanceReal) {
                            instances_real.add((IModelInstanceReal) aRealResult);
                        }
                    } catch (PropertyAccessException e) {
                    } catch (PropertyNotFoundException e) {
                    }
                }
            }
            if (instances_real.size() == 0 && LOGGER.isDebugEnabled()) {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_NoRealInstanceFound;
                LOGGER.warn(msg);
            }
        } else {
            msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_NoProviderClassInstanceFound;
            LOGGER.warn(msg);
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#asType(Type)}.
	 * </p>
	 */
    @Test
    public void testAsType01() {
        for (IModelInstanceReal aReal : instances_real) {
            IModelInstanceElement anotherReal;
            IModelInstanceElement anInteger;
            IModelInstanceElement aString;
            try {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_AsTypeIsWrong;
                msg = NLS.bind(msg, type_real);
                anotherReal = aReal.asType(type_real);
                assertTrue(msg, anotherReal instanceof IModelInstanceReal);
                assertEquals(msg, aReal.getDouble(), ((IModelInstanceReal) anotherReal).getDouble());
            } catch (AsTypeCastException e) {
                fail(msg);
            }
            try {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_AsTypeIsWrong;
                msg = NLS.bind(msg, type_string);
                aString = aReal.asType(type_string);
                assertTrue(msg, aString instanceof IModelInstanceString);
            } catch (AsTypeCastException e) {
                fail(msg);
            }
            try {
                msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_AsTypeIsWrong;
                msg = NLS.bind(msg, type_integer);
                anInteger = aReal.asType(type_integer);
                assertTrue(msg, anInteger instanceof IModelInstanceInteger);
                assertEquals(msg, (Long) aReal.getDouble().longValue(), ((IModelInstanceInteger) anInteger).getLong());
            } catch (AsTypeCastException e) {
                fail(msg);
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#asType(Type)} with illegal
	 * arguments.
	 * </p>
	 * 
	 * @throws AsTypeCastException
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testAsType02() throws AsTypeCastException {
        for (IModelInstanceReal aReal : instances_real) {
            aReal.asType(null);
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#asType(Type)}.
	 * </p>
	 * 
	 * @throws AsTypeCastException
	 */
    @Test
    public void testAsType03() throws AsTypeCastException {
        for (IModelInstanceReal aReal : instances_real) {
            IModelInstanceElement anotherReal;
            IModelInstanceElement anInteger;
            IModelInstanceElement aString;
            msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_AsTypeIsWrong;
            msg = NLS.bind(msg, type_integer);
            anInteger = aReal.asType(type_integer);
            anotherReal = anInteger.asType(type_real);
            assertTrue(msg, anotherReal instanceof IModelInstanceReal);
            if (anInteger.isUndefined()) {
                assertTrue(msg, anotherReal.isUndefined());
            } else {
                assertEquals(msg, ((IModelInstanceInteger) anInteger).getDouble(), ((IModelInstanceReal) anotherReal).getDouble());
            }
            msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_AsTypeIsWrong;
            msg = NLS.bind(msg, type_string);
            aString = aReal.asType(type_string);
            anotherReal = aString.asType(type_real);
            assertTrue(msg, anotherReal instanceof IModelInstanceReal);
            if (aString.isUndefined()) {
                assertTrue(msg, anotherReal.isUndefined());
            } else {
                assertEquals(msg, new Double(Double.parseDouble(((IModelInstanceString) aString).getString())), ((IModelInstanceReal) anotherReal).getDouble());
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#copyForAtPre()}.
	 * </p>
	 */
    @Test
    public void testCopyForAtPre() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_CopyForAtPreIsWrong;
        for (IModelInstanceReal aReal : instances_real) {
            try {
                assertNotNull(msg, aReal.copyForAtPre());
            } catch (CopyForAtPreException e) {
                fail(msg);
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#equals(Object)}.
	 * </p>
	 */
    @Test
    public void testEquals() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_EqualsIsWrong;
        for (IModelInstanceReal aReal : instances_real) {
            for (IModelInstanceReal anotherReal : instances_real) {
                if (aReal.getDouble().equals(anotherReal.getDouble())) {
                    assertTrue(msg, aReal.equals(anotherReal));
                } else {
                    assertFalse(msg, aReal.equals(anotherReal));
                }
                assertFalse(msg, aReal.equals(null));
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#getDouble()}.
	 * </p>
	 */
    @Test
    public void testGetDouble() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_GetDoubleIsWrong;
        for (IModelInstanceReal aReal : instances_real) {
            if (aReal.isUndefined()) {
                assertNull(msg, aReal.getDouble());
            } else {
                assertNotNull(msg, aReal.getDouble());
            }
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#getTypes()}.
	 * </p>
	 */
    @Test
    public void testGetTypes() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_GetTypesIsWrong;
        for (IModelInstanceReal aReal : instances_real) {
            assertNotNull(msg, aReal.getType());
            assertTrue(msg, aReal.getType() instanceof PrimitiveType);
            assertEquals(msg, PrimitiveTypeKind.REAL, ((PrimitiveType) aReal.getType()).getKind());
        }
    }

    /**
	 * <p>
	 * Tests the method {@link IModelInstanceReal#isUndefined()}.
	 * </p>
	 */
    @Test
    public void testIsUndefined() {
        msg = ModelInstanceTypeTestSuiteMessages.TestModelInstanceReal_IsUndefinedIsWrong;
        for (IModelInstanceReal aReal : instances_real) {
            if (aReal.isUndefined()) {
                assertNull(msg, aReal.getDouble());
            } else {
                assertNotNull(msg, aReal.getDouble());
            }
        }
    }
}
