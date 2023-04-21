package net.sourceforge.testxng.domain;

import org.testng.annotations.Test;

/**
 * Unit test to test StringsLiterallyEqualComparator.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: StringsLiterallyEqualComparatorUnitTest.java 50 2010-04-02 20:36:53Z filipvanlaenen $
 *
 */
public class StringsLiterallyEqualComparatorUnitTest {

    private final StringsLiterallyEqualComparator comparator = new StringsLiterallyEqualComparator();

    @Test(groups = "unit")
    public void equalStringShouldTestEquivalent() {
        assert comparator.equivalent("Foo", "Foo");
    }

    @Test(groups = "unit")
    public void differentStringsShouldNotTestEquivalent() {
        assert !comparator.equivalent("Foo", "Bar");
    }
}
