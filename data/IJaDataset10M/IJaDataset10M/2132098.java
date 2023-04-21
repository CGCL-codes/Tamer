package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.EntityCollectionType;

/**
 * Tests the {@link EntityCollectionType} enumeration class.
 * 
 * @author rdenaux
 * 
 */
public class EntityCollectionTypeTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetByName() {
        assertGetByName(EntityCollectionType.Disjunction, "Disjunction");
        assertGetByName(EntityCollectionType.List, "List");
    }

    private void assertGetByName(EntityCollectionType aType, String aString) {
        assertEquals(aType, EntityCollectionType.getByName(aString));
    }
}
