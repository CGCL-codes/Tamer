package cruise.umple.implementation.php;

import org.junit.*;
import cruise.umple.implementation.ImmutableUnidirectionalOptionalNTest;

public class PhpImmutableUnidirectionalOptionalNTest extends ImmutableUnidirectionalOptionalNTest {

    @Before
    public void setUp() {
        super.setUp();
        language = "Php";
        languagePath = "php";
    }
}
