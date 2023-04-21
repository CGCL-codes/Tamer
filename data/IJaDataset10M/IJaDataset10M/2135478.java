package org.rivalry.core.datacollector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.rivalry.core.model.Candidate;
import org.rivalry.core.model.Category;
import org.rivalry.core.model.Criterion;
import org.rivalry.core.model.RivalryData;
import org.rivalry.core.model.TestData;

/**
 * Provides tests for the <code>DefaultDataPostProcessor</code> class.
 */
public class DefaultDataPostProcessorTest {

    /** Data post processor. */
    private DataPostProcessor _processor;

    /** Rivalry data. */
    private RivalryData _rivalryData;

    /**
     * Test the <code>postProcess()</code> method.
     */
    @Test
    public void postProcess() {
        Collections.shuffle(_rivalryData.getCandidates());
        Collections.shuffle(_rivalryData.getCategories());
        Collections.shuffle(_rivalryData.getCriteria());
        _processor.postProcess(_rivalryData);
        final Candidate candidate = _rivalryData.getCandidates().get(0);
        assertThat(candidate.getName(), is("1"));
        final Category category = _rivalryData.getCategories().get(0);
        assertThat(category.getName(), is("A"));
        {
            final Criterion criterion = _rivalryData.getCriteria().get(0);
            assertThat(criterion.getName(), is("a"));
            assertThat(criterion.getMinimumRating(), is(1.1));
            assertThat(criterion.getMaximumRating(), is(3.1));
        }
        {
            final Criterion criterion = _rivalryData.getCriteria().get(2);
            assertThat(criterion.getName(), is("c"));
            assertThat(criterion.getMinimumRating(), is(1.3));
            assertThat(criterion.getMaximumRating(), is(3.3));
        }
    }

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        final DataCollectorInjector injector = new DefaultDataCollectorInjector();
        _processor = new DefaultDataPostProcessor(injector.injectCandidateComparator(), injector.injectCategoryComparator(), injector.injectCriterionComparator());
        final TestData testData = new TestData();
        _rivalryData = testData.createRivalryData();
    }
}
