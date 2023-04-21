package test.de.offis.semanticmm4u.user_profiles_connector.xml;

import de.offis.semanticmm4u.failures.MM4UUserProfilesConnectorException;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotSetOrAddUserProfileNodeValueException;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotStoreUserProfileException;
import de.offis.semanticmm4u.user_profiles_connector.UserProfileAccessorToolkit;
import de.offis.semanticmm4u.user_profiles_connector.xml.XMLUserProfileConnectorLocator;
import de.offis.semanticmm4u.user_profiles_connector.xml.profile.UProMProfile;
import de.offis.semanticmm4u.user_profiles_connector.xml.profile.UProMProfileNode;
import de.offis.semanticmm4u.user_profiles_connector.xml.repository.IOFilesystemLocator;

public class TypesTest extends SetUpTestCase {

    private UProMProfile userProfile = null;

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        IOFilesystemLocator ioFilesystemLocator = new IOFilesystemLocator(this.getUProMHome() + "xml-data/model/core.xsd", this.getUProMHome() + "xml-data/model/extensionTest.xsd", this.getUProMHome() + "xml-data/profiles/", ".xml", this.getUProMHome(), this.getUProMConfigurationFileName());
        XMLUserProfileConnectorLocator userProfileConnectorLocator = new XMLUserProfileConnectorLocator(ioFilesystemLocator, 1, 0);
        this.userProfileManager = UserProfileAccessorToolkit.getFactory(userProfileConnectorLocator);
        try {
            this.userProfile = (UProMProfile) userProfileManager.getUserProfile("peter_schmidt");
        } catch (MM4UUserProfilesConnectorException e) {
            System.out.println(e);
            fail();
        }
    }

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(TypesTest.class);
    }

    /**
	 * Testet die Verwendung des nicht beschraekten Datentypen.
	 */
    public void testTypes() {
        UProMProfileNode name = (UProMProfileNode) this.userProfile.findNodes("Name").firstElement();
        try {
            name.setValue("darf_keinen_Wert_tragen");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        UProMProfileNode street = (UProMProfileNode) this.userProfile.findNodes("Street").firstElement();
        try {
            street.setValue(null);
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            street.setValue("Nebenstra�e 24-26 a und b");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        assertEquals(street.getValues().size(), 1);
        assertTrue(street.getValues().contains("Nebenstra�e 24-26 a und b"));
    }

    /**
	 * Testet die Verwendung eines Boolean-Typs an einem Knoten.
	 */
    public void testBooleanType() {
        UProMProfileNode sinistral = (UProMProfileNode) this.userProfile.findNodes("Sinistral").firstElement();
        try {
            sinistral.setValue("true");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            sinistral.setValue("hier_darf_nur_true_oder_false_stehen");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
    }

    /**
	 * Testet die Verwendung eines Aufzaehlungstypen an einem Knoten.
	 */
    public void testEnumerationType() {
        UProMProfileNode maritalStatus = (UProMProfileNode) this.userProfile.findNodes("MaritalStatus").firstElement();
        UProMProfileNode gender = (UProMProfileNode) this.userProfile.findNodes("Gender").firstElement();
        try {
            maritalStatus.setValue("married");
            gender.setValue("male");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            maritalStatus.setValue("vielleicht");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            gender.setValue("vielleicht");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
    }

    /**
	 * Testet die Verwendung eines Datumstypen (anlehnend an ISO 8601) an einem Knoten.
	 */
    public void testDateType() {
        UProMProfileNode dateOfBirth = (UProMProfileNode) this.userProfile.findNodes("DateOfBirth").firstElement();
        try {
            dateOfBirth.setValue("1972-04-20");
            dateOfBirth.setValue("1000-01-01");
            dateOfBirth.setValue("2000-12-31");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            this.userProfileManager.setUserProfile(this.userProfile);
        } catch (MM4UCannotStoreUserProfileException e) {
            System.out.println(e);
            fail();
        }
        try {
            dateOfBirth.setValue("2001-02-30");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            dateOfBirth.setValue("80-12-31");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            dateOfBirth.setValue("1967-13-01");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            dateOfBirth.setValue("1967-02-32");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            dateOfBirth.setValue("1967-2-32");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            dateOfBirth.setValue("1967-02-1");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            dateOfBirth.setValue("1967:02-32");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            dateOfBirth.setValue("gar_kein_datum");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
    }

    /**
	 * Testet die Verwendung eines NonNegativeIntegerTypen.
	 */
    public void testNonNegativeInteger() {
        UProMProfileNode weight = (UProMProfileNode) this.userProfile.findNodes("Weight").firstElement();
        try {
            weight.setValue("85");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            weight.setValue("-1");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            weight.setValue("80.5");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            weight.setValue("kein_integer");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
    }

    /**
	 * Testet die Verwendung des Float-Typen.
	 */
    public void testFloat() {
        UProMProfileNode bodyTemperature = (UProMProfileNode) this.userProfile.findNodes("BodyTemperature").firstElement();
        try {
            bodyTemperature.setValue("85");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("+9000");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("-7");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("+0");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("-0");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("0");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("-0.001");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("+1.001");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("3883.0301");
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
            System.out.println(e);
            fail();
        }
        try {
            bodyTemperature.setValue("-1e");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("dfge");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("80.5.5");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("+-33");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("3+3");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue(".54545");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("-.3");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("+.12");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("12.");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("-14.");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("+1234.");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue(".");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("+");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
        try {
            bodyTemperature.setValue("-");
            fail();
        } catch (MM4UCannotSetOrAddUserProfileNodeValueException e) {
        }
    }
}
