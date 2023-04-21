package org.gbif.ecat.fuzzy;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;
import static junit.framework.Assert.assertEquals;

public class FuzzyNameMatcherLuceneTest extends FuzzyNameMatcherBaseTest {

    @Before
    public void setUp() throws IOException {
        m = new FuzzyNameMatcherLucene(getTestNames());
    }
}
