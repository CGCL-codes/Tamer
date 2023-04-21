package ikube.action.rule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import ikube.ATest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Couck
 * @since 29.03.2011
 * @version 01.00
 */
public class IsMultiSearcherInitialisedTest extends ATest {

    private IsMultiSearcherInitialised rule;

    public IsMultiSearcherInitialisedTest() {
        super(IsMultiSearcherInitialisedTest.class);
    }

    @Before
    public void before() {
        rule = new IsMultiSearcherInitialised();
    }

    @Test
    public void evaluate() {
        when(index.getMultiSearcher()).thenReturn(null);
        boolean isMultiSearcherInitialized = rule.evaluate(indexContext);
        assertFalse(isMultiSearcherInitialized);
        when(index.getMultiSearcher()).thenReturn(multiSearcher);
        isMultiSearcherInitialized = rule.evaluate(indexContext);
        assertTrue(isMultiSearcherInitialized);
    }
}
