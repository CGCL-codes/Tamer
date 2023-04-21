package org.demis.elf.customerCustomfieldValue;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;
import java.io.Serializable;

public class CustomerCustomfieldValueTest implements Serializable {

    @Test
    public void newInstanceHasNoPrimaryKey() {
        CustomerCustomfieldValue model = new CustomerCustomfieldValue();
        assertFalse(model.hasPrimaryKey());
    }

    @Test
    public void hasPrimaryKeyReturnsTrue() {
        CustomerCustomfieldValue model = new CustomerCustomfieldValue();
        model.setCustomerCustomfieldValueId(generateString(32));
        assertNotNull(model.getCustomerCustomfieldValueId());
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
        CustomerCustomfieldValue model = new CustomerCustomfieldValue();
        assertEquals(model, model);
        assertSame(model, model);
        assertEquals(model.hashCode(), model.hashCode());
    }

    @Test
    public void equalsWithNull() {
        CustomerCustomfieldValue model = new CustomerCustomfieldValue();
        assertFalse(model.equals(null));
        assertFalse(model.equals(new Object()));
    }

    @Test
    public void equalsUsingPrimaryKey() {
        CustomerCustomfieldValue model1 = new CustomerCustomfieldValue();
        CustomerCustomfieldValue model2 = new CustomerCustomfieldValue();
        String id = generateString(32);
        model1.setCustomerCustomfieldValueId(id);
        model2.setCustomerCustomfieldValueId(id);
        assertTrue(model1.hasPrimaryKey());
        assertTrue(model2.hasPrimaryKey());
        assertTrue(model1.hashCode() == model2.hashCode());
        assertTrue(model1.equals(model2));
        assertTrue(model2.equals(model1));
    }

    @Test
    public void equalsUsingId() {
        CustomerCustomfieldValue model1 = new CustomerCustomfieldValue();
        CustomerCustomfieldValue model2 = new CustomerCustomfieldValue();
        assertFalse(model1.hasPrimaryKey());
        assertFalse(model2.hasPrimaryKey());
        assertFalse(model1.hashCode() == model2.hashCode());
        assertFalse(model1.equals(model2));
        assertFalse(model2.equals(model1));
    }
}
