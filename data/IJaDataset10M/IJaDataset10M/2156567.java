package integrationTests.loops;

import org.junit.*;
import integrationTests.*;

public final class ForStatementsTest extends CoverageTest {

    ForStatements tested = new ForStatements();

    @Test
    public void forInSeparateLines() {
        tested.forInSeparateLines();
        tested.forInSeparateLines();
        findMethodData(9, "forInSeparateLines");
        assertPaths(2, 1, 2);
        assertPath(4, 0);
        assertPath(5, 2);
    }
}
