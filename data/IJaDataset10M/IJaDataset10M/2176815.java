package twoadw.reference.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelibra.type.EasyDate;
import twoadw.TwoadwTest;
import twoadw.reference.country.Countries;
import twoadw.reference.country.Country;

/**
 * JUnit tests for States.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class StatesTest {

    private static States states;

    private static Countries countries;

    private static Country sampleCountry;

    @BeforeClass
    public static void beforeTests() throws Exception {
        countries = TwoadwTest.getSingleton().getTwoadw().getReference().getCountries();
        sampleCountry = countries.createCountry("CA", "Canada");
        states = sampleCountry.getStates();
    }

    @Before
    public void beforeTest() throws Exception {
        states.getErrors().empty();
    }

    @Test
    public void stateCreated() throws Exception {
        State state01 = states.createState(sampleCountry, "QC", "Québec");
        State state02 = states.createState(sampleCountry, "ON", "Ontario");
        assertTrue(states.contain(state01));
        assertTrue(states.contain(state02));
        assertTrue(states.getErrors().isEmpty());
    }

    @Test
    public void stateUpdate() throws Exception {
        State state = states.createState(sampleCountry, "QC", "Québec");
        State stateCopy = state.copy();
        stateCopy.setStateCode("QUE");
        stateCopy.setStateName("Quebec");
        assertTrue(state.equals(stateCopy));
        assertTrue(state.equalOid(stateCopy));
        assertFalse(state.equalProperties(stateCopy));
        states.update(state, stateCopy);
        assertTrue(states.getErrors().isEmpty());
    }

    @Test
    public void stateEquality() throws Exception {
        State state = states.createState(sampleCountry, "QC", "Québec");
        State stateCopy = state.copy();
        assertEquals(state, stateCopy);
        assertNotSame(state, stateCopy);
        assertTrue(states.getErrors().isEmpty());
    }

    @Test
    public void idUnique() throws Exception {
        State state = states.createState(sampleCountry, "QC", "Québec");
        State stateNotUnique = states.createState(sampleCountry, "QC", "Québec");
        assertFalse(states.contain(stateNotUnique));
        assertNotNull(states.getErrors().getError("State.id.unique"));
    }

    @Test
    public void stateCodeRequired() throws Exception {
        State state = states.createState(sampleCountry, null, "Québec");
        assertFalse(states.contain(state));
        assertNotNull(states.getErrors().getError("State.stateCode.required"));
    }

    @Test
    public void stateCodeLength() throws Exception {
        String stringlength = "";
        while (stringlength.length() <= 16) {
            stringlength = stringlength + "1";
        }
        State state = states.createState(sampleCountry, stringlength, "Québec");
        assertFalse(states.contain(state));
        assertNotNull(states.getErrors().getError("State.stateCode.length"));
    }

    @Test
    public void stateNameeRequired() throws Exception {
        State state = states.createState(sampleCountry, "QC", null);
        assertFalse(states.contain(state));
        assertNotNull(states.getErrors().getError("State.stateName.required"));
    }

    @Test
    public void stateNameLength() throws Exception {
        String stringlength = "";
        while (stringlength.length() <= 64) {
            stringlength = stringlength + "1";
        }
        State state = states.createState(sampleCountry, "QC", stringlength);
        assertFalse(states.contain(state));
        assertNotNull(states.getErrors().getError("State.stateName.length"));
    }

    @After
    public void afterTest() throws Exception {
        for (State state : states.getList()) {
            states.remove(state);
        }
    }

    @AfterClass
    public static void afterTests() throws Exception {
        countries.remove(sampleCountry);
        TwoadwTest.getSingleton().close();
    }
}
