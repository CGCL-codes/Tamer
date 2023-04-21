package org.rivalry.example.bookaward;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.rivalry.core.model.Candidate;
import org.rivalry.core.model.Criterion;
import org.rivalry.core.model.RivalryData;

/**
 * Provides tests for the <code>BookAwardDataCollector</code> class.
 */
public class MysteryAwardDataCollectorTest {

    /**
     * Test the <code>fetchData()</code> method.
     */
    @Test
    public void fetchData() {
        final MysteryAwardDataCollector dataCollector = new MysteryAwardDataCollector();
        final String author0 = "Laura Lippman";
        final String author1 = "Michael Connelly";
        final List<String> authors = new ArrayList<String>();
        authors.add(author0);
        authors.add(author1);
        final RivalryData rivalryData = new RivalryData();
        dataCollector.fetchData(authors, rivalryData);
        System.out.println();
        for (final Candidate candidate : rivalryData.getCandidates()) {
            System.out.println("candidate = " + candidate.getName() + " " + candidate.getPage());
        }
        assertThat(rivalryData.getCandidates().size(), is(14));
        assertThat(rivalryData.getCategories().size(), is(0));
        assertThat(rivalryData.getCriteria().size(), is(2));
        final Criterion criterion0 = rivalryData.findCriterionByName(author0);
        assertNotNull(criterion0);
        final Criterion criterion1 = rivalryData.findCriterionByName(author1);
        assertNotNull(criterion1);
        {
            final Candidate candidate = rivalryData.findCandidateByName("The Agatha Awards");
            assertNotNull(candidate);
            assertThat(candidate.getRating(criterion0), is(1.0));
            assertNull(candidate.getRating(criterion1));
        }
        {
            final Candidate candidate = rivalryData.findCandidateByName("The Edgar Awards");
            assertNotNull(candidate);
            assertThat(candidate.getRating(criterion0), is(1.0));
            assertThat(candidate.getRating(criterion1), is(1.0));
        }
        {
            final Candidate candidate = rivalryData.findCandidateByName("The Macavity Awards");
            assertNotNull(candidate);
            assertThat(candidate.getRating(criterion0), is(1.0));
            assertThat(candidate.getRating(criterion1), is(1.0));
        }
    }
}
