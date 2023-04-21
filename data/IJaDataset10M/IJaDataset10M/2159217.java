package org.hip.vif.bom.impl.test;

import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.sys.AssertionFailedError;
import org.hip.vif.bom.Person;
import org.hip.vif.bom.PersonHome;
import org.hip.vif.exc.ExternIDNotUniqueException;
import junit.framework.TestCase;

/**
 * Created on 21.07.2003
 * @author Benno Luthiger
 */
public class PersonImplTest extends TestCase {

    DataHouseKeeper data;

    public PersonImplTest(String arg0) {
        super(arg0);
        data = DataHouseKeeper.getInstance();
    }

    protected void tearDown() throws Exception {
        data.deleteAllFromPerson();
    }

    public void testNew() {
        PersonHome lPersonHome = data.getPersonHome();
        String[] lExpected = { "Man", "Adam", "Just a test" };
        try {
            int lCount = lPersonHome.getCount();
            Person lPerson = (Person) lPersonHome.create();
            lPerson.ucNewPerson(lExpected[0], lExpected[1], "", "", lExpected[2]);
            assertEquals("Count 1", lCount + 1, lPersonHome.getCount());
            lPerson = (Person) lPersonHome.create();
            lPerson.ucNewPerson(lExpected[1], lExpected[0], "", "", lExpected[2] + lExpected[2]);
            assertEquals("Count 2", lCount + 2, lPersonHome.getCount());
            try {
                lPerson = (Person) lPersonHome.create();
                lPerson.ucNewPerson(lExpected[0], lExpected[1], "", "", lExpected[2]);
                fail("Should'nt get here");
            } catch (ExternIDNotUniqueException exc) {
            }
            try {
                lPerson = (Person) lPersonHome.create();
                lPerson.ucNewPerson("", lExpected[1], "", "", lExpected[2]);
                fail("Should'nt get here");
            } catch (AssertionFailedError exc) {
            }
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(PersonHome.KEY_NAME, lExpected[0]);
            lKey.setValue(PersonHome.KEY_FIRST_NAME, lExpected[1]);
            lPerson = (Person) lPersonHome.findByKey(lKey);
            assertEquals("Remark", lExpected[2], lPerson.get(PersonHome.KEY_REMARK));
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }
}
