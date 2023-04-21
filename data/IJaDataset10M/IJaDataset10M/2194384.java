package cruise.umple.implementation.ruby;

import org.junit.*;
import cruise.umple.implementation.ImmutableUnidirectionalMNTest;

public class RubyImmutableUnidirectionalMNTest extends ImmutableUnidirectionalMNTest {

    @Before
    public void setUp() {
        super.setUp();
        language = "Ruby";
        languagePath = "ruby";
    }
}
