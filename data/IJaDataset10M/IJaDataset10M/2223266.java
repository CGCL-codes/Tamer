package net.sourceforge.testxng.domain;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;

public class StringsLiterallyEqualComparatorIntegrationTest extends AbstractEngineOnADirIntegrationTest {

    private static final String LITERALLY_EQUAL_STRINGS = "LiterallyEqualStrings";

    private static final String WHITESPACE_BREAKS_EQUIVALENCE = "WhitespaceBreaksEquivalence";

    private static final String DIR = TEST_RESOURCES_DIR + "comparators";

    @Test(groups = "integration")
    public void literallyEqualStrings() {
        runTestAndAssertForSuccess(LITERALLY_EQUAL_STRINGS);
    }

    @Test(groups = "integration")
    public void whitespaceBreaksEquivalence() {
        runTestAndAssertForFailure(WHITESPACE_BREAKS_EQUIVALENCE);
    }

    @BeforeTest(groups = "integration")
    void beforeTest() throws IOException, MalformedTestDefinitionFileException {
        initializeLogger(StringsLiterallyEqualComparatorIntegrationTest.class);
        initializeTestMap(DIR);
    }
}
