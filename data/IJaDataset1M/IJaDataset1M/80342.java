package org.apache.tools.ant.util.regexp;

/**
 * Tests for the jakarta-oro implementation of the Regexp interface.
 *
 */
public class JakartaOroRegexpTest extends RegexpTest {

    public Regexp getRegexpImplementation() {
        return new JakartaOroRegexp();
    }

    public JakartaOroRegexpTest(String name) {
        super(name);
    }
}
