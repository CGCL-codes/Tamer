package pcgen.core.chooser;

import pcgen.AbstractCharacterTestCase;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.EquipmentList;
import pcgen.util.TestHelper;
import java.lang.Class;
import java.lang.reflect.Field;
import java.util.List;

/**
 * {@code DomainChoiceManagerTest} test that the DomainChoiceManager class is functioning correctly.
 *
 * @author Andrew Wilson <nuance@sourceforge.net>
 */
public class DomainChoiceManagerTest extends AbstractCharacterTestCase {

    /**
	 * Constructs a new {@code DomainChoiceManagerTest}.
	 */
    public DomainChoiceManagerTest() {
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        EquipmentList.clearEquipmentMap();
    }

    /**
	 * Test the constructor 
	 */
    public void test001() {
        PObject pObj = new PObject();
        pObj.setName("My PObject");
        pObj.setChoiceString("NUMCHOICES=4|DOMAIN|Foo|Bar|Baz|Qux|Quux");
        is(pObj.getChoiceString(), strEq("NUMCHOICES=4|DOMAIN|Foo|Bar|Baz|Qux|Quux"));
        PlayerCharacter aPC = getCharacter();
        ChoiceManagerList choiceManager = ChooserUtilities.getChoiceManager(pObj, null, aPC);
        is(choiceManager, not(eq(null)), "Found the chooser");
        is(choiceManager.typeHandled(), strEq("DOMAIN"), "got expected chooser");
        try {
            Class cMClass = choiceManager.getClass();
            Field aField = (Field) TestHelper.findField(cMClass, "numberOfChoices");
            is(aField.get(choiceManager), eq(4));
            aField = (Field) TestHelper.findField(cMClass, "choices");
            List choices = (List) aField.get(choiceManager);
            is(new Integer(choices.size()), eq(5));
            is(choices.get(0), strEq("Foo"));
            is(choices.get(1), strEq("Bar"));
            is(choices.get(2), strEq("Baz"));
            is(choices.get(3), strEq("Qux"));
            is(choices.get(4), strEq("Quux"));
        } catch (IllegalAccessException e) {
            System.out.println(e);
        }
    }
}
