package de.cologneintelligence.fitgoodies;

import java.util.Comparator;
import java.util.Iterator;
import org.jmock.integration.junit3.MockObjectTestCase;
import de.cologneintelligence.fitgoodies.adapters.TypeAdapterHelper;
import fit.Counts;

/**
 * $Id: FitGoodiesTestCase.java 51 2011-09-04 16:41:57Z jochen_wierum $
 * @author jwierum
 */
public abstract class FitGoodiesTestCase extends MockObjectTestCase {

    protected static Counts mkCounts(final int r, final int w, final int i, final int e) {
        final Counts c = new Counts();
        c.right = r;
        c.wrong = w;
        c.ignores = i;
        c.exceptions = e;
        return c;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TypeAdapterHelper.reset();
        de.cologneintelligence.fitgoodies.database.SetupHelper.reset();
        de.cologneintelligence.fitgoodies.date.SetupHelper.reset();
        de.cologneintelligence.fitgoodies.file.FileFixtureHelper.reset();
        de.cologneintelligence.fitgoodies.parsers.ParserHelper.reset();
        de.cologneintelligence.fitgoodies.references.CrossReferenceHelper.reset();
        de.cologneintelligence.fitgoodies.alias.AliasHelper.reset();
        de.cologneintelligence.fitgoodies.runners.RunnerHelper.reset();
        de.cologneintelligence.fitgoodies.mail.SetupHelper.reset();
        de.cologneintelligence.fitgoodies.log4j.LogHelper.reset();
        de.cologneintelligence.fitgoodies.selenium.SetupHelper.reset();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        de.cologneintelligence.fitgoodies.database.DriverMock.cleanup();
    }

    public final void assertStringIterator(final Iterator<?> it, final String[] expected) {
        int i = 0;
        while (it.hasNext()) {
            assertEquals(it.next().toString(), expected[i]);
            i++;
        }
        assertEquals(expected.length, i);
    }

    public final <T> void assertCompares(final T o1, final T o2, final Comparator<T> comp, final int expectedSign) {
        assertTrue("Unexpected result when comparing: " + o1.toString() + " <> " + o2.toString(), Math.signum(comp.compare(o1, o2)) == Math.signum(expectedSign));
        assertTrue("Unexpected result when comparing: " + o2.toString() + " <> " + o1.toString(), Math.signum(comp.compare(o2, o1)) == Math.signum(-expectedSign));
    }

    public final void assertContains(final Object[] array, final Object element) {
        for (final Object a : array) {
            if (element.equals(a)) {
                return;
            }
        }
        fail("array does not contain " + element.toString());
    }

    public final void assertArrayElements(final Object[] expected, final Object[] actual) {
        assertEquals(expected.length, actual.length);
        for (final Object e : expected) {
            assertContains(actual, e);
        }
    }

    public final void assertArray(final Object[] expected, final Object[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

    public final void assertNotContains(final Object[] array, final Object element) {
        for (final Object a : array) {
            if (element.equals(a)) {
                fail("array contains " + element.toString());
            }
        }
    }

    protected final void assertNotEquals(final Object expected, final Object actual) {
        assertFalse("expected: anything but <" + expected + "> but was: <" + actual + ">", expected == actual);
    }

    public final void assertContains(final String needle, final String haystack) {
        assertNotNull("String was null", haystack);
        assertTrue("\"" + haystack + "\" did not contain \"" + needle + "\"", haystack.contains(needle));
    }

    public static void assertNull(final Object o) {
        if (o != null) {
            fail("Should be null, but is: " + o.toString());
        }
    }
}
