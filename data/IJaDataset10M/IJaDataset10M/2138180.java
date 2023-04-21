package org.apache.tools.ant.filters;

import java.io.IOException;
import org.apache.tools.ant.BuildFileTest;

/** JUnit Testcases for No new line when filterchain used
 */
public class NoNewLineTest extends BuildFileTest {

    public NoNewLineTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/filters/build.xml");
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void testNoAddNewLine() throws IOException {
        executeTarget("testNoAddNewLine");
    }
}
