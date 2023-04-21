package org.jcvi.glk.elvira.report.locator;

import static junit.framework.Assert.assertEquals;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestStringLocators {

    private static final String ID = "something";

    Locator locator;

    URL expectedURL;

    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][] { { new TigrFluAFastaLocator(), "ftp://ftp.tigr.org/private/infl_a_virus/" + ID + ".fasta" }, { new TigrFluBFastaLocator(), "ftp://ftp.tigr.org/private/infl_b_virus/" + ID + ".fasta" }, { new GenbankTraceArchiveLocator(), "http://www.ncbi.nlm.nih.gov/Traces/trace.cgi?cmd=retrieve&size=&s=&m=&retrieve.x=0&retrieve.y=0&val=species_code%3D%27" + ID + "%27" } });
    }

    public TestStringLocators(Locator locator, String url) throws MalformedURLException {
        this.locator = locator;
        this.expectedURL = new URL(url);
    }

    @Test
    public void valid() {
        assertEquals(expectedURL, locator.getUrlFor(ID));
    }
}
