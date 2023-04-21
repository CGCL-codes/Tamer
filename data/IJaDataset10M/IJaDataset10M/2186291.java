package nl.BobbinWork.testutils.parameterized;

import java.io.IOException;
import org.junit.Ignore;

/**
 * A description of a to-be-instantiated {@link HappyTestRunParameters} object.<br>
 * <br>
 * Usage example in a method annotated with @<code>Parameters</code> in a
 * subclass of {@link ParameterizedFixture}:<br>
 * <code>run("tag").expects("1").withInput(1)</code>
 * 
 * @author J. Pol
 */
@Ignore("This is a test tool, not a tester")
public class TestRunParametersBuilder {

    private final String tag;

    private TestRunParametersBuilder(final String tag) {
        this.tag = tag;
    }

    /**
   * Creates a builder instance. A builder instance is designed to create one
   * instance of {@link HappyTestRunParameters}, otherwise you will get the same
   * tag for multiple test runs.
   * 
   * @param tag
   *          a descriptive tag that identifies the test run. The description
   *          documents the purpose of the test run. A unique tag helps to find
   *          the definition of the test run when it fails.
   * 
   * @return a builder instance
   */
    public static TestRunParametersBuilder run(final String tag) {
        return new TestRunParametersBuilder(tag);
    }

    /**
   * Sets the expected results of a sad test run.
   * 
   * @param expectedExceptionClass
   *          the class of the expected exception
   * @param patterns
   *          list of patterns that must be found in the exception's message
   * @return a builder that only needs the input for the test run
   */
    public SadBuilder expects(final Class<? extends Exception> expectedExceptionClass, final String... patterns) {
        return new SadBuilder(tag, expectedExceptionClass, patterns);
    }

    /**
   * Sets the expected results of a sad test run.
   * 
   * @param patterns
   *          list of patterns that must be found in the exception's message
   * @return a builder that only needs the input for the test run
   */
    public PhraseBuilder expectsIllegalArgument(final String... patterns) {
        return expects(IllegalArgumentException.class, patterns);
    }

    /**
   * Sets the expected results of a sad test run.
   * 
   * @param patterns
   *          list of patterns that must be found in the exception's message
   * @return a builder that only needs the input for the test run
   */
    public PhraseBuilder expectsNullPointer(final String... patterns) {
        return expects(NullPointerException.class, patterns);
    }

    /**
   * Sets the expected results of a sad test run.
   * 
   * @param patterns
   *          list of patterns that must be found in the exception's message
   * @return a builder that only needs the input for the test run
   */
    public PhraseBuilder expectsIndexOutOfBounds(final String... patterns) {
        return expects(IndexOutOfBoundsException.class, patterns);
    }

    /**
   * Sets the expected results of a sad test run.
   * 
   * @param patterns
   *          list of patterns that must be found in the exception's message
   * @return a builder that only needs the input for the test run
   */
    public PhraseBuilder expectsUnsupportedOperation(final String... patterns) {
        return expects(UnsupportedOperationException.class, patterns);
    }

    /**
   * Sets the expected results of a sad test run.
   * 
   * @param patterns
   *          list of patterns that must be found in the exception's message
   * @return a builder that only needs the input for the test run
   */
    public PhraseBuilder expectsIOException(final String... patterns) {
        return expects(IOException.class, patterns);
    }

    /**
   * Sets the expected results of a sad test run.
   * 
   * @param patterns
   *          list of patterns that must be found in the exception's message
   * @return a builder that only needs the input for the test run
   */
    public PhraseBuilder expectsArrayIndexOutOfBounds(final String... patterns) {
        return expects(ArrayIndexOutOfBoundsException.class, patterns);
    }

    /**
   * Sets the expected results of a happy test run.
   * 
   * @param deltas
   *          each non null delta[i] assumes expectedValues[i] can be cast to a
   *          double which will be compared with the actual result with a
   *          tolerance
   * @param expectedValues
   *          the objects expected to be returned by
   *          {@link ParameterizedFixture#produceTestResults}
   * 
   * @return an extended builder
   */
    public HappyBuilder expects(final Double[] deltas, final Object... expectedValues) {
        return new HappyBuilder(tag, expectedValues, deltas);
    }

    /**
   * Sets the expected results of a happy test run.
   * 
   * @param expectedValues
   *          the objects expected to be returned by
   *          {@link ParameterizedFixture#produceTestResults}
   * 
   * @return an extended builder
   */
    public HappyBuilder expects(final Object... expectedValues) {
        return new HappyBuilder(tag, expectedValues, null);
    }
}
