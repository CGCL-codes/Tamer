package org.dishevelled.commandline;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import junit.framework.TestCase;
import org.dishevelled.commandline.argument.StringArgument;

/**
 * Unit test for ArgumentList.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class ArgumentListTest extends TestCase {

    public void testConstructors() {
        ArgumentList arguments0 = new ArgumentList();
        assertNotNull(arguments0);
        ArgumentList arguments1 = new ArgumentList(new Argument[0]);
        assertNotNull(arguments1);
        ArgumentList arguments2 = new ArgumentList(new StringArgument("f", "foo", "foo", true), new StringArgument("b", "bar", "bar", true));
        assertNotNull(arguments2);
        ArgumentList arguments3 = new ArgumentList(Collections.<Argument<?>>emptyList());
        assertNotNull(arguments3);
        try {
            new ArgumentList((Argument<?>) null);
            fail("ctr((Argument) null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ArgumentList((Argument[]) null);
            fail("ctr((Argument[]) null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ArgumentList((Collection<Argument<?>>) null);
            fail("ctr((Collection<Argument>) null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testAdd() {
        ArgumentList arguments = new ArgumentList();
        try {
            arguments.add(null);
            fail("add(null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        Argument<String> stringArgument = new StringArgument("s", "string", "String argument", true);
        Argument<String> conflictingShortName = new StringArgument("s", "conflicting-short-name", "Conflicting short name", true);
        Argument<String> conflictingLongName = new StringArgument("c", "string", "Conflicting long name", true);
        assertTrue(arguments.add(stringArgument));
        try {
            arguments.add(conflictingShortName);
            fail("add(conflictingShortName) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            arguments.add(conflictingLongName);
            fail("add(conflictingLongName) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testAddAll() {
        ArgumentList arguments = null;
        arguments = new ArgumentList();
        try {
            arguments.addAll(null);
            fail("addAll(null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        Argument<String> stringArgument = new StringArgument("s", "string", "String argument", true);
        Argument<String> secondStringArgument = new StringArgument("e", "second-string", "Second string argument", true);
        Argument<String> conflictingShortName = new StringArgument("s", "conflicting-short-name", "Conflicting short name", true);
        Argument<String> conflictingLongName = new StringArgument("c", "string", "Conflicting long name", true);
        try {
            arguments.addAll(Arrays.asList(new Argument<?>[] { stringArgument, conflictingShortName }));
            fail("addAll(..., conflictingShortName) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        arguments = new ArgumentList();
        try {
            arguments.addAll(Arrays.asList(new Argument<?>[] { stringArgument, conflictingLongName }));
            fail("addAll(..., conflictingLongName) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        arguments = new ArgumentList();
        try {
            arguments.addAll(Arrays.asList(new Argument<?>[] { stringArgument, stringArgument }));
            fail("addAll(stringArgument, stringArgument) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        arguments = new ArgumentList();
        arguments.addAll(Arrays.asList(new Argument<?>[] { stringArgument, secondStringArgument }));
        boolean foundStringArgument = false;
        boolean foundSecondStringArgument = false;
        for (Argument<?> a : arguments) {
            if (a.equals(stringArgument)) {
                foundStringArgument = true;
            }
            if (a.equals(secondStringArgument)) {
                foundSecondStringArgument = true;
            }
        }
        assertTrue("found stringArgument", foundStringArgument);
        assertTrue("found secondStringArgument", foundSecondStringArgument);
    }
}
