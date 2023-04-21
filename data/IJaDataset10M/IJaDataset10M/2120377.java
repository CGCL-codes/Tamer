package people;

import org.junit.Test;
import placing.Place;
import placing.PlaceNotFoundException;
import placing.Room;
import util.Debug;
import junit.framework.TestCase;

public class PersonTest extends TestCase {

    Person person_a, person_b, person_c, person_d, person_e, person_f;

    Room room_a, room_b;

    Place place_a, place_b;

    protected void setUp() throws Exception {
        room_a = new Room(123);
        room_b = new Room(124);
        place_a = new Place(room_b, 87);
        place_b = new Place(room_a, 4053);
        person_a = new Person(977537, "K�re", "Kaviar");
        person_b = new Person(8976, "Freddy", "Falkberget");
        person_c = new Person(127976, "�got", "�berg");
        person_d = new Person(9869679, "Per", "Potte", "per.potte@student.uib.no", "Not to be trusted", new Place(room_a, 23));
        person_e = new Person(9869679, "Lise", "Lang", "lise.lang@student.uib.no", "Needs a big chair", place_b);
        person_f = new Person(977537, "K�re", "Kaviar", "kare.kaviar@student.uib.no", "Same as before", place_a);
    }

    public void testEquals() {
        assertEquals(person_a, person_f);
    }

    public void testAssignation() {
        assertFalse(person_a.isAssigned());
        assertFalse(person_b.isAssigned());
        assertFalse(person_c.isAssigned());
        assertFalse(person_d.isAssigned());
        assertFalse(person_e.isAssigned());
        assertFalse(person_f.isAssigned());
        person_a.assign(place_a);
        assertTrue(person_a.isAssigned());
        try {
            assertEquals(place_a, person_a.getCurrentPlace());
        } catch (PlaceNotFoundException e) {
            fail();
        }
        person_a.assign(place_b);
        try {
            assertEquals(place_b, person_a.getCurrentPlace());
        } catch (PlaceNotFoundException e) {
            fail();
        }
        person_a.unAssign();
        assertFalse(person_a.isAssigned());
        assertFalse(person_b.isAssigned());
        assertFalse(person_c.isAssigned());
        assertFalse(person_d.isAssigned());
        assertFalse(person_e.isAssigned());
        assertFalse(person_f.isAssigned());
    }

    public void testPreferations() {
        assertFalse(person_a.hasPreferredPlace());
        assertFalse(person_b.hasPreferredPlace());
        assertFalse(person_c.hasPreferredPlace());
        assertTrue(person_d.hasPreferredPlace());
        assertTrue(person_e.hasPreferredPlace());
        assertTrue(person_f.hasPreferredPlace());
        person_a.addPreferredPlace(new Place(room_a, 55));
        assertTrue(person_a.hasPreferredPlace());
        assertFalse(person_b.hasPreferredPlace());
        assertFalse(person_c.hasPreferredPlace());
        assertTrue(person_d.hasPreferredPlace());
        assertTrue(person_e.hasPreferredPlace());
        assertTrue(person_f.hasPreferredPlace());
        assertEquals(person_a.getPreferredPlace().getPlaceName(), room_a.getID() + "-" + 55);
        assertTrue(person_a.getPreferredPlace().getPlaceName() != room_b.getID() + "-" + 55);
        person_a.removePreferrence();
        assertFalse(person_a.hasPreferredPlace());
    }

    public void testComments() {
        assertFalse(person_a.hasComment());
        assertFalse(person_b.hasComment());
        assertFalse(person_c.hasComment());
        assertTrue(person_d.hasComment());
        assertTrue(person_e.hasComment());
        assertTrue(person_f.hasComment());
        person_a.setComment("Strange person.");
        person_b.setComment("Smells of elderberries.");
        person_c.setComment("Loves fish on mondays.");
        assertTrue(person_a.hasComment());
        assertTrue(person_b.hasComment());
        assertTrue(person_c.hasComment());
        assertTrue(person_d.hasComment());
        assertTrue(person_e.hasComment());
        assertTrue(person_f.hasComment());
        assertEquals(person_c.getComment(), "Loves fish on mondays.");
        assertTrue(person_a.getComment() != "Smells of elderberries");
    }

    @Test
    public void testComparing() {
        assertEquals(person_a, person_a);
        assertEquals(false, person_a.equals(person_b));
        person_a.setScore(200);
        person_b.setScore(250);
        if (Debug.ON) {
            System.out.println(person_a.compareTo(person_b));
        }
        assertEquals(true, person_a.compareTo(person_b) > 0);
        person_b.setScore(person_a.getScore());
        if (Debug.ON) {
            System.out.println(person_a.compareTo(person_b));
        }
        assertEquals(false, person_a.compareTo(person_b) == 0);
        person_f.setScore(6);
        assertEquals(person_a, person_f);
        Debug.ON = false;
    }
}
