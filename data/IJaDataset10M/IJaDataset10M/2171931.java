package uk.ac.leeds.comp.ui.base.modelevent;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class UIModelChangeTypeImplTest {

    String testId = "testId";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUIModelChangeTypeImpl() {
        new UIModelChangeTypeImpl(testId);
    }

    @Test
    public void testToString() {
        UIModelChangeTypeImpl testObj = new UIModelChangeTypeImpl(testId);
        assertEquals(testId, testObj.toString());
    }
}
