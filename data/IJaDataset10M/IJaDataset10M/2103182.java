package org.jmlspecs.JMLTests;

import java.io.File;
import java.util.Collections;
import java.util.Set;

/**
 * This represents a single test case with associated meta-data including tags,
 * JML language level, expected results, and test kind (e.g., parse, typecheck).
 * 
 * @author cclifton
 * 
 */
public class JMLTestCase {

    /**
	 * The file containing the input to this test case.
	 * 
	 * CONSIDER: This field should perhaps be pushed down to a FileBasedTestCase
	 * subclass, with JMLTestCase made abstract. This would accommodate using
	 * tests.xml to specify simple test cases without a separate boilerplate
	 * .java file for each test case.
	 */
    public final File inputFile;

    /**
	 * The kind of test (parse, typecheck, etc.) We might have used an enum for
	 * this, but the test kinds need to be extensible so tools can add their own
	 * kinds of tests.
	 */
    public final String kind;

    /**
	 * An immutable set of the tags for this test case.
	 */
    public final Set<String> tags;

    /**
	 * The language level of this test case, per the JML Reference Manual.
	 * Alphanumeric language levels are all designated as level 4, with the sort
	 * of level included in the tag set. For example, level C from the reference
	 * manual is recorded as level 4 with tag "concurrency", and level X from
	 * the reference manual is recorded as level 4 with tag "experimental".
	 */
    public final int level;

    /**
	 * Indicates whether a tool running this test case should terminate without
	 * error.
	 */
    public final boolean shouldPass;

    /**
	 * Constructs a test case for the given file and with the given meta-data.
	 * 
	 * @param inputFile
	 *            The file containing the input to this test case
	 * @param kind
	 *            The kind of test (parse, typecheck, etc.)
	 * @param tags
	 *            A set of tags for this test case. An unmodifiable <i>view</i>
	 *            of the given set is stored, so clients <i>must not modify the
	 *            original set</i> passed in. Such changes would be visible
	 *            through the constructed test case.
	 * @param level
	 *            The language level of this test case, per the JML Reference
	 *            Manual. See details at {@link #level}
	 * @param shouldPass
	 *            Indicates whether a tool running this test case should
	 *            terminate without error.
	 */
    public JMLTestCase(File inputFile, String kind, Set<String> tags, int level, boolean shouldPass) {
        this.kind = kind;
        this.tags = Collections.unmodifiableSet(tags);
        this.level = level;
        this.shouldPass = shouldPass;
        this.inputFile = inputFile;
    }
}
