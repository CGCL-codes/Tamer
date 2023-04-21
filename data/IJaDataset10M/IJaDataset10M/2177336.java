package org.demis.elf.documentAttachmentGroup;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;
import java.io.Serializable;
import org.demis.elf.documentAttachment.*;
import org.demis.elf.label.*;

public class DocumentAttachmentGroupTest implements Serializable {

    @Test
    public void newInstanceHasNoPrimaryKey() {
        DocumentAttachmentGroup model = new DocumentAttachmentGroup();
        assertFalse(model.hasPrimaryKey());
    }

    @Test
    public void hasPrimaryKeyReturnsTrue() {
        DocumentAttachmentGroup model = new DocumentAttachmentGroup();
        model.setDocumentAttachmentGroupId(generateString(32));
        assertNotNull(model.getDocumentAttachmentGroupId());
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
        DocumentAttachmentGroup model = new DocumentAttachmentGroup();
        assertEquals(model, model);
        assertSame(model, model);
        assertEquals(model.hashCode(), model.hashCode());
    }

    @Test
    public void equalsWithNull() {
        DocumentAttachmentGroup model = new DocumentAttachmentGroup();
        assertFalse(model.equals(null));
        assertFalse(model.equals(new Object()));
    }

    @Test
    public void equalsUsingPrimaryKey() {
        DocumentAttachmentGroup model1 = new DocumentAttachmentGroup();
        DocumentAttachmentGroup model2 = new DocumentAttachmentGroup();
        String id = generateString(32);
        model1.setDocumentAttachmentGroupId(id);
        model2.setDocumentAttachmentGroupId(id);
        assertTrue(model1.hasPrimaryKey());
        assertTrue(model2.hasPrimaryKey());
        assertTrue(model1.hashCode() == model2.hashCode());
        assertTrue(model1.equals(model2));
        assertTrue(model2.equals(model1));
    }

    @Test
    public void equalsUsingId() {
        DocumentAttachmentGroup model1 = new DocumentAttachmentGroup();
        DocumentAttachmentGroup model2 = new DocumentAttachmentGroup();
        assertFalse(model1.hasPrimaryKey());
        assertFalse(model2.hasPrimaryKey());
        assertFalse(model1.hashCode() == model2.hashCode());
        assertFalse(model1.equals(model2));
        assertFalse(model2.equals(model1));
    }
}
