package tests;

import game.ElementsInitializer;
import game.Game;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TestElementsInitializer {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void TestInstance() {
        ElementsInitializer e = new ElementsInitializer();
        assert (e.InitializeGameElements() instanceof Game);
    }
}
