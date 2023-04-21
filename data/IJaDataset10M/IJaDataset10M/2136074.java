package eu.funcnet.clients.uniprot;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class UniProtHttpClientTest {

    @Test
    public void testClient() throws Exception {
        final List<String> accessions = Arrays.asList("O15075", "P12345");
        final List<List<String>> results = UniProtHttpClient.getMetaDataByAccessions(accessions);
        assertTrue(results.size() >= 2);
        final List<String> header = Arrays.asList("Accession", "Entry name", "Status", "Protein names", "Gene names", "Organism", "Length");
        compare(header, results.get(0));
    }

    private <T> void compare(final List<T> expected, final List<T> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
