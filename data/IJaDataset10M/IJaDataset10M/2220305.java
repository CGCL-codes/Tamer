package blue.soundObject.jmask;

import junit.framework.TestCase;
import blue.soundObject.NoteList;

/**
 * 
 * @author steven
 */
public class FieldTest extends TestCase {

    public FieldTest(String testName) {
        super(testName);
    }

    /**
     * Test of generateNotes method, of class blue.soundObject.jmask.Field.
     */
    public void testGenerateNotesConstant() {
        double duration = 5.0;
        Field field = new Field();
        NoteList result = field.generateNotes(duration);
        assertEquals(5, result.size());
        Constant c = (Constant) field.getParameter(0).getGenerator();
        c.setValue(2.0);
        result = field.generateNotes(duration);
        for (int i = 0; i < result.size(); i++) {
            assertEquals("2", result.getNote(i).getPField(1));
        }
        Constant c2 = (Constant) field.getParameter(1).getGenerator();
        c2.setValue(1.5);
        result = field.generateNotes(duration);
        assertEquals(4, result.size());
    }
}
