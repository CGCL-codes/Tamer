package org.demis.orc.taglabel;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;
import java.io.Serializable;

public class TaglabelTest implements Serializable {

    @Test
    public void newInstanceHasNoPrimaryKey() {
        Taglabel model = new Taglabel();
        assertFalse(model.hasPrimaryKey());
    }

    @Test
    public void hasPrimaryKeyReturnsTrue() {
        Taglabel model = new Taglabel();
        model.setTaglabelId(generateString(32));
        assertNotNull(model.getTaglabelId());
        assertTrue(model.hasPrimaryKey());
    }

    public static String generateString(int size) {
        Random random = new Random();
        char[] letters = new char[size];
        for (int i = 0; i < size; i++) {
            letters[i] = (char) (97 + (int) (26 * random.nextFloat()));
        }
        return new String(letters);
    }

    @Test
    public void simpleEqualWithSameObject() {
        Taglabel model = new Taglabel();
        assertEquals(model, model);
        assertSame(model, model);
        assertEquals(model.hashCode(), model.hashCode());
    }

    @Test
    public void equalsWithNull() {
        Taglabel model = new Taglabel();
        assertFalse(model.equals(null));
        assertFalse(model.equals(new Object()));
    }

    @Test
    public void equalsUsingPrimaryKey() {
        Taglabel model1 = new Taglabel();
        Taglabel model2 = new Taglabel();
        String id = generateString(32);
        model1.setTaglabelId(id);
        model2.setTaglabelId(id);
        assertTrue(model1.hasPrimaryKey());
        assertTrue(model2.hasPrimaryKey());
        assertTrue(model1.hashCode() == model2.hashCode());
        assertTrue(model1.equals(model2));
        assertTrue(model2.equals(model1));
    }

    @Test
    public void equalsUsingId() {
        Taglabel model1 = new Taglabel();
        Taglabel model2 = new Taglabel();
        assertFalse(model1.hasPrimaryKey());
        assertFalse(model2.hasPrimaryKey());
        assertFalse(model1.hashCode() == model2.hashCode());
        assertFalse(model1.equals(model2));
        assertFalse(model2.equals(model1));
    }
}
