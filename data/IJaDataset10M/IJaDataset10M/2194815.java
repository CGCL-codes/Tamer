package net.galluzzo.wave.orthopermubot.permutation;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DeletionTest {

    protected Deletion deletion = new Deletion();

    @Test
    public void deleteFirstChar() {
        assertEquals("bcde", deletion.permute("abcde", 0));
    }

    @Test
    public void deleteMiddleChar() {
        assertEquals("abde", deletion.permute("abcde", 2));
    }

    @Test
    public void deleteLastChar() {
        assertEquals("abcd", deletion.permute("abcde", 4));
    }
}
